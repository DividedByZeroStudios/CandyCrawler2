package com.dividedbyzerostudios.candycrawler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Georg on 22/03/2017.
 */


public class RectNPC implements GameObject {
    private Rect rectangle;
    private int color;
    private float xVelocity;

    private Animation idle;
    private Animation walkRight;
    private Animation walkLeft;
    private AnimationManager animManager2;

    public Rect getRectangle() {
        return rectangle;
    }

    public Rect setRectangle(int inc) {
        rectangle.top += inc;
        rectangle.bottom += inc;
        return rectangle;
    }

    public RectNPC(Rect rectangle, int color, float xVelocity) {
        this.rectangle = rectangle;
        this.color = color;
        if(Math.random() >= 0.5)
            this.xVelocity = xVelocity;
        else
            this.xVelocity = -xVelocity;

        int randomiserX = (int)(Math.random()*Constants.SCREEN_WIDTH) - rectangle.width();
        rectangle.left = rectangle.left + randomiserX;
        rectangle.right = rectangle.right + randomiserX;

        int randomiserY = (int)(Math.random()*-(Constants.SCREEN_HEIGHT)/4);
        rectangle.top = rectangle.top + randomiserY;
        rectangle.bottom = rectangle.bottom + randomiserY;

        BitmapFactory bf = new BitmapFactory();
        Bitmap idleImg = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.spider);
        Bitmap walk1 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.spider_walk1);
        Bitmap walk2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.spider_walk2);

        idle = new Animation(new Bitmap[]{idleImg}, 2);
        walkRight = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), m, false);

        walkLeft = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);

        animManager2 = new AnimationManager(new Animation[]{idle, walkRight, walkLeft});
    }

    public float getxVelocity() {
        return xVelocity;
    }

    public  void setxVelocity(float value) {
        this.xVelocity = value;
    }

    public void incrementY (float y) {
        rectangle.top += y;
        rectangle.bottom += y;
    }

    public void moveX (RectNPC NPC) {
        if(rectangle.right >= Constants.SCREEN_WIDTH) {
            NPC.setxVelocity(-Math.abs(NPC.xVelocity));
        } else if ( rectangle.left <= 0) {
            NPC.setxVelocity(+Math.abs(NPC.xVelocity));
        }
        NPC.rectangle.left += NPC.xVelocity;
        NPC.rectangle.right += NPC.xVelocity;
    }

    @Override
    public void draw(Canvas canvas) {
        /*Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);*/
        animManager2.draw(canvas, rectangle);
    }

    public boolean playerCollideNPC(RectPlayer player) {
        return Rect.intersects(rectangle, player.getRectangle());
    }

    public boolean bulletCollideNPC(Bullet bullet) {
        return Rect.intersects(rectangle, bullet.getRectangle());
    }

    @Override
    public void update() {
        int state = 0;
        if(xVelocity > 0)
            state = 1;
        else if(xVelocity < -5)
            state = 2;

        animManager2.playAnim(state);
        animManager2.update();
    }

    public void update(Point point) {
        animManager2.update();
    }
}
