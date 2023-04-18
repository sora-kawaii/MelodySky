/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.math;

import xyz.Melody.Utils.math.Vec3f;
import xyz.Melody.Utils.render.gl.GLUtils;

public final class Vec2f {
    private float x;
    private float y;

    public Vec2f() {
        this(0.0f, 0.0f);
    }

    public Vec2f(Vec2f vec2f) {
        this(vec2f.x, vec2f.y);
    }

    public Vec2f(double d, double d2) {
        this((float)d, (float)d2);
    }

    public Vec2f(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public final Vec2f setX(float f) {
        this.x = f;
        return this;
    }

    public final Vec2f setY(float f) {
        this.y = f;
        return this;
    }

    public final float getX() {
        return this.x;
    }

    public final float getY() {
        return this.y;
    }

    public final Vec2f add(Vec2f vec2f) {
        return new Vec2f(this.x + vec2f.x, this.y + vec2f.y);
    }

    public final Vec2f add(double d, double d2) {
        return this.add(new Vec2f(d, d2));
    }

    public final Vec2f add(float f, float f2) {
        return this.add(new Vec2f(f, f2));
    }

    public final Vec2f sub(Vec2f vec2f) {
        return new Vec2f(this.x - vec2f.x, this.y - vec2f.y);
    }

    public final Vec2f sub(double d, double d2) {
        return this.sub(new Vec2f(d, d2));
    }

    public final Vec2f sub(float f, float f2) {
        return this.sub(new Vec2f(f, f2));
    }

    public final Vec2f scale(float f) {
        return new Vec2f(this.x * f, this.y * f);
    }

    public final Vec3f toVec3() {
        return new Vec3f(this.x, this.y, 0.0);
    }

    public final Vec2f copy() {
        return new Vec2f(this);
    }

    public final Vec2f transfer(Vec2f vec2f) {
        this.x = vec2f.x;
        this.y = vec2f.y;
        return this;
    }

    public final float distanceTo(Vec2f vec2f) {
        double d = this.x - vec2f.x;
        double d2 = this.y - vec2f.y;
        return (float)Math.sqrt(d * d + d2 * d2);
    }

    public final Vec3f toScreen() {
        return GLUtils.toWorld(this.toVec3());
    }

    public String toString() {
        return "Vec2{x=" + this.x + ", y=" + this.y + '}';
    }
}

