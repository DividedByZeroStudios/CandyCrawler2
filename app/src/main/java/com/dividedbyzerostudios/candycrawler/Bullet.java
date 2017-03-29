package com.dividedbyzerostudios.candycrawler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Georg on 27/03/2017.
 */

public class Bullet implements GameObject {

    private int x;
    private int y;
    private Rect rectangle;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        rectangle = new Rect(x + 7, y + 7, x - 7, y - 7);
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public void tick(boolean friendly) {
        if(friendly) {
            rectangle.top -= 30;
            rectangle.bottom -= 30;
        }
        else {
            rectangle.top += 30;
            rectangle.bottom += 30;
        }
    }

    public int getY() {
        return rectangle.bottom;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255, 192, 203));
        canvas.drawRect(rectangle, paint);
    }
}
