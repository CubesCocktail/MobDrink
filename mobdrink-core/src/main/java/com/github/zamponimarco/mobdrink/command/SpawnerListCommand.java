package com.github.zamponimarco.mobdrink.command;

import com.github.zamponimarco.cubescocktail.CubesCocktail;
import com.github.zamponimarco.cubescocktail.libs.command.AbstractCommand;
import com.github.zamponimarco.cubescocktail.libs.gui.model.ModelCollectionInventoryHolder;
import com.github.zamponimarco.mobdrink.MobDrink;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class SpawnerListCommand extends AbstractCommand {

    @SneakyThrows
    @Override
    protected void execute(String[] strings, CommandSender commandSender) {
        Player player = (Player) commandSender;
        player.openInventory(new ModelCollectionInventoryHolder<>(CubesCocktail.getInstance(), MobDrink.getInstance().
                getSpawnerManager(), "spawners").getInventory());
    }

    @Override
    protected boolean isOnlyPlayer() {
        return true;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("suprememob.spawner.list");
    }
}
