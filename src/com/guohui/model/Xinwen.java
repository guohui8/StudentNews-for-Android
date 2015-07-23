package com.guohui.model;


public class Xinwen {
	public String id;
	public String title;
	public String channel;
	public String desc;
	public String picurl;
	public String posttime;
	public String hits;
	public String content = "";
	public String source="";
	private String type = "";
	
	public Xinwen() {
	}
	public Xinwen(String type, String content) {
		setType(type);
		setContent(content);
	}
	public Xinwen(String id, String title, String channel, String desc,
			String picurl, String posttime, String hits) {
		super();
		this.id = id;
		this.title = title;
		this.channel = channel;
		this.desc = desc;
		this.picurl = picurl;
		this.posttime = posttime;
		this.hits = hits;
	}
	
	public Xinwen(String id, String title, String desc,
			 String posttime,String picurl) {
		super();
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.picurl = picurl;
		this.posttime = posttime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getPosttime() {
		return posttime;
	}

	public void setPosttime(String posttime) {
		this.posttime = posttime;
	}

	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}