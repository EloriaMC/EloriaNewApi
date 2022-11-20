package fr.eloria.api.data.user;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import fr.eloria.api.Api;
import fr.eloria.api.data.rank.Rank;
import lombok.Getter;
import org.bson.Document;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

@Getter
public class UserManager {

    private final MongoCollection<Document> userCollection;
    private final ConcurrentMap<UUID, User> users;

    public UserManager() {
        this.userCollection = Api.getInstance().getMongoManager().getDatabase().getCollection("users");
        this.users = Maps.newConcurrentMap();
    }

    public User getUser(UUID uuid) {
        // Get From redis
        return new User(uuid, new Rank());
    }

    public void saveUsers() {
        getUsers().clear();
    }

    public void onNetworkJoin(UUID uuid) {
        User user = getUserFromMongo(uuid);

        sendUserToRedis(user);
        addUser(user);
    }

    public void onJoin(UUID uuid) {
        User user = getUserFromRedis(uuid);

        addUser(user);
    }

    public void onNetworkLeave(UUID uuid) {
        User user = getUserFromRedis(uuid);

        sendUserToMongo(user);
        removeUserFromRedis(user);
        removeUser(uuid);
    }

    public void onLeave(UUID uuid) {
        User user = getUserFromRedis(uuid);

        sendUserToRedis(user);
        removeUser(uuid);
    }

    public void addUser(UUID uuid, User user) {
        getUsers().put(uuid, user);
    }

    public void addUser(User user) {
        addUser(user.getUuid(), user);
    }

    public void removeUser(UUID uuid) {
        getUsers().remove(uuid);
    }

    public void sendUserToRedis(User user) {

    }

    public void sendUserToMongo(User user) {

    }

    public void removeUserFromRedis(User user) {

    }

    public void removeUserFromMongo(User user) {

    }

    public User getUserFromRedis(UUID uuid) {
        return new User(uuid, new Rank());
    }

    public User getUserFromMongo(UUID uuid) {
        return new User(uuid, new Rank());
    }

    public User documentToUser(Document document) {
        return new User(
                UUID.fromString(document.getString("_id")),
                Api.getInstance().getRankManager().getRank(document.getString("rank"))
        );
    }


}
