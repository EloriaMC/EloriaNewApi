package fr.eloria.api.utils.item;

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class CustomItems {

    private final Map<String, ItemBuilder> customItems = Maps.newConcurrentMap();

    public Map<String, ItemBuilder> getCustomItems() {
        return customItems;
    }

    public ItemBuilder getItem(String itemName) {
        return getCustomItems().get(itemName);
    }

    public void addItem(ItemBuilder itemBuilder) {
        getCustomItems().putIfAbsent(itemBuilder.getItemStack().getItemMeta().getDisplayName(), itemBuilder);
    }

    public boolean contains(String itemName) {
        return getCustomItems().containsKey(itemName);
    }

}
