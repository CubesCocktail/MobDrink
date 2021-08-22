package com.github.zamponimarco.mobdrink.listener;

import com.github.zamponimarco.cubescocktail.CubesCocktail;
import com.github.zamponimarco.mobdrink.mob.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class TimerListener implements Listener {

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        Mob mob = Mob.fromEntity(e.getEntity());

        if (mob == null) {
            return;
        }

        CubesCocktail.getInstance().getTimerManager().removeAllTimers(e.getEntity());
    }

}
