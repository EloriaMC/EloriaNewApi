package fr.eloria.api.data.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import fr.eloria.api.Api;
import fr.eloria.api.data.user.data.UserSettings;
import fr.eloria.api.utils.json.IAdapter;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserAdapter implements IAdapter<User> {

    @Override
    public JsonElement serialize(User user) {
        JsonObject jsonObject = new JsonObject();
        JsonObject settings = new JsonObject();

        jsonObject.addProperty("_id", user.getUuid().toString());
        jsonObject.addProperty("rank", user.getRank().getName());

        settings.addProperty("allowFriendRequest", user.getSettings().isAllowFriendRequest());
        settings.addProperty("allowFriendNotification", user.getSettings().isAllowFriendNotification());
        settings.addProperty("allowPrivateMessage", user.getSettings().isAllowPrivateMessage());
        settings.addProperty("allowMention", user.getSettings().isAllowMention());

        jsonObject.add("settings", settings);

        return jsonObject;
    }

    @Override
    public User deserialize(JsonElement jsonElement) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        return new User(
                UUID.fromString(jsonObject.get("_id").getAsString()),
                Api.getInstance().getRankManager().getRank(jsonObject.get("rank").getAsString()),
                new UserSettings(
                        jsonObject.get("settings").getAsJsonObject().get("allowFriendRequest").getAsBoolean(),
                        jsonObject.get("settings").getAsJsonObject().get("allowFriendNotification").getAsBoolean(),
                        jsonObject.get("settings").getAsJsonObject().get("allowPrivateMessage").getAsBoolean(),
                        jsonObject.get("settings").getAsJsonObject().get("allowMention").getAsBoolean()
                        )
        );
    }

}
