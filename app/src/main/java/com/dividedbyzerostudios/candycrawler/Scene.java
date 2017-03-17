package com.dividedbyzerostudios.candycrawler;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Georg on 17/03/2017.
 */

public interface Scene {
    public void update();
    public void draw(Canvas canvas);
    public void terminate();
    public void recieveTouch(MotionEvent event);
}
