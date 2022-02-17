package com.eron.routeplanning.dataprocessing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StateReport {  //航行静态数据  去掉不必要，有12个
	
	//private int messageid;  //本消息的标识符
	/**private String transindicator;  //0-3 0=默认值   3=不再转发**/
	private String mmsi;  //用户id
	private int asiversion;  //ais版本  0=符合ITU-R M.1371-1建议书的台站  1=....1371-3...  2-3=符合将来版本的台站
	private String imonumber;  //0=不可用=默认值  不适用于SAR航空器
	private String callnumber;  //呼号  7X6比特ASCII字符     @@@@@@@=不可用=默认值
	private String name;  //最长20字符的6比特ASCII码，  20个@=不可用=默认值，对于SAR航空器，设置为“SAR航空器NNNNNNN”
	private String type;  //船舶货物类型
	/* 
	 * 0=不可用或没有船舶=默认值
	 * 1-99，参考后边船舶类型
	 * 100-199=保留，用于区域性使用
	 * 200-255=保留，用于将来使用
	 * 不使用与SAR航空器
	 */
	/* 船舶类型
	 * 50 引航船舶
	 * 51 搜救船舶
	 * 52 拖轮
	 * 53 港口补给船
	 * 54 安装有防污染设施或设备的船舶
	 * 55 执法船舶
	 * 56 备用-当地传播指配使用
	 * 57 备用-当地传播指配使用
	 * 58 医疗运送船舶
	 * 59 非武装冲突参与国的船舶和航空器
	 */
	private int posaccuracy;  //位置准确度（PA）   1=高>10m  0=低（<10m）  0=默认值
	private String size;  //总体尺寸，位置参考，  已报告位置的参考点，还表明船舶尺寸
	private String electype; //电子装置类型
	/* 
	 * 1=GPS
	 * 2=GLONASS
	 * 3=GPS/GLONASS组合
	 * 4=loran-C
	 * 5=Chayka
	 * 6=综合导航系统
	 * 7=正在研究
	 * 8=Galileo
	 * 9-14未使用
	 * 15=GNSS
	 */
	private Date eta;  //估计到达时间 MMDDHHMM  UTC
	private float draft;  //目前最大静态吃水， 以1/10m为单位，255=25.5m或更大，0=不可用=默认值
	private String desport;  //目的港口  最长20字符，采用6比特ASCII码
	/**
	private String dte;  //数据终端就绪    0=可用  1=不可用=默认值
	private String spare;  //备用，置为0
	**/
	//总共比特数目424    占用2个时隙
	/******************************************************************************************/
	public static int attributeNum = 12;  //属性的数量
	
	public StateReport() {
		super();
	}
	public StateReport(String mmsi, int asiversion, String imonumber, String callnumber, String name,
			String type, int posaccuracy, String size, String electype, Date eta, float draft, String desport) {
		super();
		this.mmsi = mmsi;
		this.asiversion = asiversion;
		this.imonumber = imonumber;
		this.callnumber = callnumber;
		this.name = name;
		this.type = type;
		this.posaccuracy = posaccuracy;
		this.size = size;
		this.electype = electype;
		this.eta = eta;
		this.draft = draft;
		this.desport = desport;
	}
	
	public String getMmsi() {
		return mmsi;
	}
	public void setMmsi(String mmsi) {
		this.mmsi = mmsi;
	}
	public int getAsiversion() {
		return asiversion;
	}
	public void setAsiversion(int asiversion) {
		this.asiversion = asiversion;
	}
	public String getImonumber() {
		return imonumber;
	}
	public void setImonumber(String imonumber) {
		this.imonumber = imonumber;
	}
	public String getCallnumber() {
		return callnumber;
	}
	public void setCallnumber(String callnumber) {
		this.callnumber = callnumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPosaccuracy() {
		return posaccuracy;
	}
	public void setPosaccuracy(int posaccuracy) {
		this.posaccuracy = posaccuracy;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getElectype() {
		return electype;
	}
	public void setElectype(String electype) {
		this.electype = electype;
	}
	public Date getEta() {
		return eta;
	}
	public void setEta(Date eta) {
		this.eta = eta;
	}
	public void setEta(String eta){//输入字符串时间时，进行转换
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.eta = sdf.parse(eta);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public float getDraft() {
		return draft;
	}
	public void setDraft(float draft) {
		this.draft = draft;
	}
	public String getDesport() {
		return desport;
	}
	public void setDesport(String desport) {
		this.desport = desport;
	}
	
	public String[] getAttrArray(){  //将属性存储在数组中返回
		String[] attributes = {
				mmsi,
				String.valueOf(asiversion),
				imonumber,
				callnumber,
				name,
				type,
				String.valueOf(posaccuracy),
				size,
				electype,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(eta),
				String.valueOf(draft),
				desport
		};
		return attributes;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String showTime = sdf.format(eta);
		return " mmsi=" + mmsi + ",\n asiversion=" + asiversion + ",\n imonumber="
				+ imonumber + ",\n callnumber=" + callnumber + ",\n name=" + name + ",\n type=" + type + ",\n posaccuracy="
				+ posaccuracy + ",\n size=" + size + ",\n electype=" + electype + ",\n eta=" + showTime + ",\n draft=" + draft
				+ ",\n desport=" + desport;
	}
	
}






