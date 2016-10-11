package seniordesign.phoneafriend.authentication;

import java.util.HashMap;

import seniordesign.phoneafriend.posting.Post;

/**
 * Created by The Alex on 9/28/2016.
 */
public class User {
    private String id;
    private String username;
    private HashMap<String , Post> posts;
    private HashMap<String , User> friends;

    public User(){}
    public User(String id , String username){
        this.id = id;
        this.username = username;
        posts = new HashMap<String , Post>();
        friends = new HashMap<String , User>();
    }

    public HashMap<String, Object> toMap(){
        HashMap<String , Object> map = new HashMap<>();
        map.put("username" , username);
        map.put("posts" , posts);
        map.put("friends" , friends);
        map.put("id" , id);
        return map;

    }

}
