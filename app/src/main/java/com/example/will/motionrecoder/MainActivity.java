package com.example.will.motionrecoder;

import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor, mSensor2;
    Context context = this;
    boolean filestatus = false;

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
        mSensorManager.registerListener(this, mSensor , 1000);
        mSensorManager.registerListener(this, mSensor2 , 1000);
    }

    public void writeData(String data,String filename)
    {
        PrintWriter node;
        String filepath ="/root/sdcard/Pictures/"+ filename;
        try
        {

            File file = new File(filepath);
            if(!file.exists()){
                file = new File(filepath);
            }
            node = new  PrintWriter(new FileWriter(file,true));
            node.print(data+","+"hello");
            node.append("\r\n");
            node.print("world");
            node.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void recordhandler(View v)
    {
        if(filestatus == false) {
            filestatus = true;
            Button p1_button = (Button) v;
            p1_button.setText("Stop");
        }
        else{
            EditText et = (EditText) findViewById(R.id.username);
            et.setText("");
            filestatus = false;
            Button p1_button = (Button) v;
            p1_button.setText("Start Recording");
        }
    }


}


