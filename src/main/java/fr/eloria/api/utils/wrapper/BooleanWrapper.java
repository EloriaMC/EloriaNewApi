package fr.eloria.api.utils.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BooleanWrapper {

    private final boolean condition;

    public static BooleanWrapper of(boolean condition) {
        return new BooleanWrapper(condition);
    }

    public BooleanWrapper ifTrue(Runnable runnable) {
        if (condition) runnable.run();
        return this;
    }

    public BooleanWrapper ifFalse(Runnable runnable) {
        if (!condition) runnable.run();
        return this;
    }

}
