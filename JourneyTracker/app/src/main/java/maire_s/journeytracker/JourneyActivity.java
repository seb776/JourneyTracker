package maire_s.journeytracker;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class JourneyActivity extends AppCompatActivity {
    private boolean _isTracking;
    private GPSView _gpsTracker;
    private Button _btnTrack;
    private TextView _gpsActive;
    private TextView _curSpeed;
    private TextView _avgSpeed;
    private TextView _overallTime;
    private Timer _timer;
    private int  _seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _gpsTracker = (GPSView)findViewById(R.id.gpsTracker);
        _btnTrack = (Button) findViewById(R.id.buttonTrack);
        _btnTrack.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                _gpsTracker.SwitchTracking();
                _applyTrackState();
            }
        });

        _timer = null;

        _gpsActive = (TextView)findViewById(R.id.gpsActive);
        _curSpeed = (TextView)findViewById(R.id.curSpeed);
        _avgSpeed = (TextView)findViewById(R.id.avgSpeed);
        _overallTime = (TextView)findViewById(R.id.overallTime);

        _seconds = 0;
        setGPSStatusText(_gpsTracker.GetGPSStatus());
        setCurrentSpeedText(false, 0.0f);
        setAverageSpeedText(0.0f);
        setOverallTimeText(_seconds);
        _applyTrackState();

    }

    public void setGPSStatusText(boolean status)
    {
        _gpsActive.setText("GPS " + (status ? "Active" : "Inactive"));
        if (!status)
        {
            setCurrentSpeedText(false, 0.0f);
        }
    }

    private void setCurrentSpeedText(boolean known, float value)
    {
        _curSpeed.setText("Current speed: " + (known ? Float.toString(value) + "km/h" : "N/A"));
    }

    private void setAverageSpeedText(float value)
    {
        _avgSpeed.setText("Average speed: " + Float.toString(value) + "km/h");
    }

    private void setOverallTimeText(int seconds)
    {
        _overallTime.setText("Overall time: " + Integer.toString(seconds) + "s");
    }

    private void _applyTrackState()
    {
        if (_gpsTracker.IsTracking()) {
            _btnTrack.setText("Stop tracking");
            _timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    ++_seconds;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setOverallTimeText(_seconds);
                            setAverageSpeedText(_gpsTracker.GetAverageSpeed());
                            setCurrentSpeedText(true, _gpsTracker.GetCurrentSpeed());
                        }
                    });
                }
            };
            _timer.scheduleAtFixedRate(timerTask, 0, 1000);
        }
        else {
            _btnTrack.setText("Start Tracking");
            setCurrentSpeedText(false, 0.0f);
            if (_timer != null) {
                _timer.cancel();
                _timer.purge();
                _timer = null;
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
