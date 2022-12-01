package fr.eloria.api.data.database.redis.channel;

import com.google.common.collect.Sets;
import com.google.gson.reflect.TypeToken;
import fr.eloria.api.data.database.redis.messenger.IRedisMessenger;
import fr.eloria.api.utils.json.GsonUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class RedisChannel<T> implements IRedisChannel<T> {

    private final String name;
    private final TypeToken<T> type;
    private final IRedisMessenger messenger;
    private final JedisPool jedisPool;
    private final Set<IRedisChannelListener<T>> listeners;

    public RedisChannel(String name, TypeToken<T> type, IRedisMessenger messenger, JedisPool jedisPool) {
        this.name = name;
        this.type = type;
        this.messenger = messenger;
        this.jedisPool = jedisPool;
        this.listeners = Sets.newHashSet();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IRedisMessenger getMessenger() {
        return messenger;
    }

    @Override
    public TypeToken<T> getType() {
        return type;
    }

    @Override
    public Set<IRedisChannelListener<T>> getListeners() {
        return listeners;
    }

    @Override
    public void sendMessage(T object) {
        callListenersSend(object);

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(getName(), GsonUtils.GSON.toJson(object));
        }
    }

    public void callListeners(T object) {
        getListeners().forEach(listener -> listener.listen(this, object));
    }

    public void callListenersSend(T object) {
        getListeners().forEach(listener -> listener.send(this, object));
    }

    @Override
    public void addListener(IRedisChannelListener<T> listener) {
        getListeners().add(listener);
    }

    @Override
    public void removeListener(IRedisChannelListener<T> listener) {
        getListeners().remove(listener);
    }

}
