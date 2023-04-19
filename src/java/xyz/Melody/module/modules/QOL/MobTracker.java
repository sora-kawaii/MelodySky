/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.ClientLib.SkyblockArea;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.rendering.EventRenderEntityModel;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.ScoreboardUtils;
import xyz.Melody.Utils.render.FadeUtil;
import xyz.Melody.Utils.render.OutlineUtils;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class MobTracker
extends Module {
    private Option<Boolean> ghost = new Option<Boolean>("Ghost", true);
    private Option<Boolean> bat = new Option<Boolean>("Bat", true);
    private Option<Boolean> starMob = new Option<Boolean>("Starred Mob", true);
    private Option<Boolean> dragon = new Option<Boolean>("Ender Dragon", true);
    private Option<Boolean> arrows = new Option<Boolean>("Arrows", false);
    private Option<Boolean> necron = new Option<Boolean>("Wither", true);
    private Option<Boolean> wither = new Option<Boolean>("Wither(Crimson)", true);
    private Option<Boolean> farming = new Option<Boolean>("Endangered Animal", true);
    private HashMap<Integer, Color> mobIDs = new HashMap();
    public HashMap<EntityLivingBase, Color> checked = new HashMap();
    private ArrayList<Entity> endangeredAnimals = new ArrayList();

    public MobTracker() {
        super("MobTracker", new String[]{"mt"}, ModuleType.Render);
        this.addValues(this.ghost, this.bat, this.starMob, this.dragon, this.necron, this.wither, this.arrows, this.farming);
        this.setModInfo("Entity ESP.");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    public void onRenderEntityModel(EventRenderEntityModel event) {
        if (((Boolean)this.starMob.getValue()).booleanValue()) {
            if (!this.mobIDs.isEmpty() && this.mobIDs.containsKey(event.getEntity().getEntityId()) && !this.checked.containsKey(event.getEntity())) {
                this.checked.put(event.getEntity(), this.mobIDs.get(event.getEntity().getEntityId()));
            }
            if (this.checked.containsKey(event.getEntity())) {
                OutlineUtils.outlineEntity(event, this.checked.get(event.getEntity()));
            }
        }
    }

    @EventHandler
    private void onRender3D(EventRender3D event) {
        this.mobIDs.clear();
        for (EntityLivingBase elb : this.checked.keySet()) {
            if (elb == null) {
                this.checked.remove(elb);
                break;
            }
            if (!elb.isEntityAlive()) {
                this.checked.remove(elb);
                break;
            }
            if (!elb.isDead) continue;
            this.checked.remove(elb);
            break;
        }
        for (Entity entity : this.mc.theWorld.getLoadedEntityList()) {
            List<Entity> mobs;
            String name;
            Color col;
            Color c;
            if (entity instanceof EntityArrow && ((Boolean)this.arrows.getValue()).booleanValue()) {
                c = new Color(Colors.WHITE.c);
                col = new Color(c.getRed(), c.getGreen(), c.getBlue());
                RenderUtil.drawFilledESP(entity, col, event);
            }
            if (entity instanceof EntityWither && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crimson_Island && ((Boolean)this.wither.getValue()).booleanValue() && !entity.isInvisible()) {
                c = new Color(Colors.RED.c);
                col = new Color(c.getRed(), c.getGreen(), c.getBlue());
                RenderUtil.drawFilledESP(entity, col, event, 3.0f);
            }
            if (entity instanceof EntityCreeper && ScoreboardUtils.scoreboardContains("The Mist") && ((Boolean)this.ghost.getValue()).booleanValue()) {
                c = new Color(Colors.RED.c);
                col = new Color(c.getRed(), c.getGreen(), c.getBlue());
                if (!this.checked.keySet().contains((EntityLivingBase)entity)) {
                    this.checked.put((EntityLivingBase)entity, col);
                    RenderUtil.drawFilledESP(entity, col, event);
                }
            }
            if (entity instanceof EntityBat && ((Boolean)this.bat.getValue()).booleanValue()) {
                c = new Color(Colors.BLUE.c);
                col = new Color(c.getRed(), c.getGreen(), c.getBlue());
                if (!this.checked.keySet().contains((EntityLivingBase)entity)) {
                    this.checked.put((EntityLivingBase)entity, col);
                    RenderUtil.drawFilledESP(entity, col, event);
                }
            }
            if (entity instanceof EntityDragon && ((Boolean)this.dragon.getValue()).booleanValue() && !Client.inDungeons) {
                c = new Color(Colors.YELLOW.c);
                col = new Color(c.getRed(), c.getGreen(), c.getBlue());
                RenderUtil.drawFilledESP(entity, col, event);
            }
            if (entity instanceof EntityWither && ((Boolean)this.necron.getValue()).booleanValue() && !entity.isInvisible() && Client.inDungeons) {
                c = FadeUtil.PURPLE.getColor();
                col = new Color(c.getRed(), c.getGreen(), c.getBlue());
                RenderUtil.drawFilledESP(entity, col, event);
            }
            if (((Boolean)this.farming.getValue()).booleanValue() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.The_Farming_Island && entity.hasCustomName() && this.isEndangeredAnimal(name = entity.getCustomNameTag().toLowerCase()) != null && this.isEndangeredAnimal(name) != EAnimalTypes.NONE && !(mobs = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(0.0, 2.0, 0.0), this::lambda$onRender3D$0)).isEmpty() && mobs.get(0) instanceof EntityLivingBase && !this.checked.keySet().contains((EntityLivingBase)mobs.get(0))) {
                Entity shabi = mobs.get(0);
                switch (this.isEndangeredAnimal(name).toString()) {
                    case "Trackable": {
                        Color col2 = FadeUtil.PURPLE.getColor();
                        RenderUtil.drawFilledESP(shabi, col2, event, 2.3f);
                        break;
                    }
                    case "Untrackable": {
                        Color col2 = FadeUtil.BLUE.getColor();
                        RenderUtil.drawFilledESP(shabi, col2, event, 2.3f);
                        break;
                    }
                    case "Undetected": {
                        Color col2 = FadeUtil.GREEN.getColor();
                        RenderUtil.drawFilledESP(shabi, col2, event, 2.3f);
                        break;
                    }
                    case "Endangered": {
                        Color col2 = FadeUtil.WHITE.getColor();
                        RenderUtil.drawFilledESP(shabi, col2, event, 2.3f);
                        break;
                    }
                    case "Elusive": {
                        Color col2 = FadeUtil.RED.getColor();
                        RenderUtil.drawFilledESP(shabi, col2, event, 2.3f);
                        break;
                    }
                }
            }
            if (!((Boolean)this.starMob.getValue()).booleanValue()) continue;
            Entity entity1 = entity;
            if (entity.hasCustomName() && entity.getCustomNameTag().contains("\u272f") && !(mobs = this.mc.theWorld.getEntitiesInAABBexcluding(entity1, entity1.getEntityBoundingBox().expand(0.0, 3.0, 0.0), this::lambda$onRender3D$1)).isEmpty()) {
                boolean isMiniBoss;
                boolean bl = isMiniBoss = entity.getName().toUpperCase().equals("SHADOW ASSASSIN") || entity.getName().toUpperCase().equals("LOST ADVENTURER") || entity.getName().toUpperCase().equals("DIAMOND GUY");
                if (entity != this.mc.thePlayer && !isMiniBoss) {
                    this.mobIDs.put(mobs.get(0).getEntityId(), new Color(135, 206, 250));
                    if (mobs.get(0) instanceof EntityLivingBase && !this.checked.keySet().contains((EntityLivingBase)mobs.get(0))) {
                        RenderUtil.drawFilledESP(mobs.get(0), new Color(135, 206, 250), event);
                    }
                }
            }
            if (entity instanceof EntityEnderman && entity.isInvisible()) {
                entity.setInvisible(false);
            }
            if (!(entity instanceof EntityPlayer)) continue;
            switch (entity.getName().toUpperCase()) {
                case "SHADOW ASSASSIN": {
                    entity.setInvisible(false);
                    this.mobIDs.put(entity.getEntityId(), new Color(Colors.RED.c));
                    if (this.checked.keySet().contains((EntityLivingBase)entity)) break;
                    RenderUtil.drawFilledESP(entity, new Color(Colors.RED.c), event);
                    break;
                }
                case "LOST ADVENTURER": {
                    this.mobIDs.put(entity.getEntityId(), new Color(Colors.GREEN.c));
                    if (this.checked.keySet().contains((EntityLivingBase)entity)) break;
                    RenderUtil.drawFilledESP(entity, new Color(Colors.GREEN.c), event);
                    break;
                }
                case "DIAMOND GUY": {
                    this.mobIDs.put(entity.getEntityId(), new Color(Colors.BLUE.c));
                    if (this.checked.keySet().contains((EntityLivingBase)entity)) break;
                    RenderUtil.drawFilledESP(entity, new Color(Colors.BLUE.c), event);
                }
            }
        }
    }

    private EAnimalTypes isEndangeredAnimal(String name) {
        if (name.contains("trackable")) {
            return EAnimalTypes.Trackable;
        }
        if (name.contains("untrackable")) {
            return EAnimalTypes.Untrackable;
        }
        if (name.contains("undetected")) {
            return EAnimalTypes.Undetected;
        }
        if (name.contains("endangered")) {
            return EAnimalTypes.Endangered;
        }
        if (name.contains("elusive")) {
            return EAnimalTypes.Elusive;
        }
        return EAnimalTypes.NONE;
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load event) {
        this.checked.clear();
    }

    private boolean lambda$onRender3D$1(Entity e) {
        return !(e instanceof EntityArmorStand) && e != this.mc.thePlayer;
    }

    private boolean lambda$onRender3D$0(Entity e) {
        return !(e instanceof EntityArmorStand) && e != this.mc.thePlayer;
    }

    static enum EAnimalTypes {
        NONE,
        Trackable,
        Untrackable,
        Undetected,
        Endangered,
        Elusive;

    }
}

