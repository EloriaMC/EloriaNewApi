package fr.eloria.api;

import fr.eloria.api.data.database.DatabaseCredentials;
import fr.eloria.api.data.database.mongo.MongoManager;
import fr.eloria.api.data.database.redis.RedisManager;
import fr.eloria.api.data.rank.RankManager;
import fr.eloria.api.data.user.UserManager;
import fr.eloria.api.utils.AbstractHandler;
import lombok.Getter;

@Getter
public class Api extends AbstractHandler {

    private static Api instance;

    private final MongoManager mongoManager;
    private final RedisManager redisManager;

    private final RankManager rankManager;
    private final UserManager userManager;

    public Api() {
        instance = this;
        this.mongoManager = new MongoManager(new DatabaseCredentials().setUrl("mongodb://localhost:27017").setDatabaseName("eloria"));
        this.redisManager = new RedisManager(new DatabaseCredentials());

        this.rankManager = new RankManager();
        this.userManager = new UserManager();

        this.load();
    }

    @Override
    public void load() {
        getRankManager().loadRanks();
    }

    public static Api getInstance() {
        return instance;
    }

    @Override
    public void unload() {
        getRankManager().saveRanks();

        getRedisManager().disconnect();
        getMongoManager().disconnect();
    }

}