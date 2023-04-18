/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import xyz.Melody.Event.Event;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPostUpdate;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.Module;

public class EventBus {
    private ConcurrentHashMap<Class<? extends Event>, List<Handler>> registry = new ConcurrentHashMap();
    private final Comparator<Handler> comparator = (handler, handler2) -> Byte.compare(((Handler)handler).priority, ((Handler)handler2).priority);
    private final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static final EventBus instance = new EventBus();

    public static EventBus getInstance() {
        return instance;
    }

    public void register(Object ... objectArray) {
        for (Object object : objectArray) {
            for (Method method : object.getClass().getDeclaredMethods()) {
                if (method.getParameterCount() != 1 || !method.isAnnotationPresent(EventHandler.class)) continue;
                Class<?> clazz = method.getParameterTypes()[0];
                if (!this.registry.containsKey(clazz)) {
                    this.registry.put(clazz, new CopyOnWriteArrayList());
                }
                this.registry.get(clazz).add(new Handler(method, object, method.getDeclaredAnnotation(EventHandler.class).priority()));
                this.registry.get(clazz).sort(this.comparator);
            }
        }
    }

    public void unregister(Object ... objectArray) {
        for (Object object : objectArray) {
            for (List<Handler> list : this.registry.values()) {
                for (Handler handler : list) {
                    if (handler.parent != object) continue;
                    list.remove(handler);
                }
            }
        }
    }

    public <E extends Event> E call(E e) {
        boolean bl = e instanceof EventTick || e instanceof EventPreUpdate || e instanceof EventPostUpdate;
        List<Handler> list = this.registry.get(e.getClass());
        if (list != null && !list.isEmpty()) {
            for (Handler handler : list) {
                try {
                    if (list instanceof Module) {
                        if (((Module)((Object)list)).isEnabled()) {
                            if (bl) {
                                Helper.mc.field_71424_I.func_76320_a(((Module)((Object)list)).getName());
                            }
                            if (bl) {
                                Helper.mc.field_71424_I.func_76319_b();
                            }
                        }
                    } else {
                        if (bl) {
                            Helper.mc.field_71424_I.func_76320_a("non module");
                        }
                        if (bl) {
                            Helper.mc.field_71424_I.func_76319_b();
                        }
                    }
                    handler.handler.invokeExact(handler.parent, e);
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return e;
    }

    private class Handler {
        private MethodHandle handler;
        private Object parent;
        private byte priority;

        public Handler(Method method, Object object, byte by) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            MethodHandle methodHandle = null;
            try {
                methodHandle = EventBus.this.lookup.unreflect(method);
            }
            catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
            if (methodHandle != null) {
                this.handler = methodHandle.asType(methodHandle.type().changeParameterType(0, Object.class).changeParameterType(1, Event.class));
            }
            this.parent = object;
            this.priority = by;
        }
    }
}

