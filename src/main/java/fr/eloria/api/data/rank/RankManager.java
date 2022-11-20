package fr.eloria.api.data.rank;

import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import fr.eloria.api.Api;
import lombok.Getter;
import org.bson.Document;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Getter
public class RankManager {

    private final MongoCollection<Rank> rankCollection;
    private final ConcurrentMap<String, Rank> ranks;

    public RankManager() {
        this.rankCollection = Api.getInstance().getMongoManager().getDatabase().getCollection("ranks", Rank.class);
        this.ranks = Maps.newConcurrentMap();
    }

    public void loadRanks() {
        getRankCollection().find().iterator().forEachRemaining(this::addRank);
    }

    public void saveRanks() {
        getRanks().values().forEach(this::updateRankInMongo);
        getRanks().values().stream().map(Rank::getName).forEach(this::removeRank);
    }

    public void addRank(Rank rank) {
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
        getRankCollection().updateOne(Filters.eq("_id", rankName), new Document("$set", newRank), new UpdateOptions().upsert(true));
    }

    public Rank getRankFromMongo(String rankName) {
        return getRankCollection().find(Filters.eq("_id", rankName)).first();
    }

    public void updateRankInMongo(Rank newRank) {
        updateRankInMongo(newRank.getName(), newRank);
    }

    public void updateRankInMongo(String rankName) {
        updateRankInMongo(rankName, getRanks().get(rankName));
    }

    public void sendRankToMongo(Rank rank) {
        getRankCollection().insertOne(rank);
    }

    public void removeRankFromMongo(String rankName) {
        getRankCollection().deleteOne(new Document("_id", rankName));
    }

    public List<Rank> getRanksOrdainedByPower() {
        return getRanks()
                .values()
                .stream()
                .sorted(Comparator.comparingInt(Rank::getPower))
                .collect(Collectors.toList());
    }

}
