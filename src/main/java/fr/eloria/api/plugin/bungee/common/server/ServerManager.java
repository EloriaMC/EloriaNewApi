package fr.eloria.api.plugin.bungee.common.server;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import fr.eloria.api.data.server.ServerType;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import fr.eloria.api.utils.json.GsonUtils;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

@Getter
public class ServerManager {

    private final BungeePlugin plugin;

    private final MongoCollection<ServerType> typesCollection;
    private final List<ServerType> serverTypes;

    public ServerManager(BungeePlugin plugin) {
        this.plugin = plugin;
        this.typesCollection = getPlugin().getApi().getMongoManager().getDatabase().getCollection("serverTypes", ServerType.class);
        this.serverTypes = Lists.newLinkedList();
    }

    private String getRedisKey(String typeName) {
        return "serverType:" + typeName;
    }

    public void loadTypes() {
        getTypesCollection().find().iterator().forEachRemaining(this::addServerType);
    }

    public void saveTypes() {
        getServerTypes().forEach(this::removeServerType);
    }

    public void addServerType(ServerType serverType) {
        sendTypeToRedis(serverType);
        getServerTypes().add(serverType);
        System.out.println("[ServerManager] Added " + serverType.getName() + " server type");
    }

    public void removeServerType(ServerType serverType) {
        removeTypeFromRedis(serverType.getName());
        getServerTypes().remove(serverType);
        System.out.println("[ServerManager] Remove " + serverType.getName() + " server type");
    }

    public ServerType getTypeFromRedis(String typeName) {
        return GsonUtils.GSON.fromJson(getPlugin().getApi().getRedisManager().get(getRedisKey(typeName)), ServerType.class);
    }

    public void sendTypeToRedis(ServerType serverType) {
        getPlugin().getApi().getRedisManager().set(getRedisKey(serverType.getName()), GsonUtils.GSON.toJson(serverType));
    }

    public void removeTypeFromRedis(String typeName) {
        getPlugin().getApi().getRedisManager().del(getRedisKey(typeName));
    }

    public void updateTypeInMongo(ServerType serverType) {
        updateTypeInMongo(serverType.getName(), serverType);
    }

    public void updateTypeInMongo(String typeName, ServerType serverType) {
        getTypesCollection().updateOne(Filters.eq("_id", typeName), new Document("$set", serverType), new UpdateOptions().upsert(true));
    }

}
