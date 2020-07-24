package Tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

public class MyImageView extends androidx.appcompat.widget.AppCompatImageView {

    private TouchImageViewListener listener;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                Log.e("touch", "touch up");
                if (listener != null)
                    listener.onTouch(false);
                break;
            case MotionEvent.ACTION_DOWN:

                Log.e("touch", "touch down");
                if (listener != null)
                    listener.onTouch(true);

                break;
            case MotionEvent.ACTION_MOVE:

                Log.e("touch", "touch move");

                break;
        }

        return false;
    }

    public interface TouchImageViewListener {
        void onTouch(boolean isDown);
    }


    public void setOnTouchImageViewListener(TouchImageViewListener listener) {
        this.listener = listener;
    }


}
