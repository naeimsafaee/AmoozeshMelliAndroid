package Data;

import com.google.gson.annotations.SerializedName;

public class User {


    @SerializedName("id")
    private int ID;

    @SerializedName("fullName")
    private String fullName;

    public int getID() {
        return ID;
    }

    public String getFullName() {
        return fullName;
    }

}
