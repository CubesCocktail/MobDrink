package com.github.zamponimarco.mobdrink.skill;

import com.github.zamponimarco.cubescocktail.action.group.ActionGroup;
import com.github.zamponimarco.cubescocktail.key.Key;
import com.github.zamponimarco.cubescocktail.key.Keyed;
import com.github.zamponimarco.cubescocktail.libs.annotation.Enumerable;
import com.github.zamponimarco.cubescocktail.libs.annotation.Serializable;
import com.github.zamponimarco.cubescocktail.libs.model.Model;
import com.github.zamponimarco.cubescocktail.libs.model.ModelPath;
import com.github.zamponimarco.mobdrink.mob.Mob;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Enumerable.Parent(classArray = {SpawnSkill.class, TriggeredSkill.class, TimedSkill.class})
public abstract class Skill implements Model, Cloneable, Keyed {

    protected static final String GROUPS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y0ZDVhNGFiYjY0ZGIxMWI0NTcxZTc0N2M1OGU0MDMwMThmNjQ5YzE4MTZjNjUwOWY5YTNmN2E3ODIxYjQ4ZSJ9fX0=";

    @Serializable(stringValue = true)
    protected UUID mobId;

    @Serializable(stringValue = true)
    protected UUID id;

    @Serializable(headTexture = GROUPS_HEAD, description = "gui.mob.skill.groups")
    protected List<ActionGroup> groups;

    public Skill(ModelPath<Mob> path) {
        this(path.getRoot().getId(), UUID.randomUUID(), Lists.newArrayList());
    }

    public Skill(UUID mobId, UUID id, List<ActionGroup> groups) {
        this.mobId = mobId;
        this.id = id;
        this.groups = groups;
    }

    public Skill(Map<String, Object> map) {
        this.mobId = UUID.fromString((String) map.getOrDefault("mobId", UUID.randomUUID()));
        this.id = UUID.fromString((String) map.getOrDefault("id", UUID.randomUUID()));
        this.groups = (List<ActionGroup>) map.getOrDefault("groups", Lists.newArrayList());
    }

    public void executeActions(Map<String, Object> map) {
        groups.forEach(group -> group.executeGroup(map));
    }

    @Override
    public abstract Skill clone();

    @Override
    public Key getKey() {
        return new SkillKey(mobId, id);
    }

    public abstract String getName();

}
