package fr.eloria.api.utils.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public class ValidateGui extends Gui {

    private final ItemStack borderItem, informationItem;
    private final Gui previousGui;
    private ValidateAction validateAction;

    public ValidateGui(JavaPlugin plugin, String inventoryName, ItemStack borderItem, ItemStack informationItem, Gui previousGui, ValidateAction validateAction) {
        super(plugin, inventoryName, 3);
        this.borderItem = borderItem;
        this.informationItem = informationItem;
        this.previousGui = previousGui;
        this.validateAction = validateAction;
    }

    @Override
    public void setup() {
        setItems(getBorders(), getBorderItem());

        //setItem(11, confirmButton());

        setItem(13, new GuiButton(getInformationItem()));

        //setItem(15, denyButton());
    }


   /* public GuiButton confirmButton() {
        return new GuiButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setWoolColor(DyeColor.GREEN).setDisplayName("&7» &aConfirmer").build(), event -> getValidateAction().validateAction());
    }

    public GuiButton denyButton() {
        return new GuiButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setWoolColor(DyeColor.RED).setDisplayName("&7» &cAnnuler").build(),
                event -> getValidateAction().denyAction(getPreviousGui(), (Player) event.getWhoClicked()));
    }*/

}
