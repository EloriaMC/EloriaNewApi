package fr.eloria.api.utils.scoreboard;

import fr.eloria.api.utils.SpigotUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
public class Board {

    private FastBoard fastBoard;
    private String title;
    private List<String> lines;

    public Board setTitle(String title) {
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        return this;
    }

    public Board setLines(String... lines) {
        this.lines = SpigotUtils.coloredTexts(Arrays.asList(lines));
        return this;
    }

    public Board build(Player player) {
        this.fastBoard = new FastBoard(player);
        return this;
    }

}
