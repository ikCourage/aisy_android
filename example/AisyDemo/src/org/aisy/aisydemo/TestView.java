package org.aisy.aisydemo;

import java.util.ArrayList;

import org.aisy.display.USprite;
import org.aisy.listoy.Listoy;
import org.aisy.listoy.ListoyEnum;
import org.aisy.scroller.ScrollerView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TestView extends USprite {
	
	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public TestView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public TestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * @param context
	 */
	public TestView(Context context) {
		super(context);
		init();
	}
	
	private void init()
	{
		TextView tv = new TextView(getContext());
		tv.setText("source: https://github.com/ikCourage/aisy_android");
		addView(tv);
		
		ArrayList<Object> arr = new ArrayList<Object>();
		for (int i = 0; i < 100; i++) {
			arr.add(i, "ITEM " + i);
		}
		//source: https://github.com/ikCourage/aisy_android
		Listoy listoy = new Listoy(getContext());
		//如果 item 数量比较多时，应设置为 ListoyEnum.MODE_SHOW（只实例化可显示区域的内容，并且参照 reSet 的重用）
		//如果 item 数量不多，则可以使用默认的 ListoyEnum.MODE_ALL（实例化全部），此时应手动设置 setRowColumn
		listoy.setMode(ListoyEnum.MODE_SHOW);
		listoy.setLayout(ListoyEnum.LAYOUT_VERTICAL);
		//每一项之间的间隔
		//listoy.setPadding(0);
		//多行一列
		//listoy.setRowColumn(arr.size(), 1);
		//三行三列
		//listoy.setRowColumn(3, 3);
		listoy.setItemRenderer(Item.class);
		listoy.setDataProvider(arr);
		//listoy.initializeView();
		listoy.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//addView(listoy);
		
		ScrollerView scrollerView = new ScrollerView(getContext());
		scrollerView.addView(listoy);
		addView(scrollerView);
		//清空
		//ScrollerView.clear();
	}

}
