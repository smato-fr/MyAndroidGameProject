package fr.smato.gameproject.game.model.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import fr.smato.gameproject.R;
import fr.smato.gameproject.game.GameView;
import fr.smato.gameproject.game.model.objects.Location;
import fr.smato.gameproject.game.model.utils.GameViewI;
import fr.smato.gameproject.game.model.utils.Interactive;
import fr.smato.gameproject.utils.callback.Event;

public class Chest extends LevelEntity implements Interactive {

    private static Bitmap chest_closed = GameView.loadImage(R.drawable.chest_close);
    private static Bitmap chest_closed_near = GameView.loadImage(R.drawable.chest_close_near);
    private static Bitmap chest_opened = GameView.loadImage(R.drawable.chest_open);


    private Bitmap image;


    private boolean open;
    private boolean near;

    public Chest(Context context, GameViewI game) {
        super(context, game, null);
        setOnTouch(new Event() {
            @Override
            public void onEvent() {
                image = chest_closed_near;
                near = true;
            }
        });

        this.open = false;
        this.near = false;
        this.image = chest_closed;
    }

    @Override
    public void resize(Location loc, Object... objects3) {
        super.resize(loc, objects3);
        chest_closed = GameView.resizeImage(chest_closed, (int) objects3[1], (int) objects3[2]);
        chest_closed_near = GameView.resizeImage(chest_closed_near, (int) objects3[1], (int) objects3[2]);
        chest_opened = GameView.resizeImage(chest_opened, (int) objects3[1], (int) objects3[2]);

        this.image = chest_closed;
    }

    @Override
    public void update() {
        if (!open) {
            if (!near)
                super.update();

            else if (!isTouched(game.getPlayerEntity().getX(), game.getPlayerEntity().getY())) {
                near = false;
                image = chest_closed;
            }


        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, (float) location.x, (float) location.y, null);
    }

    @Override
    public void onInteract() {
        open();
    }

    public void open() {
        if (near) {
            open = true;
            image = chest_opened;
        }
    }
}
