package com.guohui.widget;


import com.guohui.student.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
public class ZYBasicItem extends LinearLayout {
	
	public static final int TEXT_TYPE_SMALL = 1;
	public static final int TEXT_TYPE_YELLOW_COLOR = 1 << 1;
	public static final int TEXT_TYPE_GRAY_COLOR = 1 << 2;
	public static final int TEXT_TYPE_BLACK_COLOR = 1 << 3;
	public static final int TEXT_TYPE_BOLD = 1 << 4;
	private int checked;
	private boolean clickable;
	private String count;
	private int count_textType;
	private String input;
	private String input_hint;
	private int input_maxLength;
	private int input_textType;
	private int input_type;
	private ImageView itemArrow;
	private CheckBox itemCheckBox;
	private TextView itemCount;
	private ZYEditText itemInput;
	private TextView itemSubtitle;
	private TextView itemTitle;
	private LinearLayout itemTitleLay;
	private Context mContext;
	private String subTitle;
	private int subTitle_textType;
	private String title;
	private int title_textType;

	public ZYBasicItem(Context context) {
		this(context, null);
		mContext = context;
	}

	public ZYBasicItem(Context context, AttributeSet attributeset) {
		super(context, attributeset);
		this.mContext = context;
		TypedArray typedarray = context.obtainStyledAttributes(attributeset,
				R.styleable.ZYBasicItem);
		this.title = typedarray.getString(0);
		this.subTitle = typedarray.getString(1);
		this.input = typedarray.getString(2);
		this.input_hint = typedarray.getString(3);
		this.input_type = typedarray.getInt(4, 1);
		this.input_maxLength = typedarray.getInt(5, 0);
		this.count = typedarray.getString(6);
		this.checked = typedarray.getInt(7, 0);
		this.title_textType = typedarray.getInt(9, 0);
		this.subTitle_textType = typedarray.getInt(10, 0);
		this.count_textType = typedarray.getInt(11, 0);
		this.input_textType = typedarray.getInt(12, 0);
		this.clickable = typedarray.getBoolean(8, false);
		typedarray.recycle();
		setupView(context);
	}

	private int dip2px(float f) {
		return (int) (0.5F + f
				* mContext.getResources().getDisplayMetrics().density);
	}

	private void setTextType(TextView textview, int i) {
		Resources resources = mContext.getResources();
		if (i != 0) {
			if ((i & 0x1) == TEXT_TYPE_SMALL)
				textview.setTextAppearance(mContext,
						R.style.content_page_small_text);
			if ((i & 0x2) == TEXT_TYPE_YELLOW_COLOR)
				textview.setTextColor(resources
						.getColorStateList(R.color.text_yellow_color_selector));
			if ((i & 0x4) == TEXT_TYPE_GRAY_COLOR)
				textview.setTextColor(resources
						.getColorStateList(R.color.text_gray_color_selector));
			if ((i & 0x8) == TEXT_TYPE_BLACK_COLOR)
				textview.setTextColor(resources
						.getColorStateList(R.color.text_color_selector));
			if ((i & 0x10) == TEXT_TYPE_BOLD)
				textview.getPaint().setFakeBoldText(true);
			else
				textview.getPaint().setFakeBoldText(false);
		}
	}

