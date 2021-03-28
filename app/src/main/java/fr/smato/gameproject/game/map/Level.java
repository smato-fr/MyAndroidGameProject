package fr.smato.gameproject.game.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import fr.smato.gameproject.game.WaitingGameView;

public abstract class Level {


    protected int[][] tileMap;
    protected Bitmap textures[];

    protected int mapWidht, mapHeight;
    protected float tileWidth, tileHeigth;

    private final String roomName;


    public Level(int mapWidth, int mapHeight, Bitmap textures[], String roomName) {
        this.mapWidht = mapWidth;
        this.mapHeight = mapHeight;
        this.tileMap = new int[mapHeight][mapWidth];
        this.textures = textures;
        this.roomName = roomName;
    }

    public  void resize(int screenWidth, int screenHeight) {

        tileWidth = (float) (screenWidth/mapWidht);
        tileHeigth = (float) (screenHeight/mapHeight);

        for (int i = 0; i < textures.length; i++) {
            textures[i] = WaitingGameView.resizeImage(textures[i], (int) tileWidth, (int) tileHeigth);
        }

    }

    public abstract void init();
    public abstract void update();
    public void draw(Canvas canvas) {

       for (int y = 0; y < mapHeight; y++) {
           for (int x = 0; x < mapWidht; x++) {
               canvas.drawBitmap(textures[tileMap[y][x]], x*tileWidth, y*tileHeigth, null);
           }
       }

    }

    public int getMapWidht() {
        return mapWidht;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public boolean isSolidTile(int y, int x) {
        if (x >= mapWidht || y >= mapHeight) return false;

        return tileMap[y][x] != 0;

    }

    public String getRoomName() {
        return roomName;
    }
}
