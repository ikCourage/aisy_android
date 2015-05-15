package org.aisy.display;

import org.aisy.interfaces.IClear;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class USprite extends ViewGroup implements IClear
{
	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public USprite(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public USprite(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 */
	public USprite(Context context) {
		super(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = 0, height = 0;
		int x, y, w, h;
		Class<USprite> US = USprite.class;
		USprite us;
		View obj;
		for (int i = 0, l = getChildCount(); i < l; ++i) {
        	obj = getChildAt(i);
        	measureChild(obj, widthMeasureSpec, heightMeasureSpec);
        	if (US.isInstance(obj)) {
        		us = (USprite)obj;
        		x = (int)us.getuX();
        		y = (int)us.getuY();
        		w = us.getuWidth() + x;
        		h = us.getuHeight() + y;
        	}
        	else {
        		x = obj.getLeft();
        		y = obj.getTop();
        		w = obj.getMeasuredWidth() + x;
        		h = obj.getMeasuredHeight() + y;
        	}
        	if (w > 0) {
        		if (x >= width || w > width) {
        			width = w;
        		}
        		else if (x < 0) {
        			if (w > width) {
        				width = w;
        			}
        		}
        	}
        	if (h > 0) {
        		if (y >= height || h > height) {
        			height = h;
        		}
        		else if (y < 0) {
        			if (h > height) {
        				height = h;
        			}
        		}
        	}
        }
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed == false) return;
		View obj;
		for (int i = 0, len = getChildCount(); i < len; i++) {
			obj = getChildAt(i);
			obj.layout(obj.getLeft(), obj.getTop(), obj.getLeft() + obj.getMeasuredWidth(), obj.getTop() + obj.getMeasuredHeight());
		}
	}
	
	public void layout()
	{
		layout(getuX(), getuY(), getuX() + getuWidth(), getuY() + getuHeight());
	}
	
	public void setuX(int value)
	{
		setLeft(value);
	}
	
	public void setuY(int value)
	{
		setTop(value);
	}
	
	public void setuLeft(int value)
	{
		setLeft(value);
	}
	
	public void setuRight(int value)
	{
		setRight(value);
	}
	
	public void setuTop(int value)
	{
		setTop(value);
	}
	
	public void setuBottom(int value)
	{
		setBottom(value);
	}
	
	public void setuWidth(int value)
	{
		setMeasuredDimension(value, getuHeight());
	}
	
	public void setuHeight(int value)
	{
		setMeasuredDimension(getuWidth(), value);
	}
	
	public void setuSize(int width, int height)
	{
		setMeasuredDimension(width, height);
	}
	
	public int getuX()
	{
		return getLeft();
	}
	
	public int getuY()
	{
		return getTop();
	}
	
	public int getuLeft()
	{
		return getLeft();
	}
	
	public int getuRight()
	{
		return getRight();
	}
	
	public int getuTop()
	{
		return getTop();
	}
	
	public int getuBottom()
	{
		return getBottom();
	}
	
	public int getuWidth()
	{
		return getMeasuredWidth();
	}
	
	public int getuHeight()
	{
		return getMeasuredHeight();
	}
	
	public void clearView()
	{
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
	}

	public void clear()
	{
		Class<IClear> IC = IClear.class;
		int i = getChildCount();
		View obj;
		while (i > 0) {
			i--;
			obj = getChildAt(i);
			if (IC.isInstance(obj)) ((IClear)obj).clear();
			else removeViewAt(i);
		}
		if (null != getParent()) ((ViewGroup)getParent()).removeView(this);
		obj = null;
		IC = null;
	}
}
