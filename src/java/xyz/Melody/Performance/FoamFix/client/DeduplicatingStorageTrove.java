/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.client;

import gnu.trove.set.hash.TCustomHashSet;
import gnu.trove.strategy.HashingStrategy;
import xyz.Melody.Performance.FoamFix.client.IDeduplicatingStorage;

public final class DeduplicatingStorageTrove<T>
extends TCustomHashSet<T>
implements IDeduplicatingStorage<T> {
    public DeduplicatingStorageTrove(HashingStrategy<T> strategy) {
        super(strategy);
    }

    @Override
    public T deduplicate(T o) {
        int i = this.index(o);
        if (i >= 0) {
            return (T)this._set[i];
        }
        this.add(o);
        return o;
    }
}

