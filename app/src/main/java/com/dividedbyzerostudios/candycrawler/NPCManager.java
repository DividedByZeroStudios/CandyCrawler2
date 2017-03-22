package com.dividedbyzerostudios.candycrawler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by Georg on 22/03/2017.
 */

public class NPCManager {
    private ArrayList<RectNPC> monsters;
    private int amountNPC;
    private int NPCGap;

    private Rect r = new Rect();

    private float x = (float) Math.random() * (Constants.SCREEN_WIDTH + 20);
    private float y = (float) Math.random() * (Constants.SCREEN_HEIGHT + 20);

    private long startTime;
    private long initTime;

    private int tempTime = 0;
    private boolean plusScore = false;

    public NPCManager(int amountNPC, int NPCGap) {
        this.amountNPC = amountNPC;
        this.NPCGap = NPCGap;

        startTime = initTime = System.currentTimeMillis();

        monsters = new ArrayList<>();

        populateNPCS();
    }

    public boolean playerCollideNPC(RectPlayer player) {
        for(RectNPC NPC : monsters) {
            if(NPC.playerCollideNPC(player))
                return true;
        }
        return false;
    }

    private void populateNPCS() {
        if(monsters.size() >= amountNPC)
                return;
        int currY = -5*Constants.SCREEN_HEIGHT/4;
        while(currY < 0) {
            monsters.add(new RectNPC(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0)));
            currY += 100 + NPCGap;
        }
    }

    public void update() {
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float)(Math.sqrt(1 + (startTime - initTime)/2000.0))*Constants.SCREEN_HEIGHT/(10000.0f); //change the first divider to change speed, higher slows down
        for(RectNPC NPC : monsters) {
            NPC.incrementY(speed * elapsedTime);
        }
        if(monsters.get(monsters.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            monsters.add(0, new RectNPC(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0)));
            monsters.remove(monsters.size() - 1);
            //score -= 10;
            plusScore = true;
            tempTime = (int)System.currentTimeMillis();
            x = (float) (Math.random() * (Constants.SCREEN_WIDTH)) + 100;
            y = (float) (Math.random() * ((Constants.SCREEN_HEIGHT /2))) + 100;

        }
    }

    public void draw(Canvas canvas) {
        for(RectNPC NPC : monsters)
            NPC.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.BLACK);
        //canvas.drawText(" " + score, 50, 50 + paint.descent() - paint.ascent(), paint);

        if(plusScore) {
            if ((int)System.currentTimeMillis() - tempTime >= 200)
                plusScore = false;
            Paint paint2 = new Paint();
            paint2.setTextSize(100);
            paint2.setColor(Color.RED);
            drawPlusScore(canvas, paint2, "-10!");
        }
    }
    private void drawPlusScore(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.getClipBounds(r);
        paint.getTextBounds(text, 0, text.length(), r);
        canvas.drawText(text, x, y,paint);

    }
}
