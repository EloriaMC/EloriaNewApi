package fr.eloria.api.data.database.redis;

import fr.eloria.api.data.database.AbstractDatabase;
import fr.eloria.api.data.database.DatabaseCredentials;
import lombok.Getter;

@Getter
public class RedisManager extends AbstractDatabase {

    public RedisManager(DatabaseCredentials credentials) {
        super(credentials);
        this.connect();
    }

}
