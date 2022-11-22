package fr.eloria.api.data.user.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

@Getter
@Setter
@AllArgsConstructor
public class UserSettings {

    private boolean allowFriendRequest, allowFriendNotification, allowPrivateMessage, allowMention;

    public Document toDocument() {
        return new Document()
                .append("allowFriendRequest", isAllowFriendRequest())
                .append("allowFriendNotification", isAllowFriendNotification())
                .append("allowPrivateMessage", isAllowPrivateMessage())
                .append("allowMention", isAllowMention());
    }

}
