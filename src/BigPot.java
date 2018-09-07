/****************************************************************
 * BigPot.java
 * Do not modify this file!
 * This file is for GUI, you don't need to read or understand this file.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

public class BigPot extends Pot {

    private static final long serialVersionUID = -9024340637590238213L;
    private final String playerName;

    public BigPot(String playerName) {
        super(true, -1);
        this.playerName = playerName;
        this.setPreferredSize(new Dimension(120, 300));
    }

    @Override
    protected void initBeans() {
        beans = Collections.synchronizedList(new ArrayList<Bean>());
        refresh();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(playerName, g);
        int textWidth = (int) rect.getWidth();
        g.drawString(playerName, (getWidth() - textWidth) / 2, 20);
    }


}
