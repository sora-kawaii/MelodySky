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
    public void onRenderEntityModel(EventRenderEntityModel eventRenderEntityModel) {
        if (((Boolean)this.starMob.getValue()).booleanValue()) {
            if (!this.mobIDs.isEmpty() && this.mobIDs.containsKey(eventRenderEntityModel.getEntity().getEntityId()) && !this.checked.containsKey(eventRenderEntityModel.getEntity())) {
                this.checked.put(eventRenderEntityModel.getEntity(), this.mobIDs.get(eventRenderEntityModel.getEntity().getEntityId()));
            }
            if (this.checked.containsKey(eventRenderEntityModel.getEntity())) {
                OutlineUtils.outlineEntity(eventRenderEntityModel, this.checked.get(eventRenderEntityModel.getEntity()));
            }
        }
    }

    @EventHandler
    private void onRender3D(EventRender3D eventRender3D) {
        this.mobIDs.clear();
        for (EntityLivingBase entity2 : this.checked.keySet()) {
            if (entity2 == null) {
                this.checked.remove(entity2);
                break;
            }
            if (!entity2.isEntityAlive()) {
                this.checked.remove(entity2);
                break;
            }
            if (!entity2.isDead) continue;
            this.checked.remove(entity2);
            break;
        }
        for (Entity entity2 : this.mc.theWorld.getLoadedEntityList()) {
            List<Entity> list;
            Object object;
            if (entity2 instanceof EntityArrow && ((Boolean)this.arrows.getValue()).booleanValue()) {
                object = new Color(Colors.WHITE.c);
                list = new Color(((Color)object).getRed(), ((Color)object).getGreen(), ((Color)object).getBlue());
                RenderUtil.drawFilledESP(entity2, (Color)((Object)list), eventRender3D);
            }
            if (entity2 instanceof EntityWither && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crimson_Island && ((Boolean)this.wither.getValue()).booleanValue() && !entity2.isInvisible()) {
                object = new Color(Colors.RED.c);
                list = new Color(((Color)object).getRed(), ((Color)object).getGreen(), ((Color)object).getBlue());
                RenderUtil.drawFilledESP(entity2, (Color)((Object)list), eventRender3D, 3.0f);
            }
            if (entity2 instanceof EntityCreeper && ScoreboardUtils.scoreboardContains("The Mist") && ((Boolean)this.ghost.getValue()).booleanValue()) {
                object = new Color(Colors.RED.c);
                list = new Color(((Color)object).getRed(), ((Color)object).getGreen(), ((Color)object).getBlue());
                if (!this.checked.keySet().contains((EntityLivingBase)entity2)) {
                    this.checked.put((EntityLivingBase)entity2, (Color)((Object)list));
                    RenderUtil.drawFilledESP(entity2, (Color)((Object)list), eventRender3D);
                }
            }
            if (entity2 instanceof EntityBat && ((Boolean)this.bat.getValue()).booleanValue()) {
                object = new Color(Colors.BLUE.c);
                list = new Color(((Color)object).getRed(), ((Color)object).getGreen(), ((Color)object).getBlue());
                if (!this.checked.keySet().contains((EntityLivingBase)entity2)) {
                    this.checked.put((EntityLivingBase)entity2, (Color)((Object)list));
                    RenderUtil.drawFilledESP(entity2, (Color)((Object)list), eventRender3D);
                }
            }
            if (entity2 instanceof EntityDragon && ((Boolean)this.dragon.getValue()).booleanValue() && !Client.inDungeons) {
                object = new Color(Colors.YELLOW.c);
                list = new Color(((Color)object).getRed(), ((Color)object).getGreen(), ((Color)object).getBlue());
                RenderUtil.drawFilledESP(entity2, (Color)((Object)list), eventRender3D);
            }
            if (entity2 instanceof EntityWither && ((Boolean)this.necron.getValue()).booleanValue() && !entity2.isInvisible() && Client.inDungeons) {
                object = FadeUtil.PURPLE.getColor();
                list = new Color(((Color)object).getRed(), ((Color)object).getGreen(), ((Color)object).getBlue());
                RenderUtil.drawFilledESP(entity2, (Color)((Object)list), eventRender3D);
            }
            if (((Boolean)this.farming.getValue()).booleanValue() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.The_Farming_Island && entity2.hasCustomName() && this.isEndangeredAnimal((String)(object = entity2.getCustomNameTag().toLowerCase())) != null && this.isEndangeredAnimal((String)object) != EAnimalTypes.NONE && !(list = this.mc.theWorld.getEntitiesInAABBexcluding(entity2, entity2.getEntityBoundingBox().expand(0.0, 2.0, 0.0), entity -> !(entity instanceof EntityArmorStand) && entity != this.mc.thePlayer)).isEmpty() && list.get(0) instanceof EntityLivingBase && !this.checked.keySet().contains((EntityLivingBase)list.get(0))) {
                Entity entity3 = list.get(0);
                switch (this.isEndangeredAnimal((String)object).toString()) {
                    case "Trackable": {
                        Color color = FadeUtil.PURPLE.getColor();
                        RenderUtil.drawFilledESP(entity3, color, eventRender3D, 2.3f);
                        break;
                    }
                    case "Untrackable": {
                        Color color = FadeUtil.BLUE.getColor();
                        RenderUtil.drawFilledESP(entity3, color, eventRender3D, 2.3f);
                        break;
                    }
                    case "Undetected": {
                        Color color = FadeUtil.GREEN.getColor();
                        RenderUtil.drawFilledESP(entity3, color, eventRender3D, 2.3f);
                        break;
                    }
                    case "Endangered": {
                        Color color = FadeUtil.WHITE.getColor();
                        RenderUtil.drawFilledESP(entity3, color, eventRender3D, 2.3f);
                        break;
                    }
                    case "Elusive": {
                        Color color = FadeUtil.RED.getColor();
                        RenderUtil.drawFilledESP(entity3, color, eventRender3D, 2.3f);
                        break;
                    }
                }
            }
            if (!((Boolean)this.starMob.getValue()).booleanValue()) continue;
            object = entity2;
            if (entity2.hasCustomName() && entity2.getCustomNameTag().contains("\u272f") && !(list = this.mc.theWorld.getEntitiesInAABBexcluding((Entity)object, ((Entity)object).getEntityBoundingBox().expand(0.0, 3.0, 0.0), entity -> !(entity instanceof EntityArmorStand) && entity != this.mc.thePlayer)).isEmpty()) {
                boolean bl;
                boolean bl2 = bl = entity2.getName().toUpperCase().equals("SHADOW ASSASSIN") || entity2.getName().toUpperCase().equals("LOST ADVENTURER") || entity2.getName().toUpperCase().equals("DIAMOND GUY");
                if (entity2 != this.mc.thePlayer && !bl) {
                    this.mobIDs.put(list.get(0).getEntityId(), new Color(135, 206, 250));
                    if (list.get(0) instanceof EntityLivingBase && !this.checked.keySet().contains((EntityLivingBase)list.get(0))) {
                        RenderUtil.drawFilledESP(list.get(0), new Color(135, 206, 250), eventRender3D);
                    }
                }
            }
            if (entity2 instanceof EntityEnderman && entity2.isInvisible()) {
                entity2.setInvisible(false);
            }
            if (!(entity2 instanceof EntityPlayer)) continue;
            switch (entity2.getName().toUpperCase()) {
                case "SHADOW ASSASSIN": {
                    entity2.setInvisible(false);
                    this.mobIDs.put(entity2.getEntityId(), new Color(Colors.RED.c));
                    if (this.checked.keySet().contains((EntityLivingBase)entity2)) break;
                    RenderUtil.drawFilledESP(entity2, new Color(Colors.RED.c), eventRender3D);
                    break;
                }
                case "LOST ADVENTURER": {
                    this.mobIDs.put(entity2.getEntityId(), new Color(Colors.GREEN.c));
                    if (this.checked.keySet().contains((EntityLivingBase)entity2)) break;
                    RenderUtil.drawFilledESP(entity2, new Color(Colors.GREEN.c), eventRender3D);
                    break;
                }
                case "DIAMOND GUY": {
                    this.mobIDs.put(entity2.getEntityId(), new Color(Colors.BLUE.c));
                    if (this.checked.keySet().contains((EntityLivingBase)entity2)) break;
                    RenderUtil.drawFilledESP(entity2, new Color(Colors.BLUE.c), eventRender3D);
                }
            }
        }
    }

    private EAnimalTypes isEndangeredAnimal(String string) {
        if (string.contains("trackable")) {
            return EAnimalTypes.Trackable;
        }
        if (string.contains("untrackable")) {
            return EAnimalTypes.Untrackable;
        }
        if (string.contains("undetected")) {
            return EAnimalTypes.Undetected;
        }
        if (string.contains("endangered")) {
            return EAnimalTypes.Endangered;
        }
        if (string.contains("elusive")) {
            return EAnimalTypes.Elusive;
        }
        return EAnimalTypes.NONE;
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        this.checked.clear();
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

