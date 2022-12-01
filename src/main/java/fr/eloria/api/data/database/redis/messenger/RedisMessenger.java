package fr.eloria.api.data.database.redis.messenger;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import fr.eloria.api.data.database.redis.channel.IRedisChannel;
import fr.eloria.api.data.database.redis.channel.RedisChannel;
import fr.eloria.api.data.database.redis.channel.RedisPubSub;
import fr.eloria.api.utils.MultiThreading;
import lombok.Getter;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@Getter
public class RedisMessenger implements IRedisMessenger {

    private final Map<String, RedisChannel<?>> channels;

    private final JedisPool jedisPool;
    private final RedisPubSub redisPubSub;

    public RedisMessenger(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        this.channels = Maps.newConcurrentMap();
        this.redisPubSub = new RedisPubSub(this);
    }

    @Override
    public Map<String, RedisChannel<?>> getChannels() {
        return channels;
    }

    private void register(RedisChannel<?> channel, boolean subscribe) {
        getChannels().put(channel.getName(), channel);
        if (subscribe) MultiThreading.runAsync(() -> getJedisPool().getResource().subscribe(getRedisPubSub(), channel.getName()));
    }

    @Override
    public void register(String name, TypeToken<?> type, boolean subscribe) {
        register(new RedisChannel<>(name, type, this, getJedisPool()), subscribe);
    }

    @Override
    public <T> IRedisChannel<T> getChannel(String name, TypeToken<T> type) {
        return (RedisChannel<T>) getChannels().get(name);
    }

    @Override
    public void disconnect() {
        getChannels().keySet().forEach(getRedisPubSub()::unsubscribe);
        getChannels().clear();
    }

}
