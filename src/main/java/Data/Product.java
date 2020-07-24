package Data;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    private int ID;

    @SerializedName("price")
    private int price;

    @SerializedName("gift_price")
    private int gift_price;

    @SerializedName("download_able")
    private boolean download_able;

    @SerializedName("title")
    private String title;

    @SerializedName("image_url")
    private String image_url;

    public String getImage_url() {
        if (!image_url.contains("https"))
            image_url = image_url.replaceAll("http", "https");

        return image_url;
    }

    public int getID() {
        return ID;
    }

    public int getPrice() {
        if(price == 0)
            return getGift_price();
        return price;
    }

    public int getGift_price() {
        return gift_price;
    }

    public boolean isDownload_able() {
        return download_able;
    }

    public String getTitle() {
        return title;
    }

}
