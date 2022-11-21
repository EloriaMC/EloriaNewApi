package fr.eloria.api.utils;

import com.google.gson.*;

import java.lang.reflect.Type;

public interface IAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {

    JsonElement serialize(T type);

    T deserialize(JsonElement jsonElement) throws JsonParseException;

    @Override
    default JsonElement serialize(T clazz, Type type, JsonSerializationContext jsonSerializationContext) {
        return serialize(clazz);
    }

    @Override
    default T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return deserialize(jsonElement);
    }

}