/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.fakemc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public final class FakeGameProfile
extends GameProfile {
    private UUID id;
    private String name;
    private PropertyMap properties = new PropertyMap();
    private boolean legacy;

    public FakeGameProfile(UUID uUID, String string) {
        super(uUID, string);
        if (uUID == null && StringUtils.isBlank(string)) {
            throw new IllegalArgumentException("Name and ID cannot both be blank");
        }
        this.id = uUID;
        this.name = string;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public PropertyMap getProperties() {
        return this.properties;
    }

    @Override
    public boolean isComplete() {
        return this.id != null && StringUtils.isNotBlank(this.getName());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        GameProfile gameProfile = (GameProfile)object;
        if (this.id != null ? !this.id.equals(gameProfile.getId()) : gameProfile.getId() != null) {
            return false;
        }
        return !(this.name != null ? !this.name.equals(gameProfile.getName()) : gameProfile.getName() != null);
    }

    @Override
    public int hashCode() {
        int n = this.id != null ? this.id.hashCode() : 0;
        n = 31 * n + (this.name != null ? this.name.hashCode() : 0);
        return n;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", this.id).append("name", this.name).append("properties", this.properties).append("legacy", this.legacy).toString();
    }

    @Override
    public boolean isLegacy() {
        return this.legacy;
    }
}

