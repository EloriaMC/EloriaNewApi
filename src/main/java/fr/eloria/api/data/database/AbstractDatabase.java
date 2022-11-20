package fr.eloria.api.data.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractDatabase {

    private final DatabaseCredentials credentials;

    public void connect() {}

    public void disconnect() {}

}

