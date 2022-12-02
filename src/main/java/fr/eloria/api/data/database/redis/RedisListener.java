package fr.eloria.api.data.database.redis;

import lombok.Getter;
import redis.clients.jedis.JedisPubSub;

@Getter
public abstract class RedisListener extends JedisPubSub {

    private final String channelName;

    public RedisListener(String channelName) {
        this.channelName = channelName;
    }

}
