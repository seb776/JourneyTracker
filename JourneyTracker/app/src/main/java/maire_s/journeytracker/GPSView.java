package maire_s.journeytracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class GPSView extends View {
    private float _maxSpeedDisplay;
    private float _speedDisplaySteps;
    private int _samplesCount;

    private float _avgSpeed;
    private float _currentSpeed;
    private boolean _isTracking;

    public GPSView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _maxSpeedDisplay = 60.0f;
        _speedDisplaySteps = 10.0f;
        _isTracking = false;
    }


    public void SwitchTracking()
    {
        _isTracking = !_isTracking;
    }
    public boolean IsTracking()
    {
        return _isTracking;
    }
    @Override
    public void onDraw(Canvas canvas)
    {
        canvas.translate(0, 0);
        super.onDraw(canvas);
        Paint black = new Paint();
        black.setColor(Color.rgb(0, 0, 0));

        Paint green = new Paint();
        green.setColor(Color.rgb(0, 255, 0));
        green.setStrokeWidth(2);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), black);
        float verticalStepsSpeed = canvas.getHeight() / (_maxSpeedDisplay / _speedDisplaySteps);
        float speedLine = 0.0f;
        while (speedLine < canvas.getHeight())
        {
            canvas.drawLine(0.0f, speedLine, canvas.getWidth(), speedLine, green);
            speedLine += verticalStepsSpeed;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec - 200);
    }


}