/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import javax.imageio.ImageIO;
import xyz.Melody.Client;

public final class WindowsNotification {
    private static Image image;
    private static TrayIcon trayIcon;
    private static SystemTray tray;
    private static int num;

    public static void init() {
        new Thread(() -> {
            try {
                image = ImageIO.read(Client.class.getResource("/assets/minecraft/Melody/wi/ic (0).png"));
                trayIcon = new TrayIcon(image);
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("MelodySky");
                tray.add(trayIcon);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                while (true) {
                    WindowsNotification.animate();
                }
            }
            catch (Exception exception) {
                return;
            }
        }, "Icon Controller").start();
    }

    private static void animate() throws Exception {
        Thread.sleep(75L);
        if (num == 12) {
            num = 0;
        }
        if (num == 1) {
            Thread.sleep(2000L);
        }
        image = ImageIO.read(Client.class.getResource("/assets/minecraft/Melody/wi/ic (" + num + ").png"));
        tray.getTrayIcons()[0].setImage(image);
        ++num;
    }

    public static void show(String string, String string2) {
        if (SystemTray.isSupported()) {
            WindowsNotification.displayTray(string, string2);
        } else {
            Client.instance.logger.error("System tray not supported!");
        }
    }

    private static void displayTray(String string, String string2) {
        trayIcon.displayMessage(string, string2, TrayIcon.MessageType.INFO);
    }

    public static void stop() {
        if (trayIcon != null && tray != null) {
            tray.remove(trayIcon);
        }
    }

    static {
        tray = SystemTray.getSystemTray();
        num = 0;
    }
}

