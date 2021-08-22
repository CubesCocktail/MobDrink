package com.github.zamponimarco.mobdrink.mob.options;

import com.github.zamponimarco.cubescocktail.action.source.ActionSource;
import com.github.zamponimarco.cubescocktail.action.targeter.ActionTarget;
import com.github.zamponimarco.cubescocktail.ai.goal.GoalSelector;
import com.github.zamponimarco.cubescocktail.ai.target.TargetSelector;
import com.github.zamponimarco.cubescocktail.libs.annotation.Serializable;
import com.github.zamponimarco.cubescocktail.libs.model.Model;
import com.google.common.collect.Lists;
import org.bukkit.entity.Mob;

import java.util.List;
import java.util.Map;

public class BehaviorOptions implements Model {

    private static final String TARGET_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc4N2I3YWZiNWE1OTk1Mzk3NWJiYTI0NzM3NDliNjAxZDU0ZDZmOTNjZWFjN2EwMmFjNjlhYWU3ZjliOCJ9fX0=";
    private static final String GOALS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDhmZDcxMjZjZDY3MGM3OTcxYTI4NTczNGVkZmRkODAyNTcyYTcyYTNmMDVlYTQxY2NkYTQ5NDNiYTM3MzQ3MSJ9fX0=";

    @Serializable(headTexture = TARGET_HEAD, description = "gui.general-options.behavior-options.target-selectors")
    private List<TargetSelector> targetSelectors;

    @Serializable(headTexture = GOALS_HEAD, description = "gui.general-options.behavior-options.target-selectors")
    private List<GoalSelector> goalSelectors;

    public BehaviorOptions() {
        this(Lists.newArrayList(), Lists.newArrayList());
    }

    public BehaviorOptions(Map<String, Object> map) {
        this.targetSelectors = (List<TargetSelector>) map.getOrDefault("targetSelectors", Lists.newArrayList());
        this.goalSelectors = (List<GoalSelector>) map.getOrDefault("goalSelectors", Lists.newArrayList());
    }

    public BehaviorOptions(List<TargetSelector> targetSelectors, List<GoalSelector> goalSelectors) {
        this.targetSelectors = targetSelectors;
        this.goalSelectors = goalSelectors;
    }

    public void setMobBehavior(Mob e, ActionSource source, ActionTarget target) {
        targetSelectors.forEach(targetSelector -> targetSelector.applyToEntity(e, source, target));
        goalSelectors.forEach(targetSelector -> targetSelector.applyToEntity(e, source, target));
    }
}
