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
    public FixedEntityLookHelper(EntityLiving entity) {
        super(entity);
    }

    @Override
    public void onUpdateLook() {
        ((EntityLookHelperAccessor)((Object)this)).getEntity().rotationPitch = 0.0f;
        if (((EntityLookHelperAccessor)((Object)this)).isLooking()) {
            ((EntityLookHelperAccessor)((Object)this)).setLooking(false);
            double d0 = ((EntityLookHelperAccessor)((Object)this)).getPosX() - ((EntityLookHelperAccessor)((Object)this)).getEntity().posX;
            double d1 = ((EntityLookHelperAccessor)((Object)this)).getPosY() - (((EntityLookHelperAccessor)((Object)this)).getEntity().posY + (double)((EntityLookHelperAccessor)((Object)this)).getEntity().getEyeHeight());
            double d2 = ((EntityLookHelperAccessor)((Object)this)).getPosZ() - ((EntityLookHelperAccessor)((Object)this)).getEntity().posZ;
            double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
            float f = (float)((double)FixedEntityLookHelper.tan(d2, d0) * 180.0 / Math.PI) - 90.0f;
            float f1 = (float)(-((double)FixedEntityLookHelper.tan(d1, d3) * 180.0 / Math.PI));
            ((EntityLookHelperAccessor)((Object)this)).getEntity().rotationPitch = this.updateRotation(((EntityLookHelperAccessor)((Object)this)).getEntity().rotationPitch, f1, ((EntityLookHelperAccessor)((Object)this)).getDeltaLookPitch());
            ((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead = this.updateRotation(((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead, f, ((EntityLookHelperAccessor)((Object)this)).getDeltaLookYaw());
        } else {
            ((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead = this.updateRotation(((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead, ((EntityLookHelperAccessor)((Object)this)).getEntity().renderYawOffset, 10.0f);
        }
        float f2 = MathHelper.wrapAngleTo180_float(((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead - ((EntityLookHelperAccessor)((Object)this)).getEntity().renderYawOffset);
        if (!((EntityLookHelperAccessor)((Object)this)).getEntity().getNavigator().noPath()) {
            if (f2 < -75.0f) {
                ((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead = ((EntityLookHelperAccessor)((Object)this)).getEntity().renderYawOffset - 75.0f;
            }
            if (f2 > 75.0f) {
                ((EntityLookHelperAccessor)((Object)this)).getEntity().rotationYawHead = ((EntityLookHelperAccessor)((Object)this)).getEntity().renderYawOffset + 75.0f;
            }
        }
    }

    private float updateRotation(float a, float b, float c) {
        float f = MathHelper.wrapAngleTo180_float(b - a);
        if (f > c) {
            f = c;
        }
        if (f < -c) {
            f = -c;
        }
        return a + f;
    }

    public static float tan(double a, double b) {
        return FastTrig.atan2(a, b);
    }
}

