package fr.eloria.api.data.rank;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import fr.eloria.api.Api;
import fr.eloria.api.utils.json.GsonUtils;
import lombok.Getter;
import org.bson.Document;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Getter
public class RankManager {

    private final MongoCollection<Document> rankCollection;
    private final ConcurrentMap<String, Rank> ranks;

    public RankManager() {
        this.rankCollection = Api.getInstance().getMongoManager().getDatabase().getCollection("ranks");
        this.ranks = Maps.newConcurrentMap();
    }

    private String getRedisKey(String rankName) {
        return "rank:" + rankName;
    }

    public void loadRanks() {
        if (Api.getInstance().isBungee())
            getRankCollection().find().iterator().forEachRemaining(document -> addRank(GsonUtils.GSON.fromJson(document.toJson(), Rank.class)));
        else
            getRanksFromRedis().forEach(this::addRank);
    }

    public void saveRanks() {
        if (Api.getInstance().isBungee()) {
            getRanks().values().forEach(rank -> {
                updateRankInMongo(rank);
                removeRankFromRedis(rank.getName());
            });
        }

        getRanks().values().stream().map(Rank::getName).forEach(this::removeRank);
    }

    private void addRank(Rank rank) {
        if (Api.getInstance().isBungee()) sendRankToRedis(rank);
        getRanks().putIfAbsent(rank.getName(), rank);
        System.out.println("[RankManager] Added " + rank.getName() + " rank");
    }

    public Rank getRank(String rankName) {
        return getRanks().values().stream().filter(rank -> rank.getName().equals(rankName)).findFirst().orElse(getDefaultRank());
    }

    public Rank getDefaultRank() {
        return getRanks().values().stream().filter(Rank::isDefaultRank).findFirst().orElse(null);
    }

    public Rank getRankWithMorePower() {
        return getRanks().values().stream().max(Comparator.comparingInt(Rank::getPower)).orElse(null);
    }

    public Rank getRankWithLessPower() {
        return getRanks().values().stream().min(Comparator.comparingInt(Rank::getPower)).orElse(null);
    }

    public void removeRank(String rankName) {
        getRanks().remove(rankName);
    }

    public boolean exist(String rankName) {
        return getRanks().containsKey(rankName);
    }

    public void updateRankInMongo(String rankName, Rank newRank) {
        getRankCollection().updateOne(Filters.eq("name", rankName), new Document("$set", newRank.toDocument()), new UpdateOptions().upsert(true));
    }

    public Rank getRankFromMongo(String rankName) {
        return GsonUtils.GSON.fromJson(Objects.requireNonNull(getRankCollection().find(Filters.eq("name", rankName)).first()).toJson(), Rank.class);
    }

    public void updateRankInMongo(Rank newRank) {
        updateRankInMongo(newRank.getName(), newRank);
    }

    public void updateRankInMongo(String rankName) {
        updateRankInMongo(rankName, getRanks().get(rankName));
    }

    public void sendRankToMongo(Rank rank) {
        getRankCollection().insertOne(rank.toDocument());
    }

    public void removeRankFromMongo(String rankName) {
        getRankCollection().deleteOne(new Document("name", rankName));
    }

    public Rank getRankFromRedis(String rankName) {
        return Api.getInstance().getRedisManager().get(getRedisKey(rankName), Rank.class);
    }

    public void sendRankToRedis(Rank rank) {
        Api.getInstance().getRedisManager().set(getRedisKey(rank.getName()), rank);
    }

    public void removeRankFromRedis(String rankName) {
        Api.getInstance().getRedisManager().del(getRedisKey(rankName));
    }

    public List<Rank> getRanksFromRedis() {
        List<Rank> ranks = Lists.newLinkedList();
        Api.getInstance().getRedisManager().keys(getRedisKey("*")).forEach(key -> ranks.add(Api.getInstance().getRedisManager().get(key, Rank.class)));
        return ranks;
    }

    public List<Rank> getRanksOrdainedByPower() {
        return getRanks().values().stream().sorted(Comparator.comparingInt(Rank::getPower).reversed()).collect(Collectors.toList());
    }

}
