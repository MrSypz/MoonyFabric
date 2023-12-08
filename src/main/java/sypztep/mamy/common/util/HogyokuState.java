package sypztep.mamy.common.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.HashMap;

import static sypztep.mamy.common.MamyMod.MODID;

public class HogyokuState extends PersistentState {
    public static final String ID = MODID + "HogyokuState";

    public final HashMap<String, Integer> map;
    public HogyokuState() {
        map = new HashMap<>();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        for (String s : map.keySet()) {
            nbt.putInt(s, map.get(s));
        }
        return nbt;
    }

    public static HogyokuState fromNbt(NbtCompound nbt) {
        HogyokuState h = new HogyokuState();
        for (String s : nbt.getKeys()) {
            h.map.put(s, nbt.getInt(s));
        }
        return h;
    }

    public static HogyokuState get(MinecraftServer server) {
        PersistentStateManager manager = server.getOverworld().getPersistentStateManager();
        return manager.getOrCreate(HogyokuState::fromNbt, HogyokuState::new, ID);
    }
}
