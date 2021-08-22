package com.github.zamponimarco.mobdrink.mob.options;

import com.github.zamponimarco.cubescocktail.action.source.ActionSource;
import com.github.zamponimarco.cubescocktail.action.targeter.ActionTarget;
import com.github.zamponimarco.cubescocktail.libs.annotation.Serializable;
import com.github.zamponimarco.cubescocktail.libs.model.Model;
import com.github.zamponimarco.cubescocktail.loot.DropTable;
import com.github.zamponimarco.cubescocktail.loot.SimpleDropTable;
import com.github.zamponimarco.cubescocktail.value.StringValue;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.Map;

@Getter
@Setter
public class GeneralOptions implements Model {

    private static final StringValue NAME_DEFAULT = new StringValue("null");
    private static final boolean NAME_VISIBLE_DEFAULT = false;

    private static final String ATTRIBUTE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUwNTU2YTg0MTY0MDY5ZGYzYjg5NTkzOWQwYWI1MDhmZmE4ZTE0MDQ3MTA2OTM4YjU1OWY1ODg5ZTViZmJlNCJ9fX0=";
    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";
    private static final String VISIBLE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjMxMmUxYjkzZTM1NDRkMGVkMDFlMDQ3MTZlNWUyZjNlYThlZDc5OWFlMDI1M2U0YjE4MjRkZThiMzAwMmY2NCJ9fX0=";
    private static final String BEHAVIOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTE4MjUzZjNhNjhiNGFiM2U1ZDM2Yzk3NDU2YTJiMmZjNzlhNjg2NjJjMzAxODRhNTdkZDZiZTE1MWYwYmNiOSJ9fX0=";
    private static final String LOOT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTliOTA2YjIxNTVmMTkzNzg3MDQyMzM4ZDA1Zjg0MDM5MWMwNWE2ZDNlODE2MjM5MDFiMjk2YmVlM2ZmZGQyIn19fQ==";
    private static final String EQUIPMENT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGQzMjAxMGI0NDE2NDNlNWNjMTlmMzM3ZDJkOTE0OWQyODIxYjRmNjAwNjIwZjFiMGViZDQ3ODQwNWI3ZDgxNCJ9fX0";

    @Serializable(headTexture = ATTRIBUTE_HEAD, description = "gui.general-options.attributes")
    private AttributeOptions attributeOptions;

    @Serializable(headTexture = BEHAVIOR_HEAD, description = "gui.general-options.behavior")
    private BehaviorOptions behaviorOptions;

    @Serializable(headTexture = NAME_HEAD, description = "gui.general-options.name",
            additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Optional(defaultValue = "NAME_DEFAULT")
    private StringValue customName;

    @Serializable(headTexture = VISIBLE_HEAD, description = "gui.general-options.name-visible")
    @Serializable.Optional(defaultValue = "NAME_VISIBLE_DEFAULT")
    private boolean customNameVisible;

    @Serializable(headTexture = LOOT_HEAD, description = "gui.general-options.loot", additionalDescription = {"gui.additional-tooltips.recreate"})
    private DropTable dropTable;

    @Serializable(headTexture = EQUIPMENT_HEAD, description = "gui.general-options.equipment")
    private EquipmentOptions equipmentOptions;

    public GeneralOptions() {
        this(new AttributeOptions(), NAME_DEFAULT.clone(), NAME_VISIBLE_DEFAULT, new BehaviorOptions(),
                new SimpleDropTable(), new EquipmentOptions());
    }

    public GeneralOptions(Map<String, Object> map) {
        this.attributeOptions = (AttributeOptions) map.getOrDefault("attributeOptions", new AttributeOptions());
        this.customName = (StringValue) map.getOrDefault("customName", NAME_DEFAULT.clone());
        this.customNameVisible = (boolean) map.getOrDefault("customNameVisible", NAME_VISIBLE_DEFAULT);
        this.behaviorOptions = (BehaviorOptions) map.getOrDefault("behaviorOptions", new BehaviorOptions());
        this.dropTable = (DropTable) map.getOrDefault("dropTable", new SimpleDropTable());
        this.equipmentOptions = (EquipmentOptions) map.getOrDefault("equipmentOptions", new EquipmentOptions());
    }

    public GeneralOptions(AttributeOptions attributeOptions, StringValue customName, boolean customNameVisible,
                          BehaviorOptions behaviorOptions, DropTable dropTable, EquipmentOptions equipmentOptions) {
        this.attributeOptions = attributeOptions;
        this.behaviorOptions = behaviorOptions;
        this.customName = customName;
        this.customNameVisible = customNameVisible;
        this.dropTable = dropTable;
        this.equipmentOptions = equipmentOptions;
    }

    public void buildOptions(LivingEntity e, ActionSource source, ActionTarget target) {
        attributeOptions.buildAttributes(e, source, target);
        behaviorOptions.setMobBehavior((Mob) e, source, target);
        if (!customName.getRealValue(target, source).equals("null")) {
            e.setCustomName(customName.getRealValue(target, source));
        }
        e.setCustomNameVisible(customNameVisible);
        equipmentOptions.applyToEntity(e, target, source);
    }
}
