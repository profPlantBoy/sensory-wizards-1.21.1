package net.profplantboy.sensorywizards.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.profplantboy.sensorywizards.spell.EquippedSpellComponent;
import net.profplantboy.sensorywizards.spell.ModComponents;

import java.util.List;

public class WandItem extends Item {
    public WandItem(Settings settings) {
        super(settings);
    }

    // This is the final, correct method signature for tooltips
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        EquippedSpellComponent equipped = stack.getOrDefault(ModComponents.EQUIPPED_SPELL, EquippedSpellComponent.EMPTY);

        if (equipped != null && !equipped.spellId().isEmpty()) {
            tooltip.add(Text.of("Equipped: " + equipped.spellId()));
        }
    }
}