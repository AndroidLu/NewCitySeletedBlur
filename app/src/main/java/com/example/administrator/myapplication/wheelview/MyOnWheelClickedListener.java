package com.example.administrator.myapplication.wheelview;

/**
 * Wheel clicked listener interface.
 * <p>The onItemClicked() method is called whenever a wheel item is clicked
 * <li> New Wheel position is set
 * <li> Wheel view is scrolled
 */
public interface MyOnWheelClickedListener {
    /**
     * Callback method to be invoked when current item clicked
     * @param wheel the wheel view
     * @param itemIndex the index of clicked item
     */
    void onItemClicked(MyWheelView wheel, int itemIndex);
}
