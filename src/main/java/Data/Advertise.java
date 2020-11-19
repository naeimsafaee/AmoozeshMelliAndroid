package Data;

import com.google.gson.annotations.SerializedName;

public class Advertise {

    @SerializedName("id")
    private int id;

    @SerializedName("video_url")
    private String video_url;

    @SerializedName("title")
    private String title;

    public int getId() {
        return id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public String getTitle() {
        return title;
    }

}
