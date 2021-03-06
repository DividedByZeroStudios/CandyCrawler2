package com.dividedbyzerostudios.candycrawler;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

import java.util.Iterator;
import java.util.concurrent.SynchronousQueue;

import static com.dividedbyzerostudios.candycrawler.Constants.CURRENT_CONTEXT;
import static com.dividedbyzerostudios.candycrawler.Constants.SCREEN_HEIGHT;

/**
 * Created by Georg on 17/03/2017.
 */

public class GameplayScene implements Scene {

    private Rect r = new Rect();

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;
    private NPCManager npcManager;
    private Rect background = new Rect(0, -50, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    private AnimationManager animManager;
    private Animation endgamescreen;

    private boolean movingPlayer = false;

    private boolean gameOver = false;
    private long gameOverTime;
    private boolean plusScore = false;
    private int tempTime1;

    private float xa = (float) Math.random() * (Constants.SCREEN_WIDTH + 20);
    private float ya = (float) Math.random() * (Constants.SCREEN_HEIGHT + 20);



    public GameplayScene() {
        player = new RectPlayer(new Rect(150, 150, 300, 300), Color.rgb(255, 0, 0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3* Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        npcManager = new NPCManager(Constants.NUMBER_ENEMIES, Constants.SCREEN_HEIGHT + 100);
        obstacleManager = new ObstacleManager(210, Constants.SCREEN_HEIGHT + 100, 75, Color.BLACK, Color.rgb(102, 51, 0));


        BitmapFactory bf = new BitmapFactory();
        Bitmap idleImg = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.endgamescreen);
        endgamescreen = new Animation(new Bitmap[]{idleImg}, 1, true);
        animManager = new AnimationManager(new Animation[]{endgamescreen});
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3* Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);
        player.bulletReset();
        Constants.bgMove = Constants.originalbgMove;
        obstacleManager = new ObstacleManager(200, Constants.SCREEN_HEIGHT + 100, 75, Color.BLACK, Color.rgb(102, 51, 0));
        npcManager = new NPCManager(Constants.NUMBER_ENEMIES, Constants.SCREEN_HEIGHT + 100);
        movingPlayer = false;
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectangle().contains((int)event.getX(), (int)event.getY()))
                    movingPlayer = true;
                if(gameOver && System.currentTimeMillis() - gameOverTime >= 500) {
                    reset();
                    gameOver = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer)
                    playerPoint.set((int)event.getX(), (int)event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        //canvas.drawColor(Color.DKGRAY);

        player.draw(canvas);
        obstacleManager.draw(canvas);
        npcManager.draw(canvas);

        if (gameOver) {
            Paint paint = new Paint();
            paint.setTextSize(50);
            paint.setTypeface(Typeface.create("Courier", Typeface.NORMAL));
            paint.setColor(Color.DKGRAY);
            Paint paint3 = new Paint();
            paint3.setTextSize(60);
            paint3.setTypeface(Typeface.create("Arial", Typeface.NORMAL));
            paint3.setColor(Color.DKGRAY);
            //canvas.drawRect(background, paint3);

            animManager.draw(canvas, background);

            drawCenterText(canvas, paint, "Game Over! Tap to retry.");
            drawScoreText(canvas, paint3, "Your Score was " + Constants.SCORE);

        }

        if(plusScore) {
            if ((int)System.currentTimeMillis() - tempTime1 >= 300)
                plusScore = false;
            Paint paint2 = new Paint();
            paint2.setTextSize(100);
            paint2.setColor(Color.GREEN);
            drawPlusScore(canvas, paint2, "+10!");
        }
    }

    public boolean gameOverStatus() {
        if(gameOver)
            return true;
        else
            return false;
    }


    @Override
    public void update() {

        if(gameOver)
            Constants.bgMove = 0;

        if (!gameOver) {
            for (Iterator<Bullet> iterator = player.getList().iterator(); iterator.hasNext(); ) {
                if (npcManager.bulletCollide(iterator.next())) {
                    iterator.remove();
                    Constants.SCORE += 10;
                    plusScore = true;
                    tempTime1 = (int) System.currentTimeMillis();
                    xa = (float) (Math.random() * (Constants.SCREEN_WIDTH)) + 100;
                    ya = (float) (Math.random() * ((Constants.SCREEN_HEIGHT / 2))) + 100;
                }
            }

            for (Iterator<Bullet> iterator = player.getList().iterator(); iterator.hasNext(); ) {
                if (obstacleManager.BULLETCollide(iterator.next())) {
                    iterator.remove();
                }
            }

            for (Iterator<Bullet> iterator = player.getList().iterator(); iterator.hasNext(); ) {
                if (obstacleManager.BULLETCollideDoor(iterator.next())) {
                    iterator.remove();
                }
            }

            for (Iterator<Bullet> iterator = player.getList().iterator(); iterator.hasNext(); ) {
                if(iterator.next().getY() <= 0)
                    iterator.remove();
            }

            for(Bullet bullet : player.getList()) {
                bullet.tick(true);
            }

            }
            if (!gameOver) {
                player.update(playerPoint);
                player.update();
                obstacleManager.update();
                if (obstacleManager.playerCollide(player)) {
                    gameOver = true;
                    gameOverTime = System.currentTimeMillis();
                }
            }
            if (!gameOver) {
                npcManager.update();
                if (npcManager.playerCollideNPC(player)) {
                    gameOver = true;
                    gameOverTime = System.currentTimeMillis();
                }
            }

            for(RectNPC NPC : npcManager.getList())
                if (obstacleManager.NPCCollide(NPC)) {
                    NPC.getRectangle().top -= 200;
                    NPC.getRectangle().bottom -= 200;
                }

            int i = 0;
            while (i < (Constants.NUMBER_ENEMIES - 1)) {
                if (npcManager.getList().size() <= 1)
                    return;
                if (Rect.intersects(npcManager.getList().get(i).getRectangle(), npcManager.getList().get(i + 1).getRectangle()))
                    npcManager.getList().get(i).setRectangle(-200);
                i += 1;
            }

            if(gameOver) {
                int state = 0;
                animManager.playAnim(state);
                animManager.update();
            }
        }


    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    private void drawScoreText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = (cWidth / 2f - r.width() / 2f - r.left);
        float y = (cHeight / 2f + r.height() / 2f - r.bottom) + 150;
        canvas.drawText(text, x, y, paint);
    }

    private void drawPlusScore(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.getClipBounds(r);
        paint.getTextBounds(text, 0, text.length(), r);
        canvas.drawText(text, xa, ya,paint);

    }
}
