package com.github.zamponimarco.mobdrink.command;

import com.github.zamponimarco.cubescocktail.libs.command.AbstractCommand;
import com.github.zamponimarco.cubescocktail.libs.core.Libs;
import com.github.zamponimarco.cubescocktail.libs.util.MessageUtils;
import com.github.zamponimarco.mobdrink.MobDrink;
import com.github.zamponimarco.mobdrink.mob.Mob;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MobSpawnCommand extends AbstractCommand {

    @Override
    protected void execute(String[] strings, CommandSender commandSender) {
        Player player = (Player) commandSender;
        if (strings.length < 1) {
            player.sendMessage(Libs.getLocale().get("messages.command.mob-incorrect-usage"));
            return;
        }

        Mob mob = MobDrink.getInstance().getMobManager().getByName(strings[0]);
        if (mob == null) {
            player.sendMessage(Libs.getLocale().get("messages.command.mob-not-found"));
            return;
        }

        int level = 1;

        if (strings.length > 1 && strings[1].matches("\\d+")) {
            level = Integer.parseInt(strings[1]);
        }

        mob.spawn(player.getLocation(), level);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], MobDrink.getInstance().getMobManager().getMobs().stream().
                    map(Mob::getName).collect(Collectors.toList()), completions);
        }

        Collections.sort(completions);
        return completions;
    }

    @Override
    protected boolean isOnlyPlayer() {
        return true;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("suprememob.mob.spawn");
    }
}
