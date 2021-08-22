package com.github.zamponimarco.mobdrink.skill;

import com.github.zamponimarco.cubescocktail.action.group.ActionGroup;
import com.github.zamponimarco.cubescocktail.annotation.PossibleSources;
import com.github.zamponimarco.cubescocktail.annotation.PossibleTargets;
import com.github.zamponimarco.cubescocktail.libs.annotation.Enumerable;
import com.github.zamponimarco.cubescocktail.libs.core.Libs;
import com.github.zamponimarco.cubescocktail.libs.model.ModelPath;
import com.github.zamponimarco.cubescocktail.libs.util.ItemUtils;
import com.github.zamponimarco.cubescocktail.libs.util.MessageUtils;
import com.github.zamponimarco.cubescocktail.source.CasterSource;
import com.github.zamponimarco.cubescocktail.source.Source;
import com.github.zamponimarco.cubescocktail.trgt.CasterTarget;
import com.github.zamponimarco.cubescocktail.trgt.Target;
import com.github.zamponimarco.mobdrink.mob.Mob;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Enumerable.Child
@PossibleTargets("getPossibleTargets")
@PossibleSources("getPossibleSources")
@Enumerable.Displayable(name = "&6&lSpawn Skill", description = "gui.mob.skill.spawn.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQ3ZTJlNWQ1NWI2ZDA0OTQzNTE5YmVkMjU1N2M2MzI5ZTMzYjYwYjkwOWRlZTg5MjNjZDg4YjExNTIxMCJ9fX0=")
public class SpawnSkill extends Skill {

    private static final String SPAWN_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQ3ZTJlNWQ1NWI2ZDA0OTQzNTE5YmVkMjU1N2M2MzI5ZTMzYjYwYjkwOWRlZTg5MjNjZDg4YjExNTIxMCJ9fX0";

    public SpawnSkill(ModelPath<Mob> path) {
        super(path);
    }

    public SpawnSkill(UUID mobId, UUID id, List<ActionGroup> groups) {
        super(mobId, id, groups);
    }

    public SpawnSkill(Map<String, Object> map) {
        super(map);
    }


    public Collection<Class<? extends Target>> getPossibleTargets() {
        return Sets.newHashSet(CasterTarget.class);
    }

    public Collection<Class<? extends Source>> getPossibleSources() {
        return Sets.newHashSet(CasterSource.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(
                Libs.getWrapper().skullFromValue(SPAWN_HEAD),
                MessageUtils.color("&6&lSpawn skill"),
                Lists.newArrayList());
    }

    @Override
    public Skill clone() {
        return new SpawnSkill(mobId, UUID.randomUUID(), groups.stream().map(ActionGroup::clone).collect(Collectors.toList()));
    }

    @Override
    public String getName() {
        return "Spawn Skill";
    }
}
