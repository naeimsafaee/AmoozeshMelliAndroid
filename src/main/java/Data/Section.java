package Data;

import com.google.gson.annotations.SerializedName;

public class Section {

    @SerializedName("id")
    private int ID;

    @SerializedName("title")
    private String title;

    @SerializedName("shamsi_opening_date")
    private String ShamsiOpeningDate;

    @SerializedName("is_locked")
    private Boolean isLocked;

    @SerializedName("has_paid")
    private Boolean has_paid;

    @SerializedName("gift_price")
    private int GiftPrice;

    @SerializedName("early_price")
    private int EarlyPrice;

    @SerializedName("price")
    private int Price;

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getShamsiOpeningDate() {
        return ShamsiOpeningDate;
    }

    public Boolean IsLocked() {
        return isLocked;
    }

    public Boolean hasPaid() {
        return has_paid;
    }

    public int getGiftPrice() {
        return GiftPrice;
    }

    public int getEarlyPrice() {
        return EarlyPrice;
    }

    public int getPrice() {
        return Price;
    }

    public void setHas_paid(Boolean has_paid) {
        this.has_paid = has_paid;
    }
}
