package org.aisy.listoy;

import java.util.List;

import org.aisy.display.USprite;
import org.aisy.listoy.Listoy.ScrollF;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 
 * Listoy Item
 * 
 */
public class ListoyItem extends USprite
{
	/**
	 * 名称
	 */
	protected String NAME;
	/**
	 * item 数据
	 */
	protected Object itemInfo;
	/**
	 * 索引
	 */
	protected int index;
	
	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ListoyItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * @param context
	 * @param attrs
	 */
	public ListoyItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * @param context
	 */
	public ListoyItem(Context context) {
		super(context);
	}
	
	public void init(String name, int index, Object data)
	{
		NAME = name;
		this.index = index;
		itemInfo = data;
		
		name = null;
		data = null;
	}
	
	public boolean inShow(ScrollF f)
	{
		return f.inShow(index, this);
	}
	
	public int reSet(ScrollF f, List<Object> arr, int length)
	{
		index = f.reSet(index, this);
		if (index < length) {
			itemInfo = arr.get(index);
		}
		else {
			itemInfo = null;
		}
		return index;
	}
	
	/**
	 * 清空
	 */
	@Override
	public void clear()
	{
		super.clear();
		itemInfo = null;
		NAME = null;
	}
}
