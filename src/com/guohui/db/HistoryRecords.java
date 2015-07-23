package com.guohui.db;

/**
 * history记录类
 * @author Administrator
 *
 */
public class HistoryRecords {
	private String id;
	private String newsid;
	private String title;
	private String typeid;

	public HistoryRecords(String id, String newsid, String title,String typeid) {
		super();
		this.id = id;
		this.newsid = newsid;
		this.title = title;
		this.typeid = typeid;
	}

	public HistoryRecords(String newsid, String title, String typeid) {
		super();
		this.newsid = newsid;
		this.title = title;
		this.typeid = typeid;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
	
	public String getNewsid() {
		return newsid;
	}

	public void setNewsid(String newsid) {
		this.newsid = newsid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {		
		this.title = title;
	}
	
	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
}
