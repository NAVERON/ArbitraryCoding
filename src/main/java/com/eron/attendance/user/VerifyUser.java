package com.eron.attendance.user;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import javafx.concurrent.Task;

/**
 * 验证功能
 *
 * @author ERON 从数据源索引 用户，用来验证 传进来的用户是否是合法的
 */
public class VerifyUser extends Task<WorkRecord> {
    // 验证用户登录信息

    private User user = null;

    public VerifyUser(User user) {
        this.user = user;
    }

    public boolean isCorrect() {
        User tempUser = this.validateUser(user.getName());
        if (tempUser != null) {
            if (tempUser.getPassword().equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    private User validateUser(String name) { // 通过用户名获取用户完整信息，后面比对来判断是否为合法用户
        return new User("admin", new String("root"));
    }

    @Override
    protected WorkRecord call() throws Exception {
        // TODO Auto-generated method stub

        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet("http://121.12.250.200:8089/");

        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();

        if (entity != null) {

            InputStream is = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            while ((str = br.readLine()) != null) {
                System.out.println("" + str);
            }
        }

        System.out.println("Login form get: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }
        System.out.println("Initial set of cookies:");
        List<Cookie> cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }
        ////////////////////////////////////////////////////////////////////
        HttpPost httpost = new HttpPost("http://121.12.250.200:8089/jt/framework/passport/login?"
                + "client=jsmisApp&success=aHR0cDovLzEyMS4xMi4yNTAuMjAwOjgwODkvZnJhbWUvZnJhbWUvaW5k"
                + "ZXguamh0bWw%3D&failure=aHR0cDovLzEyMS4xMi4yNTAuMjAwOjgwODkvbG9naW4vaW5kZXguamh0bW"
                + "w%3D&sign=088adfde1ca4b409303e42449d7b37d1");

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", "null"));
        nvps.add(new BasicNameValuePair("password", "null"));

        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        response = httpclient.execute(httpost);

        System.out.println("Response " + response.toString());
        entity = response.getEntity();

        System.out.println("Login form get: " + response.getStatusLine());
        if (entity != null) {

            InputStream is = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            while ((str = br.readLine()) != null) {
                System.out.println("" + str);
            }
        }

        System.out.println("Post logon cookies:");
        cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }
        httpclient.getConnectionManager().shutdown();

        return null;
    }

    public boolean test() {  //测试task线程功能
        Task<Integer> task = null;

        task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                int iterations;
                for (iterations = 0; iterations < 1000; iterations++) {
                    if (isCancelled()) {
                        updateMessage("Cancelled");
                        break;
                    }
                    updateMessage("Iteration " + iterations);
                    updateProgress(iterations, 1000);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException interrupted) {
                        if (isCancelled()) {
                            updateMessage("Cancelled");
                            break;
                        }
                    }
                }
                return iterations;
            }
        };

        return true;
    }

}
