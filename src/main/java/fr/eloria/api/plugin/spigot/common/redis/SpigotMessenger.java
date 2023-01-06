package fr.eloria.api.plugin.spigot.common.redis;

import com.google.common.collect.Lists;
import fr.eloria.api.data.database.redis.RedisListener;
import fr.eloria.api.data.database.redis.RedisMessenger;
import fr.eloria.api.data.database.redis.RedisPubSub;
import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.utils.json.GsonUtils;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpigotMessenger implements RedisMessenger {

    @Getter private final SpigotPlugin plugin;

    @Getter private final StatefulRedisPubSubConnection<String, String> connection;
    private final List<RedisListener> listeners;

    public SpigotMessenger(SpigotPlugin plugin) {
        this.plugin = plugin;
        this.connection = getPlugin().getApi().getRedisManager().getRedisClient().connectPubSub();
        this.listeners = Lists.newLinkedList();
    }

    public void load() {

    }

    @Override
    public List<RedisListener> getListeners() {
        return listeners;
    }

    public RedisListener getListener(String channel) {
        return getListeners().stream().filter(listener -> listener.getName().equals(channel)).findFirst().orElse(null);
    }

    public void addListeners(RedisListener... listeners) {
        getConnection().addListener(new RedisPubSub(this));
        Arrays.asList(listeners).forEach(this::addListener);
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
