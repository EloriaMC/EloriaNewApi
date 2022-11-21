package fr.eloria.api.data.user;

import fr.eloria.api.data.rank.Rank;
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
    private Rank rank;

    public Document toDocument() {
        return new Document("_id", this.uuid.toString())
                .append("rank", this.rank.getName());
    }

}

