package com.example.will.motionrecoder;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.SensorEventListener;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
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
import java.util.Stack;

//TYPE_GYROSCOPE first + TYPE_ACCELEROMETER
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    static class sensordata {
        public int sensortype;
        public Long timestamp;
        public String data;
        public sensordata(int a, Long b, String c){
            sensortype = a;
            timestamp = b;
            data = c;
        }
    }

    private SensorManager mSensorManager;
    private Sensor mSensor, mSensor2;
    PrintWriter node;
    Context context = this;
    boolean filestatus = false;
    String filename;
    Stack<sensordata> databuffer = new Stack();
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //6532
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensor2 = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor , 1000);
        mSensorManager.registerListener(this, mSensor2 , 1000);
    }

    public void onSensorChanged(SensorEvent event) {
        TextView tv1 = (TextView) findViewById(R.id.textView);
        tv1.setText("Hello");
        Sensor sensor = event.sensor;
        Long time = event.timestamp;
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
            if(filestatus == true){
                String data = x +","+y + "," + z; //1
                sensordata tmp = new sensordata(1,time,data);
                if(databuffer.isEmpty()) {
                    databuffer.push(tmp);
                }
                else{
                    sensordata poped = databuffer.pop();
                    if (poped.sensortype != 0){
                        //do nothing
                        databuffer.push(tmp);
                    }
                    else{
                        if(Math.abs(poped.timestamp - tmp.timestamp) < 1000000){
                            writeData(tmp.timestamp+", "+ poped.data+", "+tmp.data);
                        }
                        else{
                            databuffer.push(tmp);
                        }
                    }
                }
            }
        }else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
             x = event.values[0];
             y = event.values[1];
             z = event.values[2];
            TextView tv3 = (TextView) findViewById(R.id.textView3);
            tv3.setText("time:"+ time + "\nx:"+x +" \n y:"+y + "\n z:" + z);
            if(filestatus == true){
                String data = x +","+y + "," + z; //0
                sensordata tmp = new sensordata(0,time,data);
                if(databuffer.isEmpty()) {
                    databuffer.push(tmp);
                }
                else{
                    sensordata poped = databuffer.pop();
                    if (poped.sensortype != 1){
                        //do nothing
                        databuffer.push(tmp);
                    }
                    else{
                        if(Math.abs(poped.timestamp - tmp.timestamp) < 1000000){
                            writeData(tmp.timestamp+", "+ tmp.data+", "+poped.data);
                        }
                        else{
                            databuffer.push(tmp);
                        }
                    }
                }
            }
        }

    }


    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }


    public void writeData(String data)
    {
            node.print(data);
            node.append("\r\n");
    }
    public void makewriter(String filename) {
        String filepath = Environment.getExternalStorageDirectory().toString()+ "/Download/"+filename+".txt";
        try
        {
            File file = new File(filepath);
            if(!file.exists()){
                file.createNewFile();
            }
            node = new  PrintWriter(new FileWriter(file,true));}
        catch (Exception e)
            {
                e.printStackTrace();
            }
    }

    public void recordhandler(View v)
    {
        EditText et = (EditText) findViewById(R.id.filename);
        if(filestatus == false) {
            filestatus = true;
            Button p1_button = (Button) v;
            p1_button.setText("Stop");
            filename = et.getText().toString();
            makewriter(filename);
        }
        else{
            et.setText("");
            filestatus = false;
            Button p1_button = (Button) v;
            node.close();
            p1_button.setText("Start Recording");
        }
    }


    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to grant permission
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


}


