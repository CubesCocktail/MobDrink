package com.github.zamponimarco.mobdrink.mob;

import com.github.zamponimarco.cubescocktail.CubesCocktail;
import com.github.zamponimarco.cubescocktail.action.args.ActionArgument;
import com.github.zamponimarco.cubescocktail.action.args.ActionArgumentKey;
import com.github.zamponimarco.cubescocktail.action.source.ActionSource;
import com.github.zamponimarco.cubescocktail.action.source.EntitySource;
import com.github.zamponimarco.cubescocktail.action.targeter.EntityTarget;
import com.github.zamponimarco.cubescocktail.database.NamedModel;
import com.github.zamponimarco.cubescocktail.libs.annotation.Serializable;
import com.github.zamponimarco.cubescocktail.libs.core.Libs;
import com.github.zamponimarco.cubescocktail.libs.model.ModelPath;
import com.github.zamponimarco.cubescocktail.libs.util.ItemUtils;
import com.github.zamponimarco.cubescocktail.libs.util.MapperUtils;
import com.github.zamponimarco.cubescocktail.libs.util.MessageUtils;
import com.github.zamponimarco.cubescocktail.timer.Timerable;
import com.github.zamponimarco.cubescocktail.trigger.EntitySpawnTrigger;
import com.github.zamponimarco.cubescocktail.trigger.Trigger;
import com.github.zamponimarco.mobdrink.MobDrink;
import com.github.zamponimarco.mobdrink.mob.options.GeneralOptions;
import com.github.zamponimarco.mobdrink.skill.Skill;
import com.github.zamponimarco.mobdrink.skill.SpawnSkill;
import com.github.zamponimarco.mobdrink.skill.TimedSkill;
import com.github.zamponimarco.mobdrink.skill.TriggeredSkill;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class Mob extends NamedModel {

    private static final String ACTUATORS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmJiMTI1NmViOWY2NjdjMDVmYjIxZTAyN2FhMWQ1MzU1OGJkYTc0ZTI0MGU0ZmE5ZTEzN2Q4NTFjNDE2ZmU5OCJ9fX0=";
    private static final String OPTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWMyZmYyNDRkZmM5ZGQzYTJjZWY2MzExMmU3NTAyZGM2MzY3YjBkMDIxMzI5NTAzNDdiMmI0NzlhNzIzNjZkZCJ9fX0=";

    private static int counter = 1;

    @Serializable(stringValue = true)
    private UUID id;
    @Serializable(displayItem = "typeHead", stringValue = true, description = "gui.mob.type", fromList = "getSpawnableEntities", fromListMapper = "spawnableEntitiesMapper")
    private EntityType type;
    @Serializable(headTexture = ACTUATORS_HEAD, description = "gui.mob.actuators")
    private List<Skill> skills;
    @Serializable(headTexture = OPTIONS_HEAD, description = "gui.mob.general-options")
    private GeneralOptions generalOptions;

    public Mob() {
        super(nextAvailableName());
        this.id = UUID.randomUUID();
        this.type = EntityType.ZOMBIE;
        this.skills = Lists.newArrayList();
        this.generalOptions = new GeneralOptions();
    }

    public Mob(String name, UUID id, EntityType type, List<Skill> skills, GeneralOptions generalOptions) {
        super(name);
        this.id = id;
        this.type = type;
        this.skills = skills;
        this.generalOptions = generalOptions;
        counter++;
    }

    public Mob(Map<String, Object> map) {
        super(map);
        //this.id = UUID.fromString((String) map.get("id"));
        this.id = UUID.fromString((String) map.getOrDefault("id", UUID.randomUUID().toString()));
        this.type = EntityType.valueOf((String) map.get("type"));
        this.skills = (List<Skill>) map.getOrDefault("skills", Lists.newArrayList());
        this.generalOptions = (GeneralOptions) map.getOrDefault("generalOptions", new GeneralOptions());
        legacyTransition(map);
        counter++;
    }

    private static String nextAvailableName() {
        String name;
        do {
            name = "mob" + counter;
            counter++;
        } while (MobDrink.getInstance().getMobManager().getByName(name) != null);
        return name;
    }

    public static List<Object> getSpawnableEntities(ModelPath path) {
        return Arrays.stream(EntityType.values()).filter(type -> type.isSpawnable() &&
                org.bukkit.entity.Mob.class.isAssignableFrom(type.getEntityClass())).
                collect(Collectors.toList());
    }

    public static Function<Object, ItemStack> spawnableEntitiesMapper() {
        return MapperUtils.getEntityTypeMapper();
    }

    public static Mob fromEntity(Entity e) {
        if (e == null) {
            return null;
        }

        String name = e.getPersistentDataContainer().getOrDefault(new NamespacedKey(CubesCocktail.getInstance(), "mob"),
                PersistentDataType.STRING, "");

        if (name.equals("")) {
            return null;
        }

        return MobDrink.getInstance().getMobManager().getByName(name);
    }

    public static boolean isMob(Entity e) {
        return fromEntity(e) != null;
    }

    public static int getLevel(Entity e) {
        if (!isMob(e)) {
            return 0;
        }

        return e.getPersistentDataContainer().getOrDefault(new NamespacedKey(CubesCocktail.getInstance(), "level"),
                PersistentDataType.INTEGER, 1);
    }

    @Deprecated
    private void legacyTransition(Map<String, Object> map) {
        List<Trigger> oldSkillSet = (List<Trigger>) map.get("actuators");
        if (oldSkillSet != null && !oldSkillSet.isEmpty()) {
            oldSkillSet.forEach(trigger -> {
                Skill skill;
                if (trigger instanceof EntitySpawnTrigger) {
                    skill = new SpawnSkill(id, UUID.randomUUID(), trigger.getGroups());
                } else {
                    skill = new TriggeredSkill(id, UUID.randomUUID(), trigger.getGroups(), trigger, trigger.getCooldown());
                }
                this.skills.add(skill);
            });
        }
    }

    @Override
    protected boolean isAlreadyPresent(String s) {
        return MobDrink.getInstance().getMobManager().getByName(s) != null;
    }

    @SneakyThrows
    public Entity spawn(Location l, int level) {
        return spawn(l, level, null);
    }

    public Entity spawn(Location l, int level, ActionSource source) {
        org.bukkit.entity.Mob e = (org.bukkit.entity.Mob) l.getWorld().spawnEntity(l, type);
        e.getPersistentDataContainer().set(new NamespacedKey(CubesCocktail.getInstance(), "mob"),
                PersistentDataType.STRING, name);
        e.getPersistentDataContainer().set(new NamespacedKey(CubesCocktail.getInstance(), "level"),
                PersistentDataType.INTEGER, level);
        e.setMetadata("drops", new FixedMetadataValue(CubesCocktail.getInstance(), generalOptions.getDropTable()));

        if (source == null) {
            source = new EntitySource(e, new ItemStack(Material.CARROT));
        }
        EntityTarget target = new EntityTarget(e);
        generalOptions.buildOptions(e, source, target);

        ActionArgument args = new ActionArgument();
        args.setArgument(ActionArgumentKey.CASTER, source.getCaster());
        args.setArgument(ActionArgumentKey.SPAWNED, e);
        skills.stream().filter(skill -> skill instanceof SpawnSkill).forEach(skill -> skill.executeActions(args));
        skills.stream().filter(skill -> skill instanceof TimedSkill).forEach(skill -> CubesCocktail.getInstance().
                getTimerManager().addNewTimers(e, (TimedSkill) skill));
        return e;
    }

    public ItemStack typeHead() {
        return MapperUtils.getEntityTypeMapper().apply(type);
    }

    public Skill getSkillById(UUID id) {
        return skills.stream().filter(skill -> skill.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(typeHead(), MessageUtils.color("&6&lName: &c" + name), Libs.getLocale().
                getList("gui.additional-tooltips.delete"));
    }

}
