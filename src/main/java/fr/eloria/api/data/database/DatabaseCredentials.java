package fr.eloria.api.data.database;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

    public DatabaseCredentials setUrl(String url) {
        this.url = url;
        return this;
    }

    public DatabaseCredentials setHost(String host) {
        this.host = host;
        return this;
    }

    public DatabaseCredentials setUsername(String username) {
        this.username = username;
        return this;
    }

    public DatabaseCredentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public DatabaseCredentials setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
        return this;
    }

    public DatabaseCredentials setPort(int port) {
        this.port = port;
        return this;
    }

}