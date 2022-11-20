package fr.eloria.api.data.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

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

    @BsonProperty(value = "_id")
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

}
