package Data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Question {

    @SerializedName("id")
    private int ID;

    @SerializedName("title")
    private String QuestionTitle;

    @SerializedName("image_url")
    private String ImageTitleUrl;

    @SerializedName("answer_file_url")
    private String answer_file_url;

    @SerializedName("options")
    private ArrayList<Option> options;

    private int answered_question = 0;

    public int getAnswered_question() {
        return answered_question;
    }

    public void setAnswered_question(int answered_question) {
        this.answered_question = answered_question;
    }

    public int getID() {
        return ID;
    }

    public String getQuestionTitle() {
        if (QuestionTitle == null)
            return "null";
        return QuestionTitle;
    }

    public String getImageTitleUrl() {
        if(ImageTitleUrl == null)
            return "null";
        if (!ImageTitleUrl.contains("https"))
            ImageTitleUrl = ImageTitleUrl.replaceAll("http", "https");

        return ImageTitleUrl;
    }

    public String getAnswer_file_url() {
        if(answer_file_url == null)
            return "null";
        if (!answer_file_url.contains("https"))
            answer_file_url = answer_file_url.replaceAll("http", "https");

        return answer_file_url;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

}
