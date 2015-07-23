package com.guohui.model;


public class Image {
	public String aticleId;
	public String imageURL;
	public String title;
	public String desc;

	public String getAticleId() {
		return aticleId;
	}

	public void setAticleId(String aticleId) {
		this.aticleId = aticleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	@Override
	public String toString() {
		return "ImageList [imageURL=" + imageURL + ", title=" + title + "]";
	}
}