package com.github.zamponimarco.mobdrink.skill;

import com.github.zamponimarco.cubescocktail.CubesCocktail;
import com.github.zamponimarco.cubescocktail.action.group.ActionGroup;
import com.github.zamponimarco.cubescocktail.annotation.PossibleSources;
import com.github.zamponimarco.cubescocktail.annotation.PossibleTargets;
import com.github.zamponimarco.cubescocktail.cooldown.CooldownOptions;
import com.github.zamponimarco.cubescocktail.cooldown.Cooldownable;
import com.github.zamponimarco.cubescocktail.cooldown.bar.NoBar;
import com.github.zamponimarco.cubescocktail.libs.annotation.Enumerable;
import com.github.zamponimarco.cubescocktail.libs.annotation.Serializable;
import com.github.zamponimarco.cubescocktail.libs.model.ModelPath;
import com.github.zamponimarco.cubescocktail.source.Source;
import com.github.zamponimarco.cubescocktail.trgt.Target;
import com.github.zamponimarco.cubescocktail.trigger.HitEntityTrigger;
import com.github.zamponimarco.cubescocktail.trigger.Trigger;
import com.github.zamponimarco.cubescocktail.trigger.TriggerListener;
import com.github.zamponimarco.mobdrink.mob.Mob;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Enumerable.Child
@PossibleTargets("getPossibleTargets")
@PossibleSources("getPossibleSources")
@Enumerable.Displayable(name = "&6&lTriggered Skill", description = "gui.mob.skill.triggered.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY1Mjg2ZTNlNmZhMDBlNGE2MGJiODk2NzViOWFhNzVkNmM5Y2RkMWVjODQwZDFiY2MyOTZiNzFjOTJmOWU0MyJ9fX0")
public class TriggeredSkill extends Skill implements TriggerListener, Cooldownable {

    private static final String TRIGGER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY1Mjg2ZTNlNmZhMDBlNGE2MGJiODk2NzViOWFhNzVkNmM5Y2RkMWVjODQwZDFiY2MyOTZiNzFjOTJmOWU0MyJ9fX0=";
    private static final String COOLDOWN_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";


    @Serializable(headTexture = TRIGGER_HEAD, description = "gui.mob.skill.triggered.trigger")
    private Trigger trigger;
    @Serializable(headTexture = COOLDOWN_HEAD, description = "gui.mob.skill.triggered.cooldown")
    private int cooldown;

    public TriggeredSkill(ModelPath<Mob> path) {
        super(path);
        this.trigger = new HitEntityTrigger();
        this.cooldown = 0;
        trigger.registerListener(this);
        registerKeyed();
    }

    public TriggeredSkill(UUID mobId, UUID id, List<ActionGroup> groups, Trigger trigger, int cooldown) {
        super(mobId, id, groups);
        this.trigger = trigger;
        this.cooldown = cooldown;
        trigger.registerListener(this);
        registerKeyed();
    }

    public TriggeredSkill(Map<String, Object> map) {
        super(map);
        this.trigger = (Trigger) map.getOrDefault("trigger", new HitEntityTrigger());
        this.cooldown = (int) map.getOrDefault("cooldown", 0);
        trigger.registerListener(this);
        registerKeyed();
    }

    public Collection<Class<? extends Target>> getPossibleTargets() {
        return new HashSet<>(trigger.getPossibleTargets());
    }

    public Collection<Class<? extends Source>> getPossibleSources() {
        return new HashSet<>(trigger.getPossibleSources());
    }


    @Override
    public Skill clone() {
        return new TriggeredSkill(mobId, UUID.randomUUID(), groups.stream().map(ActionGroup::clone).collect(Collectors.toList()),
                trigger.clone(), cooldown);
    }

    @Override
    public String getName() {
        return trigger.getName();
    }

    @Override
    public CooldownOptions getCooldownOptions() {
        return new CooldownOptions(cooldown, new NoBar());
    }

    @Override
    public void onTrigger(Map<String, Object> map) {
        LivingEntity caster = (LivingEntity) map.get("caster");

        Mob mob = Mob.fromEntity(caster);

        if (mob != null && mob.getSkills().contains(this)) {
            executeTriggers(map, caster, mob);
        }
    }

    private void executeTriggers(Map<String, Object> map, LivingEntity caster, Mob mob) {
        if (cooldown > 0) {
            if (CubesCocktail.getInstance().getCooldownManager().getCooldown(caster, getKey()) > 0) {
                return;
            } else {
                CubesCocktail.getInstance().getCooldownManager().addCooldown(caster, getKey(), cooldown,
                        getCooldownOptions().getBar());
            }
        }

        executeActions(map);
    }

    @Override
    public ItemStack getGUIItem() {
        return trigger.getGUIItem();
    }

    @Override
    public @NotNull Trigger getTrigger() {
        return trigger;
    }
}
