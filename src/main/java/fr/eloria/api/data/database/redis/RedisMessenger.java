package fr.eloria.api.data.database.redis;

import java.util.List;

public interface RedisMessenger {

    List<RedisListener> getListeners();

}
