package com.dividedbyzerostudios.candycrawler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.dividedbyzerostudios.candycrawler.Constants.SCREEN_HEIGHT;

/**
 * Created by Georg on 12/03/2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;

    public static final int WIDTH = 540;
    public static final int HEIGHT = 960;

    private SceneManager manager;

    private Background bg;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        Constants.CURRENT_CONTEXT = context;

        thread = new MainThread(getHolder(), this);

        manager = new SceneManager();

        setFocusable(true);
    }

        @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
    public void surfaceCreated(SurfaceHolder holder) {
            bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));
            bg.setVector(Constants.bgMove);
            thread = new MainThread(getHolder(), this);


            thread.setRunning(true);
            thread.start();
        }

        @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            while(true) {
                try {
                    thread.setRunning(false);
                    thread.join();
                } catch(Exception e) {e.printStackTrace();}
                retry = false;
            }
            }

@Override
public boolean onTouchEvent (MotionEvent event) {

    manager.recieveTouch(event);

    return true;
    //return super.onTouchEvent(event);
}

    public void update() {

        manager.update();
        bg.setVector(Constants.bgMove);
        bg.update();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        bg.draw(canvas);

        manager.draw(canvas);
    }
}
