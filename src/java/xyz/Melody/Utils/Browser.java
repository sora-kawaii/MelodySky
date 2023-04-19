/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

import chrriis.dj.nativeswing.NSOption;
import chrriis.dj.nativeswing.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import xyz.Melody.Utils.ll;

public final class Browser
extends JPanel {
    private JPanel webBrowserPanel;
    public boolean closed = false;
    private JWebBrowser webBrowser;
    private JFrame frame;

    public Browser(String url, String title, boolean visible, boolean resizable, boolean onTop, NSOption ... runtime) {
        this.closed = false;
        new Thread(() -> this.lambda$new$0(url, title, visible, resizable, onTop, runtime), "Thread-Call").start();
    }

    public Browser(String url, String title, boolean visible, boolean resizable, boolean onTop, int width, int height, NSOption ... runtime) {
        this.closed = false;
        new Thread(() -> this.lambda$new$1(url, title, visible, resizable, onTop, width, height, runtime), "Thread-Call").start();
    }

    private Browser(String url, NSOption ... options) {
        super(new BorderLayout());
        this.webBrowserPanel = new JPanel(new BorderLayout());
        this.webBrowser = new JWebBrowser(options);
        this.webBrowser.navigate(url);
        this.webBrowser.setJavascriptEnabled(true);
        this.webBrowser.setBarsVisible(false);
        this.webBrowser.setStatusBarVisible(false);
        this.webBrowserPanel.add((Component)this.webBrowser, "Center");
        this.add((Component)this.webBrowserPanel, "Center");
    }

    public void openForm(String url, String title, boolean visible, boolean resizable, boolean onTop, int w, int h, NSOption ... runtime) {
        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        SwingUtilities.invokeLater(new ll(this, title, url, runtime, visible, onTop, resizable, w, h));
    }

    public void close() {
        this.closed = true;
        this.frame.dispose();
    }

    private void lambda$new$1(String url, String title, boolean visible, boolean resizable, boolean onTop, int width, int height, NSOption[] runtime) {
        this.openForm(url, title, visible, resizable, onTop, width, height, runtime);
    }

    private void lambda$new$0(String url, String title, boolean visible, boolean resizable, boolean onTop, NSOption[] runtime) {
        this.openForm(url, title, visible, resizable, onTop, 700, 800, runtime);
    }

    static /* synthetic */ JFrame access$002(Browser x0, JFrame x1) {
        x0.frame = x1;
        return x0.frame;
    }

    static /* synthetic */ JFrame access$000(Browser x0) {
        return x0.frame;
    }

    Browser(String x0, NSOption[] x1, ll x2) {
        this(x0, x1);
    }
}

