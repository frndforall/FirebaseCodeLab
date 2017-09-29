package retrofit.cybersoft.testretrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nagendra on 07/04/16.
 */
public class Question {
    public List<String> tags = new ArrayList<String>();
    public Owner owner;
    public boolean isAnswered;
    public int viewCount;
    public int answerCount;
    public int score;
    public int lastActivityDate;
    public int creationDate;
    public int questionId;
    public String link;
    public String title;
    public int lastEditDate;
}
