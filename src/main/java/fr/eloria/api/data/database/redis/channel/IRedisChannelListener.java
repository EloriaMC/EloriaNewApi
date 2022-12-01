package fr.eloria.api.data.database.redis.channel;

public interface IRedisChannelListener<T> {

    void listen(IRedisChannel<T> channel, T object);

    default void send(IRedisChannel<T> channel, T object) {}

}
