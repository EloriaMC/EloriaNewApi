package fr.eloria.api.plugin.bungee.matchmaking;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import fr.eloria.api.utils.json.GsonUtils;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

@Getter
public class MatchMakingManager {

    private final BungeePlugin plugin;

    private final MongoCollection<MatchQueue> queueCollection;
    private final List<MatchQueue> queues;

    public MatchMakingManager(BungeePlugin plugin) {
        this.plugin = plugin;
        this.queueCollection = plugin.getApi().getMongoManager().getDatabase().getCollection("queues", MatchQueue.class);
        this.queues = Lists.newLinkedList();
    }

    private String getRedisKey(String queueName) {
        return "queues:" + queueName;
    }

    public void loadQueues() {
        getQueueCollection().find().iterator().forEachRemaining(this::addQueue);
    }

    public void saveQueues() {
        getQueues().forEach(queue -> {
                updateQueueInMongo(queue.getName(), queue);
                removeQueueFromRedis(queue.getName());
        });

        getQueues().forEach(getQueues()::remove);
    }

    private void addQueue(MatchQueue queue) {
        sendQueueToRedis(queue);
        getQueues().add(queue);
        System.out.println("[MatchMakingManager] Added " + queue.getName() + " queue");
    }

    public MatchQueue getQueueFromRedis(String queueName) {
        return GsonUtils.GSON.fromJson(getPlugin().getApi().getRedisManager().getJedis().hget(getRedisKey(queueName), "json"), MatchQueue.class);
    }

    public void sendQueueToRedis(MatchQueue queue) {
        getPlugin().getApi().getRedisManager().getJedis().hset(getRedisKey(queue.getName()), "json", GsonUtils.GSON.toJson(queue));
    }

    public void removeQueueFromRedis(String queueName) {
        getPlugin().getApi().getRedisManager().getJedis().hdel(getRedisKey(queueName), queueName);
    }

    public void updateQueueInMongo(String queueName, MatchQueue newQueue) {
        getQueueCollection().updateOne(Filters.eq("_id", queueName), new Document("$set", newQueue), new UpdateOptions().upsert(true));
    }

}
