package fr.eloria.api.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GsonUtils {

    public final Gson GSON = buildGson();

    private Gson buildGson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapterFactory(InterfaceAdapter.getInterfaceTypeAdapterFactory())
                .create();
    }

}

