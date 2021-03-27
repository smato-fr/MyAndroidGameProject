package fr.smato.gameproject.model;

import fr.smato.gameproject.utils.Data;

public abstract class FriendI implements Data {

    public abstract String getId();
    public abstract User getUser();
    public abstract int getFriendState();
    public abstract boolean fromCurrentUser();
}
