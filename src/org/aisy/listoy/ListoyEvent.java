package org.aisy.listoy;

/**
 * 
 * Listoy 事件
 * 静态字符串为 Type
 * 含 @param data 的是接收参数
 * 
 */
public class ListoyEvent
{
	/**
	 * 上一页
	 */
	static final public String PREVIOUS = "PREVIOUS";
	/**
	 * 下一页
	 */
	static final public String NEXT = "NEXT";
	/**
	 * 上一页结束
	 */
	static final public String PREVIOUS_END = "PREVIOUS_END";
	/**
	 * 下一页结束
	 */
	static final public String NEXT_END = "NEXT_END";
	/**
	 * 当前页数
	 * @param data: [currentPage, totalPage]
	 */
	static final public String PAGE = "PAGE";
	/**
	 * 由 ItemRenderer 抛出
	 * 获取对指定 Item 的操作
	 * @param data: *
	 */
	static final public String ITEM_EVENT = "ITEM_EVENT";
	/**
	 * ItemRenderer 的可接收参数
	 * 显示区域的宽高
	 * @param data: Array[width:number, height:number]
	 */
	static final public String ITEM_GET_MASK = "ITEM_GET_MASK";
	/**
	 * ItemRenderer 的可接收参数
	 * 判断是否在显示区域
	 */
	static final public String ITEM_INSHOW = "ITEM_INSHOW";
	/**
	 * ItemRenderer 的可接收参数
	 * 移除 ItemRenderer
	 */
	static final public String ITEM_REMOVE = "ITEM_REMOVE";
	/**
	 * ItemRenderder 的可接收参数
	 * 重置 ItemRenderer
	 */
	static final public String ITEM_RESET = "ITEM_RESIET";
}
