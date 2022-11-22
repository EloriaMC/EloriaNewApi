package fr.eloria.api.data.user;

import fr.eloria.api.data.rank.Rank;
import fr.eloria.api.data.user.data.UserSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.Document;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private UUID uuid;
    private Rank rank;

    private UserSettings settings;

    public Document toDocument() {
        return new Document("_id", getUuid().toString())
                .append("rank", getRank().getName())
                .append("settings", getSettings().toDocument());
    }

    public String toStrings() {
        return "User{" +
                "uuid=" + uuid +
                ", rank=" + rank +
                ", settings=" + settings +
                '}';
    }
}

