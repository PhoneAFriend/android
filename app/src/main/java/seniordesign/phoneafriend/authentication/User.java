package seniordesign.phoneafriend.authentication;

import java.util.HashMap;
import java.util.StringTokenizer;

import seniordesign.phoneafriend.posting.Post;

/**
 * Created by The Alex on 9/28/2016.
 */
public class User {
    private String id;
    private String useremail;
    private String username;

    public User(){}
    public User(String id , String useremail, String username){
        this.id = id;
        this.useremail = useremail;
        this.username = username;

    }

    public HashMap<String, Object> toMap(){
        HashMap<String , Object> map = new HashMap<>();
        map.put("id" , id);
        map.put("useremail",useremail);
        map.put("username" , username);

        return map;

    }

}
