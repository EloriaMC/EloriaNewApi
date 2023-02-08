package fr.eloria.api.data.user;

import fr.eloria.api.Api;
import fr.eloria.api.data.rank.Rank;
import fr.eloria.api.data.user.data.UserSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private UUID uuid;
    private String rankName;
    private int coins;

    private UserSettings settings;
    private Map<String, Map<String, Object>> stats;

    public void setRank(Rank rank) {
        this.rankName = rank.getName();
        Api.getInstance().getUserManager().sendUserToRedis(this);
    }

    public Map<String, Object> getStat(String statName) {
        getStats().putIfAbsent(statName, new HashMap<>());
        return getStats().get(statName);
    }

    public Number getStat(String statName, String key) {
        return (Number) getStat(statName).getOrDefault(key, 0);
    }

    public void addStats(String statName, String key, int value) {
        getStat(statName).compute(key, (k, v) -> (v == null) ? value : ((Number) v).intValue() + value);
        Api.getInstance().getUserManager().sendUserToRedis(this);
    }

    public void addStats(String statName, String key, double value) {
        getStat(statName).compute(key, (k, v) -> (v == null) ? value : ((Number) v).doubleValue() + value);
        Api.getInstance().getUserManager().sendUserToRedis(this);
    }

    public void addStats(String statName, String key, long value) {
        getStat(statName).compute(key, (k, v) -> (v == null) ? value : ((Number) v).longValue() + value);
        Api.getInstance().getUserManager().sendUserToRedis(this);
    }

    public void addStats(String statName, String key, Object value) {
        getStat(statName).put(key, value);
    }

    public void addCoins(int coins) {
        this.coins = getCoins() + coins;
    }

    public void removeCoins(int coins) {
        this.coins = getCoins() - coins;
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

