package fr.eloria.api.utils.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;

public final class InterfaceAdapter implements TypeAdapterFactory {

    private static final TypeAdapterFactory interfaceTypeAdapterFactory = new InterfaceAdapter();

    private InterfaceAdapter() {}

    static TypeAdapterFactory getInterfaceTypeAdapterFactory() {
        return interfaceTypeAdapterFactory;
    }

    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        return !typeToken.getRawType().isInterface() ? null : new InterfaceTypeAdapter<T>(gson).nullSafe();
    }

    private static final class InterfaceTypeAdapter<T> extends TypeAdapter<T> {

        private static final String TYPE_PROPERTY = "type";
        private static final String DATA_PROPERTY = "data";

        private final Gson gson;

        private InterfaceTypeAdapter(final Gson gson) {
            this.gson = gson;
        }

        @Override
        public void write(JsonWriter jsonWriter, T t) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name(TYPE_PROPERTY);
            jsonWriter.value(t.getClass().getName());
            jsonWriter.name(DATA_PROPERTY);
            gson.toJson(t, t.getClass(), jsonWriter);
            jsonWriter.endObject();
        }

        @Override
        public T read(final JsonReader in) throws IOException {
            try {
                in.beginObject();
                final String name = in.nextName();
                final Object value;
                switch ( name ) {
                    case TYPE_PROPERTY:
                        final String type = in.nextString();
                        if ( !in.nextName().equals(DATA_PROPERTY) )
                            throw new MalformedJsonException("Expected " + DATA_PROPERTY + " at " + in);

                        value = gson.fromJson(in, Class.forName(type));
                        break;
                    case DATA_PROPERTY:
                        final JsonElement jsonElement = gson.fromJson(in, JsonElement.class);
                        if ( !in.nextName().equals(TYPE_PROPERTY) )
                            throw new MalformedJsonException("Expected " + TYPE_PROPERTY + " at " + in);

                        value = gson.fromJson(jsonElement, Class.forName(in.nextString()));
                        break;
                    default:
                        throw new MalformedJsonException("Unrecognized " + name + " at " + in);
                }

                if ( in.hasNext() ) throw new IllegalStateException("Unexpected " + in.nextName() + " at " + in);
                in.endObject();
                @SuppressWarnings("unchecked")
                final T castValue = (T) value;
                return castValue;
            } catch ( final ClassNotFoundException ex ) {
                throw new IOException(ex);
            }
        }
    }

}