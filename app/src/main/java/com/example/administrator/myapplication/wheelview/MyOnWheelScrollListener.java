package com.example.administrator.myapplication.wheelview;

/**
 * Wheel scrolled listener interface.
 */
public interface MyOnWheelScrollListener {
	/**
	 * Callback method to be invoked when scrolling started.
	 * @param wheel the wheel view whose state has changed.
	 */
	void onScrollingStarted(MyWheelView wheel);
	
	/**
	 * Callback method to be invoked when scrolling ended.
	 * @param wheel the wheel view whose state has changed.
	 */
	void onScrollingFinished(MyWheelView wheel);
}
