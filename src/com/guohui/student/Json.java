package com.guohui.student;

import com.google.gson.annotations.Expose;

public class Json {
	@Expose
	public boolean ok;
	@Expose
	public boolean err;
	@Expose
	public String msg, title;
	@Expose
	public String sessionid;
	@Expose
	public String userid;
	@Expose
	public String encodepass;
	@Expose
	public int albumid;
	@Expose
	public albuminfo[] albumlist;
	@Expose
	public photoinfo[] photolist;

}

class pclass {
	@Expose
	public String id;
	@Expose
	public String name;
}

class albuminfo {
	@Expose
	public int id;
	@Expose
	public String img;
	@Expose
	public wh wh;
}

class photoinfo {
	@Expose
	public int id;
	@Expose
	public String url;
}

class wh {
	@Expose
	public int w;
	@Expose
	public int h;
}