/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.Math;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.MathHelper;
import xyz.Melody.Performance.Math.FastTrig;
import xyz.Melody.injection.mixins.entity.EntityLookHelperAccessor;

public class FixedEntityLookHelper
extends EntityLookHelper {
    public FixedEntityLookHelper(EntityLiving entityLiving) {
        super(entityLiving);
    }

    @Override
    public void onUpdateLook() {
        ((EntityLookHelperAccessor)((Object)this)).getEntity().rotationPitch = 0.0f;
        if (((EntityLookHelperAccessor)((Object)this)).isLooking()) {
            ((EntityLookHelperAccessor)((Object)this)).setLooking(false);
            double d = ((EntityLookHelperAccessor)((Object)this)).getPosX() - ((EntityLookHelperAccessor)((Object)this)).getEntity().posX;
            double d2 = ((EntityLookHelperAccessor)((Object)this)).getPosY() - (((EntityLookHelperAccessor)((Object)this)).getEntity().posY + (double)((EntityLookHelperAccessor)((Object)this)).getEntity().getEyeHeight());
            double d3 = ((EntityLookHelperAccessor)((Object)this)).getPosZ() - ((EntityLookHelperAccessor)((Object)this)).getEntity().posZ;
            double d4 = MathHelper.sqrt_double(d * d + d3 * d3);
            float f = (float)((double)FixedEntityLookHelper.tan(d3, d) * 180.0 / Math.PI) - 90.0f;
            float f2 = (float)(-((double)FixedEntityLookHelper.tan(d2, d4) * 180.0 / Math.PI));
            ((EntityLookHelperAccessor)((Object)this)).getEntity().rotationPitch = this.updateRotation(((EntityLookHelperAccessor)((Object)this)).getEntity().rotationPitch, f2, ((EntityLookHelperAccessor)((Object)this)).getDeltaLookPitch());
            ((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead = this.updateRotation(((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead, f, ((EntityLookHelperAccessor)((Object)this)).getDeltaLookYaw());
        } else {
            ((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead = this.updateRotation(((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead, ((EntityLookHelperAccessor)((Object)this)).getEntity().renderYawOffset, 10.0f);
        }
        float f = MathHelper.wrapAngleTo180_float(((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead - ((EntityLookHelperAccessor)((Object)this)).getEntity().renderYawOffset);
        if (!((EntityLookHelperAccessor)((Object)this)).getEntity().getNavigator().noPath()) {
            if (f < -75.0f) {
                ((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead = ((EntityLookHelperAccessor)((Object)this)).getEntity().renderYawOffset - 75.0f;
            }
            if (f > 75.0f) {
                ((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead = ((EntityLookHelperAccessor)((Object)this)).getEntity().renderYawOffset + 75.0f;
            }
        }
    }

    private float updateRotation(float f, float f2, float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4;
    }

    public static float tan(double d, double d2) {
        return FastTrig.atan2(d, d2);
    }
}

