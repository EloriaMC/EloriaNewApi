package fr.eloria.api.data.user;

import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import fr.eloria.api.Api;
import fr.eloria.api.utils.json.GsonUtils;
import lombok.Getter;
import org.bson.Document;

import java.util.Objects;
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

    private String getRedisKey(UUID uuid) {
        return "users:" + uuid.toString();
    }

    public void saveUsers() {
        getUsers().clear();
    }

    public void addUser(User user) {
        getUsers().put(user.getUuid(), user);
    }

    public void removeUser(User user) {
        getUsers().remove(user.getUuid());
    }

    public User getUserFromRedis(UUID uuid) {
        return Api.getInstance().getRedisManager().get(getRedisKey(uuid), User.class);
    }

    public User getUserFromMongo(UUID uuid) {
        return GsonUtils.GSON.fromJson(Objects.requireNonNull(getUserCollection().find(Filters.eq("_id", uuid.toString())).first()).toJson(), User.class);
    }

    public boolean userExistInMongo(UUID uuid) {
        return getUserCollection().find(Filters.eq("_id", uuid.toString())).first() != null;
    }

    public void sendUserToRedis(User user) {
        Api.getInstance().getRedisManager().set(getRedisKey(user.getUuid()), user);
    }

    public void sendUserToMongo(User user) {
        getUserCollection().updateOne(Filters.eq("_id", user.getUuid().toString()), new Document("$set", user.toDocument()), new UpdateOptions().upsert(true));
    }

    public void removeUserFromRedis(User user) {
        Api.getInstance().getRedisManager().del(getRedisKey(user.getUuid()));
    }

    public void removeUserFromMongo(User user) {
        getUserCollection().deleteOne(new Document("_id", user.getUuid().toString()));
    }

}
