package fr.eloria.api.data.database.redis.channel;

import com.google.gson.reflect.TypeToken;
import fr.eloria.api.data.database.redis.messenger.IRedisMessenger;

import java.util.Set;

public interface IRedisChannel<T> {

    String getName();

    IRedisMessenger getMessenger();

    TypeToken<T> getType();

    void sendMessage(T object);

    void addListener(IRedisChannelListener<T> listener);

    void removeListener(IRedisChannelListener<T> listener);

    Set<IRedisChannelListener<T>> getListeners();

}
