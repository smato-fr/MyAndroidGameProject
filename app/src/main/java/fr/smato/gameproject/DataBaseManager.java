package fr.smato.gameproject;



import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.smato.gameproject.model.Friend;
import fr.smato.gameproject.model.FriendI;
import fr.smato.gameproject.model.User;
import fr.smato.gameproject.popup.ErrorPopup;
import fr.smato.gameproject.utils.Data;
import fr.smato.gameproject.utils.Updater;
import fr.smato.gameproject.utils.callback.Event;
import fr.smato.gameproject.utils.callback.UserLoadCallback;

public class DataBaseManager {

    public final static FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    public static User currentUser;

    public final static DatabaseReference rootDatabaseRef = FirebaseDatabase.getInstance().getReference();
    public final static DatabaseReference userDatabaseRef = rootDatabaseRef.child("Users");
    public final static Map<String, User> users = new HashMap<>();
    public final static Map<String, FriendI> friends = new HashMap<>();


    private static Updater currentActivity;
    private static boolean connected = false;
    public static void updateData(final Updater currentActivity) {



        //check connexion
        DatabaseReference connectedRef = rootDatabaseRef.child(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.d("database", "connected");
                    DataBaseManager.connected = true;
                } else {
                    if (DataBaseManager.connected) {
                        Log.d("database", "disconnected");
                        onError();
                    } else {


                        (new Thread(new Runnable() {

                        @Override
                            public void run() {

                                boolean running = true;
                                int timeOut = 50;
                                while (running) {

                                    try {
                                        if (DataBaseManager.connected || timeOut <= 0) {
                                            running = false;
                                            continue;
                                        }

                                        timeOut--;
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (!DataBaseManager.connected) {
                                    ((AppCompatActivity) currentActivity).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            onError();
                                        }
                                    });
                                }

                            }
                        })).start();


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onError();
            }
        });


        DataBaseManager.currentActivity = currentActivity;
        users.clear();
        friends.clear();

        userDatabaseRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
                updateFriendData();
                currentActivity.update();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onError();
            }
        });
    }

    public static boolean isConnected() {
        return connected;
    }


    private static boolean friendReferenceLoaded = false;
        private static void updateFriendData() {

            if (!friendReferenceLoaded) {

                friendReferenceLoaded = true;
                Query query1 = rootDatabaseRef.child("Friends").orderByChild("friend1")
                        .equalTo(firebaseUser.getUid());

                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshot) {
                        List<String> toRemove = new ArrayList<>();
                        for (String id : friends.keySet()) {
                            if (friends.get(id).fromCurrentUser()) {
                               toRemove.add(id);
                            }
                        }
                        for (String id : toRemove) {
                            friends.remove(id);
                        }

                        for (final DataSnapshot ds : snapshot.getChildren()) {
                            loadFriend(ds);
                        }
                        currentActivity.update();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        onError();
                    }
                });

                Query query2 = rootDatabaseRef.child("Friends").orderByChild("friend2")
                        .equalTo(firebaseUser.getUid());

                query2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshot) {
                        List<String> toRemove = new ArrayList<>();
                        for (String id : friends.keySet()) {
                            if (!friends.get(id).fromCurrentUser()) {
                               toRemove.add(id);
                            }
                        }
                        for (String id : toRemove) {
                            friends.remove(id);
                        }

                        for (final DataSnapshot ds : snapshot.getChildren()) {
                            loadFriend(ds);
                        }
                        currentActivity.update();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        onError();
                    }
                });
            }
        }



    private static FriendI createFriendI(final String id, final User friend, final int state, final boolean fromCurrentUser) {
        return new FriendI() {
            @Override
            public String getId() {
                return id;
            }

            @Override
            public User getUser() {
                return friend;
            }

            @Override
            public int getFriendState() {
                return state;
            }

            @Override
            public boolean fromCurrentUser() {
                return fromCurrentUser;
            }
        };
    }

    private static void loadFriend(final DataSnapshot ds) {
        final Friend friend = ds.getValue(Friend.class);

        final int state =  friend.getState();
        final String id = ds.getKey();

        if (friend != null && state != 0) {
            final User f1 = getUserById(friend.getFriend1());
            final User f2 = getUserById(friend.getFriend2());

            if (f1 == null) {
               loadUser(friend.getFriend1(), new UserLoadCallback() {

                   @Override
                   public void onEvent(User user) {
                       friends.put(id, createFriendI(id, user, state, false));
                   }
               });
            } else if (f2 == null) {
                loadUser(friend.getFriend2(), new UserLoadCallback() {
                    @Override
                    public void onEvent(User user) {
                        friends.put(id, createFriendI(id, user, state, true));
                    }
                });
            } else {
                if (friend.getFriend1().equals(firebaseUser.getUid())) {;
                    friends.put(id, createFriendI(id, f2, state, true));
                } else if (friend.getFriend2().equals(firebaseUser.getUid())) {

                    friends.put(id, createFriendI(id, f1, state, false));
                }
            }

        }
    }

    public static void loadUser(final String userId, final UserLoadCallback callback) {

        userDatabaseRef.child(userId).addValueEventListener(new ValueEventListener() {

            boolean called = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (users.containsKey(userId)) {
                    User target = users.get(userId);
                    target.setUsername(user.getUsername());
                    target.setImageURL(user.getImageURL());
                    target.setStatus(user.getStatus());
                } else {
                    users.put(userId, user);
                }
                ;
                if (!called && callback != null) {
                    callback.onEvent(user);
                    called = true;
                }

                currentActivity.update();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onError();
            }
        });

    }


    public static void setStatus(String status) {

        DatabaseReference ref = userDatabaseRef.child(firebaseUser.getUid());

        HashMap<String, Object> map = new HashMap<>();
        map.put("status", status);

        ref.updateChildren(map);
    }


    public static User getUserById(String id) {
        if (id.equals(currentUser.getId())) return currentUser;

        return users.get(id);
    }

    public static FriendI getFriendIyId(String id) {
        for (FriendI friend : friends.values()) {
            if (friend.getUser().getId().equals(id)) return friend;
        }

        return null;
    }

    public static void onError() {
        new ErrorPopup((AppCompatActivity) currentActivity, "Veuillez vérifier votre réseau et recommencer...");
    }

}
