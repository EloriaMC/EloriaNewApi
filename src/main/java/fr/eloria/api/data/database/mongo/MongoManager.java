package fr.eloria.api.data.database.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import fr.eloria.api.data.database.AbstractDatabase;
import fr.eloria.api.data.database.DatabaseCredentials;
import lombok.Getter;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

@Getter
public class MongoManager extends AbstractDatabase {

    private MongoClientSettings mongoClientSettings;
    private MongoClient mongoClient;

    public MongoManager(DatabaseCredentials credentials) {
        super(credentials);
        this.connect();
    }

    @Override
    public void connect() {
        CodecRegistry pojoCodecProvider = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecProvider);

        this.mongoClientSettings = MongoClientSettings.builder().applyConnectionString(new ConnectionString(getCredentials().getUrl())).codecRegistry(codecRegistry).build();
        this.mongoClient = MongoClients.create(this.mongoClientSettings);
    }

    public MongoDatabase getDatabase() {
        return getMongoClient().getDatabase(getCredentials().getDatabaseName());
    }

    @Override
    public void disconnect() {
        getMongoClient().close();
    }

}
