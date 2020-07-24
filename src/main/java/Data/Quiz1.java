package Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Quiz1 {

    @SerializedName("id")
    private int ID;

    @SerializedName("title")
    private String QuizTitle;

    @SerializedName("quiz_time")
    private int QuizTime;

    @SerializedName("answer_file")
    private String AnswerFile;

    @SerializedName("is_locked")
    private Boolean isLocked;

    @SerializedName("has_paid")
    private Boolean has_paid;

    @SerializedName("shamsi_quiz_date")
    private String ShamsiQuizQate;

    @SerializedName("gift_price")
    private int GiftPrice;

    @SerializedName("early_price")
    private int EarlyPrice;

    @SerializedName("price")
    private int Price;

    @SerializedName("questions")
    private List<Question> questions;


    public int getID() {
        return ID;
    }

    public String getQuizTitle() {
        return QuizTitle;
    }

    public int getQuizTime() {
        return QuizTime;
    }

    public String getAnswerFile() {
        return AnswerFile;
    }

    public Boolean isLocked() {
        return isLocked;
    }

    public String getShamsiQuizQate() {
        return ShamsiQuizQate;
    }

    public int getGiftPrice() {
        return GiftPrice;
    }

    public int getEarlyPrice() {
        return EarlyPrice;
    }

    public int getPrice() {
        return Price;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Boolean getHas_paid() {
        return has_paid;
    }
}
