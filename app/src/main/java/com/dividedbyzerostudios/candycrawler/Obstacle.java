package com.dividedbyzerostudios.candycrawler;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Georg on 12/03/2017.
 */

public class Obstacle implements GameObject {

    private int color;
    private int doorColor;
    private Rect rectangle2;
    private Rect rectangle;
    private Rect rectangledoor;

    private boolean removeDoor;

    public Rect getRectangle() {
        return rectangle;
    }

    public void incrementY (float y) {
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
        rectangledoor.top += y;  //remove this whilst door can't be shot down
        rectangledoor.bottom += y;
    }


    public Obstacle(int rectHeight, int color, int doorColor, int startX, int startY, int playerGap) {

        this.color = color;
        this.doorColor = doorColor;
        rectangle = new Rect(0, startY, startX, startY + rectHeight);
        rectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + rectHeight);
        rectangledoor = new Rect(startX, startY, startX + playerGap, startY + rectHeight);
    }

    public boolean playerCollide(RectPlayer player) {
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle()) || Rect.intersects(rectangledoor, player.getRectangle());
    }

    public boolean NPCCollide(RectNPC NPC) {
        return Rect.intersects(rectangle, NPC.getRectangle()) || Rect.intersects(rectangle2, NPC.getRectangle()) || Rect.intersects(rectangledoor, NPC.getRectangle());
    }

    public boolean bulletCollideObstacle(Bullet bullet) {
        return Rect.intersects(rectangle, bullet.getRectangle()) || Rect.intersects(rectangle2, bullet.getRectangle());
    }

    public boolean bulletCollideDoor(Bullet bullet) {
        if (Rect.intersects(rectangledoor, bullet.getRectangle())) {
            removeDoor = true;
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        Paint paint2 = new Paint();
        paint2.setColor(doorColor);
        canvas.drawRect(rectangle, paint);
        canvas.drawRect(rectangle2, paint);
        canvas.drawRect(rectangledoor, paint2);
    }

    @Override
    public void update() {
        if(removeDoor)
            rectangledoor = new Rect(0,0,0,0);
    }
}
