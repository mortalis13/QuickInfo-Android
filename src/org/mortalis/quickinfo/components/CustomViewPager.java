package org.mortalis.quickinfo.components;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class CustomViewPager extends ViewPager {

  public CustomViewPager(Context context) {
    super(context);
    postInitViewPager();
  }

  public CustomViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    postInitViewPager();
  }

  private ScrollerCustomDuration mScroller = null;

  /**
   * Override the Scroller instance with our own class so we can change the
   * duration
   */
  private void postInitViewPager() {
    try {
      Field scroller = ViewPager.class.getDeclaredField("mScroller");
      scroller.setAccessible(true);
      Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
      interpolator.setAccessible(true);

      mScroller = new ScrollerCustomDuration(getContext(), (Interpolator) interpolator.get(null));
      scroller.set(this, mScroller);
    }
    catch (Exception e) {
    }
  }

  /**
   * Set the factor by which the duration will change
   */
  public void setScrollDuration(int scrollFactor) {
    mScroller.setScrollDuration(scrollFactor);
  }

  private class ScrollerCustomDuration extends Scroller {
    private int mScrollFactor = 1;

    public ScrollerCustomDuration(Context context) {
      super(context);
    }

    public ScrollerCustomDuration(Context context, Interpolator interpolator) {
      super(context, interpolator);
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDuration(int scrollFactor) {
      mScrollFactor = scrollFactor;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
      super.startScroll(startX, startY, dx, dy, mScrollFactor);
    }
  }

}
