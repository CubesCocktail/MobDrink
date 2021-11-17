package com.github.zamponimarco.mobdrink.manager;

import com.github.zamponimarco.mobdrink.mob.Mob;

import java.util.List;

public abstract class MobManager {

    public abstract List<Mob> getMobs();

    public abstract Mob getByName(String name);
}
