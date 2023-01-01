package fr.eloria.api.utils.scoreboard;

import com.google.common.collect.Maps;
import fr.eloria.api.utils.SpigotUtils;
import fr.eloria.api.utils.wrapper.OptionalWrapper;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
public abstract class Board<P extends JavaPlugin> {

    private final P plugin;

    private String title;
    private Map<Integer, String> lines;

    private final ScheduledExecutorService firstExecutor, secondExecutor;
    private final ConcurrentMap<UUID, FastBoard> boards;

    public Board(P plugin) {
        this.plugin = plugin;
        this.boards = Maps.newConcurrentMap();
        this.firstExecutor = Executors.newScheduledThreadPool(16);
        this.secondExecutor = Executors.newScheduledThreadPool(1);
        this.lines = Maps.newHashMap();
        this.updateBoards();
    }

    public abstract List<String> getLines(UUID uuid);

    public void setTitle(String title) {
        this.title = SpigotUtils.coloredText(title);
    }

    public void setLines(String... lines) {
        this.lines = SpigotUtils.stringListToMap(Arrays.asList(lines));
    }

    public void setLines(List<String> lines) {
        this.lines = SpigotUtils.stringListToMap(lines);
    }

    public void updateTitle(String title) {
        getBoards().forEach((uuid, fastBoard) -> fastBoard.updateTitle(SpigotUtils.coloredText(title)));
    }

    public void updateLine(int line, String newLine) {
        OptionalWrapper.ofNullable(getLines().get(line))
                .ifPresent()
                .apply(s -> getLines().put(line, SpigotUtils.coloredText(newLine)))
                .elseApply(() -> getLines().putIfAbsent(line, SpigotUtils.coloredText(newLine)));
    }

    public void addLine(int line, String newLine) {
        getLines().put(line, SpigotUtils.coloredText(newLine));
    }

    public void addLine(String newLine) {
        getLines().values().add(SpigotUtils.coloredText(newLine));
    }

    private void updateBoards() {
        getFirstExecutor().scheduleAtFixedRate(() -> getBoards().forEach((uuid, fastBoard) ->
                getSecondExecutor().execute(() -> fastBoard.updateLines(getLines(uuid)))), 1, 1, TimeUnit.SECONDS);
    }

    public void onJoin(Player player) {
        FastBoard fastBoard = new FastBoard(player);
        fastBoard.updateTitle(getTitle());
        getBoards().put(player.getUniqueId(), fastBoard);
    }

    public void onLeave(Player player) {
        Optional.ofNullable(getBoards().remove(player.getUniqueId())).ifPresent(FastBoard::delete);
    }

}