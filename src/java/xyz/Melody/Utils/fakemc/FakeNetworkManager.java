/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.fakemc;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import java.net.SocketAddress;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;

public final class FakeNetworkManager
extends NetworkManager {
    public FakeNetworkManager(EnumPacketDirection enumPacketDirection) {
        super(enumPacketDirection);
    }

    public Channel channel() {
        return new Channel(){

            @Override
            public EventLoop eventLoop() {
                return null;
            }

            @Override
            public Channel parent() {
                return null;
            }

            @Override
            public ChannelConfig config() {
                return null;
            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public boolean isRegistered() {
                return false;
            }

            @Override
            public boolean isActive() {
                return false;
            }

            @Override
            public ChannelMetadata metadata() {
                return null;
            }

            @Override
            public SocketAddress localAddress() {
                return null;
            }

            @Override
            public SocketAddress remoteAddress() {
                return null;
            }

            @Override
            public ChannelFuture closeFuture() {
                return null;
            }

            @Override
            public boolean isWritable() {
                return false;
            }

            public long bytesBeforeUnwritable() {
                return 0L;
            }

            public long bytesBeforeWritable() {
                return 0L;
            }

            @Override
            public Channel.Unsafe unsafe() {
                return null;
            }

            @Override
            public ChannelPipeline pipeline() {
                return null;
            }

            @Override
            public ByteBufAllocator alloc() {
                return null;
            }

            @Override
            public ChannelPromise newPromise() {
                return null;
            }

            @Override
            public ChannelProgressivePromise newProgressivePromise() {
                return null;
            }

            @Override
            public ChannelFuture newSucceededFuture() {
                return null;
            }

            @Override
            public ChannelFuture newFailedFuture(Throwable throwable) {
                return null;
            }

            @Override
            public ChannelPromise voidPromise() {
                return null;
            }

            @Override
            public ChannelFuture bind(SocketAddress socketAddress) {
                return null;
            }

            @Override
            public ChannelFuture connect(SocketAddress socketAddress) {
                return null;
            }

            @Override
            public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress2) {
                return null;
            }

            @Override
            public ChannelFuture disconnect() {
                return null;
            }

            @Override
            public ChannelFuture close() {
                return null;
            }

            @Override
            public ChannelFuture deregister() {
                return null;
            }

            @Override
            public ChannelFuture bind(SocketAddress socketAddress, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture connect(SocketAddress socketAddress, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture disconnect(ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture close(ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture deregister(ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public Channel read() {
                return null;
            }

            @Override
            public ChannelFuture write(Object object) {
                return null;
            }

            @Override
            public ChannelFuture write(Object object, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public Channel flush() {
                return null;
            }

            @Override
            public ChannelFuture writeAndFlush(Object object, ChannelPromise channelPromise) {
                return null;
            }

            @Override
            public ChannelFuture writeAndFlush(Object object) {
                return null;
            }

            @Override
            public <T> Attribute<T> attr(AttributeKey<T> attributeKey) {
                return new Attribute<T>(this){
                    final I this$1;
                    {
                        this.this$1 = i;
                    }

                    public T setIfAbsent(T t) {
                        return null;
                    }

                    public T getAndSet(T t) {
                        return null;
                    }

                    public AttributeKey<T> key() {
                        return null;
                    }

                    public T getAndRemove() {
                        return null;
                    }

                    public void remove() {
                    }

                    public T get() {
                        return null;
                    }

                    public boolean compareAndSet(T t, T t2) {
                        return false;
                    }

                    public void set(T t) {
                    }
                };
            }

            public <T> boolean hasAttr(AttributeKey<T> attributeKey) {
                return false;
            }

            @Override
            public int compareTo(Channel channel) {
                return 0;
            }
        };
    }
}

