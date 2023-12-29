package sypztep.sincereloyalty;

import org.jetbrains.annotations.Contract;

public interface TridentRecaller {
    /**
     * Returns {@code true} if the status was changed through this call
     */
    // no need for a prefix, the signature has a custom type
    void updateRecallStatus(RecallStatus recalling);

    @Contract(pure = true)
    RecallStatus getCurrentRecallStatus();

    enum RecallStatus {
        CHARGING, NONE, RECALLING
    }
}
