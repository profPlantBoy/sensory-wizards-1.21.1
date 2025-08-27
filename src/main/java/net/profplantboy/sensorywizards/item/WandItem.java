package net.profplantboy.sensorywizards.item;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.profplantboy.sensorywizards.network.CastSpellPayload;
import net.profplantboy.sensorywizards.spell.EquippedSpellComponent;
import net.profplantboy.sensorywizards.spell.ModComponents;

import java.util.List;

public class WandItem extends Item {
    public WandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            EquippedSpellComponent equipped = user.getStackInHand(hand).getOrDefault(ModComponents.EQUIPPED_SPELL, EquippedSpellComponent.EMPTY);
            if (equipped != null && !equipped.spellId().isEmpty()) {
                ClientPlayNetworking.send(new CastSpellPayload(equipped.spellId()));
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        EquippedSpellComponent equipped = stack.getOrDefault(ModComponents.EQUIPPED_SPELL, EquippedSpellComponent.EMPTY);

        if (equipped != null && !equipped.spellId().isEmpty()) {
            tooltip.add(Text.of("Equipped: " + equipped.spellId()));
        }
    }
}