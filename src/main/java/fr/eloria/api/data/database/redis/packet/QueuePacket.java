package fr.eloria.api.data.database.redis.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class QueuePacket {

    private final String game;
    private final UUID player;
    private final QueueAction action;

    public enum QueueAction {
        ADD,
        REMOVE;
    }

}
