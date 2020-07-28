package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessages.PostReq;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SystemData {

    private ConcurrentHashMap<String, String> userAndpassword;
    private ConcurrentHashMap<String, Integer> loggedInUsers; // Map a userName to current connectionID
    private ConcurrentHashMap<String, Queue<String>> followers; //Map a user to following list,
    private ConcurrentHashMap<String, Queue<Message>> postsAndPm; //Map a user to a list of posted messages
    private ConcurrentHashMap<String, Queue<Message>> notifications;
    private List<String> registerdUsers; // registered users list by order;

    public SystemData() {
        userAndpassword = new ConcurrentHashMap<>();
        loggedInUsers = new ConcurrentHashMap<>();
        followers = new ConcurrentHashMap<>();
        postsAndPm = new ConcurrentHashMap<>();
        notifications = new ConcurrentHashMap<>();
        registerdUsers = new LinkedList<>();
    }
//register a user if it is possible
    public synchronized boolean regUser(String userName, String password, int connectionId) {
        if(!isRegistered(userName) && searchLoggedInUser(connectionId) == null) {
            userAndpassword.put(userName, password);
            registerdUsers.add(userName);
            followers.put(userName, new ConcurrentLinkedQueue<>());
            postsAndPm.put(userName, new ConcurrentLinkedQueue<>());
            notifications.put(userName, new ConcurrentLinkedQueue<>());
            return true;
        }
        return false;
    }

    public boolean isRegistered(String userName) {
        return userAndpassword.containsKey(userName);
    }

    public synchronized boolean logIn(String userName, String password, int connectionId) {
        if(isPossibleToLogIn(userName, password, connectionId)) {
            loggedInUsers.put(userName, connectionId);
            return true;
        }
        return false;
    }

    public synchronized boolean isLoggedInByUserName(String userName) {  //It is sync because in the moment he is logging out, we don't want him to get new notification.
        if (userName != null && loggedInUsers.containsKey(userName)) {
            return true;
        } else {
            return false;

        }

    }

    public boolean isCorrectPass(String userName, String password) {
        return userAndpassword.get(userName).equals(password);
    }
    //This function checks if it is posibble to log in - if registered, ir not connected, if the password is correct, and if the connection handler not connected with other user
    public boolean isPossibleToLogIn(String userName, String password, int connectionId) {
        if (isRegistered(userName) && !isLoggedInByUserName(userName) && isCorrectPass(userName, password) && searchLoggedInUser(connectionId) == null)
            return true;
        else
            return false;
    }


    //return the userName if he is logged in. else, return null
    public String searchLoggedInUser(int connectionId) {
        for (String key : loggedInUsers.keySet()) {
            if (loggedInUsers.get(key) == connectionId)
                return key;
        }
        return null;
    }

    public void logOut (int connectionId){
        synchronized (notifications.get(searchLoggedInUser(connectionId))) {  // It is sync because in the moment he is logging out, we don't want him to get new notification.
            for (String key : loggedInUsers.keySet()) {
                if (loggedInUsers.get(key) == connectionId)
                    loggedInUsers.remove(key);
            }
        }
    }

    public List<String> followUsers (List<String> userListToFollow, String userName){
        List<String> succeedUsers = new LinkedList<>();
        for (String userToFollow : userListToFollow) {
            if(isRegistered(userToFollow) && !followers.get(userName).contains(userToFollow)) {
                followers.get(userName).add(userToFollow);
                succeedUsers.add(userToFollow);
            }
        }
        return succeedUsers;
    }

    public List<String> unFollow(List <String> userListToUnFollow, String userName) {
        List<String> succeedUsers = new LinkedList<>();
        for (String userToUnFollow : userListToUnFollow) {
            if(isRegistered(userToUnFollow) && followers.get(userName).contains(userToUnFollow)) {
                followers.get(userName).remove(userToUnFollow);
                succeedUsers.add(userToUnFollow);
            }
        }
        return succeedUsers;
    }

    public List<String> whoFollowsMe(String userName){
        List<String> myFollowers = new LinkedList<>();
        for(String key : followers.keySet()) {
            if (followers.get(key).contains(userName))
                myFollowers.add(key);
        }
        return myFollowers;

    }

    public void addPostOrPm (String userName, Message msg){
        synchronized (postsAndPm) {
            postsAndPm.get(userName).add(msg);
        }
    }

    public void addNotification(String userName, Message msg){
        synchronized (notifications) {
            notifications.get(userName).add(msg);
        }
    }

    public int getId (String userName){
        return loggedInUsers.get(userName);
    }

    public Message getNotification(String userName){
        return notifications.get(userName).poll();
    }

    public List<String> getAllRegisteredUsers(){
        return registerdUsers;
    }

    public short numOfFollowing(String userName){
        return (short)followers.get(userName).size();
    }

    public short numOfPosts(String userName){
        short numberOfPosts = 0;
        for (Message m : postsAndPm.get(userName)) {
            if(m instanceof PostReq)
                numberOfPosts++;
        }
        return numberOfPosts;
    }
}
