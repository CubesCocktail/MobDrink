package com.github.zamponimarco.mobdrink.mob.options;

import com.github.zamponimarco.cubescocktail.action.source.ActionSource;
import com.github.zamponimarco.cubescocktail.action.targeter.ActionTarget;
import com.github.zamponimarco.cubescocktail.libs.annotation.Serializable;
import com.github.zamponimarco.cubescocktail.libs.model.Model;
import com.github.zamponimarco.cubescocktail.value.NumericValue;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class AttributeOptions implements Model {

    private static final NumericValue VALUE_DEFAULT = new NumericValue(-1);

    private static final String HEALTH_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjEyNjZiNzQ4MjQyMTE1YjMwMzcwOGQ1OWNlOWQ1NTIzYjdkNzljMTNmNmRiNGViYzkxZGQ0NzIwOWViNzU5YyJ9fX0=";
    private static final String SPEED_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQyMzI5YTljNDEwNDA4NDE5N2JkNjg4NjE1ODUzOTg0ZDM3ZTE3YzJkZDIzZTNlNDEyZGQ0MmQ3OGI5OGViIn19fQ==";
    private static final String ARMOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUyNTU5ZjJiY2VhZDk4M2Y0YjY1NjFjMmI1ZjJiNTg4ZjBkNjExNmQ0NDY2NmNlZmYxMjAyMDc5ZDI3Y2E3NCJ9fX0=";
    private static final String DAMAGE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=";


    @Serializable(headTexture = HEALTH_HEAD, description = "gui.general-options.attribute-options.max-health",
            additionalDescription = {"gui.general-options.attribute-options.default-value", "gui.additional-tooltips.value"})
    @Serializable.Number(minValue = -1, scale = 1)
    @Serializable.Optional(defaultValue = "VALUE_DEFAULT")
    private NumericValue maxHealth;

    @Serializable(headTexture = SPEED_HEAD, description = "gui.general-options.attribute-options.movement-speed",
            additionalDescription = {"gui.general-options.attribute-options.default-value", "gui.additional-tooltips.value"})
    @Serializable.Number(minValue = -1)
    @Serializable.Optional(defaultValue = "VALUE_DEFAULT")
    private NumericValue movementSpeed;

    @Serializable(headTexture = ARMOR_HEAD, description = "gui.general-options.attribute-options.armor",
            additionalDescription = {"gui.general-options.attribute-options.default-value", "gui.additional-tooltips.value"})
    @Serializable.Number(minValue = -1)
    @Serializable.Optional(defaultValue = "VALUE_DEFAULT")
    private NumericValue armor;

    @Serializable(headTexture = DAMAGE_HEAD, description = "gui.general-options.attribute-options.damage",
            additionalDescription = {"gui.general-options.attribute-options.default-value", "gui.additional-tooltips.value"})
    @Serializable.Number(minValue = -1)
    @Serializable.Optional(defaultValue = "VALUE_DEFAULT")
    private NumericValue damage;

    public AttributeOptions() {
        this(VALUE_DEFAULT.clone(), VALUE_DEFAULT.clone(), VALUE_DEFAULT.clone(), VALUE_DEFAULT.clone());
    }

    public AttributeOptions(Map<String, Object> map) {
        this.maxHealth = (NumericValue) map.getOrDefault("maxHealth", VALUE_DEFAULT.clone());
        this.movementSpeed = (NumericValue) map.getOrDefault("movementSpeed", VALUE_DEFAULT.clone());
        this.armor = (NumericValue) map.getOrDefault("armor", VALUE_DEFAULT.clone());
        this.damage = (NumericValue) map.getOrDefault("damage", VALUE_DEFAULT.clone());
    }

    public AttributeOptions(NumericValue maxHealth, NumericValue movementSpeed, NumericValue armor, NumericValue damage) {
        this.maxHealth = maxHealth;
        this.movementSpeed = movementSpeed;
        this.armor = armor;
        this.damage = damage;
    }

    public void buildAttributes(LivingEntity e, ActionSource source, ActionTarget target) {
        setAttribute(Attribute.GENERIC_MAX_HEALTH, e, maxHealth.getRealValue(target, source));
        setAttribute(Attribute.GENERIC_MOVEMENT_SPEED, e, movementSpeed.getRealValue(target, source));
        setAttribute(Attribute.GENERIC_ARMOR, e, armor.getRealValue(target, source));
        setAttribute(Attribute.GENERIC_ATTACK_DAMAGE, e, damage.getRealValue(target, source));
    }


    private void setAttribute(Attribute attribute, LivingEntity entity, double value) {
        AttributeInstance inst = entity.getAttribute(attribute);


        if (inst == null) {
            entity.registerAttribute(attribute);
            inst = entity.getAttribute(attribute);
        }

        if (value == -1 || inst == null) {
            return;
        }

        inst.setBaseValue(value);
        if (attribute.equals(Attribute.GENERIC_MAX_HEALTH)) {
            entity.setHealth(value);
        }
    }
}
