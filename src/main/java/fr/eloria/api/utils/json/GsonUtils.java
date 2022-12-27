package fr.eloria.api.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.eloria.api.data.rank.Rank;
import fr.eloria.api.data.rank.RankAdapter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GsonUtils {

    public final Gson GSON = buildGson();

    private Gson buildGson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .registerTypeAdapter(Rank.class, new RankAdapter())
                .create();
    }

}

