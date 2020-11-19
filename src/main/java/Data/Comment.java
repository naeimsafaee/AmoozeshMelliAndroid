package Data;

import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("id")
    private int ID;

    @SerializedName("text")
    private String text;

    @SerializedName("user")
    private User user;


    private Comment my_reply;

    public Comment getReply() {
        return my_reply;
    }

    public void setReply(Comment reply) {
        this.my_reply = reply;
    }

    public int getID() {
        return ID;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

}
