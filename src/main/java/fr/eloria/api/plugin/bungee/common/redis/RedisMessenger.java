package fr.eloria.api.plugin.bungee.common.redis;

import com.google.common.collect.Lists;
import fr.eloria.api.data.database.redis.RedisListener;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import fr.eloria.api.plugin.bungee.common.redis.listener.QueueListener;
import fr.eloria.api.utils.MultiThreading;
import fr.eloria.api.utils.json.GsonUtils;
import lombok.Getter;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
public class RedisMessenger {

    private final BungeePlugin plugin;

    private final List<RedisListener> listeners;

    public RedisMessenger(BungeePlugin plugin) {
        this.plugin = plugin;
        this.listeners = Lists.newLinkedList();
    }

    public void load() {
        MultiThreading.schedule(() -> addListeners(new QueueListener(getPlugin())), 1, TimeUnit.SECONDS);
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
        try (Jedis jedis = getPlugin().getApi().getRedisManager().getJedisPool().getResource()) {
            jedis.subscribe(listener.getPubSub(), listener.getName());
        }
    }

    public void removeListeners(String... channels) {
        Arrays.asList(channels)
                .forEach(this::removeListener);
    }

    public void removeListener(String channel) {
        getListeners().remove(getListener(channel));
    }

    public <T> void sendMessage(String channel, T packet) {
        getPlugin().getApi().getRedisManager().getJedis().publish(channel, GsonUtils.GSON.toJson(packet));
    }

    public void unload() {
        getListeners().clear();
    }

}
