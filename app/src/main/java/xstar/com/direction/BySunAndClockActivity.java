package xstar.com.direction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BySunAndClockActivity extends AppCompatActivity {

    ClockDirectionView cdv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_sun_and_clock);
        cdv = (ClockDirectionView) findViewById(R.id.cdv);
        cdv.setScale_len(20);
    }
}
