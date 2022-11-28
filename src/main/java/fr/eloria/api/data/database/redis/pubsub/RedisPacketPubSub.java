package fr.eloria.api.data.database.redis.pubsub;

import fr.eloria.api.utils.MultiThreading;
import fr.eloria.api.utils.json.GsonUtils;
import redis.clients.jedis.JedisPubSub;

public class RedisPacketPubSub extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        RedisPacketWrapper packetWrapper = GsonUtils.GSON.fromJson(message, RedisPacketWrapper.class);

        MultiThreading.runAsync(packetWrapper.getPacket()::onReceive);
    }

}
