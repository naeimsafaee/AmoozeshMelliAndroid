package Data;

import com.google.gson.annotations.SerializedName;

public class OptionPart {

    @SerializedName("id")
    private int ID;

    @SerializedName("title")
    private String OptionTitle;

    @SerializedName("image_url")
    private String ImageUrl;

    @SerializedName("is_correct")
    private boolean is_correct;

    public boolean isIs_correct() {
        return is_correct;
    }

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

}
