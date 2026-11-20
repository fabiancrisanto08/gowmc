package com.spartanforge.godofwar.item;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.registry.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

/**
 * Materiales de las 3 Armaduras Icónicas de God of War:
 * - Armadura de Ares (Fuego y Fuerza de Guerra)
 * - Armadura de Zeus (Velocidad y Rayos del Olimpo)
 * - Armadura de Berserker Nórdica (Resistencia y Furia al bajar la vida)
 */
public enum ModArmorMaterial implements ArmorMaterial {
    ARES("ares", 40, new int[]{4, 9, 7, 4}, 18, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.5F, 0.15F,
        () -> Ingredient.of(ModItems.CHAOS_ASH.get())),
    ZEUS("zeus", 44, new int[]{5, 10, 8, 5}, 22, SoundEvents.ARMOR_EQUIP_GOLD, 4.0F, 0.2F,
        () -> Ingredient.of(ModItems.OLYMPUS_ESSENCE.get())),
    BERSERKER("berserker", 42, new int[]{4, 9, 8, 4}, 20, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.8F, 0.2F,
        () -> Ingredient.of(ModItems.ASGARDIAN_STEEL.get()));

    private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};

    private final String name;
    private final int durabilityMultiplier;
    private final int[] defenseForSlot;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    ModArmorMaterial(
        String name,
        int durabilityMultiplier,
        int[] defenseForSlot,
        int enchantmentValue,
        SoundEvent equipSound,
        float toughness,
        float knockbackResistance,
        Supplier<Ingredient> repairIngredient
    ) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.defenseForSlot = defenseForSlot;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return BASE_DURABILITY[type.getSlot().getIndex()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.defenseForSlot[type.getSlot().getIndex()];
    }

    @Override public int getEnchantmentValue() { return this.enchantmentValue; }
    @Override public SoundEvent getEquipSound() { return this.equipSound; }
    @Override public Ingredient getRepairIngredient() { return this.repairIngredient.get(); }
    @Override public String getName() { return GodOfWarMod.MOD_ID + ":" + this.name; }
    @Override public float getToughness() { return this.toughness; }
    @Override public float getKnockbackResistance() { return this.knockbackResistance; }
}
