package fr.eloria.api.data.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ServerStatus {

    OPEN ("Ouvert"),
    PLAYING ("En jeu"),
    WHITELISTED ("Privé"),
    CLOSING ("Arrêt ...");

    private final String status;

    public static ServerStatus getByName(String name) {
        return Arrays.stream(values())
                .filter(status -> status.getStatus().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
