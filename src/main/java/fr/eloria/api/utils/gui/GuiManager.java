package fr.eloria.api.utils.gui;

import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

@Getter
public class GuiManager {

    private final JavaPlugin plugin;
    private final ConcurrentMap<String, Gui> guis;

    public GuiManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.guis = Maps.newConcurrentMap();
    }

    public final void registerGuis(Gui... guis) {
        Arrays.asList(guis)
                .forEach(this::registerGui);
    }

    public <T> void registerGui(T clazz) {
        if (clazz instanceof Gui)
            getGuis().put(clazz.getClass().getName(), (Gui) clazz);
    }

    public void open(String className, Player player) {
        getGuis().get(className).onOpen(player);
    }

    public Gui getGui(String clazzName) {
        return getGuis().get(clazzName);
    }

}
