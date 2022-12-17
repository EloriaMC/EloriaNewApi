package fr.eloria.api.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.DedicatedServer;
import net.minecraft.server.v1_8_R3.MinecraftServer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

@UtilityClass
public class ServerProperties {

    public void savePropertiesFile() {
        ((DedicatedServer) MinecraftServer.getServer()).propertyManager.savePropertiesFile();
    }

    public void setServerProperty(final ServerProperty property, final Object value) {
        try {
            Properties properties = new Properties();
            try {
                FileInputStream inputStream = new FileInputStream("server.properties");
                FileOutputStream outputStream = new FileOutputStream("server.properties");

                properties.load(inputStream);
                properties.setProperty(property.getPropertyName(), value.toString());
                properties.store(outputStream, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        }
    }

    @Getter
    @AllArgsConstructor
    public enum ServerProperty {

        SPAWN_PROTECTION("spawn-protection"),
        SERVER_NAME("server-name"),
        FORCE_GAMEMODE("force-gamemode"),
        NETHER("allow-nether"),
        DEFAULT_GAMEMODE("gamemode"),
        QUERY("enable-query"),
        PLAYER_IDLE_TIMEOUT("player-idle-timeout"),
        DIFFICULTY("difficulty"),
        SPAWN_MONSTERS("spawn-monsters"),
        OP_PERMISSION_LEVEL("op-permission-level"),
        RESOURCE_PACK_HASH("resource-pack-hash"),
        RESOURCE_PACK("resource-pack"),
        ANNOUNCE_PLAYER_ACHIEVEMENTS("announce-player-achievements"),
        PVP("pvp"),
        SNOOPER("snooper-enabled"),
        LEVEL_NAME("level-name"),
        LEVEL_TYPE("level-type"),
        LEVEL_SEED("level-seed"),
        HARDCORE("hardcore"),
        COMMAND_BLOCKS("enable-command-blocks"),
        MAX_PLAYERS("max-players"),
        PACKET_COMPRESSION_LIMIT("network-compression-threshold"),
        MAX_WORLD_SIZE("max-world-size"),
        IP("server-ip"),
        PORT("server-port"),
        DEBUG_MODE("debug"),
        SPAWN_NPCS("spawn-npcs"),
        SPAWN_ANIMALS("spawn-animals"),
        FLIGHT("allow-flight"),
        VIEW_DISTANCE("view-distance"),
        WHITE_LIST("white-list"),
        GENERATE_STRUCTURES("generate-structures"),
        MAX_BUILD_HEIGHT("max-build-height"),
        MOTD("motd"),
        REMOTE_CONTROL("enable-rcon");

        private final String propertyName;

    }

}
