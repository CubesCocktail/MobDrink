package com.github.zamponimarco.mobdrink;

import com.github.zamponimarco.cubescocktail.addon.Addon;
import com.github.zamponimarco.mobdrink.manager.MobManager;

public abstract class MobDrink extends Addon {

    public static MobDrink getInstance() {
        return null;
    }

    public abstract MobManager getMobManager();

}