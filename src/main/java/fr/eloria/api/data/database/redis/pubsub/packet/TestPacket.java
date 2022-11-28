package fr.eloria.api.data.database.redis.pubsub.packet;

import fr.eloria.api.data.database.redis.pubsub.RedisPacket;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TestPacket implements RedisPacket {

    private String text;

    @Override
    public String getName() {
        return "TestPacket";
    }

    @Override
    public void onReceive() {
        System.out.println("[Redis] message received: " + text);
    }

}
