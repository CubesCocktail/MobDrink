package com.github.zamponimarco.mobdrink.manager;

import com.github.zamponimarco.cubescocktail.libs.model.ModelManager;
import com.github.zamponimarco.mobdrink.MobDrink;
import com.github.zamponimarco.mobdrink.spawner.Spawner;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;

@Getter
public class SpawnerManager extends ModelManager<Spawner> implements Listener {

    private final List<Spawner> spawners;

    public SpawnerManager(Class<Spawner> classObject, String databaseType, JavaPlugin plugin) {
        super(classObject, databaseType, plugin, ImmutableMap.of("name", "spawner",
                "fileSupplier", (Supplier<File>) () -> {
                    String fileName = "spawner.yml";
                    File dataFile = new File(MobDrink.getInstance().getDataFolder(), fileName);
                    if (!dataFile.exists()) {
                        MobDrink.getInstance().saveResource(fileName);
                    }
                    return dataFile;
                }));
        this.spawners = fetchModels();
        this.spawners.forEach(Spawner::startSpawnTask);
    }

    public Spawner getByName(String name) {
        return spawners.stream().filter(spawner -> spawner.getName().equals(name)).findFirst().orElse(null);
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        spawners.stream().filter(spawner -> spawner.getLocation().getWrapped().getChunk().equals(e.getChunk())).
                forEach(Spawner::stopSpawnTask);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        spawners.stream().filter(spawner -> spawner.getLocation().getWrapped().getChunk().equals(e.getChunk())).
                forEach(Spawner::startSpawnTask);
    }

}
