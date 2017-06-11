package xstar.com.direction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BySunAndClockActivity extends AppCompatActivity implements View.OnClickListener {

    ClockDirectionView cdv;

    Button back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_sun_and_clock);
        cdv = (ClockDirectionView) findViewById(R.id.cdv);
        back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        cdv.setScale_len(20);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                startActivity(new Intent(this, GuideActivity.class));
                finish();
                break;
        }
    }
}
