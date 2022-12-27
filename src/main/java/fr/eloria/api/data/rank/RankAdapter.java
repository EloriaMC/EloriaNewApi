package fr.eloria.api.data.rank;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class RankAdapter implements JsonSerializer<Rank>, JsonDeserializer<Rank> {

    @Override
    public JsonElement serialize(Rank rank, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        rank.getPermissions().forEach(permission -> jsonArray.add(new JsonPrimitive(permission)));

        jsonObject.addProperty("name", rank.getName());
        jsonObject.addProperty("prefix", rank.getPrefix());
        jsonObject.addProperty("power", rank.getPower());
        jsonObject.addProperty("defaultRank", rank.getName());
        jsonObject.add("permissions", jsonArray);

        return jsonObject;
    }

    @Override
    public Rank deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        List<String> permissions = new LinkedList<>();
        JsonArray jsonArray = jsonObject.get("permissions").getAsJsonArray();

        jsonArray.forEach(element -> permissions.add(element.getAsString()));

        return new Rank(
                jsonObject.get("name").getAsString(),
                jsonObject.get("prefix").getAsString(),
                jsonObject.get("power").getAsInt(),
                jsonObject.get("defaultRank").getAsBoolean(),
                permissions
                );
    }

}
