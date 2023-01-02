package fr.eloria.api.data.database.redis;

import fr.eloria.api.data.database.AbstractDatabase;
import fr.eloria.api.data.database.DatabaseCredentials;
import fr.eloria.api.utils.json.GsonUtils;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Getter
public class RedisManager extends AbstractDatabase {

    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> redisConnection;

    public RedisManager(DatabaseCredentials credentials) {
        super(credentials);
        this.connect();
    }

    @Override
    public void connect() {
        this.redisClient = RedisClient.create("redis://" + getCredentials().getHost() + ":" + getCredentials().getPort() + "/");
        this.redisConnection = redisClient.connect();
    }

    public List<String> keys(String key) {
        try {
            return getRedisConnection().async().keys(key).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void set(String key, T clazz) {
        getRedisConnection().async().set(key, GsonUtils.GSON.toJson(clazz));
    }

    public void set(String key, String json) {
        getRedisConnection().async().set(key, json);
    }

    public <T> T get(String key, Class<T> clazz) {
        try {
            return GsonUtils.GSON.fromJson(getRedisConnection().async().get(key).get(), clazz);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String key) {
        try {
            return getRedisConnection().async().get(key).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void del(String key) {
        getRedisConnection().async().del(key);
    }

    @Override
    public void disconnect() {
        getRedisConnection().close();
        getRedisClient().shutdown();
    }

}
