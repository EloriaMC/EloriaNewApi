package fr.eloria.api.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.eloria.api.data.user.User;
import fr.eloria.api.data.user.UserAdapter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GsonUtils {

    public final Gson GSON = buildGson();

    private Gson buildGson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(User.class, new UserAdapter())
                .create();
    }

}

