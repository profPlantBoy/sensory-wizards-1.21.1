package net.profplantboy.sensorywizards.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import net.profplantboy.sensorywizards.block.ModBlocks;
import net.profplantboy.sensorywizards.item.ModItems;
import net.profplantboy.sensorywizards.screen.WandCarverScreenHandler;

public class WandCarverBlockEntity extends BlockEntity
        implements ExtendedScreenHandlerFactory<BlockPos>, net.minecraft.inventory.Inventory {

    /**
     * Slots:
     * 0 = wood input
     * 1 = core input
     * 2 = focus input (optional)
     * 3 = output
     */
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(4, ItemStack.EMPTY);

    // simple sync/progress (dummy for now)
    private final PropertyDelegate properties = new ArrayPropertyDelegate(1);

    public WandCarverBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.WAND_CARVER_ENTITY, pos, state);
    }

    /* ---------- Inventory implementation ---------- */
    @Override
    public int size() { return items.size(); }

    @Override
    public boolean isEmpty() {
        for (ItemStack s : items) if (!s.isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack = items.get(slot);
        if (stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack split = stack.split(amount);
        markDirty();
        return split;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack s = items.get(slot);
        items.set(slot, ItemStack.EMPTY);
        markDirty();
        return s;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > stack.getMaxCount()) stack.setCount(stack.getMaxCount());
        markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clear() {
        items.clear();
        markDirty();
    }

    public PropertyDelegate getProperties() { return properties; }

    /* ---------- Extended screen opening data (1.21+) ---------- */
    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos; // send BlockPos to the client
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.sensorywizards.wand_carver");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        // Server-side constructor; client side should read the BlockPos from the extended open data
        return new WandCarverScreenHandler(syncId, playerInventory, this, this.properties, this.pos);
    }

    /* ---------- Very simple "craft" logic for now ---------- */
    public void tryCraft() {
        ItemStack wood = items.get(0);
        ItemStack core = items.get(1);
        ItemStack focus = items.get(2);
        ItemStack output = items.get(3);

        boolean hasWood = !wood.isEmpty() && wood.isOf(Items.STICK);
        boolean hasCore = !core.isEmpty() && core.isOf(Items.FEATHER);
        boolean hasSpace = output.isEmpty();

        if (hasWood && hasCore && hasSpace) {
            ItemStack result = new ItemStack(ModItems.WANDS.get("wooden_wand"));

            // 1.21+ custom per-stack data via Data Components
            NbtCompound custom = new NbtCompound();
            custom.putString("wand_wood", "holly");
            custom.putString("wand_core", "phoenix_feather");
            if (!focus.isEmpty() && focus.isOf(Items.AMETHYST_SHARD)) {
                custom.putString("wand_focus", "amethyst");
            }
            result.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(custom));

            items.set(3, result);
            wood.decrement(1);
            core.decrement(1);
            if (!focus.isEmpty() && focus.isOf(Items.AMETHYST_SHARD)) {
                focus.decrement(1);
            }
            markDirty();
        }
    }

    public void dropContents(net.minecraft.world.World world, BlockPos pos) {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                ItemEntity e = new ItemEntity(world,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        stack.copy());
                world.spawnEntity(e);
            }
        }
        clear();
    }

    /* ---------- Save/Load (1.21 signatures) ---------- */
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.writeNbt(nbt, lookup);
        // Writes inventory under default "Items" list tag
        Inventories.writeNbt(nbt, this.items, lookup);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.readNbt(nbt, lookup);
        this.items.clear();
        Inventories.readNbt(nbt, this.items, lookup);
    }
}
