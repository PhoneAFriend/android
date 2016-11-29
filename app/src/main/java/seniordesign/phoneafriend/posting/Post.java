package seniordesign.phoneafriend.posting;

import com.google.firebase.database.DataSnapshot;

import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by The Alex, refined by REB.
 */
public class Post {
    /* Attributes */
    private String questionTitle;
    private String questionText;
    private String answered;
    private String datePosted;
    private String postedBy;
    private String subject;
    private String questionImageURL;
    private String postKey;


    /* Constructors */
    public Post(){}
    public Post(String questionTitle, String questionText , String postedBy , String subject, String questionImageURL, String postKey){
        this.questionTitle = questionTitle;
        this.questionText = questionText;
        answered = "false";
        datePosted = "Need to configure";//Calendar.getInstance().getTime().toString();
        this.postedBy = postedBy;
        this.subject = subject;
        this.questionImageURL = questionImageURL;
        this.postKey = postKey;
    }

    public Post(DataSnapshot postSnap){
        this.questionTitle = postSnap.child("questionTitle").getValue().toString();
        this.questionText = postSnap.child("questionText").getValue().toString();
        this.answered = postSnap.child("answered").getValue().toString();
        this.datePosted = postSnap.child("datePosted").getValue().toString();
        this.postedBy = postSnap.child("postedBy").getValue().toString();
        this.subject = postSnap.child("subject").getValue().toString();
        this.questionImageURL = postSnap.child("questionImageURL").getValue().toString();
    }

    public String getQuestionTitle(){return questionTitle;}
    public String getQuestionText(){return questionText;}
    public String getPostedBy(){return postedBy;}
    public String getDatePosted(){return datePosted;}
    public String getPostKey() { return postKey;}

    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setQuestionImageURL(String questionImageURL) {
        this.questionImageURL = questionImageURL;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Map<String, Object> toMap(){
        HashMap<String , Object> map = new HashMap<>();
        map.put("answered" , answered);
        map.put("datePosted" , datePosted);
        map.put("postedBy", postedBy);
        map.put("questionImageURL" , questionImageURL);
        map.put("questionText" , questionText);
        map.put("questionTitle" , questionTitle);
        map.put("subject" , subject);
        return map;
    };

    public void initFromMap(HashMap<String , String> map){
        questionText = map.get("questionText");
        questionTitle = map.get("questionTitle");
        questionImageURL = map.get("questionImageURL");
        //answered = map.get("answered");
        datePosted = map.get("datePosted").toString();
        postedBy = map.get("postedBy");
        subject = map.get("subject");
    }


}
