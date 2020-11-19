package Data;

import com.google.gson.annotations.SerializedName;

public class Subject {

  @SerializedName("id")
  private int ID;

  @SerializedName("title")
  private String title;

  public int getID() {
    return ID;
  }

  public void setID(int ID) {
    this.ID = ID;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
