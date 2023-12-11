package sypztep.mamy.common.interfaces;

import java.util.Random;

public interface LivingEntityInvoker {

    void mamy$setCritical(boolean flag);

    boolean mamy$isCritical();
    default float calculateCritDamage(float amount) {
        float totalCritRate = this.getTotalCritRate();
        float totalCritDMG = this.getTotalCritDamage();
        if (!this.storeCrit().mamy$isCritical() && (!(totalCritDMG > 0.0F) || !(totalCritRate > 0.0F) || !(totalCritRate >= 100.0F) && !(this.getRand().nextFloat() < totalCritRate / 100.0F))) {
            return amount;
        } else {
            this.storeCrit().mamy$setCritical(true);
            return amount * (100.0F + totalCritDMG) / 100.0F;
        }
    }

    Random getRand();

    default LivingEntityInvoker storeCrit() {
        return this;
    }

    default void mamy$setCritRate(float critRate) {
    }

    default void mamy$setCritDamage(float critDamage) {
    }

    default float mamy$getCritRate() { //Crit rate = 0
        return 0.0F;
    }

    default float mamy$getCritRateFromEquipped() {
        return 0.0F;
    }

    default float getTotalCritRate() {
        return this.mamy$getCritRate() + this.mamy$getCritRateFromEquipped();
    }

    default float mamy$getCritDamage() {//Crit damage = 50%
        return 50.0F;
    }

    default float mamy$getCritDamageFromEquipped() {
        return 0.0F;
    }

    default float getTotalCritDamage() {
        return this.mamy$getCritDamage() + this.mamy$getCritDamageFromEquipped();
    }
}
