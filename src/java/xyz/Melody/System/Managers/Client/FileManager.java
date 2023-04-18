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

    public static File getConfigFile(String string) {
        File file = new File(dir, String.format("%s.txt", string));
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
    public static List<String> read(String string) {
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            InputStreamReader inputStreamReader;
            FileInputStream fileInputStream;
            Object object;
            block38: {
                File file;
                if (!dir.exists()) {
                    dir.mkdir();
                }
                if (!(file = new File(dir, string)).exists()) {
                    file.createNewFile();
                }
                object = null;
                try {
                    String string2;
                    Object object2;
                    block39: {
                        fileInputStream = new FileInputStream(file);
                        try {
                            block37: {
                                inputStreamReader = new InputStreamReader((InputStream)fileInputStream, Charset.forName("UTF-8"));
                                try {
                                    object2 = new BufferedReader(inputStreamReader);
                                    try {
                                        string2 = "";
                                        while ((string2 = ((BufferedReader)object2).readLine()) != null) {
                                            arrayList.add(string2);
                                        }
                                    }
                                    finally {
                                        if (object2 != null) {
                                            ((BufferedReader)object2).close();
                                        }
                                    }
                                    if (inputStreamReader != null) {
                                        inputStreamReader.close();
                                    }
                                    if (object == null) {
                                        object = object2 = null;
                                    } else {
                                        object2 = null;
                                        if (object != object2) {
                                            ((Throwable)object).addSuppressed((Throwable)object2);
                                        }
                                    }
                                    if (inputStreamReader == null) break block37;
                                }
                                catch (Throwable throwable) {
                                    if (object == null) {
                                        Object var10_11 = null;
                                        object = var10_11;
                                    } else {
                                        Throwable throwable2 = null;
                                        if (object != throwable2) {
                                            ((Throwable)object).addSuppressed(throwable2);
                                        }
                                    }
                                    if (inputStreamReader == null) throw throwable;
                                    inputStreamReader.close();
                                    throw throwable;
                                }
                                inputStreamReader.close();
                            }
                            if (fileInputStream == null) break block38;
                            fileInputStream.close();
                            object2 = arrayList;
                            if (object == null) {
                                string2 = null;
                                object = string2;
                            } else {
                                string2 = null;
                                if (object != string2) {
                                    ((Throwable)object).addSuppressed((Throwable)((Object)string2));
                                }
                            }
                            if (fileInputStream == null) break block39;
                        }
                        catch (Throwable throwable) {
                            if (object == null) {
                                Object var12_14 = null;
                                object = var12_14;
                            } else {
                                Throwable throwable3 = null;
                                if (object != throwable3) {
                                    ((Throwable)object).addSuppressed(throwable3);
                                }
                            }
                            if (fileInputStream == null) throw throwable;
                            fileInputStream.close();
                            throw throwable;
                        }
                        fileInputStream.close();
                    }
                    if (object == null) {
                        return object2;
                    }
                    string2 = null;
                    if (object == string2) return object2;
                    ((Throwable)object).addSuppressed((Throwable)((Object)string2));
                    return object2;
                }
                catch (Throwable throwable) {
                    if (object == null) {
                        Throwable throwable4 = null;
                        object = throwable4;
                        throw throwable;
                    }
                    Throwable throwable5 = null;
                    if (object == throwable5) throw throwable;
                    ((Throwable)object).addSuppressed(throwable5);
                    throw throwable;
                }
            }
            if (object == null) {
                inputStreamReader = null;
                object = inputStreamReader;
            } else {
                inputStreamReader = null;
                if (object != inputStreamReader) {
                    ((Throwable)object).addSuppressed((Throwable)((Object)inputStreamReader));
                }
            }
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (object == null) {
                fileInputStream = null;
                object = fileInputStream;
                return arrayList;
            }
            fileInputStream = null;
            if (object == fileInputStream) return arrayList;
            ((Throwable)object).addSuppressed((Throwable)((Object)fileInputStream));
            return arrayList;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        return arrayList;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void save(String string, String string2, boolean bl) {
        try {
            File file = new File(dir, string);
            if (!file.exists()) {
                file.createNewFile();
            }
            Object object = null;
            try {
                try (FileWriter fileWriter = new FileWriter(file, bl);){
                    fileWriter.write(string2);
                }
                if (object == null) {
                    fileWriter = null;
                    object = fileWriter;
                } else {
                    fileWriter = null;
                    if (object != fileWriter) {
                        ((Throwable)object).addSuppressed((Throwable)((Object)fileWriter));
                    }
                }
            }
            catch (Throwable throwable) {
                if (object == null) {
                    Throwable throwable2 = null;
                    object = throwable2;
                } else {
                    Throwable throwable3 = null;
                    if (object != throwable3) {
                        ((Throwable)object).addSuppressed(throwable3);
                    }
                }
                throw throwable;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static List<String> read(File file) {
        return FileManager.read(file.getAbsolutePath());
    }
}

