package Data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionPart extends Part {

    @SerializedName("id")
    private transient int ID;

    @SerializedName("title")
    private String QuestionTitle;

    @SerializedName("image_url")
    private String ImageTitleUrl;

    @SerializedName("options")
    private ArrayList<OptionPart> options;

    public int getID() {
        return ID;
    }

    public String getQuestionTitle() {
        if (QuestionTitle == null)
            return "null";
        return QuestionTitle;
    }

    public String getImageTitleUrl() {
        if (ImageTitleUrl == null)
            return "null";
        if (!ImageTitleUrl.contains("https"))
            ImageTitleUrl = ImageTitleUrl.replaceAll("http", "https");

        return ImageTitleUrl;
    }

    public ArrayList<OptionPart> getOptions() {
        return options;
    }

}
