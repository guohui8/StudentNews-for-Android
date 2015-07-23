package com.guohui.widget;


import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

public class ZYEditText extends EditText {
	
	int maxLength;
	
	public ZYEditText(Context paramContext) {
		super(paramContext);
	}
	
	public ZYEditText(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}
	
	public ZYEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
	}
	
	public void setMaxLength(int paramInt) {
		this.maxLength = paramInt;
	    if (paramInt > 0) {
	      InputFilter[] arrayOfInputFilter = new InputFilter[1];
	      arrayOfInputFilter[0] = new InputFilter.LengthFilter(paramInt);
	      setFilters(arrayOfInputFilter);
	    }
	}
	
	public void setText(CharSequence paramCharSequence, TextView.BufferType paramBufferType) {
		super.setText(paramCharSequence, paramBufferType);
		if (paramCharSequence == null)
			paramCharSequence = "";
		int i;
		if (maxLength > 0 && maxLength < paramCharSequence.length())
			i = maxLength;
		else
			i = paramCharSequence.length();
		setSelection(i);
	}
}