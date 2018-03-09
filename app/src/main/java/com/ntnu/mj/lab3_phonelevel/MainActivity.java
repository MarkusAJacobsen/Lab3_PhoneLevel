package com.ntnu.mj.lab3_phonelevel;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Main activity
 */
public class MainActivity extends AppCompatActivity {
    SensorManager manager;
    Sensor sensor;
    SceneLevel ball;
    LevelListener listener;

    /**
     * Called when the application launches
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove the actionbar and run in fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();



        //Fetch accelerometer
        manager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Main view displaying the animation
        ball = new SceneLevel(getApplicationContext());

        //Listener for the sensor manager
        listener = new LevelListener(ball);

        //Set content view to the custom view
        setContentView(ball);
    }

    /**
     * Called after onCreate, registers the listener for the accelerometer
     */
    @Override
    public void onResume(){
        super.onResume();
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * If the view is paused, decouple the listener for the accelerometer
     */
    @Override
    public void onPause(){
        super.onPause();
        manager.unregisterListener(listener);
    }


    /**
     * Listener for the accelerometer, calls SceneLevel's moveBall with received event
     */
    private class LevelListener implements SensorEventListener {
        private SceneLevel level;

        LevelListener(SceneLevel level) {
            this.level = level;
        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            level.moveBall(sensorEvent);
        }

        /** Not used, NOOP
         * @param sensor Sensor
         * @param i int
         */
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

}
