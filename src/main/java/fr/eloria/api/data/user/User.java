package fr.eloria.api.data.user;

import fr.eloria.api.Api;
import fr.eloria.api.data.rank.Rank;
import fr.eloria.api.data.user.data.UserSettings;
import fr.eloria.api.utils.wrapper.BooleanWrapper;
import fr.eloria.api.utils.wrapper.OptionalWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private UUID uuid;
    private String rankName;

    private UserSettings settings;
    private Map<String, Map<String, String>> stats;

    public void setRank(Rank rank) {
        this.rankName = rank.getName();
        Api.getInstance().getUserManager().sendUserToRedis(this);
    }

    public Map<String, String> getStat(String statName) {
        getStats().putIfAbsent(statName, new HashMap<>());
        return getStats().get(statName);
    }

    public void addStats(String statName, String key, int value) {
        getStat(statName).compute(key, (k, v) -> (v == null) ? String.valueOf(value) : String.valueOf(Integer.parseInt(v) + value));
        Api.getInstance().getUserManager().sendUserToRedis(this);
    }

    public void addStats(String statName, String key, String value) {
        getStat(statName).put(key, value);
    }

    public Rank getRank() {
        return Api.getInstance().getRankManager().getRank(getRankName());
    }

    public Document toDocument() {
        return new Document("uuid", getUuid().toString())
                .append("rankName", getRank().getName())
                .append("stats", getStats())
                .append("settings", getSettings().toDocument());
    }

}

