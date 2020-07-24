package Tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class OverRecyclerView extends RecyclerView {

    private int lastEventX;
    private boolean scrollable = true;
    private OverScrollListener listener;


    public OverRecyclerView(@NonNull Context context) {
        super(context);
    }

    public OverRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OverRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int eventX = (int) event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                int xDistance = (int) getTranslationX();

                /*if (xDistance != 0 && listener != null) {

                    if (!listener.onOverScroll(xDistance, true)) {
                        animate().translationX(0)
                                .setDuration(200)
                                .setInterpolator(new DecelerateInterpolator(6))
                                .start();
                    }
                }*/
                break;
            case MotionEvent.ACTION_DOWN:

                lastEventX = eventX;
                break;
            case MotionEvent.ACTION_MOVE:

                int offset = computeHorizontalScrollOffset();
                int extent = computeHorizontalScrollExtent();
                int range = computeHorizontalScrollRange();

                int percentage = (int) (100.0 * offset / (float) (range - extent));


                if (percentage == 100 || percentage == 0) {
                    handleOverScroll(event, false);
                } else {
                    View view = getChildAt(getChildCount() - 1);
                    if (view.getWidth() <= (getWidth() + getScrollX())) {
                        handleOverScroll(event, true);
                    }
                }
                break;
        }

        if (getTranslationY() != 0) {
            return true;
        }
        return scrollable && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scrollable && super.onInterceptTouchEvent(ev);
    }

    /*public void setScrollingEnabled(boolean enabled) {
        scrollable = enabled;
    }
*/

    public void setOverScrollListener(OverScrollListener listener) {
        this.listener = listener;
    }

    private void handleOverScroll(MotionEvent ev, boolean isBottom) {
        int pointerCount = ev.getHistorySize();

        for (int p = 0; p < pointerCount; p++) {

            int historicalX = (int) ev.getHistoricalX(p);

            int xDisatnce = (historicalX - lastEventX) / 3;

            if (!isBottom) {

//                if (xDisatnce < 0)
//                setTranslationX(xDisatnce);
                Log.e("Scroll", xDisatnce + "");

                if (listener != null)
                    listener.onOverScroll(xDisatnce, false);
            }
        }
    }

    public interface OverScrollListener {
        boolean onOverScroll(int xDistance, boolean isReleased);
    }

}
