package fr.eloria.api.utils.handler;

import lombok.Data;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@Data
public abstract class AbstractListener<P extends JavaPlugin> implements Listener {

    private final P plugin;

}
