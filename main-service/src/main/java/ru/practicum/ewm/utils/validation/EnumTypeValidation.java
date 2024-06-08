package ru.practicum.ewm.utils.validation;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.enums.EventState;
import ru.practicum.ewm.enums.SortType;
import ru.practicum.ewm.enums.StateAction;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import ru.practicum.ewm.utils.errors.ErrorConstants;

@Slf4j
@UtilityClass
public class EnumTypeValidation {

    public static StateAction getValidUserAction(StateAction action) {
        return StateAction.getUserActions().stream()
                .filter(value -> value.equals(action))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(ErrorConstants.UNKNOWN_ACTION + action));
    }

    public static StateAction getValidAdminAction(StateAction action) {
        return StateAction.getAdminActions().stream()
                .filter(value -> value.equals(action))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(ErrorConstants.UNKNOWN_ACTION + action));
    }

    public static void checkValidEventStates(List<String> states) {
        Optional<String> invalidState = states.stream()
                .filter(state -> {
                    try {
                        EventState.valueOf(state);
                        return false;
                    } catch (IllegalArgumentException e) {
                        return true;
                    }
                })
                .findFirst();
        if (invalidState.isPresent()) {
            throw new IllegalArgumentException(ErrorConstants.UNKNOWN_EVENT_STATE + invalidState.get());
        }
    }

    public static SortType getValidSortType(String sort) {
        return Arrays.stream(SortType.values())
                .filter(value -> value.name().equalsIgnoreCase(sort))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(ErrorConstants.UNKNOWN_SORT_TYPE + sort));
    }
}
