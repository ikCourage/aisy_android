package org.aisy.autoclear;

import java.util.ArrayList;

import org.aisy.interfaces.IClear;

public class AisyAutoClear
{
	/**
	 * AutoClear 集合
	 */
	static protected ArrayList<AutoClear> autoClears;
	/**
	 * 当前 AutoClear
	 */
	static protected AutoClear currentAutoClear;
	
	public AisyAutoClear()
	{
	}
	
	/**
	 * 创建一个新的 AutoClear
	 * @return 
	 */
	static public AutoClear newAutoClear()
	{
		return new AutoClear();
	}
	
	/**
	 * 将 autoClear 设置为当前的 AutoClear
	 * @param autoClear
	 */
	static public void setCurrentAutoClear(AutoClear autoClear)
	{
		if (null == autoClear) return;
		currentAutoClear = autoClear;
		int i = autoClears.lastIndexOf(currentAutoClear);
		if (i != -1) autoClears.remove(i);
		autoClears.add(currentAutoClear);
		autoClear = null;
	}
	
	/**
	 * 返回当前的 AutoClear
	 * @return 
	 */
	static public AutoClear getCurrentAutoClear()
	{
		return currentAutoClear;
	}
	
	/**
	 * 添加一个 IClear 到当前的 AutoClear 中
	 * @param iclear
	 */
	static public void put(IClear iclear)
	{
		if (null == iclear) return;
		else if (null == currentAutoClear) newAutoClear();
		currentAutoClear.put(iclear);
		iclear = null;
	}
	
	/**
	 * 在全部 AutoClear 中移除一个 IClear
	 * @param iclear
	 * @return 
	 */
	static public boolean remove(IClear iclear)
	{
		if (null != iclear && null != currentAutoClear) {
			int i = autoClears.size();
			while (i > 0) {
				i--;
				if (autoClears.get(i).remove(iclear) == true) {
					iclear = null;
					return true;
				}
			}
		}
		iclear = null;
		return false;
	}
	
	/**
	 * 在全部 AutoClear 中是否包含一个 IClear
	 * @param iclear
	 * @return 
	 */
	static public boolean has(IClear iclear)
	{
		if (null != iclear && null != currentAutoClear) {
			int i = autoClears.size();
			while (i > 0) {
				i--;
				if (autoClears.get(i).has(iclear) == true) {
					iclear = null;
					return true;
				}
			}
		}
		iclear = null;
		return false;
	}
	
	/**
	 * 清除所有的 AutoClear
	 */
	static public void clear()
	{
		if (null == currentAutoClear) return;
		currentAutoClear = null;
		for (int i = 0, l = autoClears.size(); i < l; i++) autoClears.get(i).clear();
		autoClears.clear();
		autoClears = null;
	}
	
	static public class AutoClear implements IClear
	{
		/**
		 * IClear 集合
		 */
		protected ArrayList<IClear> v;
		/**
		 * 是否正在清除
		 */
		protected boolean c;
		
		public AutoClear()
		{
			if (null == autoClears) autoClears = new ArrayList<AutoClear>();
			autoClears.add(this);
			currentAutoClear = this;
		}
		
		/**
		 * 添加一个 IClear
		 * @param iclear
		 */
		public void put(IClear iclear)
		{
			if (null == iclear) return;
			else if (null == v) v = new ArrayList<IClear>();
			if (v.lastIndexOf(iclear) == -1) v.add(iclear);
			iclear = null;
		}
		
		/**
		 * 移除一个 IClear
		 * @param iclear
		 * @return 
		 */
		public boolean remove(IClear iclear)
		{
			if (c == false && null != v && null != iclear) {
				int i = v.lastIndexOf(iclear);
				iclear = null;
				if (i != -1) {
					v.remove(i);
					return true;
				}
			}
			iclear = null;
			return false;
		}
		
		/**
		 * 是否包含一个 ICler
		 * @param iclear
		 * @return 
		 */
		public boolean has(IClear iclear)
		{
			if (null != v && null != iclear) return v.lastIndexOf(iclear) != -1;
			iclear = null;
			return false;
		}
		
		/**
		 * 清除所有的 IClear
		 */
		public void clear()
		{
			if (null != v) {
				c = true;
				for (int i = 0, l = v.size(); i < l; i++) v.get(i).clear();
				v.clear();
				v = null;
				c = false;
			}
			if (null != currentAutoClear) {
				int j = autoClears.lastIndexOf(this);
				if (j != -1) {
					if (autoClears.size() == 1) {
						autoClears.clear();
						autoClears = null;
						currentAutoClear = null;
					}
					else {
						autoClears.remove(j);
						currentAutoClear = autoClears.get(autoClears.size() - 1);
					}
				}
				AisyAutoClear.remove(this);
			}
		}
	}
	
}
