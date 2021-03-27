package fr.smato.gameproject.game.model.enums;

public enum GameState {

    waiting, starting, playing;



    public static GameState getByName(String name) {
        for (GameState state : GameState.values()) {
            if (state.toString().equals(name)) return state;
        }

        return null;
    }


}
