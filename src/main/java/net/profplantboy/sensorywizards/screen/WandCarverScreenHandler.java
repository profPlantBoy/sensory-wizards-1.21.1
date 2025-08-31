package net.profplantboy.sensorywizards.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import net.profplantboy.sensorywizards.SensoryWizards;
import net.profplantboy.sensorywizards.block.entity.WandCarverBlockEntity;

public class WandCarverScreenHandler extends ScreenHandler {
    public static ScreenHandlerType<WandCarverScreenHandler> TYPE;

    public static void register() {
        TYPE = Registry.register(
                Registries.SCREEN_HANDLER,
                Identifier.of(SensoryWizards.MOD_ID, "wand_carver"),
                new ExtendedScreenHandlerType<>((syncId, playerInv, buf) -> {
                    BlockPos pos = buf.readBlockPos();
                    var be = (WandCarverBlockEntity) playerInv.player.getWorld().getBlockEntity(pos);
                    // On client, 'be' exists because the chunk is synced when the screen opens.
                    return new WandCarverScreenHandler(syncId, playerInv, be, be.getProperties(), pos);
                })
        );
    }

    private final Inventory inventory;          // server: BE inventory, client: same BE instance
    private final PropertyDelegate properties;  // sync small ints (progress, etc.)
    private final BlockPos pos;

    public WandCarverScreenHandler(int syncId, PlayerInventory playerInventory, WandCarverBlockEntity be,
                                   PropertyDelegate properties, BlockPos pos) {
        super(TYPE, syncId);
        this.inventory = be;
        this.properties = properties;
        this.pos = pos;

        // 0..2 = inputs, 3 = output
        // layout: 3 inputs on top row, output to the right
        this.addSlot(new Slot(inventory, 0, 26, 20));  // wood
        this.addSlot(new Slot(inventory, 1, 44, 20));  // core
        this.addSlot(new Slot(inventory, 2, 62, 20));  // focus
        this.addSlot(new Slot(inventory, 3, 98, 20) {  // output
            @Override public boolean canInsert(ItemStack stack) { return false; }
        });

        // player inventory (standard 9x3 + hotbar)
        int startY = 50;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, startY + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, startY + 58));
        }

        this.addProperties(properties);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    /** Shift-click transfer rules. */
    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasStack()) return ItemStack.EMPTY;

        ItemStack original = slot.getStack();
        newStack = original.copy();

        // if clicked output, move to player inventory
        if (index == 3) {
            if (!this.insertItem(original, 4, 4 + 36, true)) return ItemStack.EMPTY;
            slot.onQuickTransfer(original, newStack);
        } else if (index >= 4) {
            // from player -> try inputs (0..2)
            if (!this.insertItem(original, 0, 3, false)) return ItemStack.EMPTY;
        } else {
            // from inputs -> to player
            if (!this.insertItem(original, 4, 4 + 36, false)) return ItemStack.EMPTY;
        }

        if (original.isEmpty()) slot.setStack(ItemStack.EMPTY);
        else slot.markDirty();

        return newStack;
    }

    /** Called from the client screen when the "Carve" button is clicked. */
    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id == 0 && inventory instanceof WandCarverBlockEntity be) {
            be.tryCraft();
            return true;
        }
        return super.onButtonClick(player, id);
    }

    public BlockPos getPos() { return pos; }
}
