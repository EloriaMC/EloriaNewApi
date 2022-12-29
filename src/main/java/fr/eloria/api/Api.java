package fr.eloria.api;

import fr.eloria.api.data.database.DatabaseCredentials;
import fr.eloria.api.data.database.mongo.MongoManager;
import fr.eloria.api.data.database.redis.RedisManager;
import fr.eloria.api.data.rank.RankManager;
import fr.eloria.api.data.user.UserManager;
import fr.eloria.api.utils.handler.AbstractHandler;
import lombok.Getter;

@Getter
public class Api extends AbstractHandler {

    private static Api instance;

    private final MongoManager mongoManager;
    private final RedisManager redisManager;

    private final RankManager rankManager;
    private final UserManager userManager;

    private final boolean isBungee;

    public Api(boolean isBungee) {
        instance = this;

        this.isBungee = isBungee;

        this.mongoManager = new MongoManager(DatabaseCredentials.builder().url("mongodb://localhost:27017").databaseName("eloria").build());
        this.redisManager = new RedisManager(DatabaseCredentials.builder().host("localhost").port(6379).build());
        
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