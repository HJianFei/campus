package com.loosoo100.campus100.anyevent;

/**
 * 收货地址
 * 
 * @author yang
 * 
 */
public class MEventAddress {
	private String area;
	private String city;
	private String province;
	private String aid;
	private String cid;
	private String pid;

	public MEventAddress(String area, String city, String province, String aid,
			String cid, String pid) {
		this.area = area;
		this.city = city;
		this.province = province;
		this.aid = aid;
		this.cid = cid;
		this.pid = pid;
	}

	public String getArea() {
		return area;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getProvince() {
		return province;
	}
	
	public String getAid() {
		return aid;
	}
	
	public String getCid() {
		return cid;
	}
	
	public String getPid() {
		return pid;
	}
}
