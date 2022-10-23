package xyz.Melody.System.Managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import xyz.Melody.Client;

public class FileManager {
   private static File dir;

   public static File getConfigFile(String name) {
      File file = new File(dir, String.format("%s.txt", name));
      if (!file.exists()) {
         try {
            file.createNewFile();
         } catch (IOException var3) {
         }
      }

      return file;
   }

   public static void init() {
      if (!dir.exists()) {
         Client.firstLaunch = true;
         Client.instance.logger.info("[Melody] [CONSOLE] Detected First Launch.");
         dir.mkdir();
      }

   }

   public static List read(String file) {
      List out = new ArrayList();

      try {
         if (!dir.exists()) {
            dir.mkdir();
         }

         File f = new File(dir, file);
         if (!f.exists()) {
            f.createNewFile();
         }

         Object t = null;
         boolean var20 = false;

         FileInputStream fis;
         label836: {
            String t3;
            ArrayList var43;
            try {
               label837: {
                  InputStreamReader isr;
                  label838: {
                     var20 = true;
                     fis = new FileInputStream(f);
                     boolean var26 = false;

                     try {
                        var26 = true;
                        isr = new InputStreamReader(fis);
                        boolean var32 = false;

                        BufferedReader t2;
                        try {
                           var32 = true;
                           t2 = new BufferedReader(isr);

                           try {
                              t3 = "";

                              while((t3 = t2.readLine()) != null) {
                                 out.add(t3);
                              }
                           } finally {
                              if (t2 != null) {
                                 t2.close();
                              }

                           }

                           if (isr != null) {
                              isr.close();
                              var32 = false;
                           } else {
                              var32 = false;
                           }
                        } finally {
                           if (var32) {
                              Object t2;
                              if (t == null) {
                                 t2 = null;
                                 t = t2;
                              } else {
                                 t2 = null;
                                 if (t != t2) {
                                    ((Throwable)t).addSuppressed((Throwable)t2);
                                 }
                              }

                              if (isr != null) {
                                 isr.close();
                              }

                           }
                        }

                        if (t == null) {
                           t2 = null;
                           t = t2;
                        } else {
                           t2 = null;
                           if (t != t2) {
                              ((Throwable)t).addSuppressed(t2);
                           }
                        }

                        if (isr != null) {
                           isr.close();
                        }

                        if (fis == null) {
                           var26 = false;
                           break label838;
                        }

                        fis.close();
                        var43 = out;
                        var26 = false;
                     } finally {
                        if (var26) {
                           Object t3;
                           if (t == null) {
                              t3 = null;
                              t = t3;
                           } else {
                              t3 = null;
                              if (t != t3) {
                                 ((Throwable)t).addSuppressed((Throwable)t3);
                              }
                           }

                           if (fis != null) {
                              fis.close();
                           }

                        }
                     }

                     if (t == null) {
                        t3 = null;
                        t = t3;
                     } else {
                        t3 = null;
                        if (t != t3) {
                           ((Throwable)t).addSuppressed(t3);
                        }
                     }

                     if (fis != null) {
                        fis.close();
                        var20 = false;
                     } else {
                        var20 = false;
                     }
                     break label837;
                  }

                  if (t == null) {
                     isr = null;
                     t = isr;
                  } else {
                     isr = null;
                     if (t != isr) {
                        ((Throwable)t).addSuppressed(isr);
                     }
                  }

                  if (fis != null) {
                     fis.close();
                     var20 = false;
                  } else {
                     var20 = false;
                  }
                  break label836;
               }
            } finally {
               if (var20) {
                  Object t4;
                  if (t == null) {
                     t4 = null;
                  } else {
                     t4 = null;
                     if (t != t4) {
                        ((Throwable)t).addSuppressed((Throwable)t4);
                     }
                  }

               }
            }

            if (t == null) {
               t3 = null;
            } else {
               t3 = null;
               if (t != t3) {
                  ((Throwable)t).addSuppressed(t3);
               }
            }

            return var43;
         }

         if (t == null) {
            fis = null;
         } else {
            fis = null;
            if (t != fis) {
               ((Throwable)t).addSuppressed(fis);
            }
         }
      } catch (IOException var42) {
         var42.printStackTrace();
      }

      return out;
   }

   public static void save(String file, String content, boolean append) {
      try {
         File f = new File(dir, file);
         if (!f.exists()) {
            f.createNewFile();
         }

         Object t = null;
         boolean var12 = false;

         FileWriter t2;
         try {
            var12 = true;
            t2 = new FileWriter(f, append);

            try {
               t2.write(content);
            } finally {
               if (t2 != null) {
                  t2.close();
               }

            }

            var12 = false;
         } finally {
            if (var12) {
               Object t2;
               if (t == null) {
                  t2 = null;
               } else {
                  t2 = null;
                  if (t != t2) {
                     ((Throwable)t).addSuppressed((Throwable)t2);
                  }
               }

            }
         }

         if (t == null) {
            t2 = null;
         } else {
            t2 = null;
            if (t != t2) {
               ((Throwable)t).addSuppressed(t2);
            }
         }
      } catch (IOException var18) {
         var18.printStackTrace();
      }

   }

   static {
      File mcDataDir = Minecraft.getMinecraft().mcDataDir;
      Client.instance.getClass();
      dir = new File(mcDataDir, "Melody");
   }
}
