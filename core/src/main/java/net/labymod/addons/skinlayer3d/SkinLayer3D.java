package net.labymod.addons.skinlayer3d;

import net.labymod.addons.skinlayer3d.util.Constants;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.util.logging.Logging;

@AddonMain
public class SkinLayer3D extends LabyAddon<SkinLayer3D.SkinConfig> {

    public static Logging LOGGER = null;
    public static SkinConfig CONFIG = null;

    @Override
    protected void enable() {
        registerSettingCategory();
        CONFIG = configuration();
        LOGGER = logger();

        Constants.enabled = CONFIG.enabled.get();
        Constants.renderDistance = CONFIG.renderDistance.get();

        CONFIG.enabled().addChangeListener(val -> Constants.enabled = val);
        CONFIG.renderDistance().addChangeListener(val -> Constants.renderDistance = val);
    }

    @Override
    protected Class<? extends SkinConfig> configurationClass() {
        return SkinConfig.class;
    }

    @ConfigName("settings")
    public static class SkinConfig extends AddonConfig {

        @SwitchWidget.SwitchSetting
        private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

        @SliderWidget.SliderSetting(min = 4, max = 16, steps = 1)
        private final ConfigProperty<Integer> renderDistance = new ConfigProperty<>(14);

        @Override
        public ConfigProperty<Boolean> enabled() {
            return enabled;
        }

        public ConfigProperty<Integer> renderDistance() {
            return renderDistance;
        }

    }
}
