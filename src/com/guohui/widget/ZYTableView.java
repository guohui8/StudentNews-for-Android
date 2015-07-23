package com.guohui.widget;


import com.guohui.student.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ZYTableView extends LinearLayout {

	public static abstract interface ClickListener {
		public abstract void onClick(int i);
	}

	private ClickListener mClickListener;
	private Context mContext;
	private int mIndexController = 0;

	public ZYTableView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.mContext = paramContext;
	}

	private int dip2px(float f) {
		return (int) (0.5F + f
				* mContext.getResources().getDisplayMetrics().density);
	}

	public void addView(View paramView) {
		if ((paramView instanceof ZYBasicItem))
			((ZYBasicItem) paramView).build();
		super.addView(paramView);
	}

	public void build() {
		if (getChildCount() <= 0) {
			setVisibility(View.GONE);
			return;
		} else {
			int i;
			int j;
			setVisibility(0);
			mIndexController = 0;
			i = dip2px(10F);

			j = 0;

			while (j < getChildCount()) {
				View view1 = getChildAt(j);
				if (mIndexController == 0)
					view1.setBackgroundResource(R.drawable.background_view_rounded_top);
				else if (mIndexController == -1 + getChildCount())
					view1.setBackgroundResource(R.drawable.background_view_rounded_bottom);
				else
					view1.setBackgroundResource(R.drawable.background_view_rounded_middle);
				if (view1 instanceof ZYBasicItem)
					view1.setPadding(i, i, i, i);
				if (view1.isClickable()) {
					view1.setTag(Integer.valueOf(mIndexController));
					view1.setOnClickListener(new View.OnClickListener() {

						public void onClick(View view2) {
							if (mClickListener != null)
								mClickListener.onClick(((Integer) view2
										.getTag()).intValue());
						}

					});
				}
				mIndexController = 1 + mIndexController;
				j++;
			}
			if (getChildCount() == 1) {
				View view = getChildAt(0);
				view.setBackgroundResource(R.drawable.background_view_rounded_single);
				if (view instanceof ZYBasicItem)
					view.setPadding(i, i, i, i);
				if (view.isClickable()) {
					view.setTag(Integer.valueOf(mIndexController));
					view.setOnClickListener(new android.view.View.OnClickListener() {

						public void onClick(View view2) {
							if (mClickListener != null)
								mClickListener.onClick(((Integer) view2
										.getTag()).intValue());
						}

					});
				}
			}
		}

	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		build();
	}

	public void removeClickListener() {
		this.mClickListener = null;
	}

	public void setClickListener(ClickListener mClickListener) {
		this.mClickListener = mClickListener;
	}
}