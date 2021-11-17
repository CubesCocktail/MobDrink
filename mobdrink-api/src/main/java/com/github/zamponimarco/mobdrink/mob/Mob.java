package com.github.zamponimarco.mobdrink.mob;

import com.github.zamponimarco.cubescocktail.action.source.ActionSource;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public abstract class Mob {

    public abstract Entity spawn(Location l, int level, ActionSource source);

    public abstract Entity spawn(Location l, int level);

    public abstract ItemStack getGUIItem();

    public abstract String getName();

    public abstract EntityType getType();

    public static Mob fromEntity(Entity e) {
        return null;
    }

    public static boolean isMob(Entity e) {
        return false;
    }

    public static int getLevel(Entity e) {
        return 0;
    }

}
