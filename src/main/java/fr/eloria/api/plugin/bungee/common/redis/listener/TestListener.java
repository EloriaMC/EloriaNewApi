package fr.eloria.api.plugin.bungee.common.redis.listener;

import fr.eloria.api.data.database.redis.RedisListener;

import java.util.function.BiConsumer;

public class TestListener implements RedisListener {

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public BiConsumer<String, String> onMessage() {
        return (channel, message) -> System.out.println(message);
    }

}
