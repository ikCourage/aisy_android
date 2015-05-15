package org.aisy.queue;

import java.util.ArrayList;

import org.aisy.interfaces.IClear;

public class Queue
{
	protected ArrayList<QueueValue> _queue;

	public Queue()
	{
	}
	
	public void put(Object value, IQueueDelegate delegate)
	{
		put(value, delegate, 0);
	}
	
	public void put(Object value, IQueueDelegate delegate, int priority)
	{
		if (null == value) {
			if (null != _queue) {
				if (_queue.size() == 1) clear();
				else {
					_queue.remove(0).clear();
					QueueValue v = _queue.get(0);
					value = v.value;
					delegate = v.delegate;
					v = null;
				}
			}
		}
		else {
			if (null == _queue) {
				_queue = new ArrayList<Queue.QueueValue>();
				_queue.add(new QueueValue(value, delegate, priority));
			}
			else {
				_queue.add(new QueueValue(value, delegate, priority));
				if (priority != 0) quickSort(_queue, 0, _queue.size());
				value = null;
			}
		}
		if (null != value && null != delegate) {
			delegate.init(value);
		}
		value = null;
		delegate = null;
	}

	protected void quickSort(ArrayList<QueueValue> v, int left, int right)
	{
		int i = left, j = right, middle = v.get((left + right) >> 1).priority;
		QueueValue t;
		do {
			while (v.get(i).priority < middle && i < right) i++;
			while (v.get(j).priority > middle && j > left) j--;
			if (i <= j) {
				t = v.get(i);
				v.set(i, v.get(j));
				v.set(j, t);
				i++;
				if (j > 0) j--;
			}
		} while (i <= j);
		if (left < j) quickSort(v, left, j);
		if (right > i) quickSort(v, i, right);
		t = null;
		v = null;
	}

	public void clear()
	{
		if (null != _queue) {
			for (int i = 0, l = _queue.size(); i < l; i++) {
				_queue.get(i).clear();
			}
			_queue.clear();
			_queue = null;
		}
	}
	
	protected class QueueValue implements IClear
	{
		public Object value;
		public IQueueDelegate delegate;
		public int priority;
		
		public QueueValue(Object value, IQueueDelegate delegate, int priority)
		{
			this.value = value;
			this.delegate = delegate;
			this.priority = priority;
		}
		
		@Override
		public void clear() {
			value = null;
			delegate = null;
		}
		
	}
	
	public interface IQueueDelegate
	{
		
		void init(Object value);
		
	}
	
}
