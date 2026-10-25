package com.example.targethud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

/** Draggable panels with switches. Left click = switch, right click = open the slider. */
public class ClickGuiScreen extends Screen {
    private static final int PW = 124, HEAD = 20, ROW = 17, SL = 22;

    private static final int PANEL = 0xDD1A1842;
    private static final int BORDER = 0x669696FF;
    private static final int ACCENT = 0xFF7C8CFF;
    private static final int SOFT = 0x337C8CFF;
    private static final int OFF = 0xFF4A4A7C;
    private static final int TEXT = 0xFFECEBFF;
    private static final int MUTED = 0xFF9B9CCB;

    private static final int[] PX = {30, 170};
    private static final int[] PY = {30, 30};

    private int dragPanel = -1, dragDx, dragDy;
    private Feature dragSlider;
    private int dragSliderX;

    public ClickGuiScreen() {
        super(Text.literal("Target HUD"));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        for (int p = 0; p < TargetHudConfig.PANELS.length; p++) {
            List<Feature> list = TargetHudConfig.in(TargetHudConfig.PANELS[p]);
            int x = PX[p], y = PY[p];
            int h = HEAD + 4;
            long on = 0;
            for (Feature m : list) {
                h += ROW + (m.hasSlider && m.open ? SL : 0);
                if (m.on) on++;
            }
            ctx.fill(x, y, x + PW, y + h, PANEL);
            ctx.drawBorder(x, y, PW, h, BORDER);
            ctx.drawTextWithShadow(this.textRenderer, TargetHudConfig.PANELS[p], x + 7, y + 6, TEXT);
            String count = on + " of " + list.size() + " on";
            ctx.drawTextWithShadow(this.textRenderer, count, x + PW - 7 - this.textRenderer.getWidth(count), y + 6, MUTED);

            int ry = y + HEAD;
            for (Feature m : list) {
                if (m.on) ctx.fill(x + 3, ry, x + PW - 3, ry + ROW - 1, SOFT);
                ctx.drawTextWithShadow(this.textRenderer, m.name, x + 8, ry + 4, m.on ? ACCENT : TEXT);
                if (m.hasSlider) ctx.drawTextWithShadow(this.textRenderer, m.open ? "v" : ">", x + PW - 42, ry + 4, MUTED);
                int tx = x + PW - 29, ty = ry + 4;
                ctx.fill(tx, ty, tx + 20, ty + 9, m.on ? ACCENT : OFF);
                int kx = m.on ? tx + 12 : tx + 1;
                ctx.fill(kx, ty + 1, kx + 7, ty + 8, m.on ? 0xFF12123A : 0xFFFFFFFF);
                ry += ROW;
                if (m.hasSlider && m.open) {
                    String label = m.name + ": " + (int) Math.round(m.value) + m.unit;
                    ctx.drawTextWithShadow(this.textRenderer, label, x + 8, ry + 1, MUTED);
                    int bw = PW - 16;
                    ctx.fill(x + 8, ry + 13, x + 8 + bw, ry + 17, OFF);
                    int fw = (int) Math.round(bw * (m.value - m.min) / (m.max - m.min));
                    ctx.fill(x + 8, ry + 13, x + 8 + fw, ry + 17, ACCENT);
                    ctx.fill(x + 8 + fw - 1, ry + 11, x + 8 + fw + 2, ry + 19, 0xFFFFFFFF);
                    ry += SL;
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        for (int p = TargetHudConfig.PANELS.length - 1; p >= 0; p--) {
            int x = PX[p], y = PY[p];
            if (mx < x || mx > x + PW) continue;
            if (my >= y && my < y + HEAD) {
                dragPanel = p;
                dragDx = (int) mx - x;
                dragDy = (int) my - y;
                return true;
            }
            int ry = y + HEAD;
            for (Feature m : TargetHudConfig.in(TargetHudConfig.PANELS[p])) {
                if (my >= ry && my < ry + ROW) {
                    if (button == 0) m.on = !m.on;
                    else if (button == 1 && m.hasSlider) m.open = !m.open;
                    return true;
                }
                ry += ROW;
                if (m.hasSlider && m.open) {
                    if (my >= ry && my < ry + SL) {
                        dragSlider = m;
                        dragSliderX = x;
                        setSlider(m, mx);
                        return true;
                    }
                    ry += SL;
                }
            }
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (dragPanel >= 0) {
            PX[dragPanel] = Math.max(0, Math.min(this.width - PW, (int) mx - dragDx));
            PY[dragPanel] = Math.max(0, Math.min(this.height - HEAD, (int) my - dragDy));
            return true;
        }
        if (dragSlider != null) {
            setSlider(dragSlider, mx);
            return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        dragPanel = -1;
        dragSlider = null;
        return super.mouseReleased(mx, my, button);
    }

    private void setSlider(Feature m, double mx) {
        double t = Math.max(0, Math.min(1, (mx - (dragSliderX + 8)) / (PW - 16)));
        double v = m.min + t * (m.max - m.min);
        m.value = m.integer ? Math.round(v) : v;
    }

    @Override
    public void removed() {
        TargetHudConfig.save();
    }
}
