package fr.smato.gameproject.game.model.enums;

import fr.smato.gameproject.R;
import fr.smato.gameproject.game.model.objects.Role;

public enum PlayerRole {

   ROLE1(R.drawable.ic_profile, Role.class), ROLE2(R.drawable.ic_profile, Role.class), ROLE3(R.drawable.ic_profile, Role.class);


    public final int COLOR_ID;
    private final Class<? extends Role> role;


    private final static int[] valuesID = new int[PlayerRole.values().length];

    static {

        for (int i = 0; i < PlayerRole.values().length; i++) {
            valuesID[i] = PlayerRole.values()[i].COLOR_ID;
        }

    }

    PlayerRole(int colorId, Class<? extends Role> role) {
        COLOR_ID = colorId;
        this.role = role;
    }


    public static int[] valuesID() {
        return valuesID;
    }


    public Role getRole() {
        try {
            return role.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
