package fr.eloria.api.utils.command.data;

import fr.eloria.api.utils.command.annotation.ESubCommand;
import lombok.Data;

import java.lang.reflect.Method;

@Data
public class ESubCommandData {

    private final Object object;
    private final Method method;

    private final ESubCommand dSubCommand;

}