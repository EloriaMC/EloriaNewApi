package fr.eloria.api.utils.scoreboard;

import com.google.common.collect.Maps;
import fr.eloria.api.Api;
import fr.eloria.api.data.user.User;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

@Getter
public class BoardManager {

    private final SpigotPlugin plugin;
    private final Board board;

    private final Map<UUID, Board> boards;

    public BoardManager(SpigotPlugin plugin, Board board) {
        this.plugin = plugin;
        this.board = board;
        this.boards = Maps.newConcurrentMap();
        this.updateBoards();
    }

    private void updateBoards() {
        getPlugin().getServer().getScheduler().runTaskTimer(getPlugin(), () ->
                getBoards().forEach(this::updateLines), 0, 20);
    }

    public void onJoin(Player player) {
        Board scoreboard = getBoard().build(player);
        scoreboard.getFastBoard().updateTitle(getBoard().getTitle());
        getBoards().put(player.getUniqueId(), scoreboard);
    }

    public void onLeave(Player player) {
        Board scoreboard = getBoards().get(player.getUniqueId());
        getBoards().remove(player.getUniqueId(), getBoard());
        if (scoreboard.getFastBoard() != null) scoreboard.getFastBoard().delete();
    }

    private void updateLines(UUID uuid, Board board) {
        User user = Api.getInstance().getUserManager().getUser(uuid);

        board.getFastBoard().updateLines(
                "",
                "Allow: " + (user.getSettings().isAllowMention() ? "&aOui" : "&cNon"),
                "");
    }

}
