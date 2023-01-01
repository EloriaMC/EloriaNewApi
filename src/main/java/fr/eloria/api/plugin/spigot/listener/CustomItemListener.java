package fr.eloria.api.plugin.spigot.listener;

import fr.eloria.api.plugin.spigot.SpigotPlugin;
import fr.eloria.api.utils.handler.AbstractListener;
import fr.eloria.api.utils.item.CustomItems;
import fr.eloria.api.utils.wrapper.BooleanWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemListener extends AbstractListener<SpigotPlugin> {

    public CustomItemListener(SpigotPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();

        if (itemStack != null && itemStack.hasItemMeta() && CustomItems.contains(itemStack.getItemMeta().getDisplayName())) {

            BooleanWrapper.of(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                    .ifTrue(() -> CustomItems.getItem(event.getItem().getItemMeta().getDisplayName()).getRightClick().accept(event))
                    .ifFalse(() -> CustomItems.getItem(event.getItem().getItemMeta().getDisplayName()).getLeftClick().accept(event));
        }
    }

}