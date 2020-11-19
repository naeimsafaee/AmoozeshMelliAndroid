package Data;


import com.google.gson.annotations.SerializedName;

public class Lesson extends Model {

    @SerializedName("id")
    private int ID;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String image_url;

    public String getImage_url() {
        if (!image_url.contains("https"))
            image_url = image_url.replaceAll("http", "https");

        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

}
