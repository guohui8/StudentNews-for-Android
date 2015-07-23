package com.guohui.loader;


import java.io.File;
import com.guohui.util.FileUtil;

public class ImageCacher {
	public enum EnumImageType {
		Photo,Adver,Yule
	}
	
	public static String GetImageFolder(EnumImageType imageType) {
		String folder = FileUtil.CACHEPATH + File.separator;
		switch(imageType) {
			default :
			case Photo :
				folder += "Photo";
				break;	
			case Adver :
				folder += "Adver";
				break;
			case Yule :
				folder += "Yule";
				break;
		}
		return folder;
	}
}