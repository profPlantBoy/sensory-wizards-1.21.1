package net.profplantboy.sensorywizards.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.profplantboy.sensorywizards.spell.LearnedSpellsComponent;
import net.profplantboy.sensorywizards.spell.ModComponents;
import net.profplantboy.sensorywizards.spell.SpellComponent;

public class SpellScroll extends Item {

    public SpellScroll(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            ItemStack stack = user.getStackInHand(hand);
            SpellComponent spellComponent = stack.get(ModComponents.SPELL_COMPONENT);

            if (spellComponent != null) {
                String spellId = spellComponent.spellType();
                LearnedSpellsComponent learnedSpells = ModComponents.LEARNED_SPELLS.get(user);

                if (learnedSpells.hasSpell(spellId)) {
                    user.sendMessage(Text.of("You already know this spell!"), false);
                } else {
                    learnedSpells.addSpell(spellId);
                    ModComponents.LEARNED_SPELLS.sync(user); // Add this line to send an update to the client
                    user.sendMessage(Text.of("You have learned the " + spellId + " spell!"), false);
                    stack.decrement(1);
                }
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand), !world.isClient());
    }
}