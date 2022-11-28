package fr.eloria.api.data.database.redis.pubsub;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RedisPacketWrapper {

    private final RedisPacket packet;

}
