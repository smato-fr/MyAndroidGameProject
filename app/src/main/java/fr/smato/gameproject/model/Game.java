package fr.smato.gameproject.model;

import java.util.List;

public class Game {

    public final static int WAITING = 0;
    public final static int STARTING = 1;
    public final static int PLAYING= 2;

    private String id;
    private int state;
    private List<LocationModel> players;





    public Game() {

    }


}
