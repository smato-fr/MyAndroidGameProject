package fr.smato.gameproject.game.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import fr.smato.gameproject.R;
import fr.smato.gameproject.game.GameView;
import fr.smato.gameproject.game.WaitingGameView;
import fr.smato.gameproject.game.model.utils.GameViewI;

public abstract class Level {

    public final static Bitmap TEXTURE_GROUND =  WaitingGameView.loadImage(R.drawable.plancher_1080_1080);
    public final static Bitmap TEXTURE_WALLH1 =  WaitingGameView.loadImage(R.drawable.murh1_1080_1080);
    public final static Bitmap TEXTURE_WALLH2 =  WaitingGameView.loadImage(R.drawable.murh2_1080_1080);
    public final static Bitmap TEXTURE_WALL =  WaitingGameView.loadImage(R.drawable.wall);


    protected int[][] tileMap;
    protected Bitmap textures[];
    protected boolean solidTiles[];

    protected final int mapWidth = 18;
    protected final int mapHeight = 11;
    protected float tileWidth, tileHeigth;

    private final String roomName;


    protected final GameViewI gameView;


    private float cameraTranslationX = 0.0f;
    private float cameraTranslationY = 0.0f;


    public Level(GameViewI gameView, Bitmap textures[], boolean solids[], String roomName) {
        this.gameView = gameView;
        this.tileMap = new int[mapHeight][mapWidth];
        this.textures = textures;
        this.solidTiles = solids;
        this.roomName = roomName;
    }

    public  void resize(int screenWidth, int screenHeight) {

        tileWidth = (float) (screenWidth/mapWidth);
        tileHeigth = (float) (screenHeight/mapHeight);



        for (int i = 0; i < textures.length; i++) {
            textures[i] = WaitingGameView.resizeImage(textures[i], (int) tileWidth, (int) tileHeigth);
        }

    }

    public abstract void init();
    public abstract void update();
    public void draw(Canvas canvas) {

       for (int y = 0; y < mapHeight; y++) {
           for (int x = 0; x < mapWidth; x++) {
               canvas.drawBitmap(textures[tileMap[y][x]], x*tileWidth - cameraTranslationX, y*tileHeigth - cameraTranslationY, null);
           }
       }

    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public boolean isSolidTile(int y, int x) {
        if (x >= mapWidth || y >= mapHeight) return false;

        return solidTiles[tileMap[y][x]];

    }

    public String getRoomName() {
        return roomName;
    }

    public void setTranslation(float vx, float vy) {
        cameraTranslationX = vx;
        cameraTranslationY = vy;
    }

    public float getCameraTranslationX() {
        return cameraTranslationX;
    }

    public float getCameraTranslationY() {
        return cameraTranslationY;
    }
}
