package fr.eloria.api.data.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import fr.eloria.api.Api;
import fr.eloria.api.utils.IAdapter;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserAdapter implements IAdapter<User> {

    @Override
    public JsonElement serialize(User user) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("_id", user.getUuid().toString());
        jsonObject.addProperty("rank", user.getRank().getName());

        return jsonObject;
    }

    @Override
    public User deserialize(JsonElement jsonElement) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        return new User(
                UUID.fromString(jsonObject.get("_id").getAsString()),
                Api.getInstance().getRankManager().getRank(jsonObject.get("rank").getAsString())
        );
    }

}
