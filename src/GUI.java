/****************************************************************
 * GUI.java
 * Do not modify this file!
 * This file is for GUI, you can adjust parameters like SLEEP_TIME here.
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;


public class GUI {
    /** pausing time when moving stones among pots **/
    public static final int SLEEP_TIME = 300;

    public Pot[] pots;
    public Pot bigPot1;
    public Pot bigPot2;

    public JTextArea textArea;
    public JScrollPane scrollPanel;
    public String logs;
    public String player1;
    public String player2;
    public JFrame frame;

    public GUI(String player1, String player2, BoardState state) {
        this.player1 = player1;
        this.player2 = player2;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("Oware");
        frame.setSize(860, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation((screen.width - 860) / 2, (screen.height - 320) / 2);
        frame.setVisible(true);
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        createPots();
        JPanel center = new JPanel(new GridLayout(2, 6));
        center.setOpaque(false);
        MyPanel panel = new MyPanel();
        panel.setLayout(new BorderLayout());
        panel.add(center, BorderLayout.CENTER);
        addPots(center, panel);
        content.add(panel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //frame.init();
                frame.setSize(860, 320);
                frame.setVisible(true);
            }
        });
    }

    public void refreshBigPots() {
        bigPot1.refresh();
        bigPot2.refresh();
    }

    private void addPots(JPanel center, MyPanel panel) {
        panel.add(bigPot1, BorderLayout.EAST);
        panel.add(bigPot2, BorderLayout.WEST);
        panel.add(scrollPanel, BorderLayout.SOUTH);


        for (int i = 11; i > 5; i--) {
            center.add(pots[i]);
        }

        for (int i = 0; i < 6; i++) {
            center.add(pots[i]);
        }
    }

    private void createPots() {
        bigPot1 = new BigPot("P1:" + player1);
        bigPot2 = new BigPot("P2:" + player2);
        pots = new Pot[12];
        for (int i = 0; i < pots.length; i++) {
            if (i < 6)
                pots[i] = new Pot(true, i);
            else
                pots[i] = new Pot(false, i);
            pots[i].createListener();
        }

        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setOpaque(false);
        logs = "";
        textArea.setText(logs);
        scrollPanel = new JScrollPane(textArea);
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

    }

    public GUI getGUI() {
        return this;
    }

    public void setPots(BoardState state) {
        int[] house = state.house;
        int[] score = state.score;
        for (int i = 0; i < 12; i++) {
            while (!pots[i].beansInitialized) {
                tSleep(50);
            }
            pots[i].removeBeans();
            pots[i].addBeans(house[i]);
        }
        while (!bigPot1.beansInitialized || !bigPot2.beansInitialized) {
            tSleep(50);
        }
        bigPot1.removeBeans();
        bigPot1.addBeans(score[0]);
        bigPot2.removeBeans();
        bigPot2.addBeans(score[1]);
    }

    public void applyMove(int move, boolean player1, BoardState context) {
        int[] house = context.house;
        int bin = -1;
        if (player1)
            bin = move;
        else
            bin = move + 6;

        //clear the original bin
        int stones = house[bin];
        house[bin] = 0;

        //clear the bin in GUI
        pots[bin].removeBeans();

        //sleep to make the move smoother
        tSleep(SLEEP_TIME);

        //scatter the stones in the consequent bins
        int stoneTemp = stones;
        int binTemp = bin;
        while(stoneTemp > 0) {
            binTemp = (binTemp+1)%12;
            if(binTemp != bin) {
                house[binTemp]++;
                pots[binTemp].addBeans(1);
                stoneTemp--;
            }
            tSleep(SLEEP_TIME);
        }


        int lastBin = binTemp;

        boolean grandSlam = true;
        if (player1) {
            for (int i = 11; i > lastBin && i >= 6; i--) {
                if (house[i] != 0) {
                    grandSlam = false;
                    break;
                }
            }
            for (int i = lastBin; i >= 6; i--) {
                if (house[i] == 1 || house[i] > 3) {
                    grandSlam = false;
                    break;
                }
            }
        } else {
            for (int i = 5; i > lastBin; i--) {
                if (house[i] != 0) {
                    grandSlam = false;
                    break;
                }
            }
            for (int i = lastBin; i >= 0 && i < 6; i--) {
                if (house[i] == 1 || house[i] > 3) {
                    grandSlam = false;
                    break;
                }
            }
        }

        tSleep(SLEEP_TIME);
        if (!grandSlam) {
            int stoneCaptured = 0;
            while ((player1 && lastBin >= 6) ||
                    (!player1 && lastBin < 6 && lastBin >= 0)) {
                if (house[lastBin] == 2 || house[lastBin] == 3) {
                    stoneCaptured += house[lastBin];
                    house[lastBin] = 0;
                    pots[lastBin].removeBeans();
                    lastBin--;
                } else {
                    break;
                }
            }
            tSleep(SLEEP_TIME);
            if (player1) {
                bigPot1.addBeans(stoneCaptured);
            } else {
                bigPot2.addBeans(stoneCaptured);
            }
        }


    }

    public void tSleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }


}
