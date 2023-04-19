/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.balance;

import com.mojang.realmsclient.dto.PlayerInfo;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S14PacketEntity;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.TimeHelper;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AntiBot
extends Module {
    private TimeHelper timer = new TimeHelper();
    private List<EntityPlayer> invalid = new ArrayList<EntityPlayer>();
    private List<EntityPlayer> whitelist = new ArrayList<EntityPlayer>();
    private List<EntityPlayer> hurtTimeCheck = new ArrayList<EntityPlayer>();
    private List<PlayerInfo> playerList = new ArrayList<PlayerInfo>();
    public Value<Boolean> remove = new Option<Boolean>("AutoRemove", true);
    public Value<Double> hurttime = new Numbers<Double>("HurtTimeCheck", 10000.0, 5000.0, 20000.0, 100.0);
    public Value<Boolean> touchedGround = new Option<Boolean>("GroundCheck", true);
    public Value<Boolean> toolCheck = new Option<Boolean>("ToolCheck", true);
    private static List<EntityPlayer> removed = new ArrayList<EntityPlayer>();
    public TimeHelper timer2 = new TimeHelper();
    public TimeHelper timer3 = new TimeHelper();
    public List<Integer> onAirInvalid = new ArrayList<Integer>();

    public AntiBot() {
        super("AntiBot", ModuleType.Balance);
        this.addValues(this.remove, this.hurttime, this.touchedGround, this.toolCheck);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (!this.hurtTimeCheck.isEmpty() && this.timer3.isDelayComplete(this.hurttime.getValue().longValue())) {
            this.hurtTimeCheck.clear();
            this.timer3.reset();
        }
        if (!this.whitelist.isEmpty() && this.timer2.isDelayComplete(3000L)) {
            this.whitelist.clear();
            this.timer2.reset();
        }
        if (!this.invalid.isEmpty() && this.timer.isDelayComplete(1000L)) {
            this.invalid.clear();
            this.timer.reset();
        }
        for (Entity o : this.mc.theWorld.getLoadedEntityList()) {
            EntityPlayer ent;
            if (!(o instanceof EntityPlayer) || (ent = (EntityPlayer)o) == this.mc.thePlayer || this.invalid.contains(ent)) continue;
            String formated = ent.getDisplayName().getFormattedText();
            String custom = ent.getCustomNameTag();
            String name = ent.getName();
            if (!formated.startsWith("\ufffd\ufffd") && formated.endsWith("\ufffd\ufffdr")) {
                this.invalid.add(ent);
            }
            if (!this.isInTablist(ent)) {
                this.invalid.add(ent);
            }
            if (ent.hurtTime > 0) {
                this.hurtTimeCheck.add(ent);
            }
            if (this.hurtTimeCheck.contains(ent) && !this.whitelist.contains(ent)) {
                this.whitelist.add(ent);
            }
            if (ent.getHeldItem() != null) {
                this.whitelist.add(ent);
            }
            if (ent.getHeldItem() == null && !this.whitelist.contains(ent) && this.toolCheck.getValue().booleanValue()) {
                this.invalid.add(ent);
            }
            if (ent.isInvisible() && !custom.equalsIgnoreCase("") && custom.toLowerCase().contains("\ufffd\ufffdc\ufffd\ufffdc") && name.contains("\ufffd\ufffdc")) {
                this.invalid.add(ent);
            }
            if (!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("\ufffd\ufffdc") && custom.toLowerCase().contains("\ufffd\ufffdr")) {
                this.invalid.add(ent);
            }
            if (formated.contains("\ufffd\ufffd8[NPC]")) {
                this.invalid.add(ent);
            }
            if (formated.contains("\ufffd\ufffdc") || custom.equalsIgnoreCase("")) continue;
            this.invalid.add(ent);
        }
    }

    @Override
    public void onEnable() {
        this.onAirInvalid.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.onAirInvalid.clear();
        super.onDisable();
    }

    @EventHandler
    public void onReceivePacket(EventPacketRecieve event) {
        S14PacketEntity packet;
        Entity entity;
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            return;
        }
        if (event.getPacket() instanceof S14PacketEntity && (entity = (packet = (S14PacketEntity)event.getPacket()).getEntity(this.mc.theWorld)) instanceof EntityPlayer && !packet.getOnGround() && !this.onAirInvalid.contains(entity.getEntityId())) {
            this.onAirInvalid.add(entity.getEntityId());
        }
    }

    public boolean isInTablist(EntityPlayer player) {
        if (Minecraft.getMinecraft().isSingleplayer()) {
            return true;
        }
        for (NetworkPlayerInfo o : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            NetworkPlayerInfo playerInfo = o;
            if (!playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) continue;
            return true;
        }
        return false;
    }

    public boolean isEntityBot(Entity e) {
        if (!Client.instance.getModuleManager().getModuleByClass(AntiBot.class).isEnabled()) {
            return false;
        }
        if (!(e instanceof EntityPlayer) || !Client.instance.getModuleManager().getModuleByClass(AntiBot.class).isEnabled()) {
            return false;
        }
        EntityPlayer player = (EntityPlayer)e;
        return this.invalid.contains(player) || !this.onAirInvalid.contains(player.getEntityId());
    }
}

