package fr.smato.gameproject.game.model.drawable;

import android.content.Context;
import android.graphics.Canvas;

import fr.smato.gameproject.game.model.utils.GameViewI;
import fr.smato.gameproject.utils.callback.Event;

public class LevelEntity extends Entity{

    private final Event onTouch;

    private boolean visible = true;

    public LevelEntity(Context context, GameViewI game, Event event) {
        super(context, game);
        onTouch = event;
    }

    @Override
    public void update() {

        if (isTouched(game.getPlayer().getX(), game.getPlayer().getY())) {
            onTouch.onEvent();
        }

    }

    @Override
    public void draw(Canvas canvas) {
        if (visible) super.draw(canvas);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
