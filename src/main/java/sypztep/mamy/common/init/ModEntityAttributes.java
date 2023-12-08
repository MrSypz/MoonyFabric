package sypztep.mamy.common.init;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.common.MamyMod;

public class ModEntityAttributes {

    public static EntityAttribute GENERIC_HOGYOKU = new ClampedEntityAttribute("attribute.name.generic.hogyoku",0,0,6).setTracked(true);

    public static void init() {
        init("generic.hogyoku", GENERIC_HOGYOKU);
    }
    private static void init(String name, EntityAttribute attribute) {
        Registry.register(Registries.ATTRIBUTE, MamyMod.id(name) ,attribute);
    }
}
