package fr.eloria.api.utils.command.data;

import com.google.common.collect.Lists;
import fr.eloria.api.utils.command.annotation.ECommand;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

@Data
public class ECommandData {

    private final Object object;
    private final Method method;

    private final ECommand command;
    private final List<ESubCommandData> subCommands = Lists.newLinkedList();

}
