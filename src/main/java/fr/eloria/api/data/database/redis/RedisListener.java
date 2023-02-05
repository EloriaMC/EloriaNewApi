package fr.eloria.api.data.database.redis;

import java.util.function.Consumer;

public interface RedisListener {

    String getName();

    Consumer<String> onMessage();

}
