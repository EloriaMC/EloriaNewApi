package fr.eloria.api.plugin.bungee.common.redis;

import com.google.common.collect.Lists;
import fr.eloria.api.data.database.redis.RedisListener;
import fr.eloria.api.data.database.redis.RedisMessenger;
import fr.eloria.api.data.database.redis.RedisPubSub;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import fr.eloria.api.plugin.bungee.common.redis.listener.QueueListener;
import fr.eloria.api.utils.MultiThreading;
import fr.eloria.api.utils.json.GsonUtils;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BungeeMessenger implements RedisMessenger {

    @Getter private final BungeePlugin plugin;

    @Getter private final StatefulRedisPubSubConnection<String, String> connection;
    private final List<RedisListener> listeners;

    public BungeeMessenger(BungeePlugin plugin) {
        this.plugin = plugin;
        this.connection = plugin.getApi().getRedisManager().getRedisClient().connectPubSub();
        this.listeners = Lists.newLinkedList();
    }

    public void load() {
        MultiThreading.schedule(() -> addListeners(new QueueListener(getPlugin())), 1, TimeUnit.SECONDS);
    }

    @Override
    public List<RedisListener> getListeners() {
        return listeners;
    }

    public RedisListener getListener(String channel) {
        return getListeners().stream().filter(listener -> listener.getName().equals(channel)).findFirst().orElse(null);
    }

    public void addListeners(RedisListener... listeners) {
        Arrays.asList(listeners).forEach(this::addListener);
        getConnection().addListener(new RedisPubSub(this));
    }

    public void addListener(RedisListener listener) {
        getListeners().add(listener);
        getConnection().async().subscribe(listener.getName());
        System.out.println("[RedisManager] Subscribed to " + listener.getName() + " channel");
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
