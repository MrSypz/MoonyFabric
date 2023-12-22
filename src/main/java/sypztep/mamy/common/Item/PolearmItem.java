package sypztep.mamy.common.Item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ToolMaterial;
import sypztep.mamy.common.init.ModEntityAttributes;

import java.util.UUID;

public class PolearmItem extends EmptySwordItem{
    private final ToolMaterial material;
    private final float attackDamage;
    private static final EntityAttributeModifier REACH_MODIFIER;
    private static final EntityAttributeModifier CRIT_CHANCE_MODIFIER;
    public PolearmItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.material = toolMaterial;
        this.attackDamage = attackDamage + toolMaterial.getAttackDamage();
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
    }
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = LinkedHashMultimap.create(super.getAttributeModifiers(slot));
        if (slot == EquipmentSlot.MAINHAND) {
            map.put(ReachEntityAttributes.ATTACK_RANGE, REACH_MODIFIER);
            map.put(ModEntityAttributes.GENERIC_CRIT_CHANCE,CRIT_CHANCE_MODIFIER);
        }
        return map;
    }
    @Override
    public ToolMaterial getMaterial() {
        return this.material;
    }

    static {
        REACH_MODIFIER = new EntityAttributeModifier(UUID.fromString("3ad3431a-2ceb-4501-b66b-2487064263c7"), "Weapon modifier", 1.5D, EntityAttributeModifier.Operation.ADDITION);
        CRIT_CHANCE_MODIFIER = new EntityAttributeModifier(UUID.fromString("29880e68-9300-4d5c-a73a-18ad9c83f80c"), "Weapon modifier",10.0D, EntityAttributeModifier.Operation.ADDITION);
    }
}
