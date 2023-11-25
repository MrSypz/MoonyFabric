package sypztep.mamy.feature.data;

import com.google.gson.JsonElement;

public class PlayerCosmeticData {
    private final String overhead;
    private final String pet;
    private final String back;

    public PlayerCosmeticData(JsonElement overhead, JsonElement back, JsonElement pet) {
        /*
            Overhead Feature part
         */
        if (overhead.isJsonNull())
            this.overhead = null;
         else
             this.overhead = overhead.getAsString();
         /*
            Back Feature part
         */
        if (back.isJsonNull())
            this.back = null;
        else
            this.back = back.getAsString();
        if (pet.isJsonNull())
            this.pet = null;
         else
            this.pet = pet.getAsString();

    }

    public String getOverhead() {
        return overhead;
    }
    public String getBack() {
        return back;
    }
    public String getPet() {
        return pet;
    }
}