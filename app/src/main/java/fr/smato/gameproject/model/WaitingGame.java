package fr.smato.gameproject.model;

public class WaitingGame {

    private String id;
    private String name;

    public WaitingGame(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public WaitingGame() {

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
