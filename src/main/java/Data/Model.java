package Data;

import com.google.gson.Gson;

public class Model {

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
