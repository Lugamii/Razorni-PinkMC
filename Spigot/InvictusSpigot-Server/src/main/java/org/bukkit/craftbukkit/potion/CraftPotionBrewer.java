package org.bukkit.craftbukkit.potion;

import com.google.common.collect.Maps;
import net.minecraft.server.MobEffect;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CraftPotionBrewer implements PotionBrewer {
    private static final Map<Integer, Collection<PotionEffect>> cache = Maps.newHashMap();

    public Collection<PotionEffect> getEffectsFromDamage(int damage) {
        if (cache.containsKey(damage))
            return cache.get(damage);

        List<?> mcEffects = net.minecraft.server.PotionBrewer.getEffects(damage, false);
        List<PotionEffect> effects = new ArrayList<>();
        if (mcEffects == null)
            return effects;

        for (Object raw : mcEffects) {
            if (!(raw instanceof MobEffect))
                continue;
            MobEffect mcEffect = (MobEffect) raw;
            PotionEffect effect = new PotionEffect(PotionEffectType.getById(mcEffect.getEffectId()),
                    mcEffect.getDuration(), mcEffect.getAmplifier());
            // Minecraft PotionBrewer applies duration modifiers automatically.
            effects.add(effect);
        }

        cache.put(damage, effects);

        return effects;
    }

    public PotionEffect createEffect(PotionEffectType potion, int duration, int amplifier) {
        return new PotionEffect(potion, potion.isInstant() ? 1 : (int) (duration * potion.getDurationModifier()),
                amplifier);
    }
}
