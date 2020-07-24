package Data;

import com.google.gson.annotations.SerializedName;

public class Grade {

    @SerializedName("id")
    private int ID;

    @SerializedName("title")
    private String title;

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

}
