package Data;

import com.google.gson.annotations.SerializedName;

public class Option {

    @SerializedName("id")
    private int ID;

    @SerializedName("title")
    private String OptionTitle;

    @SerializedName("image_url")
    private String ImageUrl;

    @SerializedName("is_correct")
    private String is_correct;

    public String getOptionTitle() {
        if (OptionTitle == null)
            return "null";
        return OptionTitle;
    }

    public String getImageUrl() {
        if(ImageUrl == null)
            return "null";
        if (!ImageUrl.contains("https"))
            ImageUrl = ImageUrl.replaceAll("http", "https");
        return ImageUrl;
    }

    public int getID() {
        return ID;
    }

    public boolean getIs_correct() {
        return !is_correct.equals("0");
    }
}
