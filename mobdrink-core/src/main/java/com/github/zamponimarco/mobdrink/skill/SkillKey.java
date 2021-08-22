package com.github.zamponimarco.mobdrink.skill;

import com.github.zamponimarco.cubescocktail.key.Key;
import com.github.zamponimarco.cubescocktail.libs.annotation.Serializable;
import com.github.zamponimarco.cubescocktail.libs.util.ItemUtils;
import com.github.zamponimarco.cubescocktail.libs.util.MessageUtils;
import com.github.zamponimarco.mobdrink.MobDrink;
import com.github.zamponimarco.mobdrink.mob.Mob;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class SkillKey implements Key {

    @Serializable(stringValue = true)
    private UUID mobId;
    @Serializable(stringValue = true)
    private UUID skillId;

    public SkillKey(Map<String, Object> map) {
        this.mobId = UUID.fromString((String) map.getOrDefault("mobId", null));
        this.skillId = UUID.fromString((String) map.getOrDefault("skillId", null));
    }

    @Override
    public ItemStack getGUIItem() {
        Mob mob = MobDrink.getInstance().getMobManager().getById(mobId);
        if (mob == null) {
            return null;
        }
        Skill skill = mob.getSkillById(skillId);
        if (skill == null) {
            return null;
        }
        return ItemUtils.getNamedItem(
                mob.getGUIItem(),
                MessageUtils.color("&6&lItem: &c" + mob.getName()),
                Lists.newArrayList(
                        MessageUtils.color("&6&lSkill: &c" + skill.getName())
                )
        );
    }

    @Override
    public Key clone() {
        return new SkillKey(mobId, skillId);
    }

    @Override
    public String getName() {
        Mob mob = MobDrink.getInstance().getMobManager().getById(mobId);
        if (mob == null) {
            return "";
        }
        Skill skill = mob.getSkillById(skillId);
        if (skill == null) {
            return "";
        }

        return String.format("%s/%s", mob.getName(), skill.getName());
    }
}