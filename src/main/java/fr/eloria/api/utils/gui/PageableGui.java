package fr.eloria.api.utils.gui;

import fr.eloria.api.utils.Pagination;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public abstract class PageableGui<E> extends Gui {

    private final int maxItems;

    private final Pagination<E> pagination;
    private Pagination<E>.Page page;

    public PageableGui(JavaPlugin plugin, String inventoryName, int rows, int maxItems) {
        super(plugin, inventoryName, rows);
        this.maxItems = maxItems;
        this.pagination = new Pagination<>(getMaxItems());
        this.page = pagination.getPage(1);
    }

    public abstract GuiButton nextPageButton();

    public abstract GuiButton previousPageButton();

    public void updatePage(Pagination<E>.Page page) {
        setPage(page);
        refresh();
    }

}