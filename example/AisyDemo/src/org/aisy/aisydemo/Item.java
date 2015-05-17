package org.aisy.aisydemo;

import java.util.List;

import org.ais.event.TEvent;
import org.ais.interfaces.ITEvent;
import org.aisy.listoy.Listoy.ScrollF;
import org.aisy.listoy.ListoyItem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class Item extends ListoyItem implements ITEvent
{

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public Item(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public Item(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 */
	public Item(Context context) {
		super(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), heightMeasureSpec);
	}
	
	public void init(String name, int index, Object data)
	{
		super.init(name, index, data);
		
		setBackgroundColor(0xAA6699FF);
		TextView tv = new TextView(getContext());
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(0xFFFFFFFF);
		tv.setText(itemInfo.toString());
		addView(tv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		//根据 xml 进行布局
		//View v = inflate(getContext(), R.layout.item, null);
		//addView(v);
		
		TEvent.newTrigger(NAME + "ITEM", this);
	}
	
	@Override
	public int reSet(ScrollF f, List<Object> arr, int length) {
		super.reSet(f, arr, length);
		if (index < length) {
			TextView tv = (TextView)getChildAt(0);
			tv.setText(String.valueOf(index));
		}
		return index;
	}
	
	@Override
	public void triggerHandler(Object[] args) {
		
	}
	
}
