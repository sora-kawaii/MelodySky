/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;

public final class GLUProjection {
    private static GLUProjection instance;
    private final FloatBuffer coords = BufferUtils.createFloatBuffer(3);
    private IntBuffer viewport;
    private FloatBuffer modelview;
    private FloatBuffer projection;
    private Vector3D frustumPos;
    private Vector3D[] frustum;
    private Vector3D[] invFrustum;
    private Vector3D viewVec;
    private double displayWidth;
    private double displayHeight;
    private double widthScale;
    private double heightScale;
    private double bra;
    private double bla;
    private double tra;
    private double tla;
    private Line tb;
    private Line bb;
    private Line lb;
    private Line rb;
    private float fovY;
    private float fovX;
    private Vector3D lookVec;

    public static GLUProjection getInstance() {
        if (instance == null) {
            instance = new GLUProjection();
        }
        return instance;
    }

    public void updateMatrices(IntBuffer intBuffer, FloatBuffer floatBuffer, FloatBuffer floatBuffer2, double d, double d2) {
        float f;
        this.viewport = intBuffer;
        this.modelview = floatBuffer;
        this.projection = floatBuffer2;
        this.widthScale = d;
        this.heightScale = d2;
        this.fovY = f = (float)Math.toDegrees(Math.atan(1.0 / (double)this.projection.get(5)) * 2.0);
        this.displayWidth = this.viewport.get(2);
        this.displayHeight = this.viewport.get(3);
        this.fovX = (float)Math.toDegrees(2.0 * Math.atan(this.displayWidth / this.displayHeight * Math.tan(Math.toRadians(this.fovY) / 2.0)));
        Vector3D vector3D = new Vector3D(this.modelview.get(0), this.modelview.get(1), this.modelview.get(2));
        Vector3D vector3D2 = new Vector3D(this.modelview.get(4), this.modelview.get(5), this.modelview.get(6));
        Vector3D vector3D3 = new Vector3D(this.modelview.get(8), this.modelview.get(9), this.modelview.get(10));
        Vector3D vector3D4 = new Vector3D(0.0, 1.0, 0.0);
        Vector3D vector3D5 = new Vector3D(1.0, 0.0, 0.0);
        double d3 = Math.toDegrees(Math.atan2(vector3D5.cross(vector3D).length(), vector3D5.dot(vector3D))) + 180.0;
        if (vector3D3.x < 0.0) {
            d3 = 360.0 - d3;
        }
        double d4 = 0.0;
        d4 = -vector3D3.y > 0.0 && d3 >= 90.0 && d3 < 270.0 || vector3D3.y > 0.0 && (d3 < 90.0 || d3 >= 270.0) ? Math.toDegrees(Math.atan2(vector3D4.cross(vector3D2).length(), vector3D4.dot(vector3D2))) : -Math.toDegrees(Math.atan2(vector3D4.cross(vector3D2).length(), vector3D4.dot(vector3D2)));
        this.lookVec = this.getRotationVector(d3, d4);
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.load(this.modelview.asReadOnlyBuffer());
        matrix4f.invert();
        this.frustumPos = new Vector3D(matrix4f.m30, matrix4f.m31, matrix4f.m32);
        this.frustum = this.getFrustum(this.frustumPos.x, this.frustumPos.y, this.frustumPos.z, d3, d4, f, 1.0, this.displayWidth / this.displayHeight);
        this.invFrustum = this.getFrustum(this.frustumPos.x, this.frustumPos.y, this.frustumPos.z, d3 - 180.0, -d4, f, 1.0, this.displayWidth / this.displayHeight);
        this.viewVec = this.getRotationVector(d3, d4).normalized();
        this.bra = Math.toDegrees(Math.acos(this.displayHeight * d2 / Math.sqrt(this.displayWidth * d * this.displayWidth * d + this.displayHeight * d2 * this.displayHeight * d2)));
        this.bla = 360.0 - this.bra;
        this.tra = this.bla - 180.0;
        this.tla = this.bra + 180.0;
        this.rb = new Line(this.displayWidth * this.widthScale, 0.0, 0.0, 0.0, 1.0, 0.0);
        this.tb = new Line(0.0, 0.0, 0.0, 1.0, 0.0, 0.0);
        this.lb = new Line(0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        this.bb = new Line(0.0, this.displayHeight * this.heightScale, 0.0, 1.0, 0.0, 0.0);
    }

    public Projection project(double d, double d2, double d3, ClampMode clampMode, boolean bl) {
        boolean bl2;
        if (this.viewport == null || this.modelview == null || this.projection == null) {
            return new Projection(0.0, 0.0, Projection.Type.FAIL);
        }
        Vector3D vector3D = new Vector3D(d, d2, d3);
        boolean[] blArray = this.doFrustumCheck(this.frustum, this.frustumPos, d, d2, d3);
        boolean bl3 = bl2 = blArray[0] || blArray[1] || blArray[2] || blArray[3];
        if (bl2) {
            boolean bl4;
            boolean bl5 = vector3D.sub(this.frustumPos).dot(this.viewVec) <= 0.0;
            boolean[] blArray2 = this.doFrustumCheck(this.invFrustum, this.frustumPos, d, d2, d3);
            boolean bl6 = bl4 = blArray2[0] || blArray2[1] || blArray2[2] || blArray2[3];
            if (bl && (!bl4 || bl4 && clampMode != ClampMode.NONE)) {
                if (bl && !bl4 || clampMode == ClampMode.DIRECT && bl4) {
                    double d4 = 0.0;
                    double d5 = 0.0;
                    if (!GLU.gluProject((float)d, (float)d2, (float)d3, this.modelview, this.projection, this.viewport, this.coords)) {
                        return new Projection(0.0, 0.0, Projection.Type.FAIL);
                    }
                    if (bl5) {
                        d4 = this.displayWidth * this.widthScale - (double)this.coords.get(0) * this.widthScale - this.displayWidth * this.widthScale / 2.0;
                        d5 = this.displayHeight * this.heightScale - (this.displayHeight - (double)this.coords.get(1)) * this.heightScale - this.displayHeight * this.heightScale / 2.0;
                    } else {
                        d4 = (double)this.coords.get(0) * this.widthScale - this.displayWidth * this.widthScale / 2.0;
                        d5 = (this.displayHeight - (double)this.coords.get(1)) * this.heightScale - this.displayHeight * this.heightScale / 2.0;
                    }
                    Vector3D vector3D2 = new Vector3D(d4, d5, 0.0).snormalize();
                    d4 = vector3D2.x;
                    d5 = vector3D2.y;
                    Line line = new Line(this.displayWidth * this.widthScale / 2.0, this.displayHeight * this.heightScale / 2.0, 0.0, d4, d5, 0.0);
                    double d6 = Math.toDegrees(Math.acos(vector3D2.y / Math.sqrt(vector3D2.x * vector3D2.x + vector3D2.y * vector3D2.y)));
                    if (d4 < 0.0) {
                        d6 = 360.0 - d6;
                    }
                    Vector3D vector3D3 = new Vector3D(0.0, 0.0, 0.0);
                    vector3D3 = d6 >= this.bra && d6 < this.tra ? this.rb.intersect(line) : (d6 >= this.tra && d6 < this.tla ? this.tb.intersect(line) : (d6 >= this.tla && d6 < this.bla ? this.lb.intersect(line) : this.bb.intersect(line)));
                    return new Projection(vector3D3.x, vector3D3.y, bl4 ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
                }
                if (clampMode != ClampMode.ORTHOGONAL || !bl4) {
                    return new Projection(0.0, 0.0, Projection.Type.FAIL);
                }
                if (!GLU.gluProject((float)d, (float)d2, (float)d3, this.modelview, this.projection, this.viewport, this.coords)) {
                    return new Projection(0.0, 0.0, Projection.Type.FAIL);
                }
                double d7 = (double)this.coords.get(0) * this.widthScale;
                double d8 = (this.displayHeight - (double)this.coords.get(1)) * this.heightScale;
                if (bl5) {
                    d7 = this.displayWidth * this.widthScale - d7;
                    d8 = this.displayHeight * this.heightScale - d8;
                }
                if (d7 < 0.0) {
                    d7 = 0.0;
                } else if (d7 > this.displayWidth * this.widthScale) {
                    d7 = this.displayWidth * this.widthScale;
                }
                if (d8 < 0.0) {
                    d8 = 0.0;
                    return new Projection(d7, d8, bl4 ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
                }
                if (d8 <= this.displayHeight * this.heightScale) {
                    return new Projection(d7, d8, bl4 ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
                }
                d8 = this.displayHeight * this.heightScale;
                return new Projection(d7, d8, bl4 ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
            }
            if (!GLU.gluProject((float)d, (float)d2, (float)d3, this.modelview, this.projection, this.viewport, this.coords)) {
                return new Projection(0.0, 0.0, Projection.Type.FAIL);
            }
            double d9 = (double)this.coords.get(0) * this.widthScale;
            double d10 = (this.displayHeight - (double)this.coords.get(1)) * this.heightScale;
            if (!bl5) {
                return new Projection(d9, d10, bl4 ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
            }
            d9 = this.displayWidth * this.widthScale - d9;
            d10 = this.displayHeight * this.heightScale - d10;
            return new Projection(d9, d10, bl4 ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
        }
        if (!GLU.gluProject((float)d, (float)d2, (float)d3, this.modelview, this.projection, this.viewport, this.coords)) {
            return new Projection(0.0, 0.0, Projection.Type.FAIL);
        }
        double d11 = (double)this.coords.get(0) * this.widthScale;
        double d12 = (this.displayHeight - (double)this.coords.get(1)) * this.heightScale;
        return new Projection(d11, d12, Projection.Type.INSIDE);
    }

    public boolean[] doFrustumCheck(Vector3D[] vector3DArray, Vector3D vector3D, double d, double d2, double d3) {
        Vector3D vector3D2 = new Vector3D(d, d2, d3);
        boolean bl = this.crossPlane(new Vector3D[]{vector3D, vector3DArray[3], vector3DArray[0]}, vector3D2);
        boolean bl2 = this.crossPlane(new Vector3D[]{vector3D, vector3DArray[0], vector3DArray[1]}, vector3D2);
        boolean bl3 = this.crossPlane(new Vector3D[]{vector3D, vector3DArray[1], vector3DArray[2]}, vector3D2);
        boolean bl4 = this.crossPlane(new Vector3D[]{vector3D, vector3DArray[2], vector3DArray[3]}, vector3D2);
        return new boolean[]{bl, bl2, bl3, bl4};
    }

    public boolean crossPlane(Vector3D[] vector3DArray, Vector3D vector3D) {
        Vector3D vector3D2 = new Vector3D(0.0, 0.0, 0.0);
        Vector3D vector3D3 = vector3DArray[1].sub(vector3DArray[0]);
        Vector3D vector3D4 = vector3DArray[2].sub(vector3DArray[0]);
        Vector3D vector3D5 = vector3D3.cross(vector3D4).snormalize();
        double d = vector3D2.sub(vector3D5).dot(vector3DArray[2]);
        double d2 = vector3D5.dot(vector3D) + d;
        return d2 >= 0.0;
    }

    public Vector3D[] getFrustum(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
        this.getRotationVector(d4, d5).snormalize();
        double d9 = 2.0 * Math.tan(Math.toRadians(d6 / 2.0)) * d7;
        double d10 = d9 * d8;
        Vector3D vector3D = this.getRotationVector(d4, d5).snormalize();
        Vector3D vector3D2 = this.getRotationVector(d4, d5 - 90.0).snormalize();
        Vector3D vector3D3 = this.getRotationVector(d4 + 90.0, 0.0).snormalize();
        Vector3D vector3D4 = new Vector3D(d, d2, d3);
        Vector3D vector3D5 = vector3D.add(vector3D4);
        Vector3D vector3D6 = new Vector3D(vector3D5.x * d7, vector3D5.y * d7, vector3D5.z * d7);
        Vector3D vector3D7 = new Vector3D(vector3D6.x + vector3D2.x * d9 / 2.0 - vector3D3.x * d10 / 2.0, vector3D6.y + vector3D2.y * d9 / 2.0 - vector3D3.y * d10 / 2.0, vector3D6.z + vector3D2.z * d9 / 2.0 - vector3D3.z * d10 / 2.0);
        Vector3D vector3D8 = new Vector3D(vector3D6.x - vector3D2.x * d9 / 2.0 - vector3D3.x * d10 / 2.0, vector3D6.y - vector3D2.y * d9 / 2.0 - vector3D3.y * d10 / 2.0, vector3D6.z - vector3D2.z * d9 / 2.0 - vector3D3.z * d10 / 2.0);
        Vector3D vector3D9 = new Vector3D(vector3D6.x + vector3D2.x * d9 / 2.0 + vector3D3.x * d10 / 2.0, vector3D6.y + vector3D2.y * d9 / 2.0 + vector3D3.y * d10 / 2.0, vector3D6.z + vector3D2.z * d9 / 2.0 + vector3D3.z * d10 / 2.0);
        Vector3D vector3D10 = new Vector3D(vector3D6.x - vector3D2.x * d9 / 2.0 + vector3D3.x * d10 / 2.0, vector3D6.y - vector3D2.y * d9 / 2.0 + vector3D3.y * d10 / 2.0, vector3D6.z - vector3D2.z * d9 / 2.0 + vector3D3.z * d10 / 2.0);
        return new Vector3D[]{vector3D7, vector3D8, vector3D10, vector3D9};
    }

    public Vector3D[] getFrustum() {
        return this.frustum;
    }

    public float getFovX() {
        return this.fovX;
    }

    public float getFovY() {
        return this.fovY;
    }

    public Vector3D getLookVector() {
        return this.lookVec;
    }

    public Vector3D getRotationVector(double d, double d2) {
        double d3 = Math.cos(-d * 0.01745329238474369 - Math.PI);
        double d4 = Math.sin(-d * 0.01745329238474369 - Math.PI);
        double d5 = -Math.cos(-d2 * 0.01745329238474369);
        double d6 = Math.sin(-d2 * 0.01745329238474369);
        return new Vector3D(d4 * d5, d6, d3 * d5);
    }

    public static class Vector3D {
        public double x;
        public double y;
        public double z;

        public Vector3D(double d, double d2, double d3) {
            this.x = d;
            this.y = d2;
            this.z = d3;
        }

        public Vector3D add(Vector3D vector3D) {
            return new Vector3D(this.x + vector3D.x, this.y + vector3D.y, this.z + vector3D.z);
        }

        public Vector3D add(double d, double d2, double d3) {
            return new Vector3D(this.x + d, this.y + d2, this.z + d3);
        }

        public Vector3D sub(Vector3D vector3D) {
            return new Vector3D(this.x - vector3D.x, this.y - vector3D.y, this.z - vector3D.z);
        }

        public Vector3D sub(double d, double d2, double d3) {
            return new Vector3D(this.x - d, this.y - d2, this.z - d3);
        }

        public Vector3D normalized() {
            double d = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
            return new Vector3D(this.x / d, this.y / d, this.z / d);
        }

        public double dot(Vector3D vector3D) {
            return this.x * vector3D.x + this.y * vector3D.y + this.z * vector3D.z;
        }

        public Vector3D cross(Vector3D vector3D) {
            return new Vector3D(this.y * vector3D.z - this.z * vector3D.y, this.z * vector3D.x - this.x * vector3D.z, this.x * vector3D.y - this.y * vector3D.x);
        }

        public Vector3D mul(double d) {
            return new Vector3D(this.x * d, this.y * d, this.z * d);
        }

        public Vector3D div(double d) {
            return new Vector3D(this.x / d, this.y / d, this.z / d);
        }

        public double length() {
            return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        }

        public Vector3D sadd(Vector3D vector3D) {
            this.x += vector3D.x;
            this.y += vector3D.y;
            this.z += vector3D.z;
            return this;
        }

        public Vector3D sadd(double d, double d2, double d3) {
            this.x += d;
            this.y += d2;
            this.z += d3;
            return this;
        }

        public Vector3D ssub(Vector3D vector3D) {
            this.x -= vector3D.x;
            this.y -= vector3D.y;
            this.z -= vector3D.z;
            return this;
        }

        public Vector3D ssub(double d, double d2, double d3) {
            this.x -= d;
            this.y -= d2;
            this.z -= d3;
            return this;
        }

        public Vector3D snormalize() {
            double d = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
            this.x /= d;
            this.y /= d;
            this.z /= d;
            return this;
        }

        public Vector3D scross(Vector3D vector3D) {
            this.x = this.y * vector3D.z - this.z * vector3D.y;
            this.y = this.z * vector3D.x - this.x * vector3D.z;
            this.z = this.x * vector3D.y - this.y * vector3D.x;
            return this;
        }

        public Vector3D smul(double d) {
            this.x *= d;
            this.y *= d;
            this.z *= d;
            return this;
        }

        public Vector3D sdiv(double d) {
            this.x /= d;
            this.y /= d;
            this.z /= d;
            return this;
        }

        public String toString() {
            return "(X: " + this.x + " Y: " + this.y + " Z: " + this.z + ")";
        }
    }

    public static class Projection {
        private final double x;
        private final double y;
        private final Type t;

        public Projection(double d, double d2, Type type) {
            this.x = d;
            this.y = d2;
            this.t = type;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public Type getType() {
            return this.t;
        }

        public boolean isType(Type type) {
            return this.t == type;
        }

        public static enum Type {
            INSIDE,
            OUTSIDE,
            INVERTED,
            FAIL;

        }
    }

    public static class Line {
        public Vector3D sourcePoint = new Vector3D(0.0, 0.0, 0.0);
        public Vector3D direction = new Vector3D(0.0, 0.0, 0.0);

        public Line(double d, double d2, double d3, double d4, double d5, double d6) {
            this.sourcePoint.x = d;
            this.sourcePoint.y = d2;
            this.sourcePoint.z = d3;
            this.direction.x = d4;
            this.direction.y = d5;
            this.direction.z = d6;
        }

        public Vector3D intersect(Line line) {
            double d = this.sourcePoint.x;
            double d2 = this.direction.x;
            double d3 = line.sourcePoint.x;
            double d4 = line.direction.x;
            double d5 = this.sourcePoint.y;
            double d6 = this.direction.y;
            double d7 = line.sourcePoint.y;
            double d8 = line.direction.y;
            double d9 = -d * d8 - d3 * d8 - d4 * (d5 - d7);
            double d10 = d2 * d8 - d4 * d6;
            if (d10 == 0.0) {
                return this.intersectXZ(line);
            }
            double d11 = d9 / d10;
            Vector3D vector3D = new Vector3D(0.0, 0.0, 0.0);
            vector3D.x = this.sourcePoint.x + this.direction.x * d11;
            vector3D.y = this.sourcePoint.y + this.direction.y * d11;
            vector3D.z = this.sourcePoint.z + this.direction.z * d11;
            return vector3D;
        }

        private Vector3D intersectXZ(Line line) {
            double d = this.sourcePoint.x;
            double d2 = this.direction.x;
            double d3 = line.sourcePoint.x;
            double d4 = line.direction.x;
            double d5 = this.sourcePoint.z;
            double d6 = this.direction.z;
            double d7 = line.sourcePoint.z;
            double d8 = line.direction.z;
            double d9 = -d * d8 - d3 * d8 - d4 * (d5 - d7);
            double d10 = d2 * d8 - d4 * d6;
            if (d10 == 0.0) {
                return this.intersectYZ(line);
            }
            double d11 = d9 / d10;
            Vector3D vector3D = new Vector3D(0.0, 0.0, 0.0);
            vector3D.x = this.sourcePoint.x + this.direction.x * d11;
            vector3D.y = this.sourcePoint.y + this.direction.y * d11;
            vector3D.z = this.sourcePoint.z + this.direction.z * d11;
            return vector3D;
        }

        private Vector3D intersectYZ(Line line) {
            double d = this.sourcePoint.y;
            double d2 = this.direction.y;
            double d3 = line.sourcePoint.y;
            double d4 = line.direction.y;
            double d5 = this.sourcePoint.z;
            double d6 = this.direction.z;
            double d7 = line.sourcePoint.z;
            double d8 = line.direction.z;
            double d9 = -d * d8 - d3 * d8 - d4 * (d5 - d7);
            double d10 = d2 * d8 - d4 * d6;
            if (d10 == 0.0) {
                return null;
            }
            double d11 = d9 / d10;
            Vector3D vector3D = new Vector3D(0.0, 0.0, 0.0);
            vector3D.x = this.sourcePoint.x + this.direction.x * d11;
            vector3D.y = this.sourcePoint.y + this.direction.y * d11;
            vector3D.z = this.sourcePoint.z + this.direction.z * d11;
            return vector3D;
        }

        public Vector3D intersectPlane(Vector3D vector3D, Vector3D vector3D2) {
            Vector3D vector3D3 = new Vector3D(this.sourcePoint.x, this.sourcePoint.y, this.sourcePoint.z);
            double d = vector3D.sub(this.sourcePoint).dot(vector3D2) / this.direction.dot(vector3D2);
            vector3D3.sadd(this.direction.mul(d));
            if (this.direction.dot(vector3D2) == 0.0) {
                return null;
            }
            return vector3D3;
        }
    }

    public static enum ClampMode {
        ORTHOGONAL,
        DIRECT,
        NONE;

    }
}

