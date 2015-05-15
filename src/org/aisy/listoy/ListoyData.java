package org.aisy.listoy;

import java.util.List;

import org.aisy.interfaces.IClear;
import org.aisy.listoy.Listoy.Group;
import org.aisy.listoy.Listoy.IItemRenderer;
import org.aisy.listoy.Listoy.ScrollF;

/**
 * 
 * Listoy 的属性绑定
 * 
 */
public class ListoyData implements IClear
{
	/**
	 * item 的容器
	 */
	public Group group;
	/**
	 * 行数
	 */
	public int row = 0;
	/**
	 * 列数
	 */
	public int column = 0;
	/**
	 * 竖向总行数
	 */
	public int rowTotal;
	/**
	 * 横向总列数
	 */
	public int columnTotal;
	/**
	 * 当前所显示行数索引
	 */
	public int curRow;
	/**
	 * 当前所显示列数索引
	 */
	public int curColumn;
	/**
	 * 横向外缩进
	 */
	public double marginH = 0;
	/**
	 * 竖向外缩进
	 */
	public double marginV = 0;
	/**
	 * 横向内缩进
	 */
	public double paddingH = 7;
	/**
	 * 竖向内缩进
	 */
	public double paddingV = 7;
	/**
	 * 横向移动偏移量
	 */
	public double movePositionH = column;
	/**
	 * 竖向移动偏移量
	 */
	public double movePositionV = row;
	/**
	 * 每行的宽度
	 */
	public double moveWidth;
	/**
	 * 每列的高度
	 */
	public double moveHeight;
	/**
	 * 显示区域的宽度
	 */
	public double maskWidth;
	/**
	 * 显示区域的高度
	 */
	public double maskHeight;
	/**
	 * itemRenderer 自定义渲染器（Class or Function）
	 */
	public Class<?> itemRenderer;
	public IItemRenderer iitemRenderer;
	/**
	 * dataProvider 将要显示的元素数组 (Array or Vector)
	 */
	public List<Object> dataProvider;
	/**
	 * 设置（横 / 竖）排版
	 */
	public int layout = ListoyEnum.LAYOUT_HORIZONTAL;
	/**
	 * 设置渲染方式
	 */
	public int mode = ListoyEnum.MODE_ALL;
	/**
	 * 总页数
	 */
	public int totalPage = 1;
	/**
	 * 当前页数
	 */
	public int curPage = 1;
	/**
	 * 设置翻页持续时间
	 */
	public double duration = 0.8;
	/**
	 * 是否有背景
	 */
	public boolean background;
	
	public ScrollF scrollF;
	
	public int widthMeasureSpec = 0;
	public int heightMeasureSpec = 0;
	
	public ListoyData()
	{
	}
	
	/**
	 * 清空
	 */
	public void clear()
	{
		totalPage = curPage = 1;
		rowTotal = columnTotal = curRow = curColumn = 0;
		moveWidth = moveHeight = maskWidth = maskHeight = 0;
		widthMeasureSpec = heightMeasureSpec = 0;
		if (null != scrollF) {
			scrollF.clear();
			scrollF = null;
		}
		if (null != group) {
			group.clear();
			group = null;
		}
		iitemRenderer = null;
		itemRenderer = null;
		dataProvider = null;
	}

}
