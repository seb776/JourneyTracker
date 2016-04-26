package maire_s.journeytracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class JourneyActivity extends AppCompatActivity {
    private boolean _isTracking;
    private GPSView _gpsTracker;
    private Button _btnTrack;
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

        _applyTrackState();


    }

    private void _applyTrackState()
    {
        if (_gpsTracker.IsTracking()) {
            _btnTrack.setText("Stop tracking");
        }
        else {
            _btnTrack.setText("Start Tracking");
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
