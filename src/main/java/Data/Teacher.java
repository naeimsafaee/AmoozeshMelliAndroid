package Data;

import com.google.gson.annotations.SerializedName;

public class Teacher {

    @SerializedName("id")
    private int ID;

    @SerializedName("teacher_id")
    private int TeacherID;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("is_global")
    private int isGlobal;

    public int getID() {
        return ID;
    }

    public int getTeacherID() {
        return TeacherID;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean IsGlobal() {
        return isGlobal == 1;
    }
}
