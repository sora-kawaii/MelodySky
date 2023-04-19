/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.System.Managers.Dungeons.DungeonTypes;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.other.TextUtils;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.FMLModules.Utils.HealthData;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class LividFinder
extends Module {
    private String realLividName;
    private String prefix;
    private EntityOtherPlayerMP realLivid;
    private EntityArmorStand lividStand;
    private final Set<String> knownLivids = new HashSet<String>();
    private boolean isMasterMode = false;
    private Option<Boolean> cc = new Option<Boolean>("CustomColor", false);
    private Numbers<Double> r = new Numbers<Double>("Red", 0.0, 0.0, 255.0, 1.0);
    private Numbers<Double> g = new Numbers<Double>("Green", 0.0, 0.0, 255.0, 1.0);
    private Numbers<Double> b = new Numbers<Double>("Blue", 0.0, 0.0, 255.0, 1.0);
    private Numbers<Double> a = new Numbers<Double>("Alpha", 0.0, 0.0, 255.0, 1.0);
    private static final Map<String, String> lividColorPrefix = new HashMap<String, String>(){
        private static final long serialVersionUID = 1L;
        {
            this.put("Vendetta", "\u00a7f");
            this.put("Crossed", "\u00a7d");
            this.put("Hockey", "\u00a7c");
            this.put("Doctor", "\u00a77");
            this.put("Frog", "\u00a72");
            this.put("Smile", "\u00a7a");
            this.put("Scream", "\u00a71");
            this.put("Purple", "\u00a75");
            this.put("Arcade", "\u00a7e");
        }
    };
    private Option<Boolean> tracer = new Option<Boolean>("Tracer", true);

    public LividFinder() {
        super("BoxLivid", new String[]{"la"}, ModuleType.Dungeons);
        this.addValues(this.tracer, this.cc, this.r, this.g, this.b, this.a);
        this.setModInfo("Create Correct Livid ESP(Tracer).");
    }

    @EventHandler
    private void tickDungeon(EventTick event) {
        DungeonTypes type = Client.instance.dungeonUtils.floor;
        if (!Client.inDungeons || type != DungeonTypes.F5 && type != DungeonTypes.M5) {
            this.knownLivids.clear();
            this.isMasterMode = false;
            this.lividStand = null;
            this.realLivid = null;
            this.realLividName = null;
            this.prefix = null;
            return;
        }
        this.isMasterMode = Client.isMMD;
    }

    @EventHandler
    private void onDraw(EventRender3D event) {
        EntityOtherPlayerMP playerMP;
        DungeonTypes type = Client.instance.dungeonUtils.floor;
        if ((type == DungeonTypes.F5 || type == DungeonTypes.M5) && (playerMP = this.realLivid) != null) {
            double posX = playerMP.lastTickPosX + (playerMP.posX - playerMP.lastTickPosX) * (double)event.getPartialTicks() - this.mc.getRenderManager().viewerPosX;
            double posY = playerMP.lastTickPosY + (playerMP.posY - playerMP.lastTickPosY) * (double)event.getPartialTicks() - this.mc.getRenderManager().viewerPosY;
            double posZ = playerMP.lastTickPosZ + (playerMP.posZ - playerMP.lastTickPosZ) * (double)event.getPartialTicks() - this.mc.getRenderManager().viewerPosZ;
            if (((Boolean)this.cc.getValue()).booleanValue()) {
                RenderUtil.entityOutlineAXIS(playerMP, new Color(((Double)this.r.getValue()).intValue(), ((Double)this.g.getValue()).intValue(), ((Double)this.b.getValue()).intValue(), ((Double)this.a.getValue()).intValue()).getRGB(), event);
            } else {
                RenderUtil.entityOutlineAXIS(playerMP, Colors.GREEN.c, event);
            }
            RenderUtil.startDrawing();
            this.drawLine(playerMP, new Color(Colors.WHITE.c), posX, posY, posZ);
            RenderUtil.stopDrawing();
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.knownLivids.clear();
        this.isMasterMode = false;
        this.lividStand = null;
        this.realLivid = null;
        this.realLividName = null;
        this.prefix = null;
        super.onDisable();
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent updateEvent) {
        DungeonTypes type = Client.instance.dungeonUtils.floor;
        if (!Client.inDungeons || type != DungeonTypes.F5 && type != DungeonTypes.M5) {
            this.knownLivids.clear();
            this.isMasterMode = false;
            this.lividStand = null;
            this.realLivid = null;
            this.realLividName = null;
            this.prefix = null;
            return;
        }
        if (updateEvent.entityLiving.getName().endsWith("Livid") && updateEvent.entityLiving instanceof EntityOtherPlayerMP) {
            if (!this.knownLivids.contains(updateEvent.entityLiving.getName())) {
                this.knownLivids.add(updateEvent.entityLiving.getName());
                this.realLividName = updateEvent.entityLiving.getName();
                this.realLivid = (EntityOtherPlayerMP)updateEvent.entityLiving;
                this.prefix = lividColorPrefix.get(this.realLividName.split(" ")[0]);
            } else if (this.realLividName != null && updateEvent.entityLiving != null && this.realLividName.equalsIgnoreCase(updateEvent.entityLiving.getName())) {
                this.realLivid = (EntityOtherPlayerMP)updateEvent.entityLiving;
            }
        } else if ((updateEvent.entityLiving.getName().startsWith(this.prefix + "\ufd3e ") || updateEvent.entityLiving.getName().startsWith(this.prefix + "\ufd3e ")) && updateEvent.entityLiving instanceof EntityArmorStand) {
            this.lividStand = (EntityArmorStand)updateEvent.entityLiving;
        }
    }

    public List<HealthData> getHealths() {
        ArrayList<HealthData> healths = new ArrayList<HealthData>();
        long health = 0L;
        if (this.lividStand != null) {
            try {
                String name = TextUtils.stripColor(this.lividStand.getName());
                String healthPart = name.split(" ")[2];
                health = TextUtils.reverseFormat(healthPart.substring(0, healthPart.length() - 1));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        healths.add(new HealthData(this.realLividName == null ? "unknown" : this.realLividName, (int)health, this.isMasterMode ? 600000000 : 7000000, true));
        return healths;
    }

    public String getBossName() {
        return this.realLividName == null ? "Livid" : this.realLividName;
    }

    private void drawLine(Entity entity, Color color, double x, double y, double z) {
        float distance = this.mc.thePlayer.getDistanceToEntity(entity);
        float xD = distance / 48.0f;
        if (xD >= 1.0f) {
            xD = 1.0f;
        }
        GlStateManager.resetColor();
        GL11.glEnable(2848);
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0, this.mc.thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.resetColor();
    }
}

