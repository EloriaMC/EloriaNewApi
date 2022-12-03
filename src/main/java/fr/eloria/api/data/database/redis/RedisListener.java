package fr.eloria.api.data.database.redis;

import redis.clients.jedis.JedisPubSub;

public interface RedisListener {

    JedisPubSub getPubSub();

    String getName();

}
