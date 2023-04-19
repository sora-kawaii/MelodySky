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

    public FakeGameProfile(UUID id, String name) {
        super(id, name);
        if (id == null && StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name and ID cannot both be blank");
        }
        this.id = id;
        this.name = name;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        GameProfile that = (GameProfile)o;
        if (this.id != null ? !this.id.equals(that.getId()) : that.getId() != null) {
            return false;
        }
        return !(this.name != null ? !this.name.equals(that.getName()) : that.getName() != null);
    }

    @Override
    public int hashCode() {
        int result = this.id != null ? this.id.hashCode() : 0;
        result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
        return result;
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

