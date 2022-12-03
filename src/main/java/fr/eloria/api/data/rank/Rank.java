package fr.eloria.api.data.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rank {

    /*

    Admin
    Responsable
    Developper
    SMod
    Mod
    Helper
    Builder
    Famous
    Youtuber
    Tiktok
    Custom
    Joueur

     */

    private String name;

    private String prefix;
    private int power;
    private boolean defaultRank;
    private List<String> permissions;

    public boolean hasPermission(String permission) {
        return getPermissions()
                .stream()
                .anyMatch(permission::equalsIgnoreCase);
    }

    public Document toDocument() {
        return new Document("name", getName())
                .append("prefix", getPrefix())
                .append("power", getPower())
                .append("defaultRank", isDefaultRank())
                .append("permissions", getPermissions());
    }

}
