package Data;

import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("id")
    private int ID;

    @SerializedName("text")
    private String text;

    @SerializedName("user")
    private User user;

    public Comment getReply() {
        return reply;
    }

    public void setReply(Comment reply) {
        this.reply = reply;
    }

    private Comment reply;


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
