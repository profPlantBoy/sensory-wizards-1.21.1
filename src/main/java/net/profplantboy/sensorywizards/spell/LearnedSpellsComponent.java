package net.profplantboy.sensorywizards.spell;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.HashSet;
import java.util.Set;

public class LearnedSpellsComponent implements Component {
    private final Set<String> spells = new HashSet<>();

    // Add this new method
    public Set<String> getSpells() {
        return this.spells;
    }

    public boolean hasSpell(String spellId) {
        return this.spells.contains(spellId);
    }

    public void addSpell(String spellId) {
        this.spells.add(spellId);
    }
    // Add this new method to the class
    public void unlearnSpell(String spellId) {
        this.spells.remove(spellId);
    }
    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        spells.clear();
        NbtList spellList = tag.getList("spells", 8);
        for (int i = 0; i < spellList.size(); i++) {
            this.spells.add(spellList.getString(i));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList spellList = new NbtList();
        for (String spellId : this.spells) {
            spellList.add(NbtString.of(spellId));
        }
        tag.put("spells", spellList);
    }
}