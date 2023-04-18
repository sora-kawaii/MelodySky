/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.render;

import xyz.Melody.Event.value.Option;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class NoArmorRender
extends Module {
    public Option<Boolean> selfOnly = new Option<Boolean>("SelfOnly", true);
    public Option<Boolean> chead = new Option<Boolean>("HideCustomHead", true);
    public Option<Boolean> armor = new Option<Boolean>("HideArmor", true);
    public Option<Boolean> arrows = new Option<Boolean>("HideArrows", true);

    public NoArmorRender() {
        super("NoArmorRender", new String[]{"armor"}, ModuleType.Render);
        this.addValues(this.selfOnly, this.chead, this.armor, this.arrows);
        this.setModInfo("Armor Invisible, Hide Arrows on Your Body.");
    }
}

