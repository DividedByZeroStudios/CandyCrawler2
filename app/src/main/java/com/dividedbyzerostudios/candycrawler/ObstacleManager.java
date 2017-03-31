package com.dividedbyzerostudios.candycrawler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by Georg on 13/03/2017.
 */

public class ObstacleManager {
    //Higher index = lower on screen = higher y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;
    private int doorcolor;

    private Rect r = new Rect();
    private float x = (float) Math.random() * (Constants.SCREEN_WIDTH + 20);
    private float y = (float) Math.random() * (Constants.SCREEN_HEIGHT + 20);

    private long startTime;
    private long initTime;

    private int tempTime = 0;
    private boolean plusScore = false;
    private boolean doorCheck = false;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color, int doorcolor) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;
        this.doorcolor = doorcolor;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        Constants.SCORE = 0;

        populateObstacles();
    }

    public boolean playerCollide(RectPlayer player) {
        for(Obstacle ob : obstacles) {
            if(ob.playerCollide(player))
                return true;
        }
        return false;
    }

    public boolean NPCCollide(RectNPC NPC) {
        for(Obstacle ob : obstacles) {
            if(ob.NPCCollide(NPC))
                return true;
        }
        return false;
    }

    public boolean BULLETCollide(Bullet bullet) {
        for(Obstacle ob : obstacles) {
            if(ob.bulletCollideObstacle(bullet))
                return true;
        }
        return false;
    }

    public boolean BULLETCollideDoor(Bullet bullet) {
        for(Obstacle ob : obstacles) {
            if(ob.bulletCollideDoor(bullet)) {
                return true;
            }
        }
        return false;
    }

    private void populateObstacles() {
        int currY = -5*Constants.SCREEN_HEIGHT/4;
        while(currY < 0) {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight, color, doorcolor, xStart, currY, playerGap + 50));
            currY += obstacleHeight + obstacleGap;
        }
    }

    public void update() {
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float)(Math.sqrt(1 + (startTime - initTime)/2000.0))*Constants.SCREEN_HEIGHT/(10000.0f); //change the first divider to change speed, higher slows down
        for(Obstacle ob : obstacles) {
            ob.incrementY(speed * elapsedTime);
            ob.update();
        }


        if(obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, color, doorcolor, xStart, obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
            obstacles.remove(obstacles.size() - 1);
            Constants.SCORE += 10;
            plusScore = true;
            tempTime = (int)System.currentTimeMillis();
            x = (float) (Math.random() * (Constants.SCREEN_WIDTH)) + 100;
            y = (float) (Math.random() * ((Constants.SCREEN_HEIGHT /2))) + 100;

        }
    }

    public void draw(Canvas canvas) {
        for(Obstacle ob : obstacles)
            ob.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        canvas.drawText(" " + Constants.SCORE, 50, 50 + paint.descent() - paint.ascent(), paint);

        if(plusScore) {
            if ((int)System.currentTimeMillis() - tempTime >= 200)
                plusScore = false;
            Paint paint2 = new Paint();
            paint2.setTextSize(100);
            paint2.setColor(Color.GREEN);
            drawPlusScore(canvas, paint2, "+10!");
        }
    }
    private void drawPlusScore(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        canvas.drawText(text, x, y,paint);

    }
}

