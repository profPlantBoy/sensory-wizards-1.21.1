package net.profplantboy.sensorywizards.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.profplantboy.sensorywizards.spell.SpellIds;

@Environment(EnvType.CLIENT)
public class SensoryWizardsModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::buildScreen;
    }

    private Screen buildScreen(Screen parent) {
        SensoryWizardsConfig cfg = AutoConfig.getConfigHolder(SensoryWizardsConfig.class).getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("text.autoconfig.sensorywizards.title"))
                .setSavingRunnable(() -> AutoConfig.getConfigHolder(SensoryWizardsConfig.class).save());

        ConfigCategory cat = builder.getOrCreateCategory(Text.translatable("text.autoconfig.sensorywizards.category.spells"));
        ConfigEntryBuilder eb = builder.entryBuilder();

        // Ensure defaults exist (safe even if already present)
        cfg.ensureDefaults(SpellIds.ALL);

        // One toggle per spell id
        for (String id : SpellIds.ALL) {
            boolean current = cfg.enabledSpells.getOrDefault(id, true);
            cat.addEntry(
                    eb.startBooleanToggle(Text.literal(id), current)
                            .setDefaultValue(true)
                            .setSaveConsumer(val -> cfg.enabledSpells.put(id, val))
                            .build()
            );
        }

        return builder.build();
    }
}
