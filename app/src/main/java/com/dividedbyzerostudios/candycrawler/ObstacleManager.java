package com.dividedbyzerostudios.candycrawler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.dividedbyzerostudios.candycrawler.MainThread.canvas;

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

    private Rect r = new Rect();

    private long startTime;
    private long initTime;

    private int score = 0;
    private int tempTime = 0;
    public boolean plusScore = false;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        populateObstacles();
    }

    public boolean playerCollide(RectPlayer player) {
        for(Obstacle ob : obstacles) {
            if(ob.playerCollide(player))
                return true;
        }
        return false;
    }

    private void populateObstacles() {
        int currY = -5*Constants.SCREEN_HEIGHT/4;
        while(currY < 0) {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;
        }
    }

    public void update() {
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float)(Math.sqrt(1 + (startTime - initTime)/2000.0))*Constants.SCREEN_HEIGHT/(10000.0f); //change the first divider to change speed, higher slows down
        for(Obstacle ob : obstacles) {
            ob.incrementY(speed * elapsedTime);
        }
        if(obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart, obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
            obstacles.remove(obstacles.size() - 1);
            score += 10;
            plusScore = true;
            tempTime = (int)System.currentTimeMillis();
        }
    }

    public void draw(Canvas canvas) {
        for(Obstacle ob : obstacles)
            ob.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.BLACK);
        canvas.drawText(" " + score, 50, 50 + paint.descent() - paint.ascent(), paint);

        if(plusScore) {
            if ((int)System.currentTimeMillis() - tempTime >= 1000) {
                plusScore = false;
            }
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
        float x = cWidth - r.width() / 4f - r.left;
        float y = r.height() - r.bottom + 100f;
        canvas.drawText(text, x, y,paint);

    }
}

