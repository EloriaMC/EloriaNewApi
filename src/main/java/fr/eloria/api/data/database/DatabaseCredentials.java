package fr.eloria.api.data.database;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DatabaseCredentials {

    private String url, host, username, password, databaseName;
    private int port;

    public DatabaseCredentials(String url, String host, String username, String password, String databaseName, int port) {
        this.url = url;
        this.host = host;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
        this.port = port;
    }

}