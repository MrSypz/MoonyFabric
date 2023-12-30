package sypztep.mamy.feature.data;

import com.google.gson.JsonElement;

public class PlayerCosmeticData {
    private final String overhead;

    public PlayerCosmeticData(JsonElement overhead) {
        /*
            Overhead Feature part
         */
        if (overhead.isJsonNull())
            this.overhead = null;
         else
             this.overhead = overhead.getAsString();
    }

    public String getOverhead() {
        return overhead;
    }
}