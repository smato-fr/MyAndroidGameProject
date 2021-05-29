package fr.smato.gameproject.game.model.enums;

import fr.smato.gameproject.R;

public enum PlayerColor {

    RED(R.color.red), ORANGE(R.color.aqua), PINK(R.color.pink), YELLOW(R.color.yellow), GREEN(R.color.green), BLUE(R.color.blue);


    public final int COLOR_ID;

    PlayerColor(int colorId) {
        COLOR_ID = colorId;
    }


}
