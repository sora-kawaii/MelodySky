/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.StringUtils;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class DamageFormat
extends Module {
    private String[] colors = new String[]{"\u00a7f", "\u00a7e", "\u00a76", "\u00a7c", "\u00a7f", "\u00a7f", "\u00a7f", "\u00a7f"};
    private static final NavigableMap<Long, String> suffixes = new TreeMap<Long, String>();

    public DamageFormat() {
        super("DamageFormat", new String[]{"gb"}, ModuleType.QOL);
        this.setModInfo("Change The Damage Numbers to xxxM or xxxk.");
    }

    @EventHandler
    private void onTick(EventTick e) {
        if (this.mc.theWorld == null) {
            return;
        }
        for (Entity entity : this.mc.theWorld.loadedEntityList) {
            if (this.mc.thePlayer.getDistanceToEntity(entity) > 24.0f || !(entity instanceof EntityArmorStand)) continue;
            EntityArmorStand as = (EntityArmorStand)entity;
            char idk2 = '\"';
            if (!as.hasCustomName() || !as.getCustomNameTag().contains("\u2727") && !as.getCustomNameTag().contains("\u2727") && !as.getCustomNameTag().contains("\u272f") && !as.getCustomNameTag().contains("\u272f") || as.getCustomNameTag().contains("M") || as.getCustomNameTag().contains("b") || as.getCustomNameTag().contains("k")) continue;
            String num = StringUtils.stripControlCodes(as.getName().replaceAll(",", "").replaceAll("\u2727", "").replaceAll("\u272f", "").replaceAll("\u272f", "").replaceAll("\u2764", "").replaceAll("\u2727", "").replaceAll(String.valueOf(idk2), "").replaceAll("'", "").replaceAll("text", "").replaceAll(":", "").replaceAll("}", "").replaceFirst("\\{", ""));
            long number = Long.parseLong(num);
            String finalName = this.format(number);
            String formattedName = "";
            for (int i = 0; i < finalName.length(); ++i) {
                char c = finalName.charAt(i);
                String colorPrefix = this.colors[0];
                colorPrefix = i >= this.colors.length ? this.colors[0] : this.colors[i];
                formattedName = formattedName + colorPrefix + String.valueOf(c);
            }
            as.setCustomNameTag("\u00a7r\u2727" + formattedName + "\u00a7e\u2727\u00a7r");
        }
    }

    public String format(long value) {
        if (value == Long.MIN_VALUE) {
            return this.format(-9223372036854775807L);
        }
        if (value < 0L) {
            return "-" + this.format(-value);
        }
        if (value < 1000L) {
            return Long.toString(value);
        }
        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();
        long truncated = value / (divideBy / 10L);
        boolean hasDecimal = truncated < 100L && (double)truncated / 10.0 != (double)(truncated / 10L);
        return hasDecimal ? (double)truncated / 10.0 + suffix : truncated / 10L + suffix;
    }

    static {
        suffixes.put(1000L, "k");
        suffixes.put(1000000L, "M");
        suffixes.put(1000000000L, "b");
    }
}

