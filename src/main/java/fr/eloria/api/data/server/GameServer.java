package fr.eloria.api.data.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameServer {

    private String name;
    private int onlinePlayers;

    private ServerStatus status;
    private ServerType type;

}
