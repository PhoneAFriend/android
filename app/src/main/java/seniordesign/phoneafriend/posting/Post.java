package seniordesign.phoneafriend.posting;

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
 * Created by The Alex on 9/28/2016.
 */
public class Post {
    /* Attributes */
    private String postId;
    private String questionTitle;
    private String questionText;
    private String answered;
    private String datePosted;
    private String postedBy;
    private String subject;
    private String questionImageURL;


    /* Constructors */
    public Post(){}
    public Post(String questionTitle, String questionText , String postedBy , String subject){
        postId = generatePostId();
        this.questionTitle = questionTitle;
        this.questionText = questionText;
        answered = "false";
        datePosted = Calendar.getInstance().getTime().toString();
        this.postedBy = postedBy;
        this.subject = subject;
        questionImageURL = "Fake URL";
    }
    public Post(String questionTitle, String questionText , String postedBy){
        postId = generatePostId();
        this.questionTitle = questionTitle;
        this.questionText = questionText;
        answered = "false";
        datePosted = Calendar.getInstance().getTime().toString();
        this.postedBy = postedBy;
        subject = "Fake Math";
        questionImageURL = "Fake URL";
    }

    /* Methods */
    private String generatePostId(){
        Random rand = new Random();
        int randInt = rand.nextInt((90-65)+1) + 65;
        char tempChar = Character.toChars(randInt)[0];
        int one,two,three;
        String pid = Character.toString(tempChar);

        for(int i = 1 ; i < 19 ; i++){
            one = rand.nextInt((90 -65)+1)+65;
            two = rand.nextInt((57-48)+1)+48;
            three = rand.nextInt((122-97)+1) +97;
            randInt = rand.nextInt(3);
            switch(randInt){
                case 0:
                    tempChar = Character.toChars(one)[0];
                    break;
                case 1:
                    tempChar = Character.toChars(two)[0];
                    break;
                case 2:
                    tempChar = Character.toChars(three)[0];
                    break;
            }
            pid += Character.toString(tempChar);
        }
        return "-"+pid;
    };
    public String getPostId(){ return postId;}
    public String getQuestionTitle(){return questionTitle;}
    public String getQuestionText(){return questionText;}
    public String getPostedBy(){return postedBy;}
    public String getDatePosted(){return datePosted;}
    public Map<String, Object> toMap(){
        HashMap<String , Object> map = new HashMap<>();
        map.put("answered" , answered);
        map.put("datePosted" , datePosted);
        map.put("postedBy" , "Uid : "+ postedBy);
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
