package maire_s.journeytracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;

public class GPSView extends View {
    private float _maxSpeedDisplay;
    private float _speedDisplaySteps;
    private int _samplesCount;
    private float _avgSpeed;
    private float _currentSpeed;
    private boolean _isTracking;
    private ArrayList<Location> _locations;
    private LocationListener _locListen;
    private LocationManager _locMgr;
    private final int _maxSamples;
    private float _averageSpeed;

    private Paint _green;
    private Paint _red;
    private Paint _white;
    private Paint _black;

    public GPSView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        _green = new Paint();
        _green.setColor(Color.rgb(0, 255, 0));
        _green.setStrokeWidth(2);

        _red = new Paint();
        _red.setColor(Color.rgb(255, 0, 0));
        _red.setStrokeWidth(2);

        _white = new Paint();
        _white.setColor(Color.rgb(255,255,255));
        _white.setStrokeWidth(2);

        _black = new Paint();
        _black.setColor(Color.rgb(0, 0, 0));

        _maxSpeedDisplay = 60.0f;
        _speedDisplaySteps = 10.0f;
        _isTracking = true;
        _maxSamples = 100;

        _locations = new ArrayList<Location>(_maxSamples);
        _locMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        final GPSView self = this; // to access the right "this" through the LocationListener
        _locListen = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (_isTracking)
                {
                    _locations.add(location);
                    if (_locations.size() >= _maxSamples)
                    {
                        _locations.remove(0);
                    }
                    _calculateAverageSpeed();
                    self.invalidate(); // To udate visual
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //((JourneyActivity)context).setGPSStatusText(status == LocationProvider.AVAILABLE);
            }

            @Override
            public void onProviderEnabled(String provider) {
                ((JourneyActivity)context).setGPSStatusText(true);
            }

            @Override
            public void onProviderDisabled(String provider) {
                ((JourneyActivity)context).setGPSStatusText(false);
            }
        };
        SwitchTracking();
    }
    public boolean GetGPSStatus()
    {
        return _locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void SwitchTracking()
    {
        _isTracking = !_isTracking;
        if (_isTracking)
        {
            _locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, _locListen);
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
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), _black);

        float verticalStepsSpeed = canvas.getHeight() / (_maxSpeedDisplay / _speedDisplaySteps);
        float speedLine = 0.0f;
        while (speedLine < canvas.getHeight())
        {
            canvas.drawLine(0.0f, speedLine, canvas.getWidth(), speedLine, _white);
            speedLine += verticalStepsSpeed;
        }
        final float averageSpeedLine = canvas.getHeight() - ( (canvas.getHeight() / _maxSpeedDisplay) * _averageSpeed);

        canvas.drawLine(0.0f, averageSpeedLine, canvas.getWidth(), averageSpeedLine, _red);

        float heightMult = canvas.getHeight() / _maxSpeedDisplay;
        int xSpace = canvas.getWidth() / (_maxSamples - 1);
        int xIt = 0;
        for (int i = 0; i < _locations.size(); ++i)
        {
            int nextIndex = Math.min(i + 1, _locations.size() - 1);
            int xStop = nextIndex * xSpace;
            int yStart = canvas.getHeight() - (int)(heightMult * _locations.get(i).getSpeed());
            int yStop = canvas.getHeight () - (int)(heightMult * _locations.get(nextIndex).getSpeed());
            canvas.drawLine(xIt, yStart, xStop, yStop, _green);
            xIt += xSpace;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int size = Math.min(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(size, size - 200);
    }

    public float GetAverageSpeed()
    {
        return _averageSpeed;
    }
    public float GetCurrentSpeed()
    {
        if (_locations.size() > 0)
            return _locations.get(_locations.size() - 1).getSpeed();
        return 0.0f;
    }
    private void _calculateAverageSpeed()
    {
        float avg = 0.0f;
        for (Location l : _locations)
            avg += l.getSpeed();
        avg /= (float)_locations.size();
        _averageSpeed = avg;
    }
}