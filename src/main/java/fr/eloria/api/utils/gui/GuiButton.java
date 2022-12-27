package fr.eloria.api.utils.gui;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Getter
@NoArgsConstructor
public class GuiButton {

    @Setter private ItemStack itemStack;
    @Setter private Consumer<InventoryClickEvent> clickEvent;

    public GuiButton(ItemStack itemStack, Consumer<InventoryClickEvent> clickEvent) {
        this.itemStack = itemStack;
        this.clickEvent = clickEvent;
    }

    public GuiButton(ItemStack itemStack) {
        this(itemStack, event -> event.setCancelled(true));
    }

}