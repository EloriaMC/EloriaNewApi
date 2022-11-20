package fr.eloria.api.utils;

import lombok.Data;

@Data
public abstract class AbstractHandler {

    public abstract void load();

    public abstract void unload();

}
