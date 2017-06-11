package xstar.com.direction;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener{

    private SensorManager sm;
    // 需要两个Sensor
    private Sensor aSensor;
    private Sensor mSensor;

    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    private static final String TAG = "sensor";

    DirectionView dv;

    Button sun_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dv = (DirectionView) findViewById(R.id.dv);
        sun_btn = (Button) findViewById(R.id.sun_btn);
        sun_btn.setOnClickListener(this);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        registerSensor();
        // 更新显示数据的方法
        calculateOrientation();
        sunLocation();
        handler.sendEmptyMessageDelayed(MSG_WHAT, MSG_DELAY);
    }


    private static final int MSG_WHAT = 100;
    private static final int MSG_DELAY = 500;

    // 再次强调：注意activity暂停的时候释放
    public void onPause() {
        super.onPause();
        sm.unregisterListener(myListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerSensor();
    }

    private void registerSensor() {
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sm.registerListener(myListener, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(myListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magneticFieldValues = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = sensorEvent.values;
            calculateOrientation();
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private float degress = 0;

    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        // 要经过一次数据格式的转换，转换为度
        values[0] = (float) Math.toDegrees(values[0]);
        Log.i(TAG, values[0] + "");
        // values[1] = (float) Math.toDegrees(values[1]);
        // values[2] = (float) Math.toDegrees(values[2]);
        degress = values[0];
        if (degress < 0) degress = 360 + degress;
        Log.i(TAG, String.format("degress:%.2f", values[0]));
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT:
                    dv.setDirectAngle((int) degress);
                    handler.sendEmptyMessageDelayed(MSG_WHAT, MSG_DELAY);
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(MSG_WHAT);
    }

    private void sunLocation() {
        // 116.402056,39.914334
        double latitude = 39.914334;
        double longitude = 116.402056;
        int year = 2016;
        int month = 12;
        int day = 12;
        int timezone = 8;
        int dlstime = 0;// 夏令时
        SunCalc sunCalc = new SunCalc();
        double sunset = sunCalc.sunset(latitude, longitude, year, month, day, timezone, dlstime);
        System.out.println("sunset:" + sunset);
        System.out.println("天文学黎明dawn:" + sunCalc.dawn(latitude, longitude, year, month, day, timezone, dlstime, 18) * 24);
        System.out.println("航海黎明dawn:" + sunCalc.dawn(latitude, longitude, year, month, day, timezone, dlstime, 12) * 24);
        System.out.println("民事黎明dawn:" + sunCalc.dawn(latitude, longitude, year, month, day, timezone, dlstime, 6) * 24);
        System.out.println("日出:" + sunCalc.sunrise(latitude, longitude, year, month, day, timezone, dlstime) * 24);
        System.out.println("日中:" + sunCalc.solarnoon(latitude, longitude, year, month, day, timezone, dlstime) * 24);
        System.out.println("日落:" + sunCalc.sunset(latitude, longitude, year, month, day, timezone, dlstime) * 24);
        System.out.println("民事黄昏:" + sunCalc.dusk(latitude, longitude, year, month, day, timezone, dlstime, 6) * 24);
        System.out.println("航海黄昏:" + sunCalc.dusk(latitude, longitude, year, month, day, timezone, dlstime, 12) * 24);
        System.out.println("天文学黄昏:" + sunCalc.dusk(latitude, longitude, year, month, day, timezone, dlstime, 18) * 24);
        // 2:03 太阳天文黎明 民事黎明相差12个角度
        double solarazimuth = sunCalc.solarazimuth(latitude, longitude, year, month, day, 14, 12, 0, timezone, dlstime);// 太阳方位角
        double solarelevation = sunCalc.solarelevation(latitude, longitude, year, month, day, 14, 12, 0, timezone, dlstime);// 太阳海拔
        double[] solarposition = sunCalc.solarposition(latitude, longitude, year, month, day, 14, 12, 0, timezone, dlstime);
        System.out.println("太阳方位角:" + solarazimuth);
        System.out.println("太阳海拔:" + solarelevation);
        System.out.println("太阳位置:" + solarposition[0] + "/" + solarposition[1]);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this,BySunAndClockActivity.class));
        finish();
    }
}
