package org.aisy.listoy;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.ais.event.TEvent;
import org.ais.interfaces.ITEvent;
import org.aisy.display.USprite;
import org.aisy.interfaces.IClear;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * 列表组件，名称必须唯一
 * 
 */
public class Listoy extends USprite implements ITEvent
{

	/**
	 * 组件名称，作为抛出事件的引用
	 */
	protected String _NAME;
	/**
	 * 数据对象
	 */
	protected ListoyData iData;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public Listoy(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public Listoy(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 */
	public Listoy(Context context) {
		super(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		getData().widthMeasureSpec = widthMeasureSpec;
		iData.heightMeasureSpec = heightMeasureSpec;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (iData.mode == ListoyEnum.MODE_SHOW) {
			if (null == iData.group && null != iData.dataProvider && (null != iData.itemRenderer || null != iData.iitemRenderer)) {
				initializeView();
			}
			iData.group.setuWidth((int)iData.maskWidth);
			iData.group.setuHeight((int)iData.maskHeight);
			setMeasuredDimension((int)iData.maskWidth, (int)iData.maskHeight);
		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (getData().mode == ListoyEnum.MODE_SHOW) {
			__scroll();
		}
		super.dispatchDraw(canvas);
	}

	protected ListoyData getData()
	{
		return null == iData ? iData = new ListoyData() : iData;
	}

	/**
	 * 计算布局
	 */
	protected void __layout()
	{
		iData.group.setuX(0);
		iData.group.setuY(0);
		if (iData.mode != ListoyEnum.MODE_SHOW) {
			iData.curPage = 1;
			iData.totalPage = iData.dataProvider.size() == 0 ? 1 : (int)Math.ceil(iData.dataProvider.size() / (iData.row * iData.column));
			iData.moveWidth = (iData.maskWidth + iData.paddingH - iData.marginH) / iData.column;
			iData.moveHeight = (iData.maskHeight + iData.paddingV - iData.marginV) / iData.row;
			if (iData.background == true) {
//				graphics.beginFill(0x000000, 0);
//				graphics.drawRect(0, 0, iData.maskWidth, iData.maskHeight);
//				graphics.endFill();
			}
		}
		switch (iData.mode) {
			case ListoyEnum.MODE_PAGE:
				break;
			case ListoyEnum.MODE_SHOW:
				if (null != iData.group && iData.group.getChildCount() != 0) {
					int len = iData.dataProvider.size();
					USprite obj = (USprite)iData.group.getChildAt(0);
					iData.moveWidth = obj.getuWidth() + iData.marginH + iData.paddingH;
					iData.moveHeight = obj.getuHeight() + iData.marginV + iData.paddingV;
					if (iData.column == 1) {
						iData.maskHeight = iData.marginV * 2 + iData.moveHeight * (len - 1) + obj.getuHeight();
					}
					else {
						iData.maskWidth = iData.marginH * 2 + iData.moveWidth * (len - 1) + obj.getuWidth();
					}
					iData.curPage = 0;
					iData.itemRenderer = null;
					obj = null;
				}
				break;
			default:
				iData.iitemRenderer = null;
				iData.itemRenderer = null;
				iData.dataProvider = null;
				break;
		}
	}

	/**
	 * 当 mode = 2 时，
	 * 通过 (ListoyEvent.ITEM_INSHOW, function(index:uint)) 
	 * 和 (ListoyEvent.ITEM_RESET, [function(index:uint, item:ListoyItem), dataProvider, dataProvider.length]) 来重用 item
	 */
	protected void __scroll()
	{
		if (null == getParent()) return;
		int s = 0;
		double l;
		if (iData.column == 1) {
			l = (((View)getParent()).getScrollY() + iData.marginV);
			if (l > 0) {
				s = (int)(l / iData.moveHeight);
				if (s > 0 && l % iData.moveHeight == 0) {
					s--;
				}
			}
		}
		else {
			l = (((View)getParent()).getScrollX() + iData.marginH);
			if (l > 0) {
				s = (int)(l / iData.moveWidth);
				if (s > 0 && l % iData.moveWidth == 0) {
					s--;
				}
			}
		}
		if (s == iData.curPage) return;
		
		ScrollF f = null == iData.scrollF ? iData.scrollF = new ScrollF() : iData.scrollF;
		f.view = iData.group;
		f.s = s;
		f.e = s + iData.row * iData.column - 1;
		
//		TEvent.trigger(getNAME() + "ITEM", new Object[]{ListoyEvent.ITEM_INSHOW, f});
//		f.setI(iData.curPage);
		f.scroll(iData.curPage, iData.dataProvider, iData.dataProvider.size());
		f.reset();
		iData.curPage = s;
//		TEvent.trigger(getNAME() + "ITEM", new Object[]{ListoyEvent.ITEM_RESET, f, iData.dataProvider, iData.dataProvider.size()});
		f = null;
	}

	/**
	 * 翻页事件
	 * @param type
	 * @param data
	 */
	public void triggerHandler(Object[] args)
	{
		String type = (String)args[0];
		Object data = args[1];
		if ((!type.equals(ListoyEvent.PREVIOUS) && !type.equals(ListoyEvent.NEXT)) || null == iData || (iData.mode == ListoyEnum.MODE_ALL && iData.totalPage < 2) || iData.mode == ListoyEnum.MODE_SHOW) return;
		if (null == data) data = new Object();
		if (type.equals(ListoyEvent.PREVIOUS)) {
			setCurrentPage(iData.curPage < 3 ? 1 : iData.curPage - 1, data);
		}
		else if (type.equals(ListoyEvent.NEXT)) {
			setCurrentPage(iData.curPage + 1, data);
		}
		type = null;
		data = null;
	}

	/**
	 * 设置组件名称
	 * @param value
	 */
	public void setNAME(String value)
	{
		if (null != _NAME) {
			TEvent.clearTrigger(_NAME + "ITEM");
			TEvent.clearTrigger(_NAME);
		}
		_NAME = null == value ? String.valueOf(Math.random()) : value;
		TEvent.newTrigger(_NAME, this);
		value = null;
	}

	/**
	 * 返回组件名称
	 * @return 
	 */
	public String getNAME()
	{
		if (null == _NAME) setNAME(null);
		return _NAME;
	}

	/**
	 * 设置 显示行数、列数
	 * @param row
	 * @param column
	 */
	public void setRowColumn(int row, int column)
	{
		getData().row = row;
		iData.column = column;

		iData.movePositionH = column;
		iData.movePositionV = row;
	}

	/**
	 * 当 mode = 2 时，
	 * 通过 item 的大小和显示区域的大小来计算并设置 row 和 column
	 * 当 layout = 0 时，row = 1
	 * 当 layout = 1 时，column = 1
	 * @param itemSize
	 * @param viewSize
	 * @param layout
	 * @return 
	 */
	public int setRowColumn2(double itemSize, double viewSize)
	{
		return setRowColumn2(itemSize, viewSize, 1);
	}

	public int setRowColumn2(double itemSize, double viewSize, int layout)
	{
		viewSize -= layout == ListoyEnum.LAYOUT_VERTICAL ? getData().marginV : getData().marginH;
		itemSize += layout == ListoyEnum.LAYOUT_VERTICAL ? iData.marginV + iData.paddingV : iData.marginH + iData.paddingH;
		int i = (int)Math.ceil(viewSize / itemSize) + 1;
		if (layout == ListoyEnum.LAYOUT_VERTICAL) {
			setRowColumn(i, 1);
		}
		else {
			setRowColumn(1, i);
		}
		return i;
	}

	/**
	 * 设置 渲染方式
	 * @param value
	 */
	public void setMode(int mode)
	{
		getData().mode = mode;
	}

	/**
	 * 设置 布局样式
	 * @param layout
	 */
	public void setLayout(int layout)
	{
		getData().layout = layout;
	}

	/**
	 * 设置 外部缩进
	 * @param margin
	 */
	public void setMargin(double margin)
	{
		setMargin(margin, 0);
	}

	/**
	 * 设置 外部缩进
	 * 当 layout = 0 时，设置（横向缩进、竖向缩进）
	 * 当 layout = 1 时，设置（横向缩进）
	 * 当 layout = 2 时，设置（竖向缩进）
	 * @param margin
	 * @param layout
	 */
	public void setMargin(double margin, int layout)
	{
		switch (layout) {
			case 0:
				getData().marginH = margin;
				getData().marginV = margin;
				break;
			case 1:
				getData().marginH = margin;
				break;
			case 2:
				getData().marginV = margin;
				break;
		}
	}

	/**
	 * 设置 内部缩进
	 * @param padding
	 */
	public void setPadding(double padding)
	{
		setPadding(padding, 0);
	}

	/**
	 * 设置 内部缩进
	 * 当 layout = 0 时，设置（横向缩进、竖向缩进）
	 * 当 layout = 1 时，设置（横向缩进）
	 * 当 layout = 2 时，设置（竖向缩进）
	 * @param padding
	 * @param layout
	 */
	public void setPadding(double padding, int layout)
	{
		switch (layout) {
			case 0:
				getData().paddingH = padding;
				getData().paddingV = padding;
				break;
			case 1:
				getData().paddingH = padding;
				break;
			case 2:
				getData().paddingV = padding;
				break;
		}
	}

	/**
	 * 设置 移动偏移量
	 * @param position
	 */
	public void setMovePosition(double position)
	{
		setMovePosition(position, 0);
	}

	/**
	 * 设置 移动偏移量
	 * 当 layout = 0 时，设置（横向、竖向）移动偏移量
	 * 当 layout = 1 时，设置（横向移动偏移量）
	 * 当 layout = 2 时，设置（竖向移动偏移量）
	 * @param position
	 * @param layout
	 */
	public void setMovePosition(double position, int layout)
	{
		switch (layout) {
			case 0:
				getData().movePositionH = position;
				getData().movePositionV = position;
				break;
			case 1:
				getData().movePositionH = position;
				break;
			case 2:
				getData().movePositionV = position;
				break;
		}
	}

	/**
	 * 设置 翻页持续时间
	 * @param duration
	 */
	public void setDuration(double duration)
	{
		getData().duration = duration;
	}

	/**
	 * 设置是否有背景
	 * @param background
	 */
	public void setBackground(boolean background)
	{
		getData().background = background;
	}

	/**
	 * 设置 自定义项目渲染器
	 * @param itemRenderer （Class or Function）
	 */
	public void setItemRenderer(Class<?> itemRenderer)
	{
		getData().itemRenderer = itemRenderer;
		itemRenderer = null;
	}
	
	public void setItemRenderer(IItemRenderer itemRenderer)
	{
		getData().iitemRenderer = itemRenderer;
		itemRenderer = null;
	}

	/**
	 * 设置 数据集
	 * @param dataProvider (Array or Vector)
	 */
	public void setDataProvider(List<Object> dataProvider)
	{
		getData().dataProvider = dataProvider;
		switch (iData.mode) {
			case ListoyEnum.MODE_SHOW:
				if (null != iData.group && iData.group.getChildCount() != 0) {
					int len = iData.dataProvider.size();
					USprite obj = (USprite)iData.group.getChildAt(0);
					if (iData.column == 1) {
						iData.maskHeight = iData.marginV * 2 + iData.moveHeight * (len - 1) + obj.getuHeight();
					}
					else {
						iData.maskWidth = iData.marginH * 2 + iData.moveWidth * (len - 1) + obj.getuWidth();
					}
					measure(0, 0);
					obj = null;
				}
				break;
		}
		dataProvider = null;
	}

	/**
	 * 设置 偏移量
	 * @param position
	 * @param data
	 */
	public void setCurrentPosition(int position)
	{
		setCurrentPosition(position, null);
	}

	public void setCurrentPosition(int position, Object data)
	{
		if (null == data) data = new Object();
//		TODO:
//		getDefinitionByName(AisySkin.TWEEN_LITE).killTweensOf(getData().group);
//		当前所处最大边界值
		int m;
		switch (iData.layout) {
			case ListoyEnum.LAYOUT_HORIZONTAL:
				m = iData.columnTotal < iData.column ? 0 : (iData.columnTotal - iData.column);
				iData.curColumn = position > m ? m : position;
				if (iData.curColumn == 0) {
					TEvent.trigger(getNAME(), new Object[]{ListoyEvent.PREVIOUS_END});
				}
				else if (iData.columnTotal <= iData.column + iData.curColumn) {
					iData.curColumn = iData.columnTotal < iData.column ? 0 : iData.curColumn;
					TEvent.trigger(getNAME(), new Object[]{ListoyEvent.NEXT_END});
				}
				iData.curPage = (int)Math.ceil(iData.curColumn / iData.column) + 1;
//				TODO:
//				data.x = -iData.curColumn * iData.moveWidth;
//				getDefinitionByName(AisySkin.TWEEN_LITE).to(iData.group, iData.duration, data);
				break;
			case ListoyEnum.LAYOUT_VERTICAL:
				m = iData.rowTotal < iData.row ? 0 : (iData.rowTotal - iData.row);
				iData.curRow = position > m ? m : position;
				if (iData.curRow == 0) {
					TEvent.trigger(getNAME(), new Object[]{ListoyEvent.PREVIOUS_END});
				}
				else if (iData.rowTotal <= iData.row + iData.curRow) {
					iData.curRow = iData.rowTotal < iData.row ? 0 : iData.curRow;
					TEvent.trigger(getNAME(), new Object[]{ListoyEvent.NEXT_END});
				}
				iData.curPage = (int)Math.ceil(iData.curRow / iData.row) + 1;
//				TODO:
//				data.y = -iData.curRow * iData.moveHeight;
//				getDefinitionByName(AisySkin.TWEEN_LITE).to(iData.group, iData.duration, data);
				break;
		}
		TEvent.trigger(getNAME(), new Object[]{ListoyEvent.PAGE, getCurrentPage(), getTotalPage()});
		data = null;
	}

	/**
	 * 设置 页数
	 * @param page
	 * @param data
	 */
	public void setCurrentPage(int page)
	{
		setCurrentPage(page, null);
	}

	public void setCurrentPage(int page, Object data)
	{
		int t = getTotalPage();
		page = page == 0 ? 1 : page;
		page = page > t ? t : page;
		boolean b = false;
//		if (null != data && data.hasOwnProperty("R") == true) {
//			b = Boolean(data["R"]);
//			delete data["R"];
//		}
		if (b == false && page == iData.curPage) return;
		switch (iData.mode) {
			case ListoyEnum.MODE_ALL:
				page--;
				switch (iData.layout) {
				case ListoyEnum.LAYOUT_HORIZONTAL:
					setCurrentPosition(page * iData.column, data);
					break;
				case ListoyEnum.LAYOUT_VERTICAL:
					setCurrentPosition(page * iData.row, data);
					break;
				}
				break;
			case ListoyEnum.MODE_PAGE:
				IItemRenderer iitemRenderer = iData.iitemRenderer;
				Class<?> itemRenderer = iData.itemRenderer;
				List<Object> dataProvider = iData.dataProvider;
				int len = iData.row * iData.column;
				int p = iData.curPage;
				List<Object> arr = dataProvider.subList((p - 1) * len, p * len);
				List<Object> arr2 = dataProvider.subList((page - 1) * len, page * len);
				clearItem();
				setItemRenderer(itemRenderer);
				setItemRenderer(iitemRenderer);
				if (page < p) {
					arr2.addAll(arr);
					setDataProvider(arr2);
					initializeView();
					double duration = iData.duration;
					setDuration(0);
					setCurrentPosition(Integer.MAX_VALUE);
					setDuration(duration);
					setCurrentPosition(0, data);
				}
				else {
					arr.addAll(arr2);
					setDataProvider(arr);
					initializeView();
					setCurrentPosition(Integer.MAX_VALUE, data);
				}
				if (page == 1) {
					TEvent.trigger(getNAME(), new Object[]{ListoyEvent.PREVIOUS_END});
				}
				else if (page == iData.totalPage) {
					TEvent.trigger(getNAME(), new Object[]{ListoyEvent.NEXT_END});
				}
				setDataProvider(dataProvider);
				iData.totalPage = t;
				iData.curPage = page;
				TEvent.trigger(getNAME(), new Object[]{ListoyEvent.PAGE, new Object[]{getCurrentPage(), getTotalPage()}});
				iitemRenderer = null;
				itemRenderer = null;
				dataProvider = null;
				arr = null;
				arr2 = null;
				break;
		}
	}

	/**
	 * 开始进行组件实例化
	 * 在 setItemRenderer，setdataProvider 后执行
	 */
	public void initializeView()
	{
		if (null == _NAME) setNAME(null);
		if (null == getData().group) iData.group = new Group(getContext());
		iData.rowTotal = 1;
		iData.columnTotal = 0;
		iData.curRow = iData.curColumn = 0;
		IItemRenderer iitemRenderer = iData.iitemRenderer;
		Class<?> itemRenderer = iData.itemRenderer;
		List<Object> dataProvider = iData.dataProvider;
		int row = iData.row;
		int column = iData.column;
		int len = dataProvider.size();
		int len2 = row * column;
		int index = 0;
		double _x = iData.marginH;
		double _y = iData.marginV;
		double _x2 = iData.marginH;
		double w = 0;
		double h = 0;
		int widthMeasureSpec = iData.widthMeasureSpec;
		int heightMeasureSpec = iData.heightMeasureSpec;
		ListoyItem obj = null;
		Constructor<?> constructor = null;
		if (null == iitemRenderer) {
			try {
				constructor = itemRenderer.getConstructor(Context.class);
			} catch (Exception e) {
				return;
			}
		}
		switch (iData.mode) {
			case ListoyEnum.MODE_PAGE:
				len2 <<= 1;
				if (len > len2 && len2 > 0) {
					len = len2;
				}
				break;
			case ListoyEnum.MODE_SHOW:
				if (len > len2 && len2 > 0) {
					len = len2;
				}
				break;
		}
		while (index < len) {
			for (int i = 0; (index < len && i < row) || row == 0; i++) {
				for (int j = 0; (index < len && j < column) || column == 0; j++) {
//					创建 item
					if (null != iitemRenderer) {
						obj = iitemRenderer.init(_NAME, index, dataProvider.get(index));
					}
					else {
						try {
							obj = (ListoyItem)constructor.newInstance(getContext());
						} catch (Exception e) {}
						obj.init(_NAME, index, dataProvider.get(index));
					}
					obj.measure(widthMeasureSpec, heightMeasureSpec);
					obj.setuX((int)_x);
					obj.setuY((int)_y);
					obj.layout();
					_x += obj.getuWidth() + iData.paddingH + iData.marginH;
					w = Math.max(w, obj.getuWidth());
					h = Math.max(h, obj.getuHeight());
					iData.group.addView(obj);
					if (_y == iData.marginV) iData.columnTotal++;
					index++;
					if (row == 0 || column == 0) {
						switch (iData.mode) {
//							case ListoyEnum.MODE_PAGE:
//								len2 <<= 1;
//								if (len > len2 && len2 > 0) {
//									len = len2;
//								}
//								break;
							case ListoyEnum.MODE_SHOW:
								switch (iData.layout) {
									case ListoyEnum.LAYOUT_HORIZONTAL:
										setRowColumn2(obj.getuWidth(), MeasureSpec.getSize(widthMeasureSpec), iData.layout);
										break;
									case ListoyEnum.LAYOUT_VERTICAL:
										setRowColumn2(obj.getuHeight(), MeasureSpec.getSize(heightMeasureSpec), iData.layout);
										break;
								}
								row = iData.row;
								column = iData.column;
								len2 = row * column;
								if (len > len2 && len2 > 0) {
									len = len2;
								}
								break;
						}
					}
				}
				if (index < len) {
					switch (iData.layout) {
						case ListoyEnum.LAYOUT_HORIZONTAL:
							_y += obj.getuHeight() + iData.paddingV + iData.marginV;
							if (i == row - 1) {
								_x2 = _x;
								_y = iData.marginV;
							}
							_x = _x2;
							break;
						case ListoyEnum.LAYOUT_VERTICAL:
							_x = iData.marginH;
							_y += obj.getuHeight() + iData.paddingV + iData.marginV;
							iData.rowTotal++;
							break;
					}
				}
			}
		}
		if (null == iData.group.getParent()) {
			addView(iData.group);
		}
		iData.group.measure(widthMeasureSpec, heightMeasureSpec);
		if (row * column >= len) {
			iData.maskWidth = iData.group.getuWidth() + iData.marginH * 2;
			iData.maskHeight = iData.group.getuHeight() + iData.marginV * 2;
		}
		else {
			iData.maskWidth = column * w + (column - 1) * iData.paddingH + (column + 1) * iData.marginH;
			iData.maskHeight = row * h + (row - 1) * iData.paddingV + (row + 1) * iData.marginV;
			iData.group.maskRect = new Rect(0, 0, (int)iData.maskWidth, (int)iData.maskHeight);
		}
		__layout();
		itemRenderer = null;
		iitemRenderer = null;
		dataProvider = null;
		obj = null;
	}

	/**
	 * 返回 翻页持续时间
	 * @return 
	 */
	public double getDuration()
	{
		return getData().duration;
	}

	/**
	 * 返回 偏移量
	 * @return 
	 */
	public int getCurrentPosition()
	{
		switch (getData().layout) {
			case ListoyEnum.LAYOUT_HORIZONTAL:
				return iData.curColumn;
			case ListoyEnum.LAYOUT_VERTICAL:
				return iData.curRow;
		}
		return 0;
	}

	/**
	 * 返回 偏移量最大值
	 * @return 
	 */
	public int getMaxPosition()
	{
		switch (getData().layout) {
			case ListoyEnum.LAYOUT_HORIZONTAL:
				return iData.columnTotal;
			case ListoyEnum.LAYOUT_VERTICAL:
				return iData.rowTotal;
		}
		return 0;
	}

	/**
	 * 返回 当前页数
	 * @return 
	 */
	public int getCurrentPage()
	{
		return getData().curPage;
	}

	/**
	 * 返回 总页数
	 * @return 
	 */
	public int getTotalPage()
	{
		return getData().totalPage;
	}

	/**
	 * 返回 显示宽度
	 * @return 
	 */
	public int getViewWidth()
	{
		return (int)(null == iData ? 0 : iData.maskWidth);
	}

	/**
	 * 返回 显示高度
	 * @return 
	 */
	public int getViewHeight()
	{
		return (int)(null == iData ? 0 : iData.maskHeight);
	}

	/**
	 * 清空显示 Item
	 * 以便重置 Listoy
	 */
	public void clearItem()
	{
		if (null == iData) return;
		iData.clear();
//		graphics.clear();
		if (null != _NAME) TEvent.clearTrigger(_NAME + "ITEM");
	}

	/**
	 * 清空显示对象及侦听
	 */
	@Override
	public void clear()
	{
		super.clear();
		if (null != iData) {
//			graphics.clear();
			iData.clear();
			iData = null;
		}
		if (null != _NAME) {
			TEvent.clearTrigger(_NAME + "ITEM");
			TEvent.clearTrigger(_NAME);
			_NAME = null;
		}
	}

	public class ScrollF implements IClear
	{
		public int s;
		public int i;
		public int j;
		public int e;
		public ArrayList<ListoyItem> v = new ArrayList<ListoyItem>();
		public USprite view;
		
		public boolean inShow(int index, ListoyItem obj) {
			if (index < s || index > e) {
				v.add(j++, obj);
				return false;
			}
			else if (index > i) {
				i = index;
			}
			return true;
		}
		
		public int reSet(int index, ListoyItem obj) {
			if (v.indexOf(obj) != -1) {
				index = i++;
				if (iData.column == 1) {
					obj.setuY((int)(iData.marginV + iData.moveHeight * index));
				}
				else {
					obj.setuX((int)(iData.marginH + iData.moveWidth * index));
				}
				obj.layout();
			}
			return index;
		}
		
		public void setI(int p) {
			i = s > p ? i + 1 : s;
			i = i > s ? i : s;
		}
		
		public void scroll(int p, List<Object> arr, int length) {
			int k, l = view.getChildCount();
			for (k = 0; k < l; k++) {
				((ListoyItem)view.getChildAt(k)).inShow(this);
			}
			setI(p);
			l = v.size();
			for (k = 0; k < l; k++) {
				((ListoyItem)v.get(k)).reSet(this, arr, length);
			}
		}
		
		public void reset() {
			i = j = 0;
			if (null != v) {
				v.clear();
			}
		}
		
		@Override
		public void clear() {
			if (null != v) {
				v.clear();
				v = null;
			}
			view = null;
		}
	}
	
	public interface IItemRenderer
	{
		ListoyItem init(String name, int index, Object data);
	}

	protected class Group extends USprite
	{
		public Rect maskRect;
		
		/**
		 * @param context
		 * @param attrs
		 * @param defStyle
		 */
		public Group(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		/**
		 * @param context
		 * @param attrs
		 */
		public Group(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		/**
		 * @param context
		 */
		public Group(Context context) {
			super(context);
		}

		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			
		}

		@Override
		protected void dispatchDraw(Canvas canvas) {
			if (null != maskRect) {
				canvas.clipRect(maskRect);
			}
			super.dispatchDraw(canvas);
		}
		
		@Override
		public void clear() {
			super.clear();
			maskRect = null;
		}

	}
}
