/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Managers.Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import xyz.Melody.Client;

public final class FileManager {
    private static File mcDataDir = Minecraft.getMinecraft().mcDataDir;
    private static File dir = new File(mcDataDir, "Melody");

    public static File getClientDir() {
        return dir;
    }

    public static File getConfigFile(String name) {
        File file = new File(dir, String.format("%s.txt", name));
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException iOException) {
                // empty catch block
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static List<String> read(String file) {
        ArrayList<String> out = new ArrayList<String>();
        try {
            File f;
            if (!dir.exists()) {
                dir.mkdir();
            }
            if (!(f = new File(dir, file)).exists()) {
                f.createNewFile();
            }
            Throwable t = null;
            try {
                FileInputStream fis = new FileInputStream(f);
                try {
                    InputStreamReader isr = new InputStreamReader((InputStream)fis, Charset.forName("UTF-8"));
                    try {
                        try (BufferedReader br = new BufferedReader(isr);){
                            String line = "";
                            while ((line = br.readLine()) != null) {
                                out.add(line);
                            }
                        }
                        if (isr != null) {
                            isr.close();
                        }
                    }
                    finally {
                        Throwable t2;
                        if (t == null) {
                            t = t2 = null;
                        } else {
                            t2 = null;
                            if (t != t2) {
                                t.addSuppressed(t2);
                            }
                        }
                        if (isr != null) {
                            isr.close();
                        }
                    }
                    if (fis == null) return out;
                    fis.close();
                    ArrayList<String> arrayList = out;
                    return arrayList;
                }
                finally {
                    Throwable t3;
                    if (t == null) {
                        t = t3 = null;
                    } else {
                        t3 = null;
                        if (t != t3) {
                            t.addSuppressed(t3);
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
            finally {
                Throwable t4;
                if (t == null) {
                    t = t4 = null;
                } else {
                    t4 = null;
                    if (t != t4) {
                        t.addSuppressed(t4);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void save(String file, String content, boolean append) {
        try {
            File f = new File(dir, file);
            if (!f.exists()) {
                f.createNewFile();
            }
            Throwable t = null;
            try (FileWriter writer = new FileWriter(f, append);){
                writer.write(content);
            }
            finally {
                Throwable t2;
                if (t == null) {
                    t = t2 = null;
                } else {
                    t2 = null;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> read(File FILE) {
        return FileManager.read(FILE.getAbsolutePath());
    }
}

