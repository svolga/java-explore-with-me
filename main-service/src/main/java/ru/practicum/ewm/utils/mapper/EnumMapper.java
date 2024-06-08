package ru.practicum.ewm.utils.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.enums.EventState;
import ru.practicum.ewm.enums.StateAction;

@UtilityClass
public class EnumMapper {

    public EventState mapToEventState(StateAction action) {
        EventState state;
        switch (action) {
            case PUBLISH_EVENT:
                state = EventState.PUBLISHED;
                break;
            case REJECT_EVENT:
            case CANCEL_REVIEW:
                state = EventState.CANCELED;
                break;
            case SEND_TO_REVIEW:
                state = EventState.PENDING;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return state;
    }

}
