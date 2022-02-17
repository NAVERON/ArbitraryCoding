/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.crawljobs;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class SearchController implements Initializable {

    private Stage primaryStage;
    private List<Notice> lists;
    private LocalDate from, to;
    private String keywords, department;
    /*  //时间格式的转换问题，显示使用local格式就不用转换了，一切正常
    LocalDate localDate = datePicker.getValue();
    Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
    Date date = Date.from(instant);
    System.out.println(localDate + "\n" + instant + "\n" + date);
    
    Date date = new Date();
    Instant instant = date.toInstant();
    LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
    System.out.println(date + "\n" + instant + "\n" + localDate);
     */
    @FXML
    private Button search_btn, cancle_btn;
    @FXML
    private ProgressBar search_progress;
    @FXML
    private HBox mannual_hbox;
    @FXML
    private DatePicker from_datepicker, to_datepicker;
    @FXML
    private TextField keywords_textfield, department_textfield;
    @FXML
    private VBox links_boxes;   //左边添加链接
    @FXML
    private WebView content_webview;  //右边显示的超链接网页界面
    @FXML
    private Label result_number_label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //设置默认的搜索时间范围
        from_datepicker.setValue(LocalDate.now().minusMonths(1));
        to_datepicker.setValue(LocalDate.now());

    }

    public SearchController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.lists = new LinkedList<Notice>();
    }

    //搜索模式
    //根据学院搜索，department输入学院名称
    //根据部门通知公告搜索，department输入部门名称
    //不输入department，搜索页面中的所有超链接            相关关键字       内容

    Thread executer = null;
    ProcessPage processpage = null;
    FutureTask<List<Notice>> futuretask = null;
    Timer updateUI = null;

    public void search() {
        
        lists.clear();
        links_boxes.getChildren().clear();
        //界面数据输入
        this.from = from_datepicker.getValue();
        this.to = to_datepicker.getValue();
        this.keywords = keywords_textfield.getText();
        this.department = department_textfield.getText();
        //取消了验证，如果没有关键字则显示所有的链接
        processpage = new ProcessPage(this.from, this.to, this.keywords, this.department);  //链接内置
        futuretask = new FutureTask<List<Notice>>(processpage);
        search_progress.progressProperty().bind(processpage.process);  //绑定进度
        
        executer = new Thread(futuretask);
        executer.start();
        
        updateUI = new Timer();
        updateUI.schedule(new TimerTask() {
            @Override
            public void run() {
                if (futuretask.isDone()) {
                    try {
                        lists = futuretask.get();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ExecutionException ex) {
                        Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    addResultHBox();
                    
                    updateUI.cancel();
                    updateUI = null;
                } else if (futuretask.isCancelled()) {
                    updateUI.cancel();
                    updateUI = null;
                }
            }
        }, 100, 200);
        
    }

    public void cancle_search() {
        if (executer != null) {
            futuretask.cancel(true);
            processpage.process.set(0);
            executer.interrupt();
            updateUI.cancel();
            updateUI = null;
            addResultHBox();
        }
        executer = null;
    }

    class ProcessPage implements Callable<List<Notice>> {  //多线程处理网页请求，异步处理，使用Jsoup库

        private String[] urls = {"http://i.whut.edu.cn/xxtg/", "http://i.whut.edu.cn/xyxw/"};
        private DoubleProperty process = new SimpleDoubleProperty(0);  //进度表示

        private LocalDate from, to;
        private String keywords, department;
        
        public ProcessPage(LocalDate from, LocalDate to, String keywords, String department) {
            this.from = from;
            this.to = to;
            this.keywords = keywords;
            this.department = department;
        }
        
        //  关于搜索中需要使用的变量存储
        //  process.set(process.get() + 0.2);
        @Override
        public List<Notice> call() throws Exception {
            
            Map<String, String> depart_name = new HashMap<String, String>();  //部门号对应的超链接，大类
            for (String search_url : urls) {       //这里获取所有大标题，两大块搜索，部门和学院
                Document doc = Jsoup.connect(search_url).timeout(10 * 1000).get();
                Element by_class = doc.getElementsByClass("text_list_menu2").first();
                Elements by_tag = by_class.getElementsByTag("a");
                for (Element a_tag : by_tag) {  //各个school的名称和对应的超链接
                    depart_name.put(a_tag.attr("href"), a_tag.text());
                }
            }
            //这里的处理方式
            //根据部门号查找，超链接，添加到
            Map<String, String> need_search = new HashMap<String, String>();  //先筛选部门和学院
            if (this.department.isEmpty()) {
                need_search.putAll(depart_name);
            } else {
                for (String key : depart_name.keySet()) {
                    if (depart_name.get(key).contains(this.department)) {
                        need_search.put(key, depart_name.get(key));
                    }
                }
            }
            //学院27个
            //部门32个
            //现在need_search是所有需要搜索关键字的链接了
            System.out.println(need_search.size() +" : " + need_search.keySet()+"\n");
            for (String key : need_search.keySet()) {  //点开大部门的链接
                String url = key;
                Document doc = Jsoup.connect(url).get();
                System.out.println(key);
                Element by_class = doc.getElementsByClass("normal_list2").first();
                Elements get_li = by_class.getElementsByTag("li");
                for (Element li : get_li) {
                    Element a = li.select("span > a").first();
                    Element strong = li.select("strong").first();
                    //提取信息进行筛选
                    String title = a.attr("title");  //名称
                    String link = a.attr("abs:href");  //超链接
                    //String content = Jsoup.connect(link).timeout(10 * 1000).get().getElementById("divToPrint").toString();  //连接内容，方便以后增加分析层
                    LocalDate date = LocalDate.parse(strong.text());
                    Notice temp = null;
                    if (date.isAfter(from) && date.isBefore(to)) {
                        if (keywords.isEmpty()) {
                            temp = new Notice(title, Jsoup.connect(link).timeout(10 * 1000).get().getElementById("divToPrint").toString(), date, link);
                            lists.add(temp);
                        } else if (title.contains(keywords)) {
                            temp = new Notice(title, Jsoup.connect(link).timeout(10 * 1000).get().getElementById("divToPrint").toString(), date, link);
                            lists.add(temp);
                        }
                    }
                }
            }
            //搜索完毕
            process.set(0);
            return lists;
        }
    }

    public synchronized void addResultHBox() {
        //根据搜索结果添加到界面显示
        Platform.runLater(() -> {
            
            Collections.sort(lists);
            result_number_label.setText("RESULT : " + lists.size());
            if (lists.isEmpty()) {
                Alert empty = new Alert(Alert.AlertType.WARNING);
                empty.setContentText("Nothing!!");
                empty.showAndWait();
                return;
            }
            for (Notice notice : lists) {
                links_boxes.getChildren().add(new ResultHBox(content_webview, notice));
            }
            
            lists.clear();
        });
    }

}
