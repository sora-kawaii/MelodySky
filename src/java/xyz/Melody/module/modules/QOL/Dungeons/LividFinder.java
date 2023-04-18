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
    private void tickDungeon(EventTick eventTick) {
        DungeonTypes dungeonTypes = Client.instance.dungeonUtils.floor;
        if (!Client.inDungeons || dungeonTypes != DungeonTypes.F5 && dungeonTypes != DungeonTypes.M5) {
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
    private void onDraw(EventRender3D eventRender3D) {
        EntityOtherPlayerMP entityOtherPlayerMP;
        DungeonTypes dungeonTypes = Client.instance.dungeonUtils.floor;
        if ((dungeonTypes == DungeonTypes.F5 || dungeonTypes == DungeonTypes.M5) && (entityOtherPlayerMP = this.realLivid) != null) {
            double d = entityOtherPlayerMP.field_70142_S + (entityOtherPlayerMP.field_70165_t - entityOtherPlayerMP.field_70142_S) * (double)eventRender3D.getPartialTicks() - this.mc.func_175598_ae().field_78730_l;
            double d2 = entityOtherPlayerMP.field_70137_T + (entityOtherPlayerMP.field_70163_u - entityOtherPlayerMP.field_70137_T) * (double)eventRender3D.getPartialTicks() - this.mc.func_175598_ae().field_78731_m;
            double d3 = entityOtherPlayerMP.field_70136_U + (entityOtherPlayerMP.field_70161_v - entityOtherPlayerMP.field_70136_U) * (double)eventRender3D.getPartialTicks() - this.mc.func_175598_ae().field_78728_n;
            if (((Boolean)this.cc.getValue()).booleanValue()) {
                RenderUtil.entityOutlineAXIS(entityOtherPlayerMP, new Color(((Double)this.r.getValue()).intValue(), ((Double)this.g.getValue()).intValue(), ((Double)this.b.getValue()).intValue(), ((Double)this.a.getValue()).intValue()).getRGB(), eventRender3D);
            } else {
                RenderUtil.entityOutlineAXIS(entityOtherPlayerMP, Colors.GREEN.c, eventRender3D);
            }
            RenderUtil.startDrawing();
            this.drawLine(entityOtherPlayerMP, new Color(Colors.WHITE.c), d, d2, d3);
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
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent livingUpdateEvent) {
        DungeonTypes dungeonTypes = Client.instance.dungeonUtils.floor;
        if (!Client.inDungeons || dungeonTypes != DungeonTypes.F5 && dungeonTypes != DungeonTypes.M5) {
            this.knownLivids.clear();
            this.isMasterMode = false;
            this.lividStand = null;
            this.realLivid = null;
            this.realLividName = null;
            this.prefix = null;
            return;
        }
        if (livingUpdateEvent.entityLiving.func_70005_c_().endsWith("Livid") && livingUpdateEvent.entityLiving instanceof EntityOtherPlayerMP) {
            if (!this.knownLivids.contains(livingUpdateEvent.entityLiving.func_70005_c_())) {
                this.knownLivids.add(livingUpdateEvent.entityLiving.func_70005_c_());
                this.realLividName = livingUpdateEvent.entityLiving.func_70005_c_();
                this.realLivid = (EntityOtherPlayerMP)livingUpdateEvent.entityLiving;
                this.prefix = lividColorPrefix.get(this.realLividName.split(" ")[0]);
            } else if (this.realLividName != null && livingUpdateEvent.entityLiving != null && this.realLividName.equalsIgnoreCase(livingUpdateEvent.entityLiving.func_70005_c_())) {
                this.realLivid = (EntityOtherPlayerMP)livingUpdateEvent.entityLiving;
            }
        } else if ((livingUpdateEvent.entityLiving.func_70005_c_().startsWith(this.prefix + "\ufd3e ") || livingUpdateEvent.entityLiving.func_70005_c_().startsWith(this.prefix + "\ufd3e ")) && livingUpdateEvent.entityLiving instanceof EntityArmorStand) {
            this.lividStand = (EntityArmorStand)livingUpdateEvent.entityLiving;
        }
    }

    public List<HealthData> getHealths() {
        ArrayList<HealthData> arrayList = new ArrayList<HealthData>();
        long l2 = 0L;
        if (this.lividStand != null) {
            try {
                String string = TextUtils.stripColor(this.lividStand.func_70005_c_());
                String string2 = string.split(" ")[2];
                l2 = TextUtils.reverseFormat(string2.substring(0, string2.length() - 1));
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        arrayList.add(new HealthData(this.realLividName == null ? "unknown" : this.realLividName, (int)l2, this.isMasterMode ? 600000000 : 7000000, true));
        return arrayList;
    }

    public String getBossName() {
        return this.realLividName == null ? "Livid" : this.realLividName;
    }

    private void drawLine(Entity entity, Color color, double d, double d2, double d3) {
        float f = this.mc.field_71439_g.func_70032_d(entity);
        float f2 = f / 48.0f;
        if (f2 >= 1.0f) {
            f2 = 1.0f;
        }
        GlStateManager.func_179117_G();
        GL11.glEnable(2848);
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0, this.mc.field_71439_g.func_70047_e(), 0.0);
        GL11.glVertex3d(d, d2, d3);
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.func_179117_G();
    }
}

