package sypztep.mamy.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import sypztep.mamy.common.enchantment.BringerDeathEnchantment;
import sypztep.mamy.common.init.ModEnchantments;
import sypztep.mamy.common.init.ModEntityComponents;
import sypztep.mamy.common.util.EnchantmentUtil;

public class BringerComponent implements AutoSyncedComponent,CommonTickingComponent {
    //TODO : Multiplayer Fix
    public static final int DEFAULT_COOLDOWN = 120;
    private final PlayerEntity obj;
    public static boolean hasDeathBringer = false;
    private static int deathCooldown = BringerDeathEnchantment.getDeathCooldown(), lastdeathCooldown = BringerDeathEnchantment.getLastdeathCooldown();
    public BringerComponent(PlayerEntity obj) {
        this.obj = obj;
    }
    @Override
    public void readFromNbt (NbtCompound tag){
        deathCooldown = tag.getInt("DeathCooldown");
//        lastdeathCooldown = tag.getInt("LastDeathCooldown");
    }
    @Override
    public void writeToNbt (NbtCompound tag){
        tag.putInt("DeathCooldown", deathCooldown);
//        tag.putInt("LastDeathCooldown", lastdeathCooldown);
    }
    @Override
    public void tick() {
        hasDeathBringer = EnchantmentUtil.hasEnchantment(ModEnchantments.BRINGER_DEATH,obj.getMainHandStack()) || EnchantmentUtil.hasEnchantment(ModEnchantments.BRINGER_DEATH,obj.getOffHandStack());
        if (hasDeathBringer) {
            sync();
            if (deathCooldown > 0)
                deathCooldown--;
        }
    }

    private void sync() {
        ModEntityComponents.BRINGER.sync(obj);
    }
    public boolean hasDeathBringer(){
        return hasDeathBringer;
    }
    public static int getDeathCooldown(){
        return deathCooldown;
    }
    public static int getLastdeathCooldown(){
        return lastdeathCooldown;
    }
    public static void setDeathCooldown(int deathCooldown2){
        deathCooldown = deathCooldown2;
        lastdeathCooldown = deathCooldown2;
    }
}