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