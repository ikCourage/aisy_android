package org.aisy.scroller;

import org.aisy.autoclear.AisyAutoClear;
import org.aisy.interfaces.IClear;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public class HorizontalScrollerView extends HorizontalScrollView implements IClear
{
	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public HorizontalScrollerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public HorizontalScrollerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 */
	public HorizontalScrollerView(Context context) {
		super(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
		final ViewGroup.LayoutParams lp = child.getLayoutParams();
		final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight(), lp.width);
		final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom(), lp.height);
		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}
	
	public void clear()
	{
		AisyAutoClear.remove(this);
		destroyDrawingCache();
		Class<IClear> IC = IClear.class;
		int i = getChildCount();
		View obj;
		while (i > 0) {
			i--;
			obj = getChildAt(i);
			if (IC.isInstance(obj)) ((IClear)obj).clear();
			else removeViewAt(i);
		}
		obj = null;
		IC = null;
		if (null != getParent()) ((ViewGroup)getParent()).removeView(this);
	}

}
