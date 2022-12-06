package fr.eloria.api.plugin.spigot.common.redis;

import com.google.common.collect.Lists;
import fr.eloria.api.data.database.redis.RedisListener;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.utils.json.GsonUtils;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RedisMessenger {

    private final SpigotPlugin plugin;

    private final StatefulRedisPubSubConnection<String, String> connection;
    private final List<RedisListener> listeners;

    public RedisMessenger(SpigotPlugin plugin) {
        this.plugin = plugin;
        this.connection = getPlugin().getApi().getRedisManager().getRedisClient().connectPubSub();
        this.listeners = Lists.newLinkedList();
    }

    public void load() {

    }

    public RedisListener getListener(String channel) {
        return getListeners().stream().filter(listener -> listener.getName().equals(channel)).findFirst().orElse(null);
    }

    public void addListeners(RedisListener... listeners) {
        Arrays.asList(listeners)
                .forEach(this::addListener);
    }

    public void addListener(RedisListener listener) {
        getListeners().add(listener);
        System.out.println("[RedisManager] Subscribed to " + listener.getName() + " channel");
        getConnection().addListener(listener.getPubSub());
        getConnection().async().subscribe(listener.getName());
    }

    public void removeListeners(String... channels) {
        Arrays.asList(channels)
                .forEach(this::removeListener);
    }

    public void removeListeners(List<String> channels) {
        channels.forEach(this::removeListener);
    }

    public void removeListener(String channel) {
        getConnection().async().unsubscribe(channel);
        getListeners().remove(getListener(channel));
    }

    public <T> void sendMessage(String channel, T packet) {
        getConnection().async().publish(channel, GsonUtils.GSON.toJson(packet));
    }

    public void unload() {
        getConnection().close();
        removeListeners(getListeners().stream().map(RedisListener::getName).collect(Collectors.toList()));
    }

}
