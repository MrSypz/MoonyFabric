package sypztep.mamy.common.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import sypztep.mamy.common.MamyMod;

public class ModDamageTypes {
    public static final RegistryKey<DamageType> BRINGITDOWN = createType("bringitdown");
    public static final RegistryKey<DamageType> BLOODLUST = createType("bloodlust");
    public static final RegistryKey<DamageType> BLOODSCYTHE = createType("bloodscythe");
    public static final RegistryKey<DamageType> BLEEDOUT = createType("bleedout");
    public static final RegistryKey<DamageType> HOGYOKU = createType("hogyoku");
    public static final RegistryKey<DamageType> ELECTRO = createType("electro");
    public static final RegistryKey<DamageType> MASKIMPACT = createType("maskimpact");
    public static RegistryKey<DamageType> createType(String name) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, MamyMod.id(name));
    }

    public static DamageSource create(World world, RegistryKey<DamageType> key, @Nullable Entity source, @Nullable Entity attacker) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key), source, attacker);
    }

    public static DamageSource create(World world, RegistryKey<DamageType> key, @Nullable Entity attacker) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key), attacker);
    }

    public static DamageSource create(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }


}
