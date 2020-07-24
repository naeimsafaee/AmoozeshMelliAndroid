package Data;

import com.google.gson.annotations.SerializedName;

public class Slide {

    @SerializedName("url")
    private String url;

    @SerializedName("title")
    public String title;

    public String getUrl() {
        if(url == null)
            return "null";
        if (!url.contains("https"))
            url = url.replaceAll("http", "https");

        return url;
    }


}
