package fr.eloria.api.plugin.spigot;

import com.google.gson.reflect.TypeToken;
import fr.eloria.api.Api;
import fr.eloria.api.data.database.redis.packet.TestPacket;
import fr.eloria.api.plugin.spigot.command.RankCommand;
import fr.eloria.api.plugin.spigot.command.ServerInfoCommand;
import fr.eloria.api.plugin.spigot.listener.PlayerListener;
import fr.eloria.api.utils.SpigotUtils;
import fr.eloria.api.utils.command.ECommandHandler;
import fr.eloria.api.utils.command.annotation.ECommand;
import fr.eloria.api.utils.command.converter.PlayerConvertor;
import fr.eloria.api.utils.command.converter.RankConvertor;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SpigotPlugin extends JavaPlugin {

    private Api api;

    private ECommandHandler commandHandler;

    @Override
    public void onEnable() {
        this.api = new Api(false);
        this.commandHandler = new ECommandHandler(this, "api");
        this.load();
    }

    public void load() {
        getCommandHandler().registerConverters(new RankConvertor(this), new PlayerConvertor());
        getCommandHandler().registerCommands(this, new ServerInfoCommand(this), new RankCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getApi().getRedisManager().getRedisMessenger().register("test", TypeToken.get(TestPacket.class), false);
    }

    @ECommand(name = "dev")
    public void execute(Player sender) {
        getApi().getRankManager().getRanksOrdainedByPower().forEach(rank -> sender.sendMessage("Grade " + rank.getName() + "#" + rank.getPower()));
        getApi().getUserManager().getUsers().get(sender.getUniqueId()).setRank(getApi().getRankManager().getRank("Admin"));
    }

    @ECommand(name = "redisTest")
    public void executeRedisTest(CommandSender sender) {
        getApi().getRedisManager().getRedisMessenger().getChannel("test", TestPacket.class).sendMessage(new TestPacket("hello redis !"));
    }

    @Override
    public void onDisable() {
        getApi().unload();
    }

}


