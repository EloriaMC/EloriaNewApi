package fr.eloria.api.plugin.spigot.common.redis;

import com.google.common.collect.Lists;
import fr.eloria.api.data.database.redis.RedisListener;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.utils.json.GsonUtils;
import lombok.Getter;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.List;

@Getter
public class RedisMessenger {

    private final SpigotPlugin plugin;

    private final List<RedisListener> listeners;

    public RedisMessenger(SpigotPlugin plugin) {
        this.plugin = plugin;
        this.listeners = Lists.newLinkedList();
    }

    public void load() {}

    public RedisListener getListener(String channel) {
        return getListeners().stream().filter(listener -> listener.getChannelName().equals(channel)).findFirst().orElse(null);
    }

    public void addListeners(RedisListener... listeners) {
        Arrays.asList(listeners)
                .forEach(this::addListener);
    }

    public void addListener(RedisListener listener) {
        try (Jedis jedis = getPlugin().getApi().getRedisManager().getJedis()) {
            getListeners().add(listener);
            jedis.subscribe(listener, listener.getChannelName());
        }
    }

    public void removeListeners(String... channels) {
        Arrays.asList(channels)
                .forEach(this::removeListener);
    }

    public void removeListener(String channel) {
        getListener(channel).unsubscribe();
        getListeners().remove(getListener(channel));
    }

    public <T> void sendMessage(String channel, T packet) {
        getPlugin().getApi().getRedisManager().getJedis().publish(channel, GsonUtils.GSON.toJson(packet));
    }

    public void unload() {
        getListeners().stream().map(RedisListener::getChannelName).forEach(this::removeListener);
    }

}
