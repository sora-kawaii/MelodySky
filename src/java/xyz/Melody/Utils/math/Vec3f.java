/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.math;

import xyz.Melody.Utils.math.Vec2f;
import xyz.Melody.Utils.render.gl.GLUtils;

public final class Vec3f {
    private double x;
    private double y;
    private double z;

    public Vec3f() {
        this(0.0, 0.0, 0.0);
    }

    public Vec3f(Vec3f vec3f) {
        this(vec3f.x, vec3f.y, vec3f.z);
    }

    public Vec3f(double d, double d2, double d3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
    }

    public final Vec3f setX(double d) {
        this.x = d;
        return this;
    }

    public final Vec3f setY(double d) {
        this.y = d;
        return this;
    }

    public final Vec3f setZ(double d) {
        this.z = d;
        return this;
    }

    public final double getX() {
        return this.x;
    }

    public final double getY() {
        return this.y;
    }

    public final double getZ() {
        return this.z;
    }

    public final Vec3f add(Vec3f vec3f) {
        return this.add(vec3f.x, vec3f.y, vec3f.z);
    }

    public final Vec3f add(double d, double d2, double d3) {
        return new Vec3f(this.x + d, this.y + d2, this.z + d3);
    }

    public final Vec3f sub(Vec3f vec3f) {
        return new Vec3f(this.x - vec3f.x, this.y - vec3f.y, this.z - vec3f.z);
    }

    public final Vec3f sub(double d, double d2, double d3) {
        return new Vec3f(this.x - d, this.y - d2, this.z - d3);
    }

    public final Vec3f scale(float f) {
        return new Vec3f(this.x * (double)f, this.y * (double)f, this.z * (double)f);
    }

    public final Vec3f copy() {
        return new Vec3f(this);
    }

    public final Vec3f transfer(Vec3f vec3f) {
        this.x = vec3f.x;
        this.y = vec3f.y;
        this.z = vec3f.z;
        return this;
    }

    public final double distanceTo(Vec3f vec3f) {
        double d = this.x - vec3f.x;
        double d2 = this.y - vec3f.y;
        double d3 = this.z - vec3f.z;
        return Math.sqrt(d * d + d2 * d2 + d3 * d3);
    }

    public final Vec2f rotationsTo(Vec3f vec3f) {
        double[] dArray = new double[]{vec3f.x - this.x, vec3f.y - this.y, vec3f.z - this.z};
        double d = Math.sqrt(dArray[0] * dArray[0] + dArray[2] * dArray[2]);
        return new Vec2f(Math.toDegrees(Math.atan2(dArray[2], dArray[0])) - 90.0, -Math.toDegrees(Math.atan2(dArray[1], d)));
    }

    public final Vec3f toScreen() {
        return GLUtils.toScreen(this);
    }

    public String toString() {
        return "Vec3{x=" + this.x + ", y=" + this.y + ", z=" + this.z + '}';
    }
}

