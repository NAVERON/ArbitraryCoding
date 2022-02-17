package com.eron.routeplanning.dataprocessing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class AccessData {

	private static final Logger log = Logger.getLogger(AccessData.class.getName());

	// 两个动态数组存储两种数据
	// List<PosReport> posreports = new ArrayList<>();
	// List<StateReport> statereports = new ArrayList<>();

	private List<String> getfromFile(File getdata) throws IOException { // 将文件数据存储到链表中
		List<String> data = new ArrayList<String>(); // 因为只是读取数据，不进行插入操作，所以数组更合适 尖括号里类型在7以上可以省略
		if (!getdata.exists()) {
			System.out.println("this file not exist!");
			// getdata.createNewFile(); //如果没有数据就没必要进行处理
			return null;
		}
		Scanner scanner = new Scanner(new FileInputStream(getdata));
		while (scanner.hasNextLine()) {
			data.add(scanner.nextLine());
		}
		scanner.close();
		// 返回读取的数据
		/*
		 * for(int i=0;i<data.size();i++){ System.out.println(data.get(i)); }
		 */
		return data;
	}

	public List<String> getfromOtherResource() {
		// 从其他数据源获取数据，存储在data数组中
		List<String> data = new ArrayList<String>();
		return data;
	}

	public List<StateReport> getStateReport() { // 获取船舶静态数据
		List<StateReport> stateReports = new ArrayList<StateReport>();
		List<String> getdata = null;
		try {
			String filePath = this.getClass().getResource("/routedata/ships.dt").getPath();
			log.info("getStateReport : " + filePath);

			getdata = getfromFile(new File(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (getdata.isEmpty()) { // 如果文件中没有内容，则不进行处理
			System.out.println("file is empty");
			return null;
		}
		StateReport statereportTemp = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = null;
		String[][] temp = null; // 存储分割后数据
		for (int i = 0; i < getdata.size(); i++) {
			String[] one = getdata.get(i).split(",");
			temp = new String[getdata.size()][one.length];
			for (int j = 0; j < one.length; j++) { // 这里可以 temp[i] = one[j];
				temp[i][j] = one[j];
			}
			try {
				dt = sf.parse(temp[i][9].trim());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			statereportTemp = new StateReport(temp[i][0].trim(), Integer.parseInt(temp[i][1].trim()), temp[i][2].trim(),
					temp[i][3].trim(), temp[i][4].trim(), temp[i][5].trim(), Integer.parseInt(temp[i][6].trim()),
					temp[i][7].trim(), temp[i][8].trim(), dt, Float.parseFloat(temp[i][10].trim()), temp[i][11].trim());
			stateReports.add(statereportTemp);
			// System.out.println(statereportTemp);
		}
		return stateReports;
	}

	public List<PosReport> getPosReport() { // 获取动态数据
		List<PosReport> posReports = new ArrayList<PosReport>();
		List<String> getdata = null;
		try {
			String filePath = this.getClass().getResource("/routedata/tracks.dt").getPath();
			log.info("getPosReport : " + filePath);

			getdata = getfromFile(new File(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (getdata.isEmpty()) { // 如果文件中没有内容，则不进行处理
			System.out.println("file is empty");
			return null;
		}
		PosReport posreportTemp = null;
		String[][] temp = null; // 存储分割后数据
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = null;
		for (int i = 0; i < getdata.size(); i++) {
			String[] one = getdata.get(i).split(",");
			temp = new String[getdata.size()][one.length];
			for (int j = 0; j < one.length; j++) {
				temp[i][j] = one[j];
			}
			try {
				dt = sf.parse(temp[i][9].trim());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			posreportTemp = new PosReport(Integer.parseInt(temp[i][0].trim()), temp[i][1].trim(), temp[i][2].trim(),
					Float.parseFloat(temp[i][3].trim()), Float.parseFloat(temp[i][4].trim()),
					Double.parseDouble(temp[i][5].trim()), Double.parseDouble(temp[i][6].trim()),
					Float.parseFloat(temp[i][7].trim()), Float.parseFloat(temp[i][8].trim()), dt, temp[i][10].trim(),
					Integer.parseInt(temp[i][11].trim()));
			posReports.add(posreportTemp);
			// System.out.println(posreportTemp);
		}
		return posReports;
	}

}
