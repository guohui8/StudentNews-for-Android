package com.guohui.interfacer;

import android.content.Context;
import android.graphics.Bitmap;

public abstract interface DownImageInterface {
	public abstract Context getContext();

	public abstract void notifyChangeImage(int paramInt1, int paramInt2,
			Bitmap paramBitmap);
}