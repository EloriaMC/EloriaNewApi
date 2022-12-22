package fr.eloria.api.utils.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();

        if (itemStack != null && CustomItems.contains(itemStack.getItemMeta().getDisplayName())) {

            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                CustomItems.getItem(event.getItem().getItemMeta().getDisplayName()).getRightClick().accept(event);
            else if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))
                CustomItems.getItem(event.getItem().getItemMeta().getDisplayName()).getLeftClick().accept(event);
        }
    }

}