package seniordesign.phoneafriend.contacts;

import java.util.HashMap;

/**
 * Created by REB.
 */

public class Contacts {
    private String username1;
    private String username2;
    private boolean u12;
    private boolean u21;

    public Contacts(){}

    public Contacts( String username1, String username2, boolean u12, boolean u21){
        this.username1 = username1;
        this.username2 = username2;
        this.u12 = u12;
        this.u21 = u21;
    }

    public String getUsername1(){
        return username1;
    }

    public String getUsername2(){
        return username2;
    }

    public boolean isU12() {
        return u12;
    }

    public boolean isU21() {
        return u21;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public void setU12(boolean u12) {
        this.u12 = u12;
    }

    public void setU21(boolean u21) {
        this.u21 = u21;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String , Object> map = new HashMap<>();
        map.put("username1",username1);
        map.put("username2",username2);
        map.put("u12",u12);
        map.put("u21",u21);
    }
}
