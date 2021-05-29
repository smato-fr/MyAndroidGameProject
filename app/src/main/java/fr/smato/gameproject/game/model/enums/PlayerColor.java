package fr.smato.gameproject.game.model.enums;

import fr.smato.gameproject.R;

public enum PlayerColor{

    RED(R.color.red), ORANGE(R.color.aqua), PINK(R.color.pink), YELLOW(R.color.yellow), GREEN(R.color.green), BLUE(R.color.blue);


    public final int COLOR_ID;


    private final static int[] valuesID = new int[PlayerColor.values().length];

    static {

        for (int i = 0; i < PlayerColor.values().length; i++) {
            valuesID[i] = PlayerColor.values()[i].COLOR_ID;
        }

    }

    PlayerColor(int colorId) {
        COLOR_ID = colorId;
    }


    public static int[] valuesID() {
        return valuesID;
    }
}
