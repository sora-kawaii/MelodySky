/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.ClickNew;

public final class AnimationUtil {
    private static float defaultSpeed = 0.125f;

    public static float mvoeUD(float current, float end, float minSpeed) {
        return AnimationUtil.moveUD(current, end, defaultSpeed, minSpeed);
    }

    public static float moveUD(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;
        if (movement > 0.0f) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0.0f) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return current + movement;
    }

    public static float calculateCompensation(float target, float current, long delta, float speed) {
        float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (diff > speed) {
            double xD = (double)(speed * (float)delta / 16.0f) < 0.25 ? 0.5 : (double)(speed * (float)delta / 16.0f);
            if ((current -= (float)xD) < target) {
                current = target;
            }
        } else if (diff < -speed) {
            double xD = (double)(speed * (float)delta / 16.0f) < 0.25 ? 0.5 : (double)(speed * (float)delta / 16.0f);
            if ((current = (float)((double)current + xD)) > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }
}

