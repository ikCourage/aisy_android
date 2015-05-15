package org.aisy.listoy;

/**
 * 
 * Listoy 常量
 * 
 */
public class ListoyEnum
{
	/**
	 * 横向排版
	 */
	static final public int LAYOUT_HORIZONTAL = 0;
	/**
	 * 竖向排版
	 */
	static final public int LAYOUT_VERTICAL = 1;
	/**
	 * 渲染全部
	 */
	static final public int MODE_ALL = 0;
	/**
	 * 渲染一页（可能会多渲染一页，但只显示一页。当 item 多时可以使用这个，但翻页快时可能不平滑）
	 */
	static final public int MODE_PAGE = 1;
	/**
	 * 适用于滚动条（只渲染可见 item，即：row * column）
	 */
	static final public int MODE_SHOW = 2;
}
