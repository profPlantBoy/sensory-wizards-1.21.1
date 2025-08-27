package net.profplantboy.sensorywizards.spell;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryWrapper;
// Import the new, correct interface
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.HashSet;
import java.util.Set;

// Implement AutoSyncedComponent. You no longer need to also implement Component.
public class LearnedSpellsComponent implements AutoSyncedComponent {
    private final Set<String> spells = new HashSet<>();

    public Set<String> getSpells() {
        return this.spells;
    }

    public boolean hasSpell(String spellId) {
        return this.spells.contains(spellId);
    }

    public void addSpell(String spellId) {
        this.spells.add(spellId);
    }

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