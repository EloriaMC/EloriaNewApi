package fr.eloria.api.plugin.bungee.common.matchmaking;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import fr.eloria.api.data.server.GameServer;
import fr.eloria.api.data.user.User;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import fr.eloria.api.utils.json.GsonUtils;
import fr.eloria.api.utils.wrapper.BooleanWrapper;
import fr.eloria.api.utils.wrapper.OptionalWrapper;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@Getter
public class MatchMakingManager {

    private final BungeePlugin plugin;

    private final MongoCollection<MatchQueue> queueCollection;
    private final List<MatchQueue> queues;
    private final List<String> requestedServer;

    public MatchMakingManager(BungeePlugin plugin) {
        this.plugin = plugin;
        this.queueCollection = plugin.getApi().getMongoManager().getDatabase().getCollection("queues", MatchQueue.class);
        this.queues = Lists.newLinkedList();
        this.requestedServer = Lists.newLinkedList();

        ProxyServer.getInstance().getScheduler().schedule(getPlugin(), this::update, 1L, 1L, TimeUnit.SECONDS);
    }

    private String getRedisKey(String queueName) {
        return "queues:" + queueName;
    }

    public MatchQueue getQueue(String queueName) {
        return getQueues().stream().filter(matchQueue -> queueName.equals(matchQueue.getName())).findFirst().orElse(null);
    }

    public String getRequestServer(String serverName) {
        return getRequestedServer().stream().filter(s -> s.equals(serverName)).findFirst().orElse(null);
    }

    public void update() {
            getQueues().stream().filter(((Predicate<? super MatchQueue>) MatchQueue::isEmpty).negate())
                    .forEach(matchQueue -> matchQueue.getQueuedPlayer()
                    .stream().map(getPlugin().getProxy()::getPlayer)
                    .forEach(player -> player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(StringUtils.capitalize(matchQueue.getName()) + " (" + matchQueue.getPosition(player.getUniqueId()) +  "/" + matchQueue.getSize() +  ")"))));
    }

    public void connect(MatchQueue queue, UUID uuid) {
        User user = getPlugin().getApi().getUserManager().getUser(uuid);

        OptionalWrapper.ofNullable(getPlugin().getLoader().getServerManager().getServerWithMorePlayer(queue.getName()))
                .ifPresent()
                .apply(gameServer -> {

                })
                .elseApply(() -> addRequestServer(queue.getName()));

        /*bestServer.ifPresent(gameServer -> {
            switch (gameServer.getStatus()) {

                case OPEN:

                    break;

                case PLAYING:
                    break;

                default:
                    if (gameServer.getOnlinePlayers() >= gameServer.getType().getMaxPlayers())
                        if (user.getRank().hasPermission("eloria.queue.bypass"))
                            ProxyServer.getInstance().getPlayer(uuid).connect(ProxyServer.getInstance().getServerInfo(gameServer.getName()));
                    break;
            }
        });*/
    }

    public void loadQueues() {
        getQueueCollection().find().iterator().forEachRemaining(this::addQueue);
    }

    public void saveQueues() {
        getQueues().forEach(this::removeQueue);
    }

    public void addQueue(MatchQueue queue) {
        sendQueueToRedis(queue);
        getQueues().add(queue);
        System.out.println("[MatchMakingManager] Added " + queue.getName() + " queue");
    }

    public void addRequestServer(String serverName) {
        OptionalWrapper.ofNullable(getRequestServer(serverName))
                .ifNotPresent()
                .apply(() -> getRequestedServer().add(serverName));
    }

    public void removeQueue(String queueName) {
        removeQueue(getQueue(queueName));
    }

    public void removeQueue(MatchQueue queue) {
        removeQueueFromRedis(queue.getName());
        getQueues().remove(queue);
        System.out.println("[MatchMakingManager] Remove " + queue.getName() + " queue");
    }

    public void removeRequestServer(String serverName) {
        getRequestedServer().remove(serverName);
        System.out.println("[MatchMakingManager] Remove " + serverName + " from requested server");
    }

    public void addPlayer(String queueName, UUID uuid) {
        BooleanWrapper.of(getQueue(queueName).getQueuedPlayer().contains(uuid))
                .ifFalse(() -> {
                    onLogout(uuid);
                    getQueue(queueName).addPlayer(uuid);
                });
    }

    public void removeAllPlayers(String queueName) {
        getQueue(queueName).getQueuedPlayer().clear();
    }

    public void removePlayer(String queueName, UUID uuid) {
        getQueue(queueName).removePlayer(uuid);
    }

    public void onLogout(UUID uuid) {
        getQueues().stream().filter(queue -> queue.contains(uuid)).findFirst().ifPresent(queue -> removePlayer(queue.getName(), uuid));
    }

    public MatchQueue getQueueFromRedis(String queueName) {
        return GsonUtils.GSON.fromJson(getPlugin().getApi().getRedisManager().get(getRedisKey(queueName)), MatchQueue.class);
    }

    public void sendQueueToRedis(MatchQueue queue) {
        getPlugin().getApi().getRedisManager().set(getRedisKey(queue.getName()), GsonUtils.GSON.toJson(queue));
    }

    public void removeQueueFromRedis(String queueName) {
        getPlugin().getApi().getRedisManager().del(getRedisKey(queueName));
    }

    public void updateQueueInMongo(MatchQueue newQueue) {
        updateQueueInMongo(newQueue.getName(), newQueue);
    }

    public void updateQueueInMongo(String queueName, MatchQueue newQueue) {
        getQueueCollection().updateOne(Filters.eq("_id", queueName), new Document("$set", newQueue), new UpdateOptions().upsert(true));
    }

}
