package com.github.zamponimarco.mobdrink.skill;

import com.github.zamponimarco.cubescocktail.action.group.ActionGroup;
import com.github.zamponimarco.cubescocktail.annotation.PossibleSources;
import com.github.zamponimarco.cubescocktail.annotation.PossibleTargets;
import com.github.zamponimarco.cubescocktail.libs.annotation.Enumerable;
import com.github.zamponimarco.cubescocktail.libs.annotation.Serializable;
import com.github.zamponimarco.cubescocktail.libs.core.Libs;
import com.github.zamponimarco.cubescocktail.libs.model.ModelPath;
import com.github.zamponimarco.cubescocktail.libs.util.ItemUtils;
import com.github.zamponimarco.cubescocktail.libs.util.MessageUtils;
import com.github.zamponimarco.cubescocktail.slot.Slot;
import com.github.zamponimarco.cubescocktail.source.CasterSource;
import com.github.zamponimarco.cubescocktail.source.RayTraceSource;
import com.github.zamponimarco.cubescocktail.source.Source;
import com.github.zamponimarco.cubescocktail.target.CasterTarget;
import com.github.zamponimarco.cubescocktail.target.ItemTarget;
import com.github.zamponimarco.cubescocktail.target.RayTraceTarget;
import com.github.zamponimarco.cubescocktail.target.Target;
import com.github.zamponimarco.cubescocktail.timer.Timerable;
import com.github.zamponimarco.mobdrink.mob.Mob;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@PossibleTargets("getPossibleTargets")
@PossibleSources("getPossibleSources")
@Enumerable.Child
@Enumerable.Displayable(name = "&6&lTimed Skill", description = "gui.mob.skill.timed.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=")
public class TimedSkill extends Skill implements Timerable {

    private static final String TIMER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0";

    @Serializable(headTexture = TIMER_HEAD, description = "gui.mob.skill.timed.timer")
    @Serializable.Number(minValue = 1, scale = 1)
    private int timer;

    public TimedSkill(ModelPath<Mob> path) {
        super(path);
        this.timer = 20;
    }

    public TimedSkill(UUID itemId, UUID id, List<ActionGroup> groups, int timer) {
        super(itemId, id, groups);
        this.timer = timer;
    }

    public TimedSkill(Map<String, Object> map) {
        super(map);
        this.timer = (int) map.getOrDefault("timer", 20);
    }

    public Collection<Class<? extends Target>> getPossibleTargets() {
        return Sets.newHashSet(CasterTarget.class, RayTraceTarget.class);
    }

    public Collection<Class<? extends Source>> getPossibleSources() {
        return Sets.newHashSet(CasterSource.class, RayTraceSource.class);
    }

    @Override
    public Skill clone() {
        return new TimedSkill(mobId, id, groups.stream().map(ActionGroup::clone).collect(Collectors.toList()), timer);
    }

    @Override
    public String getName() {
        return "Timed: &6&l" + timer;
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(
                Libs.getWrapper().skullFromValue(TIMER_HEAD),
                MessageUtils.color("&6&lTimer skill: &c" + timer),
                Lists.newArrayList());
    }

    @Override
    public BukkitRunnable getTask(LivingEntity livingEntity) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                Map<String, Object> args = new HashMap<>();
                args.put("caster", livingEntity);
                executeActions(args);
            }
        };
    }
}
