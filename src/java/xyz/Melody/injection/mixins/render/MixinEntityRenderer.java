/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.render;

import com.google.common.base.Predicates;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.module.balance.Reach;
import xyz.Melody.module.modules.others.MotionBlur;
import xyz.Melody.module.modules.render.Cam;

@SideOnly(value=Side.CLIENT)
@Mixin(value={EntityRenderer.class})
public abstract class MixinEntityRenderer {
    @Shadow
    private Entity field_78528_u;
    @Shadow
    private ShaderGroup field_147707_d;
    @Shadow
    private boolean field_175083_ad;
    @Shadow
    private Minecraft field_78531_r;
    @Shadow
    private float field_78491_C;
    @Shadow
    private float field_78490_B;
    @Shadow
    private boolean field_78500_U;

    @Inject(method="updateCameraAndRender", at={@At(value="RETURN")})
    private void postUpdateCameraAndRender(float f, long l2, CallbackInfo callbackInfo) {
        NotificationPublisher.publish(new ScaledResolution(this.field_78531_r));
    }

    @Inject(method="updateCameraAndRender", at={@At(value="INVOKE", target="Lnet/minecraft/client/shader/Framebuffer;bindFramebuffer(Z)V", shift=At.Shift.BEFORE)})
    public void updateCameraAndRender(float f, long l2, CallbackInfo callbackInfo) {
        ArrayList<ShaderGroup> arrayList = new ArrayList<ShaderGroup>();
        if (this.field_147707_d != null && this.field_175083_ad) {
            arrayList.add(this.field_147707_d);
        }
        MotionBlur motionBlur = (MotionBlur)Client.instance.getModuleManager().getModuleByClass(MotionBlur.class);
        ShaderGroup shaderGroup = motionBlur.getShader();
        if (motionBlur.isEnabled()) {
            if (shaderGroup != null) {
                arrayList.add(shaderGroup);
            }
            for (ShaderGroup shaderGroup2 : arrayList) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179096_D();
                shaderGroup2.func_148018_a(f);
                GlStateManager.func_179121_F();
            }
        }
    }

    @Inject(method="updateShaderGroupSize", at={@At(value="RETURN")})
    public void updateShaderGroupSize(int n, int n2, CallbackInfo callbackInfo) {
        if (this.field_78531_r.field_71441_e != null) {
            ShaderGroup shaderGroup;
            MotionBlur motionBlur = (MotionBlur)Client.instance.getModuleManager().getModuleByClass(MotionBlur.class);
            if (OpenGlHelper.field_148824_g && (shaderGroup = motionBlur.getShader()) != null) {
                shaderGroup.func_148026_a(n, n2);
            }
        }
    }

    @Inject(method="renderWorldPass", at={@At(value="FIELD", target="Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift=At.Shift.BEFORE)})
    private void renderWorldPass(int n, float f, long l2, CallbackInfo callbackInfo) {
        EventRender3D eventRender3D = new EventRender3D(f);
        EventBus.getInstance().call(eventRender3D);
    }

    @Inject(method="hurtCameraEffect", at={@At(value="HEAD")}, cancellable=true)
    private void injectHurtCameraEffect(CallbackInfo callbackInfo) {
        Cam cam = (Cam)Client.instance.getModuleManager().getModuleByClass(Cam.class);
        if (cam.isEnabled() && ((Boolean)cam.bht.getValue()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method="orientCamera", at={@At(value="INVOKE", target="Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D")}, cancellable=true)
    private void cameraClip(float f, CallbackInfo callbackInfo) {
        Cam cam = (Cam)Client.instance.getModuleManager().getModuleByClass(Cam.class);
        if (cam.isEnabled() && ((Boolean)cam.noClip.getValue()).booleanValue()) {
            float f2;
            callbackInfo.cancel();
            Entity entity = this.field_78531_r.func_175606_aa();
            float f3 = entity.func_70047_e();
            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).func_70608_bn()) {
                f3 = (float)((double)f3 + 1.0);
                GlStateManager.func_179109_b((float)0.0f, (float)0.3f, (float)0.0f);
                if (!this.field_78531_r.field_71474_y.field_74325_U) {
                    BlockPos blockPos = new BlockPos(entity);
                    IBlockState iBlockState = this.field_78531_r.field_71441_e.func_180495_p(blockPos);
                    ForgeHooksClient.orientBedCamera(this.field_78531_r.field_71441_e, blockPos, iBlockState, entity);
                    GlStateManager.func_179114_b((float)(entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * f + 180.0f), (float)0.0f, (float)-1.0f, (float)0.0f);
                    GlStateManager.func_179114_b((float)(entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * f), (float)-1.0f, (float)0.0f, (float)0.0f);
                }
            } else if (this.field_78531_r.field_71474_y.field_74320_O > 0) {
                double d = this.field_78491_C + (this.field_78490_B - this.field_78491_C) * f;
                if (this.field_78531_r.field_71474_y.field_74325_U) {
                    GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)((float)(-d)));
                } else {
                    f2 = entity.field_70177_z;
                    float f4 = entity.field_70125_A;
                    if (this.field_78531_r.field_71474_y.field_74320_O == 2) {
                        f4 += 180.0f;
                    }
                    if (this.field_78531_r.field_71474_y.field_74320_O == 2) {
                        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                    }
                    GlStateManager.func_179114_b((float)(entity.field_70125_A - f4), (float)1.0f, (float)0.0f, (float)0.0f);
                    GlStateManager.func_179114_b((float)(entity.field_70177_z - f2), (float)0.0f, (float)1.0f, (float)0.0f);
                    GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)((float)(-d)));
                    GlStateManager.func_179114_b((float)(f2 - entity.field_70177_z), (float)0.0f, (float)1.0f, (float)0.0f);
                    GlStateManager.func_179114_b((float)(f4 - entity.field_70125_A), (float)1.0f, (float)0.0f, (float)0.0f);
                }
            } else {
                GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)-0.1f);
            }
            if (!this.field_78531_r.field_71474_y.field_74325_U) {
                float f5 = entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * f + 180.0f;
                float f6 = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * f;
                f2 = 0.0f;
                if (entity instanceof EntityAnimal) {
                    EntityAnimal entityAnimal = (EntityAnimal)entity;
                    f5 = entityAnimal.field_70758_at + (entityAnimal.field_70759_as - entityAnimal.field_70758_at) * f + 180.0f;
                }
                Block block = ActiveRenderInfo.func_180786_a((World)this.field_78531_r.field_71441_e, (Entity)entity, (float)f);
                EntityViewRenderEvent.CameraSetup cameraSetup = new EntityViewRenderEvent.CameraSetup((EntityRenderer)((Object)this), entity, block, f, f5, f6, f2);
                MinecraftForge.EVENT_BUS.post(cameraSetup);
                GlStateManager.func_179114_b((float)cameraSetup.roll, (float)0.0f, (float)0.0f, (float)1.0f);
                GlStateManager.func_179114_b((float)cameraSetup.pitch, (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.func_179114_b((float)cameraSetup.yaw, (float)0.0f, (float)1.0f, (float)0.0f);
            }
            GlStateManager.func_179109_b((float)0.0f, (float)(-f3), (float)0.0f);
            double d = entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * (double)f;
            double d2 = entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * (double)f + (double)f3;
            double d3 = entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * (double)f;
            this.field_78500_U = this.field_78531_r.field_71438_f.func_72721_a(d, d2, d3, f);
        }
    }

    @Inject(method="getMouseOver", at={@At(value="HEAD")}, cancellable=true)
    public void getMouseOver(float f, CallbackInfo callbackInfo) {
        Entity entity2 = this.field_78531_r.func_175606_aa();
        if (entity2 != null && this.field_78531_r.field_71441_e != null) {
            Object object;
            this.field_78531_r.field_71424_I.func_76320_a("pick");
            this.field_78531_r.field_147125_j = null;
            Reach reach = (Reach)Client.instance.getModuleManager().getModuleByClass(Reach.class);
            double d = reach.isEnabled() ? (Double)reach.size.getValue() : (double)this.field_78531_r.field_71442_b.func_78757_d();
            this.field_78531_r.field_71476_x = entity2.func_174822_a(reach.isEnabled() ? (Double)reach.size.getValue() : d, f);
            double d2 = d;
            Vec3 vec3 = entity2.func_174824_e(f);
            boolean bl = false;
            if (this.field_78531_r.field_71442_b.func_78749_i()) {
                d = 6.0;
                d2 = 6.0;
            } else if (d > 3.0) {
                bl = true;
            }
            if (this.field_78531_r.field_71476_x != null) {
                d2 = this.field_78531_r.field_71476_x.field_72307_f.func_72438_d(vec3);
            }
            if (reach.isEnabled() && (object = entity2.func_174822_a(d2 = ((Double)reach.size.getValue()).doubleValue(), f)) != null) {
                d2 = ((MovingObjectPosition)object).field_72307_f.func_72438_d(vec3);
            }
            object = entity2.func_70676_i(f);
            Vec3 vec32 = vec3.func_72441_c(((Vec3)object).field_72450_a * d, ((Vec3)object).field_72448_b * d, ((Vec3)object).field_72449_c * d);
            this.field_78528_u = null;
            Vec3 vec33 = null;
            float f2 = 1.0f;
            List list = this.field_78531_r.field_71441_e.func_175674_a(entity2, entity2.func_174813_aQ().func_72321_a(((Vec3)object).field_72450_a * d, ((Vec3)object).field_72448_b * d, ((Vec3)object).field_72449_c * d).func_72314_b(f2, f2, f2), Predicates.and(EntitySelectors.field_180132_d, entity -> entity.func_70067_L()));
            double d3 = d2;
            for (int i = 0; i < list.size(); ++i) {
                double d4;
                Entity entity3 = (Entity)list.get(i);
                float f3 = entity3.func_70111_Y();
                AxisAlignedBB axisAlignedBB = entity3.func_174813_aQ().func_72314_b(f3, f3, f3);
                MovingObjectPosition movingObjectPosition = axisAlignedBB.func_72327_a(vec3, vec32);
                if (axisAlignedBB.func_72318_a(vec3)) {
                    if (!(d3 >= 0.0)) continue;
                    this.field_78528_u = entity3;
                    vec33 = movingObjectPosition == null ? vec3 : movingObjectPosition.field_72307_f;
                    d3 = 0.0;
                    continue;
                }
                if (movingObjectPosition == null || !((d4 = vec3.func_72438_d(movingObjectPosition.field_72307_f)) < d3) && d3 != 0.0) continue;
                if (entity3 == entity2.field_70154_o && !entity2.canRiderInteract()) {
                    if (d3 != 0.0) continue;
                    this.field_78528_u = entity3;
                    vec33 = movingObjectPosition.field_72307_f;
                    continue;
                }
                this.field_78528_u = entity3;
                vec33 = movingObjectPosition.field_72307_f;
                d3 = d4;
            }
            if (this.field_78528_u != null && bl) {
                double d5 = vec3.func_72438_d(vec33);
                double d6 = reach.isEnabled() ? (Double)reach.size.getValue() : 3.0;
                if (d5 > d6) {
                    this.field_78528_u = null;
                    this.field_78531_r.field_71476_x = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
                }
            }
            if (this.field_78528_u != null && (d3 < d2 || this.field_78531_r.field_71476_x == null)) {
                this.field_78531_r.field_71476_x = new MovingObjectPosition(this.field_78528_u, vec33);
                if (this.field_78528_u instanceof EntityLivingBase || this.field_78528_u instanceof EntityItemFrame) {
                    this.field_78531_r.field_147125_j = this.field_78528_u;
                }
            }
            this.field_78531_r.field_71424_I.func_76319_b();
        }
        callbackInfo.cancel();
    }
}

