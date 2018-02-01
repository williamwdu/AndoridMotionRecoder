package com.example.will.motionrecoder;

import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Context;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor, mSensor2;
    Context context = this;
    public void onSensorChanged(SensorEvent event) {
        TextView tv1 = (TextView) findViewById(R.id.textView);
        tv1.setText("Hello");
        Sensor sensor = event.sensor;
        long time = event.timestamp;
        float x,y,z;
        x=0;
        y=0;
        z=0;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
             x = event.values[0];
             y = event.values[1];
             z = event.values[2];
            TextView tv2 = (TextView) findViewById(R.id.textView2);
            tv2.setText("time:"+ time + "\nx:"+x +" \n y:"+y + "\n z:" + z);
        }else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
             x = event.values[0];
             y = event.values[1];
             z = event.values[2];
            TextView tv3 = (TextView) findViewById(R.id.textView3);
            tv3.setText("time:"+ time + "\nx:"+x +" \n y:"+y + "\n z:" + z);
        }

    }


    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //6532
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensor2 = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor , SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensor2 , SensorManager.SENSOR_DELAY_GAME);
    }


}


