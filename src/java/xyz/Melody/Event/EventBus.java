package xyz.Melody.Event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import xyz.Melody.Event.events.world.EventPostUpdate;
import xyz.Melody.Event.events.world.EventPreUpdate;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.Module;

public class EventBus {
   private ConcurrentHashMap registry = new ConcurrentHashMap();
   private final Comparator comparator = (h, h1) -> {
      return Byte.compare(h.priority, h1.priority);
   };
   private final MethodHandles.Lookup lookup = MethodHandles.lookup();
   private static final EventBus instance = new EventBus();

   public static EventBus getInstance() {
      return instance;
   }

   public void register(Object... objs) {
      Object[] arrobject = objs;
      int n = objs.length;

      for(int n2 = 0; n2 < n; ++n2) {
         Object obj = arrobject[n2];
         Method[] arrmethod = obj.getClass().getDeclaredMethods();
         int n3 = arrmethod.length;

         for(int n4 = 0; n4 < n3; ++n4) {
            Method m = arrmethod[n4];
            if (m.getParameterCount() == 1 && m.isAnnotationPresent(EventHandler.class)) {
               Class eventClass = m.getParameterTypes()[0];
               if (!this.registry.containsKey(eventClass)) {
                  this.registry.put(eventClass, new CopyOnWriteArrayList());
               }

               ((List)this.registry.get(eventClass)).add(new Handler(m, obj, ((EventHandler)m.getDeclaredAnnotation(EventHandler.class)).priority()));
               ((List)this.registry.get(eventClass)).sort(this.comparator);
            }
         }
      }

   }

   public void unregister(Object... objs) {
      Object[] arrobject = objs;
      int n = objs.length;

      for(int n2 = 0; n2 < n; ++n2) {
         Object obj = arrobject[n2];
         Iterator var6 = this.registry.values().iterator();

         while(var6.hasNext()) {
            List list = (List)var6.next();
            Iterator var8 = list.iterator();

            while(var8.hasNext()) {
               Handler data = (Handler)var8.next();
               if (data.parent == obj) {
                  list.remove(data);
               }
            }
         }
      }

   }

   public Event call(Event event) {
      boolean whiteListedEvents = event instanceof EventTick || event instanceof EventPreUpdate || event instanceof EventPostUpdate;
      List list = (List)this.registry.get(event.getClass());
      if (list != null && !list.isEmpty()) {
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            Handler data = (Handler)var4.next();

            try {
               if (list instanceof Module) {
                  if (((Module)list).isEnabled()) {
                     if (whiteListedEvents) {
                        Helper.mc.mcProfiler.startSection(((Module)list).getName());
                     }

                     if (whiteListedEvents) {
                        Helper.mc.mcProfiler.endSection();
                     }
                  }
               } else {
                  if (whiteListedEvents) {
                     Helper.mc.mcProfiler.startSection("non module");
                  }

                  if (whiteListedEvents) {
                     Helper.mc.mcProfiler.endSection();
                  }
               }

               data.handler.invokeExact(data.parent, event);
            } catch (Throwable var7) {
               var7.printStackTrace();
            }
         }
      }

      return event;
   }

   private class Handler {
      private MethodHandle handler;
      private Object parent;
      private byte priority;

      public Handler(Method method, Object parent, byte priority) {
         if (!method.isAccessible()) {
            method.setAccessible(true);
         }

         MethodHandle m = null;

         try {
            m = EventBus.this.lookup.unreflect(method);
         } catch (IllegalAccessException var7) {
            var7.printStackTrace();
         }

         if (m != null) {
            this.handler = m.asType(m.type().changeParameterType(0, Object.class).changeParameterType(1, Event.class));
         }

         this.parent = parent;
         this.priority = priority;
      }
   }
}
