# EloriaNewApi


```java
// Build command
./gradlew clean shadowJar
```

Api usage :

```java
// Get the API instance
Api.getInstance();
// Get the Spigot API instance
MainClass.getPlugin(SpigotPlugin.class).getLoader();
```

User usage :

```java
@EventHandler 
public void onEvent(AnotherEvent event) {
        User user = Api.getInstance().getUserManager().getUser(player.getUniqueId());
        
        // If the rank is null it return the default rank
        user.setRank(Api.getInstance().getRankManager().getRank("Admin"));
        
        user.getRank();
        user.getUuid();
        user.getCoins();
}
```

Redis usage :

```java
MainClass.getPlugin(SpigotPlugin.class)
        .getLoader()
        .getRedisMessenger()
        .sendMessage("someRedisChannel", new ExamplePacket("Hello world!"));

MainClass.getPlugin(SpigotPlugin.class)
        .getLoader()
        .getRedisMessenger()
        .addListener(new ExampleRedisListener(plugin));
```

ExamplePacket packet
```java
@Getter
@AllArgsConstructor
public class ExamplePacket {

    private final String text;
    
}
```

ExampleRedisListener class
```java
@AllArgsConstructor
public class ExampleRedisListener implements RedisListener {

    private final MainClass plugin;

    @Override
    public String getName() {
        return "someRedisChannel";
    }

    @Override
    public BiConsumer<String, String> onMessage() {
        return (channel, message) -> {
            ExamplePacket packet = GsonUtils.GSON.fromJson(message, ExamplePacket.class);
            System.out.println(packet.getText());
        };
    }

}
```

Server Json :

```json
{
    "name": "lobby-0",
    "onlinePlayers": 1,
    "status": "OPEN",
    "type": {
        "name": "lobby",
        "maxPlayers": 200
    }
}
```

Rank Json :

```json
{
  "name": "Joueur",
  "prefix": "&7Joueur",
  "power": 0,
  "defaultRank": "Joueur",
  "permissions": []
}
```