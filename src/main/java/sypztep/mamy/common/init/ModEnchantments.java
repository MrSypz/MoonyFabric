package sypztep.mamy.common.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.enchantment.*;

import java.util.ArrayList;
import java.util.List;

public class ModEnchantments {
    public static List<EmptyEnchantment> ModEnchantmentList = new ArrayList<>();
    //SWORD
    public static EmptyEnchantment ELECTRO_DECREE = new ElectroEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
    public static EmptyEnchantment LETHAL_TEMPO = new OnHitApplyEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
    public static EmptyEnchantment FERVOR_OF_BATTLE = new OnHitApply2Enchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
    public static EmptyEnchantment CARVE = new OnHitApplyEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
    public static EmptyEnchantment MISSILE_HOMMING = new EmptyEnchantment(Enchantment.Rarity.COMMON, EnchantmentTarget.CROSSBOW, EquipmentSlot.MAINHAND);
    //WEARABLE
    public static EmptyEnchantment VITALITY = new VitalityEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR);


    public static void init() {
        init("vitality", VITALITY);
        init("electrodecree", ELECTRO_DECREE);
        init("lethaltempo", LETHAL_TEMPO);
        init("fervorofbattle", FERVOR_OF_BATTLE);
        init("carve", CARVE);
        init("missilehomming", MISSILE_HOMMING);
    }
    public static void init(String name, EmptyEnchantment enchantment){
        Registry.register(Registries.ENCHANTMENT,MamyMod.id(name), enchantment);
        ModEnchantmentList.add(enchantment);
    }
}
