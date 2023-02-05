package fr.eloria.api.data.database.redis;

import io.lettuce.core.pubsub.RedisPubSubListener;
import lombok.Data;

import java.util.Optional;

@Data
public class RedisPubSub implements RedisPubSubListener<String, String> {

    private final RedisMessenger redisMessenger;

    @Override
    public void message(String channel, String message) {
        Optional.ofNullable(getChannel(channel))
                .ifPresent(redisListener -> redisListener.onMessage().accept(message));
    }

    @Override
    public void message(String pattern, String channel, String message) {}

    @Override
    public void subscribed(String channel, long count) {}

    @Override
    public void psubscribed(String pattern, long count) {}

    @Override
    public void unsubscribed(String channel, long count) {}

    @Override
    public void punsubscribed(String pattern, long count) {}

    private RedisListener getChannel(String channelName) {
        return getRedisMessenger().getListeners().stream().filter(redisListener -> redisListener.getName().equals(channelName)).findFirst().orElse(null);
    }

}
