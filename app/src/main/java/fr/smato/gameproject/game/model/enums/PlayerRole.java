package fr.smato.gameproject.game.model.enums;

import fr.smato.gameproject.R;

public enum PlayerRole {

   ROLE1(R.drawable.ic_profile), ROLE2(R.drawable.ic_profile), ROLE3(R.drawable.ic_profile);


    public final int COLOR_ID;


    private final static int[] valuesID = new int[PlayerRole.values().length];

    static {

        for (int i = 0; i < PlayerRole.values().length; i++) {
            valuesID[i] = PlayerRole.values()[i].COLOR_ID;
        }

    }

    PlayerRole(int colorId) {
        COLOR_ID = colorId;
    }


    public static int[] valuesID() {
        return valuesID;
    }


}
