package fr.eloria.api.data.database.redis.pubsub;

public interface RedisPacket {

    String getName();

    void onReceive();

}