	private void setupView(Context context) {
		Resources resources = context.getResources();
		android.content.res.ColorStateList colorstatelist = resources
				.getColorStateList(R.color.text_color_selector);
		itemTitleLay = new LinearLayout(context);
		itemTitleLay.setDuplicateParentStateEnabled(true);
		itemTitle = new TextView(context);
		itemTitle.setId(R.id.itemTitle);
		itemTitle.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		itemTitle.setText(title);
		itemTitle.setDuplicateParentStateEnabled(true);
		itemTitle.setTextAppearance(context,
				android.R.style.TextAppearance_Medium);
		itemTitle.setTextColor(colorstatelist);
		itemTitle.setSingleLine(true);
		itemTitle.setEllipsize(android.text.TextUtils.TruncateAt.END);
		itemTitle.setPadding(0, 0, dip2px(10F), 0);
		itemTitle.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
		setTextType(itemTitle, title_textType);
		itemTitleLay.addView(itemTitle);
		itemSubtitle = new TextView(context);
		itemSubtitle.setId(R.id.itemSubTitle);
		itemSubtitle.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		itemSubtitle.setText(subTitle);
		itemSubtitle.setDuplicateParentStateEnabled(true);
		itemSubtitle.setTextAppearance(context,
				android.R.style.TextAppearance_Medium);
		itemSubtitle.setTextColor(colorstatelist);
		itemSubtitle.setSingleLine(true);
		itemSubtitle.setEllipsize(android.text.TextUtils.TruncateAt.END);
		setTextType(itemSubtitle, subTitle_textType);
		itemTitleLay.addView(itemSubtitle);
		addView(itemTitleLay);
		itemInput = new ZYEditText(context);
		itemInput.setId(R.id.itemInput);
		LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
				-1, -2);
		itemInput.setLayoutParams(layoutparams);
		itemInput.setGravity(Gravity.CENTER_VERTICAL);
		itemInput.setText(input);
		itemInput.setDuplicateParentStateEnabled(true);
		itemInput.setTextAppearance(context,
				android.R.style.TextAppearance_Medium);
		itemInput.setTextColor(colorstatelist);
		itemInput.setSingleLine(true);
		itemInput.setEllipsize(android.text.TextUtils.TruncateAt.END);
		itemInput.setHint(input_hint);
		itemInput.setInputType(input_type);
		itemInput.setMaxLength(input_maxLength);
		itemInput.setBackgroundDrawable(null);
		itemInput.setPadding(0, 0, 0, 0);
		setTextType(itemInput, input_textType);
		addView(itemInput);
		itemCount = new TextView(context);
		itemCount.setId(R.id.itemCount);
		itemCount.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		itemCount.setText(count);
		itemCount.setDuplicateParentStateEnabled(true);
		itemCount.setTextAppearance(context,
				android.R.style.TextAppearance_Medium);
		itemCount.setTextColor(resources
				.getColorStateList(R.color.text_gray_color_selector));
		setTextType(itemCount, count_textType);
		addView(itemCount);
		itemCheckBox = new CheckBox(context);
		itemCheckBox.setId(R.id.itemCheckBox);
		itemCheckBox.setLayoutParams(new LinearLayout.LayoutParams(dip2px(26.0F), dip2px(25.0F)));
		CheckBox checkbox = itemCheckBox;
		if (checked == 1)
			checkbox.setChecked(true);
		else
			checkbox.setChecked(false);
		itemCheckBox.setPadding(0, 0, 0, 0);
		addView(itemCheckBox);
		itemArrow = new ImageView(context);
		itemArrow.setId(R.id.itemArrow);
		itemArrow.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
				-2, -2));
		itemArrow.setPadding(dip2px(10F), 0, 0, 0);
		itemArrow.setDuplicateParentStateEnabled(true);
		itemArrow.setImageResource(R.drawable.arrow);
		addView(itemArrow);
		build();
		setGravity(Gravity.CENTER_VERTICAL);
		setMinimumHeight(dip2px(45F));
	}

	public void build() {
		TextView textview = itemTitle;
		TextView textview1;
		ZYEditText dpedittext;
		TextView textview2;
		CheckBox checkbox;
		ImageView imageview;
		if (title == null)
			textview.setVisibility(View.GONE);
		else
			textview.setVisibility(View.VISIBLE);
		textview1 = itemSubtitle;
		if (subTitle == null)
			textview1.setVisibility(View.GONE);
		else
			textview1.setVisibility(View.VISIBLE);
		dpedittext = itemInput;
		if (input_hint != null || input != null)
			dpedittext.setVisibility(View.VISIBLE);
		else
			dpedittext.setVisibility(View.GONE);
		textview2 = itemCount;
		if (count != null)
			textview2.setVisibility(View.VISIBLE);
		else
			textview2.setVisibility(View.GONE);
		checkbox = itemCheckBox;
		if (checked == 0)
			checkbox.setVisibility(View.GONE);
		else
			checkbox.setVisibility(View.VISIBLE);
		imageview = itemArrow;
		if (isClickable())
			imageview.setVisibility(View.VISIBLE);
		else {
			imageview.setVisibility(View.GONE);
		}
		if (input_hint != null || input != null)
			itemTitleLay.setLayoutParams(new LinearLayout.LayoutParams(-2, -2,
					0.0F));
		else
			itemTitleLay.setLayoutParams(new LinearLayout.LayoutParams(0, -2,
					1.0F));
		if (input_hint != null || subTitle != null)
			title_textType = 4 | title_textType;
		setTextType(itemTitle, title_textType);
		setClickable(clickable);
	}

	@Override
	protected void dispatchRestoreInstanceState(
			SparseArray<Parcelable> container) {
		int i = R.id.itemInput - getId();
		ZYEditText dpedittext = (ZYEditText) findViewById(R.id.itemInput);
		if (dpedittext == null) {
			super.dispatchSaveInstanceState(container);
			return;
		}
		Parcelable parcelable = (Parcelable) container.get(i);
		if (parcelable != null)
			dpedittext.onRestoreInstanceState(parcelable);
		int j = R.id.itemCheckBox ^ getId();
		CheckBox checkbox = (CheckBox) findViewById(R.id.itemCheckBox);
		Parcelable parcelable1 = (Parcelable) container.get(j);
		if (parcelable1 != null)
			checkbox.onRestoreInstanceState(parcelable1);

	}

	@Override
	protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
		int i;
		ZYEditText zyedittext;
		i = R.id.itemInput - getId();
		zyedittext = (ZYEditText) findViewById(R.id.itemInput);
		if (zyedittext == null) {
			super.dispatchSaveInstanceState(container);
		} else {
			Parcelable parcelable = zyedittext.onSaveInstanceState();
			if (parcelable != null)
				container.put(i, parcelable);
			int j = R.id.itemCheckBox ^ getId();
			Parcelable parcelable1 = ((CheckBox) findViewById(R.id.itemCheckBox))
					.onSaveInstanceState();
			if (parcelable1 != null)
				container.put(j, parcelable1);
		}

	}

	public int getCountTextType() {
		return count_textType;
	}

	public String getInputHint() {
		return input_hint;
	}

	public int getInputMaxLength() {
		return input_maxLength;
	}

	public String getInputText() {
		return input;
	}

	public int getInputTextType() {
		return input_textType;
	}

	public int getInputType() {
		return input_type;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public int getSubTitleTextType() {
		return subTitle_textType;
	}

	public String getTitle() {
		return title;
	}

	public int getTitleTextType() {
		return title_textType;
	}

	public boolean isClickable() {
		return clickable;
	}

	public void setClickable(boolean flag) {
		super.setClickable(flag);
		clickable = flag;
	}

	public void setCount(String s) {
		count = s;
		itemCount.setText(s);
	}

	public String getCount() {
		return count;
	}

	public void setCountTextType(int i) {
		count_textType = i;
		setTextType(itemCount, i);
	}

	public void setHint(String s) {
		input_hint = s;
		itemInput.setHint(s);
	}

	public void setInputMaxLength(int i) {
		input_maxLength = i;
		itemInput.setMaxLength(i);
	}

	public void setInputText(String s) {
		input = s;
		itemInput.setText(s);
	}

	public void setInputTextType(int i) {
		input_textType = i;
		setTextType(itemInput, i);
	}

	public void setInputType(int i) {
		input_type = i;
		itemInput.setInputType(i);
	}

	public void setSubTitle(String s) {
		subTitle = s;
		itemSubtitle.setText(s);
	}

	public void setSubTitleTextType(int i) {
		subTitle_textType = i;
		setTextType(itemSubtitle, i);
	}

	public void setTitle(String s) {
		title = s;
		itemTitle.setText(s);
	}

	public void setTitleTextType(int i) {
		title_textType = i;
		setTextType(itemTitle, i);
	}

}