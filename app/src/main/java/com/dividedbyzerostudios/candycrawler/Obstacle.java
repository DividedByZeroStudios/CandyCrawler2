package com.dividedbyzerostudios.candycrawler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private AnimationManager animManager;
    private AnimationManager animManager2;
    private Animation wallTexture;
    private Animation doorTexture;


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

        setTextures();
    }

    public void setTextures() {
        BitmapFactory bf = new BitmapFactory();
        Bitmap wall = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.wall);
        Bitmap door = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.door);

        wallTexture = new Animation(new Bitmap[]{wall}, 1, true);
        doorTexture = new Animation(new Bitmap[]{door}, 1, true);

        animManager = new AnimationManager(new Animation[]{wallTexture});
        animManager2 = new AnimationManager(new Animation[]{doorTexture});
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

        animManager.draw(canvas, rectangle);
        animManager.draw(canvas, rectangle2);
        animManager2.draw(canvas, rectangledoor);


        //canvas.drawRect(rectangle, paint);
        //canvas.drawRect(rectangle2, paint);
        //canvas.drawRect(rectangledoor, paint2);
    }

    @Override
    public void update() {
        if(removeDoor)
            rectangledoor = new Rect(0,0,0,0);

        int state = 0;
        animManager2.playAnim(state);
        animManager2.update();
        animManager.playAnim(state);
        animManager.update();
    }
}
