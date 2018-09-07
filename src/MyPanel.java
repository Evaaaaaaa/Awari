/****************************************************************
 * MyPanel.java
 * Do not modify this file!
 * This file is for GUI, you don't need to read or understand this file.
 */

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MyPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -8554162233768101479L;

    @Override
    protected void paintComponent(Graphics g) {
        Image image = (new ImageIcon("wood-texture.jpg")).getImage();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        setOpaque(false);
        super.paintComponent(g);
        setOpaque(true);
    }
}
