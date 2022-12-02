package fr.eloria.api.plugin.bungee.common.redis.listener;

import fr.eloria.api.data.database.redis.RedisListener;
import fr.eloria.api.data.database.redis.packet.TestPacket;
import fr.eloria.api.utils.json.GsonUtils;

public class ChannelListener extends RedisListener {

    public ChannelListener() {
        super("channel");
    }

    @Override
    public void onMessage(String channel, String message) {
        System.out.println("[RedisManager] Packet : " + GsonUtils.GSON.fromJson(message, TestPacket.class));
    }

}
