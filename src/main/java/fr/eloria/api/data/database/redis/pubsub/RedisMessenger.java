package fr.eloria.api.data.database.redis.pubsub;

import fr.eloria.api.utils.MultiThreading;
import fr.eloria.api.utils.json.GsonUtils;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public class RedisMessenger {

    private final JedisPool jedisPool;
    private final RedisPacketPubSub pubSub;

    public RedisMessenger(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        this.pubSub = new RedisPacketPubSub();

        connect();
    }

    private void connect() {
        // Default channel
        registerChannel("eloria");
    }

    public void sendMessage(String channel, RedisPacket packet) {
        MultiThreading.runAsync(() -> {
            try (Jedis publisher = getJedisPool().getResource()) {
                publisher.publish(channel, GsonUtils.GSON.toJson(new RedisPacketWrapper(packet)));
            }
        });
    }

    public void registerChannel(String channel) {
        MultiThreading.runAsync(() ->
                getJedisPool().getResource().subscribe(getPubSub(), channel));
    }

}
