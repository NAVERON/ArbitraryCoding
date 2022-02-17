package com.eron.routeplanning.dataprocessing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PosReport {  //动态信息报告表   去掉不必要的，有12个
	
	private int messageid;  //消息id，消息1、2、3的标识符
	/**private String transindicator; //转发指示符，表示消息被转发多少次，0-3,0=默认，3=不再转发**/
	private String mmsi = null;  //用户id，唯一标识符
	private String navstate;  //导航状态
	/* 导航状态说明
	 * 0=发动机使用中    1=锚泊    2=未操纵    3=有限适航性    4=受船舶吃水限制    5=系泊
	 * 6=搁浅    7=从事捕捞    8=航行中    9=留作未来修正导航状态，用于载运危险品DG，有害物质HS或海洋污染物MP的船舶，载运IMO的C类危险品或污染物、高速船HSC
	 * 10=留作将来修正导航状态，用于载运DG、HS、MP、或载运IMO的A类危险品或污染物的船舶
	 */
	private float rotatespeed;  //旋转速率
	/* 旋转速率说明
	 * 0-+126 = 每分钟右旋最多708°或更快
	 * 0--126 = 每分钟左旋最多708°或更快
	 * 每分钟0-708度之间的值表示
	 * ROT=4.733SQRT(ROT)度/min
	 * 式中ROT感应器为旋转速率，由外部旋转速率指示符TI输入
	 * ROTAIS为舍入后最为接近的整数
	 * +127 = 以每30s右旋超过5°的速率旋转
	 * -127 = 以每30s左旋超过5°的速率旋转
	 * -128（80十六进制）标明没有可用的旋转信息
	 * ROT数据不应从COG信息算出
	 */
	private float sog;  //地面航速，步长为1/10节（0-102.2节） 1023=不可用  1022=102.2节或更快
	//private String posaccuracy;  //位置准确度（PA）   1=高>10m  0=低（<10m）  0=默认值
	private double lng;  //经度  以1/10000min 为单位的经度 180      东-正    西-负（用补码表示）   181度=不可用=默认值
	private double lat;  //纬度  同经度  北-正  南-负  91度=不可用=默认值
	private float cog;  //地面航线，以1/10为单位，0-3599  3600=不可用=默认值 3601-4095应不采用
	private float truecourse;  //实际航向  度 0-359 511=不可用=默认值
	private Date timestamp;  //UTC秒，报告的生成时间 0-59    60=不可用，人工输入=61  估计模式（航迹推算）=62  定位系统中断=63
	private String speindicator;  //特定操纵指示符
	/*
	 * 0=不可用=默认值
	 * 1=未进行特定操纵
	 * 2=进行特定操纵
	 */
	private int pollution;  //排污量
	/**
	private String spare;  //备用-》 占3比特  置为0
	private String raim;  //RAIM标志，电子定位装置接收机自主整体检测（RAIM）标志   0=未使用=默认值    1=RAIM正在使用
	private String comstate;  //通信状态
	**/
	/*
	 * 消息id = 1  SOTDMA通信状态
	 * 消息id = 2  SOTDMA通信状态
	 * 消息id = 3  ITDMA通信状态
	 */
	//各位分配有一定的比特数，一共168比特，所以发送时应该将逻辑关系转换成  二进制位数发送出去，需要底层配合计算
	/******************************************************************************************/
	public static int attributeNum = 12;  //属性数量
	
	public PosReport() {
		super();
	}
	public PosReport(int messageid, String mmsi, String navstate, float rotatespeed, float sog,
			double lng, double lat, float cog,
			float truecourse, Date timestamp, String speindicator, int pollution) {
		super();
		this.messageid = messageid;
		this.mmsi = mmsi;
		this.navstate = navstate;
		this.rotatespeed = rotatespeed;
		this.sog = sog;
		this.lng = lng;
		this.lat = lat;
		this.cog = cog;
		this.truecourse = truecourse;
		this.timestamp = timestamp;
		this.speindicator = speindicator;
		this.pollution = pollution;
	}
	
	public int getMessageid(){
		return this.messageid;
	}
	public void setMessageid(int messageid){
		this.messageid = messageid;
	}
	public String getMmsi() {
		return mmsi;
	}
	public void setMmsi(String mmsi) {
		this.mmsi = mmsi;
	}
	public String getNavstate() {
		return navstate;
	}
	public void setNavstate(String navstate) {
		this.navstate = navstate;
	}
	public float getRotatespeed() {
		return rotatespeed;
	}
	public void setRotatespeed(float rotatespeed) {
		this.rotatespeed = rotatespeed;
	}
	public float getSog() {
		return sog;
	}
	public void setSog(float sog) {
		this.sog = sog;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public float getCog() {
		return cog;
	}
	public void setCog(float cog) {
		this.cog = cog;
	}
	public float getTruecourse() {
		return truecourse;
	}
	public void setTruecourse(float truecourse) {
		this.truecourse = truecourse;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public void setTimestamp(String timestamp){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.timestamp = sdf.parse(timestamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getSpeindicator() {
		return speindicator;
	}
	public void setSpeindicator(String speindicator) {
		this.speindicator = speindicator;
	}
	public int getPollution() {
		return pollution;
	}
	public void setPollution(int pollution) {
		this.pollution = pollution;
	}
	
	public String[] getAttrArray(){  //将属性由数组返回
		/*  方法 1 - 先初始化，后赋值
		String[] attributes = new String[attributeNum];
		attributes[0] = mmsi;
		attributes[1] = navstate;
		...*/
		String[] attributes = {
				String.valueOf(messageid),
				mmsi,
				navstate,
				String.valueOf(rotatespeed),
				String.valueOf(sog),
				String.valueOf(lng),
				String.valueOf(lat),
				String.valueOf(cog),
				String.valueOf(truecourse),
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp),
				speindicator,
				String.valueOf(pollution)
		};
		return attributes;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String showTime = sdf.format(timestamp);
		return " messageid=" + messageid + ",\n mmsi=" + mmsi + ",\n navstate=" + navstate + ",\n rotatespeed=" + rotatespeed + ",\n sog=" + sog
				+ ",\n lng=" + lng + ",\n lat=" + lat + ",\n cog=" + cog + ",\n truecourse=" + truecourse + ",\n timestamp="
				+ showTime + ",\n speindicator=" + speindicator + ",\n pollution=" + pollution;
	}
	
}





