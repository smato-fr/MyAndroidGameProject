package fr.smato.gameproject.game;

import android.graphics.Canvas;

public class GameLoopThread extends Thread
{


    //UPS & FPS limiters
    public static final double MAX_UPS = 30.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;

    private final GameView game; // l'objet SurfaceView que nous verrons plus bas
    private boolean running = false; // état du thread, en cours ou non
    private double averageUPS;
    private double averageFPS;

    // constructeur de l'objet, on lui associe l'objet view passé en paramètre
    public GameLoopThread(GameView view) {
        this.game = view;
    }

    // défini l'état du thread : true ou false
    public void setRunning(boolean run) {
        running = run;
    }

    // démarrage du thread
    @Override
    public void run()
    {

        //wait for initialisation of GameView
        while (!game.isInited()) {
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //declare time and cylce vars
        long startTime, sleepTime, elapsed;
        int updateCount = 0;
        int frameCount = 0;

        Canvas canvas = null;
        startTime = System.currentTimeMillis();
        while (running)
        {


            //Try to update and render game
            try {
                canvas = game.getHolder().lockCanvas();
                synchronized (game.getHolder()) {
                    game.update();
                    updateCount++;

                    game.draw(canvas);
                }

            } catch (IllegalArgumentException | NullPointerException exception) {
                exception.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        game.getHolder().unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //pause game loop to not  exceed target UPS
            elapsed= System.currentTimeMillis() - startTime;
            sleepTime =(long) (updateCount*UPS_PERIOD - elapsed);
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            //skip frames to keep up with target UPS
            while (sleepTime < 0 && updateCount < MAX_UPS-1) {
                game.update();
                updateCount++;
                elapsed= System.currentTimeMillis() - startTime;
                sleepTime =(long) (updateCount*UPS_PERIOD - elapsed);
            }


            //calcul FPS & UPS
            elapsed= System.currentTimeMillis() - startTime;
            if (elapsed >= 1000) {
                averageUPS = updateCount / (1E-3 * elapsed);
                averageFPS = frameCount / (1E-3 * elapsed);
                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();
            }


        } // boucle while (running)
    } // public void run()

    public double getFPS() {
        return averageFPS;
    }

    public double getUPS() {
        return averageUPS;
    }


} // class GameLoopThread
