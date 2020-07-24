package Data;

import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("id")
    private int ID;

    @SerializedName("name")
    private String name;

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

}
