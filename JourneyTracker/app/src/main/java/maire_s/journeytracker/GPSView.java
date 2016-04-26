package maire_s.journeytracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class GPSView extends View {
    private float _maxSpeedDisplay;
    private float _speedDisplaySteps;
    private int _samplesCount;

    private float _avgSpeed;
    private float _currentSpeed;
    private boolean _isTracking;

    private LocationListener _locListen;
    private LocationManager _locMgr;
    private final int _maxSamples;

    Deque<Location> _locations;

    public GPSView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _maxSpeedDisplay = 60.0f;
        _speedDisplaySteps = 10.0f;
        _isTracking = true;
        _maxSamples = 100;

        _locations = new ArrayDeque<Location>(_maxSamples);
        _locMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        _locListen = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (_isTracking)
                {
                    _locations.addLast(location);
                    if (_locations.size() >= _maxSamples)
                    {
                        _locations.removeFirst();
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        SwitchTracking();
    }


    public void SwitchTracking()
    {
        _isTracking = !_isTracking;
        if (_isTracking)
        {
            _locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, _locListen);
        }
        else
        {
            _locMgr.removeUpdates(_locListen);
        }
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