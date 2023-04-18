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
    public DeduplicatingStorageTrove(HashingStrategy<T> hashingStrategy) {
        super(hashingStrategy);
    }

    @Override
    public T deduplicate(T t) {
        int n = this.index(t);
        if (n >= 0) {
            return (T)this._set[n];
        }
        this.add(t);
        return t;
    }
}

