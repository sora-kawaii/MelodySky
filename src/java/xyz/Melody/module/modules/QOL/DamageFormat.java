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
    private void onTick(EventTick eventTick) {
        if (this.mc.theWorld == null) {
            return;
        }
        for (Entity entity : this.mc.theWorld.loadedEntityList) {
            if (this.mc.thePlayer.getDistanceToEntity(entity) > 24.0f || !(entity instanceof EntityArmorStand)) continue;
            EntityArmorStand entityArmorStand = (EntityArmorStand)entity;
            char c = '\"';
            if (!entityArmorStand.hasCustomName() || !entityArmorStand.getCustomNameTag().contains("\u2727") && !entityArmorStand.getCustomNameTag().contains("\u2727") && !entityArmorStand.getCustomNameTag().contains("\u272f") && !entityArmorStand.getCustomNameTag().contains("\u272f") || entityArmorStand.getCustomNameTag().contains("M") || entityArmorStand.getCustomNameTag().contains("b") || entityArmorStand.getCustomNameTag().contains("k")) continue;
            String string = StringUtils.stripControlCodes(entityArmorStand.getName().replaceAll(",", "").replaceAll("\u2727", "").replaceAll("\u272f", "").replaceAll("\u272f", "").replaceAll("\u2764", "").replaceAll("\u2727", "").replaceAll(String.valueOf(c), "").replaceAll("'", "").replaceAll("text", "").replaceAll(":", "").replaceAll("}", "").replaceFirst("\\{", ""));
            long l2 = Long.parseLong(string);
            String string2 = this.format(l2);
            String string3 = "";
            for (int i = 0; i < string2.length(); ++i) {
                char c2 = string2.charAt(i);
                String string4 = this.colors[0];
                string4 = i >= this.colors.length ? this.colors[0] : this.colors[i];
                string3 = string3 + string4 + String.valueOf(c2);
            }
            entityArmorStand.setCustomNameTag("\u00a7r\u2727" + string3 + "\u00a7e\u2727\u00a7r");
        }
    }

    public String format(long l2) {
        if (l2 == Long.MIN_VALUE) {
            return this.format(-9223372036854775807L);
        }
        if (l2 < 0L) {
            return "-" + this.format(-l2);
        }
        if (l2 < 1000L) {
            return Long.toString(l2);
        }
        Map.Entry<Long, String> entry = suffixes.floorEntry(l2);
        Long l3 = entry.getKey();
        String string = entry.getValue();
        long l4 = l2 / (l3 / 10L);
        boolean bl = l4 < 100L && (double)l4 / 10.0 != (double)(l4 / 10L);
        return bl ? (double)l4 / 10.0 + string : l4 / 10L + string;
    }

    static {
        suffixes.put(1000L, "k");
        suffixes.put(1000000L, "M");
        suffixes.put(1000000000L, "b");
    }
}

