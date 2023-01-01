package fr.eloria.api.plugin.spigot.common;

import fr.eloria.api.data.user.User;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.utils.scoreboard.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExampleBoard extends Board<SpigotPlugin> {

    public ExampleBoard(SpigotPlugin plugin) {
        super(plugin);
        setTitle("&6&lEloria");
    }

    @Override
    public List<String> getLines(UUID uuid) {
        User user = getPlugin().getApi().getUserManager().getUser(uuid);

        updateLine(0, "");
        updateLine(1, "Allow: " + (user.getSettings().isAllowMention() ? "&aOui" : "&cNon"));
        updateLine(2, "");

        return new ArrayList<>(getLines().values());
    }

}
