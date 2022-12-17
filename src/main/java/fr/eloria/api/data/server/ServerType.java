package fr.eloria.api.data.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServerType {

    @BsonProperty(value = "_id")
    private String name;

    private int maxPlayers;

}
