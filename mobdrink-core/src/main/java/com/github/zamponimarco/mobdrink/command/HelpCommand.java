package com.github.zamponimarco.mobdrink.command;

import com.github.zamponimarco.cubescocktail.libs.command.AbstractCommand;
import com.github.zamponimarco.cubescocktail.libs.util.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class HelpCommand extends AbstractCommand {

    @Override
    protected void execute(String[] strings, CommandSender commandSender) {
        commandSender.sendMessage(MessageUtils.color("        &cSupreme&6Mob &cHelp\n" +
                "&2/sm help &7Show help message.\n" +
                "&2/sm mobs &7Show the mobs GUI.\n" +
                "&2/sm spawn [name] <level> &7Spawn the mob in your location.\n" +
                "&2For further help: &7https://discord.gg/TzREkc9"));
    }

    @Override
    protected boolean isOnlyPlayer() {
        return false;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("suprememob.admin.help");
    }
}
