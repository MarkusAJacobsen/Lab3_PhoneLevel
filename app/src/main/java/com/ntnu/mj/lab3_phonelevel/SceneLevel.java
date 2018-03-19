package com.ntnu.mj.lab3_phonelevel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.SensorEvent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by markusja on 3/2/18.
 */

/**
 * Extended view responsible of drawing the bubble and fence continually with the move logic
 */
public class SceneLevel extends View {
    private Paint paint;
    Context context;

    //Dimensions
    private int width;
    private int height;
    private final int margin = 40;
    private final int stroke  =30;

    //Fence
    private Rect fence;
    private Paint fencePaint;

    //Move variables
    private int xUpperBoundary;
    private int yUpperBoundary;
    private int bothLowerBoundary;
    private final int step = 4;
    private final int pushBack = 100;

    //Bubble
    private int bubbleX;
    private int bubbleY;
    private final int bubbleRadius  =20;
    private Paint bubblePaint;

    /**
     * Constructor, initialize all the different graphical elements which later are displayed
     * @param context Context
     */
    public SceneLevel(Context context) {
        super(context);
        this.context = context;

        fetchWindowDimensions();

        //Fence setup
        fence = new Rect(margin, margin, width-margin, height-margin);
        fencePaint = new Paint();
        fencePaint.setColor(Color.BLACK);
        fencePaint.setStrokeWidth(stroke);
        fencePaint.setStyle(Paint.Style.STROKE);
        xUpperBoundary = width-margin-stroke;
        yUpperBoundary = height-margin-stroke;
        bothLowerBoundary = 0+margin+stroke;

        //Bubble setup
        bubbleX = width/2;
        bubbleY = height/2;
        bubblePaint = new Paint();
        bubblePaint.setColor(Color.RED);
    }

    /**
     * https://stackoverflow.com/a/1016941/7036624
     */
    private void fetchWindowDimensions(){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    /**
     * Overrided draw function. Draw the bubble and fence. Will upon every cycle invalidate the
     * view such that the draw function will always be called and continually updating the view
     * @param canvas Canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(fence, fencePaint);
        canvas.drawCircle(bubbleX, bubbleY, bubbleRadius, bubblePaint);
        invalidate();
    }

    /**
     * Move logic for the ball. Checks if the ball has collided with the fence.
     * If so apply a small "bounce" to the ball, the bounce is in reality more of a pushback
     * On collision trigger a vibrate and a ping
     * @param event SensorEvent
     */
    public void moveBall(SensorEvent event){
        int xCheck =  bubbleX + (int) event.values[1];
        int yCheck = bubbleY + (int) event.values[0];

        //X axis logic
        if(xCheck >= xUpperBoundary) {
            bubbleX = bubbleX-pushBack;
            vibrate();
            ping();
        } else if(xCheck <= bothLowerBoundary) {
            bubbleX = bubbleX+pushBack;
            vibrate();
            ping();
        } else {
            bubbleX = (int) (bubbleX+step*(1*event.values[1]));
        }

        //Y axis logic
        if(yCheck >= yUpperBoundary) {
            bubbleY = bubbleY-pushBack;
            vibrate();
            ping();
        } else if(yCheck <= bothLowerBoundary) {
            bubbleY = bubbleY+pushBack;
            vibrate();
            ping();
        } else {
            bubbleY = (int) (bubbleY+step*(1*event.values[0]));
        }

    }

    /**
     * https://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate
     */
    private void vibrate(){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(100);
    }

    /**
     *  https://stackoverflow.com/questions/4441334/how-to-play-an-android-notification-sound/9622040#9622040
     */

    private void ping(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
