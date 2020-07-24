package Data;

import com.google.gson.annotations.SerializedName;

public class Video extends Part {


    @SerializedName("video_url")
    private String video_url;

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_url() {
        return video_url;
    }

}
