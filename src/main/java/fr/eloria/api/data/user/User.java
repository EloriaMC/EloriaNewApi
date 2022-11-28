package fr.eloria.api.data.user;

import fr.eloria.api.Api;
import fr.eloria.api.data.rank.Rank;
import fr.eloria.api.data.user.data.UserSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private UUID uuid;
    private String rankName;

    private UserSettings settings;

    public void setRank(Rank rank) {
        this.rankName = rank.getName();
        Api.getInstance().getUserManager().sendUserToRedis(this);
    }

    public Rank getRank() {
        return Api.getInstance().getRankManager().getRank(getRankName());
    }

    public Document toDocument() {
        return new Document("uuid", getUuid().toString())
                .append("rankName", getRank().getName())
                .append("settings", getSettings().toDocument());
    }

}

