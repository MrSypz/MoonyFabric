package sypztep.mamy.common.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import sypztep.mamy.common.MamyMod;

public class ModDamageTypes {

    public static final RegistryKey<DamageType> BRINGER = createType("bringer");
    public static final RegistryKey<DamageType> ELECTRO = createType("electro");
    public static final RegistryKey<DamageType> BATRACHOTOXIN = createType("batrachotoxin");
    public static final RegistryKey<DamageType> STIMULATION = createType("stimulation");
    public static final RegistryKey<DamageType> BACKLASH = createType("backlash");
    public static RegistryKey<DamageType> createType(String id) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(MamyMod.MODID, id));
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
