package com.github.zamponimarco.mobdrink;

import com.github.zamponimarco.cubescocktail.CubesCocktail;
import com.github.zamponimarco.cubescocktail.action.source.ActionSource;
import com.github.zamponimarco.cubescocktail.addon.Addon;
import com.github.zamponimarco.cubescocktail.addon.AddonMessage;
import com.github.zamponimarco.cubescocktail.libs.command.PluginCommandExecutor;
import com.github.zamponimarco.cubescocktail.libs.core.Libs;
import com.github.zamponimarco.mobdrink.command.HelpCommand;
import com.github.zamponimarco.mobdrink.command.MobListCommand;
import com.github.zamponimarco.mobdrink.command.MobSpawnCommand;
import com.github.zamponimarco.mobdrink.command.SpawnerListCommand;
import com.github.zamponimarco.mobdrink.listener.TimerListener;
import com.github.zamponimarco.mobdrink.manager.MobManager;
import com.github.zamponimarco.mobdrink.manager.SpawnerManager;
import com.github.zamponimarco.mobdrink.mob.Mob;
import com.github.zamponimarco.mobdrink.mob.options.AttributeOptions;
import com.github.zamponimarco.mobdrink.mob.options.BehaviorOptions;
import com.github.zamponimarco.mobdrink.mob.options.EquipmentOptions;
import com.github.zamponimarco.mobdrink.mob.options.GeneralOptions;
import com.github.zamponimarco.mobdrink.skill.*;
import com.github.zamponimarco.mobdrink.spawner.Spawner;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MobDrink extends Addon {

    private static MobDrink instance;

    static {
        ConfigurationSerialization.registerClass(Mob.class);

        ConfigurationSerialization.registerClass(GeneralOptions.class);
        ConfigurationSerialization.registerClass(AttributeOptions.class);
        ConfigurationSerialization.registerClass(BehaviorOptions.class);
        ConfigurationSerialization.registerClass(EquipmentOptions.class);
        ConfigurationSerialization.registerClass(EquipmentOptions.Equipment.class);

        ConfigurationSerialization.registerClass(Skill.class);
        ConfigurationSerialization.registerClass(SpawnSkill.class);
        ConfigurationSerialization.registerClass(TriggeredSkill.class);
        ConfigurationSerialization.registerClass(TimedSkill.class);
        ConfigurationSerialization.registerClass(SkillKey.class);

        ConfigurationSerialization.registerClass(Spawner.class);
    }

    private MobManager mobManager;
    private SpawnerManager spawnerManager;

    public static MobDrink getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        setUpLocale();
        setUpData();
        setUpListener();
        setUpCommands();
    }

    @Override
    public void renameFunction(String s, String s1) {

    }

    @Override
    public void onDisable() {
        spawnerManager.getSpawners().forEach(Spawner::stopSpawnTask);
    }

    private void setUpLocale() {
        File folder = new File(getDataFolder(), "locale");

        if (!folder.exists()) {
            folder.mkdir();
        }
        saveResource("locale" + File.separatorChar + "en-US.yml");

        File dataFile = new File(folder, "en-US.yml");

        Libs.getLocale().registerLocaleFiles(dataFile);
    }

    private void setUpData() {
        this.mobManager = new MobManager(Mob.class, "comp_yaml", CubesCocktail.getInstance());
        this.spawnerManager = new SpawnerManager(Spawner.class, "comp_yaml", CubesCocktail.getInstance());
    }

    private void setUpListener() {
        CubesCocktail.getInstance().getServer().getPluginManager().registerEvents(spawnerManager,
                CubesCocktail.getInstance());
        CubesCocktail.getInstance().getServer().getPluginManager().registerEvents(new TimerListener(),
                CubesCocktail.getInstance());
    }

    private void setUpCommands() {
        PluginCommandExecutor ex = new PluginCommandExecutor("help", new HelpCommand());
        ex.registerCommand("list", new MobListCommand());
        ex.registerCommand("spawners", new SpawnerListCommand());
        ex.registerCommand("spawn", new MobSpawnCommand());
        CubesCocktail.getInstance().getCommandExecutor().registerCommand("mob", ex);
    }

    @Override
    public <T> T sendMessageToAddon(AddonMessage<T> message) {
        String command = message.getCommand();
        List<Object> args = message.getArguments();
        Mob mob;
        switch (command) {
            case "getMobs":
                return (T) mobManager.getMobs().stream().map(m -> m.getName()).collect(Collectors.toList());
            case "spawn":
                mob = mobManager.getByName((String) args.get(1));
                if (mob == null) return null;
                return (T) mob.spawn((Location) args.get(0), (Integer) args.get(2), (ActionSource) args.get(3));
            case "getType":
                mob = mobManager.getByName((String) args.get(0));
                if (mob == null) return null;
                return (T) mob.getType();
            case "getGUIItem":
                mob = mobManager.getByName((String) args.get(0));
                if (mob == null) return null;
                return (T) mob.getGUIItem();
            case "getLevel":
                return (T) (Integer) Mob.getLevel((Entity) args.get(0));
            default:
                return null;
        }
    }
}
