package ru.practicum.ewm.enums;

import java.util.Set;

public enum StateAction {
    SEND_TO_REVIEW,
    CANCEL_REVIEW,
    PUBLISH_EVENT,
    REJECT_EVENT;

    public static Set<StateAction> getAdminActions() {
        return Set.of(PUBLISH_EVENT, REJECT_EVENT);
    }

    public static Set<StateAction> getUserActions() {
        return Set.of(SEND_TO_REVIEW, CANCEL_REVIEW);
    }
}
