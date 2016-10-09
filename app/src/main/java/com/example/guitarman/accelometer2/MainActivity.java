package com.example.guitarman.accelometer2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorMan;
    private Sensor accelerometer;
    private Sensor gyrometer;

    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    private int flag=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyrometer=sensorMan.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorMan.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorMan.registerListener(this, gyrometer, SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() ==    Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone();
            TextView tv1=(TextView) findViewById(R.id.tv1);
            TextView tv2=(TextView) findViewById(R.id.tv2);
            TextView tv3=(TextView) findViewById(R.id.tv3);
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            tv1.setText(""+x+"");
            tv2.setText(""+y+"");
            tv3.setText(""+z+"");



            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            //condition for flag
            if(mAccel>55){
                flag=1;
                Vibrator v=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(900);
                Context context=getApplicationContext();
                CharSequence text="Deleting" + mAccel;
                int duration=Toast.LENGTH_SHORT;
                Toast toast=Toast.makeText(context,text,duration);
                toast.show();
            }


            else if (mAccel > 20) {
                flag=0;
                Vibrator v=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(300);
                Context context=getApplicationContext();
                CharSequence text="Successful" + mAccel;
                int duration=Toast.LENGTH_SHORT;
                Toast toast=Toast.makeText(context,text,duration);
                toast.show();

            }


        }
        else if(event.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR){


        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }





    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);
    }




    }
