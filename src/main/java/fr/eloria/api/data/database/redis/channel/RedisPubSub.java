package fr.eloria.api.data.database.redis.channel;

import fr.eloria.api.data.database.redis.messenger.IRedisMessenger;
import fr.eloria.api.utils.json.GsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import redis.clients.jedis.JedisPubSub;

@Getter
@AllArgsConstructor
public class RedisPubSub extends JedisPubSub {

    private final IRedisMessenger messenger;

    @Override
    public void onMessage(String channel, String message) {
        RedisChannel<Object> redisChannel = (RedisChannel<Object>) getMessenger().getChannels().get(channel);

        if (redisChannel != null) {
            Object object = GsonUtils.GSON.fromJson(message, redisChannel.getType().getType());

            System.out.println("[RedisManager] Message receveid :");
            System.out.println(message);

            redisChannel.callListeners(object);
        }
    }

}
