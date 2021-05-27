package fr.smato.gameproject.game.model.utils;

import java.util.ArrayList;
import fr.smato.gameproject.game.model.objects.Player;

public class PlayerList extends ArrayList<Player> {


    public void remove(String id) {

        for (int i = 0; i < this.size(); i++) {
            Player pls = this.get(i);
            if (pls.getId().equals(id)) {
                this.remove(i);
                return;
            }

        }
    }

    public boolean containsKey(String id) {
        for (Player pls : this) {
            if (pls.getId().equals(id))
                return true;

        }

        return false;
    }

    public Player get(String id) {
        for (Player pls : this) {
            if (pls.getId().equals(id))
                return pls;

        }

        return null;
    }


}
