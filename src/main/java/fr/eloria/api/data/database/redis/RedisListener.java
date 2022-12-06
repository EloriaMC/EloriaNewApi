package fr.eloria.api.data.database.redis;

import io.lettuce.core.pubsub.RedisPubSubListener;

public interface RedisListener {

    String getName();

    void onMessage(String channel, String message);

    default RedisPubSubListener<String, String> getPubSub() {
        return new RedisPubSubListener<String, String>() {
            @Override
            public void message(String channel, String message) {
                onMessage(channel, message);
            }

            @Override
            public void message(String pattern, String channel, String message) {

            }

            @Override
            public void subscribed(String channel, long count) {

            }

            @Override
            public void psubscribed(String pattern, long count) {

            }

            @Override
            public void unsubscribed(String channel, long count) {

            }

            @Override
            public void punsubscribed(String pattern, long count) {

            }
        };
    }

}
