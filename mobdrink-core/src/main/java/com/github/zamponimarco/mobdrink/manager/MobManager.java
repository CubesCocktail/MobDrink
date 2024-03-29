package com.github.zamponimarco.mobdrink.manager;

import com.github.zamponimarco.cubescocktail.libs.model.ModelManager;
import com.github.zamponimarco.mobdrink.MobDrink;
import com.github.zamponimarco.mobdrink.mob.Mob;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Getter
public class MobManager extends ModelManager<Mob> {

    private final List<Mob> mobs;

    public MobManager(Class<Mob> classObject, String databaseType, JavaPlugin plugin) {
        super(classObject, databaseType, plugin, ImmutableMap.of("name", "mob",
                "fileSupplier", (Supplier<File>) () -> {
                    String fileName = "mob.yml";
                    File dataFile = new File(MobDrink.getInstance().getDataFolder(), fileName);
                    if (!dataFile.exists()) {
                        MobDrink.getInstance().saveResource(fileName);
                    }
                    return dataFile;
                }));
        this.mobs = fetchModels();
    }

    public Mob getByName(String name) {
        return mobs.stream().filter(mob -> mob.getName().equals(name)).findFirst().orElse(null);
    }

    public Mob getById(UUID id) {
        return mobs.stream().filter(mob -> mob.getId().equals(id)).findFirst().orElse(null);
    }
}