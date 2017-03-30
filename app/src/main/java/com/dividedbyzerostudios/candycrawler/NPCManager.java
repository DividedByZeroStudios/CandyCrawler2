package com.dividedbyzerostudios.candycrawler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    public boolean bulletCollide(Bullet bullet) {
        for(Iterator<RectNPC> iterator = monsters.iterator(); iterator.hasNext();) {
            if(iterator.next().bulletCollideNPC(bullet)) {
                iterator.remove();
                monsters.add(0, new RectNPC(new Rect(150, 150, 300, 300), Color.rgb(255, 0, 0), 25));
                return true;
            }
        }
        return false;
    }

    private void populateNPCS() {
        if(monsters.size() >= amountNPC)
                return;
        int currY = -5*Constants.SCREEN_HEIGHT/4;
        while(currY < 0) {
            monsters.add(new RectNPC(new Rect(150, 150, 300, 300), Color.rgb(255, 0, 0), 25));
            currY += 100 + NPCGap;
        }
    }

    public List<RectNPC> getList() {
        return monsters;
    }

    public void update() {
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float)(Math.sqrt(1 + (startTime - initTime)/2000.0))*Constants.SCREEN_HEIGHT/(10000.0f); //change the first divider to change speed, higher slows down
        for(RectNPC NPC : monsters) {
            NPC.incrementY(speed * elapsedTime);
            NPC.moveX(NPC);
        }
        if(monsters.size() > 0)
            if(monsters.get(monsters.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
                monsters.add(0, new RectNPC(new Rect(150, 150, 300, 300), Color.rgb(255, 0, 0), 25));
                //int increase = (int)(Math.sqrt(1 + (startTime - initTime)/2000.0));
                //Constants.NUMBER_ENEMIES = Constants.NUMBER_ENEMIES * increase ;
                monsters.remove(monsters.size() - 1);
                Constants.SCORE -= 10;
                plusScore = true;
                tempTime = (int)System.currentTimeMillis();
                x = (float) (Math.random() * (Constants.SCREEN_WIDTH)) + 100;
                y = (float) (Math.random() * ((Constants.SCREEN_HEIGHT /2))) + 100;
            }
        for(RectNPC npc : monsters)
            npc.update();
    }

    public void draw(Canvas canvas) {
        for(RectNPC NPC : monsters)
            NPC.draw(canvas);
        //Paint paint = new Paint();
        //paint.setTextSize(100);
        //paint.setColor(Color.BLACK);

        if(plusScore) {
            if ((int)System.currentTimeMillis() - tempTime >= 300)
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
