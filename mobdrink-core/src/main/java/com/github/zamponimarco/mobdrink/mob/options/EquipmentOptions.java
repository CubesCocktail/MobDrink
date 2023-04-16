package com.github.zamponimarco.mobdrink.mob.options;

import com.github.zamponimarco.cubescocktail.action.source.ActionSource;
import com.github.zamponimarco.cubescocktail.action.targeter.ActionTarget;
import com.github.zamponimarco.cubescocktail.libs.annotation.Serializable;
import com.github.zamponimarco.cubescocktail.libs.core.Libs;
import com.github.zamponimarco.cubescocktail.libs.model.Model;
import com.github.zamponimarco.cubescocktail.libs.model.ModelPath;
import com.github.zamponimarco.cubescocktail.libs.model.wrapper.ItemStackWrapper;
import com.github.zamponimarco.cubescocktail.libs.util.ItemUtils;
import com.github.zamponimarco.cubescocktail.libs.util.MapperUtils;
import com.github.zamponimarco.cubescocktail.libs.util.MessageUtils;
import com.github.zamponimarco.cubescocktail.value.NumericValue;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Function;

public class EquipmentOptions implements Model {

    private static final boolean DEFAULT_DEFAULT = true;

    private static final String DEFAULT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODI4OGZjYmRhNGZmODVhY2E3ZmYyMTEzM2QxYWZlYzllZTI0NmRkOTIzZWNhOWJmZDA5MmYyMzMzZTY2ZiJ9fX0=";
    private static final String EQUIPMENT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGQzMjAxMGI0NDE2NDNlNWNjMTlmMzM3ZDJkOTE0OWQyODIxYjRmNjAwNjIwZjFiMGViZDQ3ODQwNWI3ZDgxNCJ9fX0=";

    @Serializable(headTexture = DEFAULT_HEAD, description = "gui.general-options.equipment-options.allow")
    @Serializable.Optional(defaultValue = "DEFAULT_DEFAULT")
    boolean allowDefaultItems;
    @Serializable(headTexture = EQUIPMENT_HEAD, description = "gui.general-options.equipment-options.equipments")
    private Set<Equipment> equipment;

    public EquipmentOptions() {
        this(Sets.newHashSet(), DEFAULT_DEFAULT);
    }

    public EquipmentOptions(Map<String, Object> map) {
        this.equipment = new HashSet<>((List<Equipment>) map.getOrDefault("equipment", Lists.newArrayList()));
        this.allowDefaultItems = (boolean) map.getOrDefault("allowDefaultItems", DEFAULT_DEFAULT);
    }

    public EquipmentOptions(Set<Equipment> equipment, boolean allowDefaultItems) {
        this.equipment = equipment;
        this.allowDefaultItems = allowDefaultItems;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("equipment", new ArrayList<>(equipment));
        map.put("allowDefaultItems", allowDefaultItems);
        return map;
    }

    void applyToEntity(LivingEntity mob, ActionTarget target, ActionSource source) {
        EntityEquipment entityEquipment = mob.getEquipment();
        this.equipment.forEach(equip -> equip.applyEquipment(entityEquipment, target, source));
    }

    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class Equipment implements Model {

        private static final String CHANCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

        private static final NumericValue CHANCE_DEFAULT = new NumericValue(0);

        @Serializable(displayItem = "getSlotItem", fromList = "getSlots", fromListMapper = "getSlotMap", stringValue = true, description = "gui.general-options.equipment-options.equipment.slot")
        @EqualsAndHashCode.Include
        private EquipmentSlot slot;
        @Serializable(displayItem = "getItem", description = "gui.general-options.equipment-options.equipment.item",
                additionalDescription = "gui.additional-tooltips.item")
        private ItemStackWrapper item;
        @Serializable(headTexture = CHANCE_HEAD, description = "gui.general-options.equipment-options.equipment.chance")
        @Serializable.Number(minValue = 0, maxValue = 1)
        @Serializable.Optional(defaultValue = "CHANCE_DEFAULT")
        private NumericValue dropChance;

        public Equipment() {
            this(EquipmentSlot.HAND, new ItemStackWrapper(), CHANCE_DEFAULT.clone());
        }

        public Equipment(Map<String, Object> map) {
            this.slot = EquipmentSlot.valueOf((String) map.getOrDefault("slot", "HAND"));
            this.item = (ItemStackWrapper) map.getOrDefault("item", new ItemStackWrapper());
            this.dropChance = (NumericValue) map.getOrDefault("dropChance", CHANCE_DEFAULT.clone());
        }

        public Equipment(EquipmentSlot slot, ItemStackWrapper item, NumericValue dropChance) {
            this.slot = slot;
            this.item = item;
            this.dropChance = dropChance;
        }

        public static List<Object> getSlots(ModelPath path) {
            return Lists.newArrayList(EquipmentSlot.values());
        }

        public static Function<Object, ItemStack> getSlotMap() {
            return MapperUtils.getEquipmentSlotMapper();
        }

        public ItemStack getSlotItem() {
            return MapperUtils.getEquipmentSlotMapper().apply(slot);
        }

        public ItemStack getItem() {
            return item.getWrapped().clone();
        }

        @Override
        public ItemStack getGUIItem() {
            return ItemUtils.getNamedItem(item.getWrapped().clone(), MessageUtils.color("&6&lSlot: &c" +
                    MessageUtils.capitalize(slot.name())), Libs.getLocale().getList("gui.additional-tooltips.delete"));
        }

        protected void applyEquipment(EntityEquipment equipment, ActionTarget target, ActionSource source) {
            float dropChance = this.dropChance.getRealValue(target, source).floatValue();
            ItemStack item = this.item.getWrapped().clone();
            switch (slot) {
                case HEAD -> {
                    equipment.setHelmet(item);
                    equipment.setHelmetDropChance(dropChance);
                }
                case CHEST -> {
                    equipment.setChestplate(item);
                    equipment.setChestplateDropChance(dropChance);
                }
                case LEGS -> {
                    equipment.setLeggings(item);
                    equipment.setLeggingsDropChance(dropChance);
                }
                case FEET -> {
                    equipment.setBoots(item);
                    equipment.setBootsDropChance(dropChance);
                }
                case HAND -> {
                    equipment.setItemInMainHand(item);
                    equipment.setItemInMainHandDropChance(dropChance);
                }
                case OFF_HAND -> {
                    equipment.setItemInOffHand(item);
                    equipment.setItemInOffHandDropChance(dropChance);
                }
            }
        }

    }
}
