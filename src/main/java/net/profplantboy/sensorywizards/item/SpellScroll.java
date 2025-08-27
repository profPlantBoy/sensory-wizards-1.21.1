package net.profplantboy.sensorywizards.spell;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import java.util.HashSet;
import java.util.Set;

public class LearnedSpellsComponent implements Component {
    private final Set<String> spells = new HashSet<>();

    public boolean hasSpell(String spellId) {
        return this.spells.contains(spellId);
    }

    public void addSpell(String spellId) {
        this.spells.add(spellId);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        spells.clear();
        NbtList spellList = tag.getList("spells", 8); // 8 is the NBT type for String
        for (int i = 0; i < spellList.size(); i++) {
            this.spells.add(spellList.getString(i));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtList spellList = new NbtList();
        for (String spellId : this.spells) {
            spellList.add(NbtString.of(spellId));
        }
        tag.put("spells", spellList);
    }
}