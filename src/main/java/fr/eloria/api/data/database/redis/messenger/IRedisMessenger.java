package fr.eloria.api.data.database.redis.messenger;

import com.google.gson.reflect.TypeToken;
import fr.eloria.api.data.database.redis.channel.IRedisChannel;
import fr.eloria.api.data.database.redis.channel.RedisChannel;

import java.util.Map;

public interface IRedisMessenger {

    Map<String, RedisChannel<?>> getChannels();

    void register(String name, TypeToken<?> type, boolean subscribe);

    <T> IRedisChannel<T> getChannel(String name, TypeToken<T> type);

    default <T> IRedisChannel<T> getChannel(String name, Class<T> type){
        return getChannel(name, TypeToken.get(type));
    }

    void disconnect();

}
