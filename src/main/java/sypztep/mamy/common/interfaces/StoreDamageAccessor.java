package sypztep.mamy.common.interfaces;

public interface StoreDamageAccessor {
    float modifyDamage(float originalDamage);

    void storeModifiedDamage(float modifiedDamage);
}
