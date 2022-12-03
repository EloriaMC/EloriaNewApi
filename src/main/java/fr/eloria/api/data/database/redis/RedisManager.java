package fr.eloria.api.data.database.redis;

import fr.eloria.api.data.database.AbstractDatabase;
import fr.eloria.api.data.database.DatabaseCredentials;
import fr.eloria.api.utils.json.GsonUtils;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

@Getter
public class RedisManager extends AbstractDatabase {

    private JedisPool jedisPool;
    private final boolean useSsl;

    public RedisManager(DatabaseCredentials credentials, boolean useSsl) {
        super(credentials);
        this.useSsl = useSsl;
        this.connect();
    }

    @Override
    public void connect() {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), getCredentials().getHost(), getCredentials().getPort(), 2000, isUseSsl());
    }

    public Jedis getJedis() {
        return getJedisPool().getResource();
    }

    public Set<String> keys(String key) {
        return getJedis().keys(key);
    }

    public <T> void set(String key, T clazz) {
        getJedis().set(key, GsonUtils.GSON.toJson(clazz));
    }

    public void set(String key, String json) {
        getJedis().set(key, json);
    }

    public <T> T get(String key, Class<T> clazz) {
        return GsonUtils.GSON.fromJson(getJedis().get(key), clazz);
    }

    public String get(String key) {
        return getJedis().get(key);
    }

    public void del(String key) {
        getJedis().del(key);
    }

    @Override
    public void disconnect() {
        getJedisPool().close();
    }

}
