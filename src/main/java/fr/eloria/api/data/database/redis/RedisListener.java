package fr.eloria.api.data.database.redis;

import java.util.function.BiConsumer;

public interface RedisListener {

    String getName();

    BiConsumer<String, String> onMessage();

}
