package fr.eloria.api.utils.scoreboard;

import com.google.common.collect.Maps;
import fr.eloria.api.utils.SpigotUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

@Getter
public abstract class Board<P extends JavaPlugin> {

    private final P plugin;

    private String title;
    private List<String> lines;

    private final ConcurrentMap<UUID, FastBoard> boards;

    public Board(P plugin) {
        this.plugin = plugin;
        this.boards = Maps.newConcurrentMap();
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(getPlugin(), this::updateBoards, 20,20);
    }

    public abstract List<String> getLines(UUID uuid);

    public void setTitle(String title) {
        this.title = SpigotUtils.coloredText(title);
    }

    public void setLines(String... lines) {
        this.lines = SpigotUtils.coloredTexts(Arrays.asList(lines));
    }

    public void setLines(List<String> lines) {
        this.lines = SpigotUtils.coloredTexts(lines);
    }

    public void updateTitle(String title) {
        getBoards().forEach((uuid, fastBoard) -> fastBoard.updateTitle(SpigotUtils.coloredText(title)));
    }

    public void updateLine(int line, String newLine) {
        getLines().set(line, SpigotUtils.coloredText(newLine));
    }

    public void addLine(String newLine) {
        getLines().add(newLine);
    }

    private void updateBoards() {
        getBoards().forEach((uuid, fastBoard) -> fastBoard.updateLines(getLines(uuid)));
    }

    public void onJoin(Player player) {
        FastBoard fastBoard = new FastBoard(player);
        fastBoard.updateTitle(getTitle());
        getBoards().put(player.getUniqueId(), fastBoard);
    }

    public void onLeave(Player player) {
        Optional<FastBoard> fastBoard = Optional.ofNullable(getBoards().remove(player.getUniqueId()));
        fastBoard.ifPresent(FastBoard::delete);
    }

}
