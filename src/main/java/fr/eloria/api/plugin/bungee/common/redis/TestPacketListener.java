package fr.eloria.api.plugin.bungee.common.redis;

import fr.eloria.api.data.database.redis.channel.IRedisChannel;
import fr.eloria.api.data.database.redis.channel.IRedisChannelListener;
import fr.eloria.api.data.database.redis.packet.TestPacket;
import fr.eloria.api.plugin.bungee.BungeePlugin;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestPacketListener implements IRedisChannelListener<TestPacket> {

    private final BungeePlugin plugin;

    @Override
    public void listen(IRedisChannel<TestPacket> channel, TestPacket object) {
        System.out.println("[RedisManager] listen channel : " + channel.getName() + " | message : " + object.getText());
    }

}
