package com.ageoflegacy.aedit.ui.view.mobView;

import com.ageoflegacy.aedit.model.MudConstants;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class MudShopView {
    private int keeper; // vnum of mobile
    private int[] buyType; // item types shop will by.
    private int profitBuy;
    private int profitSell;
    private int openHour;
    private int closeHour;

    public int[] getBuyType() {
        return buyType;
    }

    public void setBuyType(int[] buyType) {
        this.buyType = buyType;
    }

    public MudShopView(int mobVnum) {
        buyType = new int[MudConstants.MAX_TRADE];
        keeper = mobVnum;
        profitBuy = 100;
        profitSell = 100;
        openHour = 0;
        closeHour = 24;
    }

    public MudShopView(int mobVnum, int[] bType, int pBuy, int pSell, int hOpen, int hClose) {
        this(mobVnum);
        buyType = bType;
        profitBuy = pBuy;
        profitSell = pSell;
        openHour = hOpen;
        closeHour = hClose;
        // System.out.println("New Shop: " + toString() );
    }

    public int getBuyProfit() {
        return profitBuy;
    }

    public int getSellProfit() {
        return profitSell;
    }

    public int getOpenHour() {
        return openHour;
    }

    public int getCloseHour() {
        return closeHour;
    }

    public void setBuyProfit(int newp) {
        if (newp < 0)
            return;

        profitBuy = newp;
    }

    public void setSellProfit(int newp) {
        if (newp < 0)
            return;

        profitSell = newp;
    }

    public void setOpenHour(int newh) {
        if (newh < 0 || newh > 23)
            return;

        openHour = newh;

        if (closeHour <= openHour)
            closeHour = openHour + 1;
    }

    public void setCloseHour(int newh) {
        if (newh < 0 || newh > 23)
            return;

        closeHour = newh;

        if (closeHour <= openHour)
            openHour = closeHour - 1;
    }

    public String toString() {
        String temp;
        temp = intToString(keeper) + " " + intToString(buyType[0]) + " " + intToString(buyType[1]) + " " + intToString(buyType[2]);
        temp = temp + " " + intToString(buyType[3]) + " " + intToString(buyType[4]);
        temp = temp + " " + intToString(profitBuy) + " " + intToString(profitSell);
        temp = temp + " " + intToString(openHour) + " " + intToString(closeHour) + "\n";
        return temp;
    }

    public int getKeeper() {
        return keeper;
    }

    public void setKeeper(int newKeeper) {
        keeper = newKeeper;
    }

    private String intToString(int a) {
        return Integer.toString(a);
    }

    public JPanel getPanel() {
        JPanel mypanel = new JPanel(true);
        mypanel.setLayout(new GridLayout(2, 2));

        final JSlider openSlide = new JSlider(JSlider.HORIZONTAL, 1, 24, 8);
        openSlide.setMajorTickSpacing(11);
        openSlide.setMinorTickSpacing(1);
        openSlide.setPaintTicks(true);
        openSlide.setPaintLabels(true);
        final TitledComponent openHolder = new TitledComponent(openSlide, "Opens at: " + getOpenHour());

        final JSlider closeSlide = new JSlider(JSlider.HORIZONTAL, 1, 24, 20);
        closeSlide.setMajorTickSpacing(11);
        closeSlide.setMinorTickSpacing(1);
        closeSlide.setPaintTicks(true);
        closeSlide.setPaintLabels(true);
        final TitledComponent closeHolder = new TitledComponent(closeSlide, "Closes at: " + getCloseHour());

        final JSlider buySlide = new JSlider(JSlider.HORIZONTAL, 25, 200, 100);
        buySlide.setMajorTickSpacing(50);
        buySlide.setPaintTicks(true);
        buySlide.setPaintLabels(true);
        final TitledComponent buyHolder = new TitledComponent(buySlide, "Buy profit: " + getBuyProfit() + "%");

        final JSlider sellSlide = new JSlider(JSlider.HORIZONTAL, 25, 200, 100);
        sellSlide.setMajorTickSpacing(50);
        sellSlide.setPaintTicks(true);
        sellSlide.setPaintLabels(true);
        final TitledComponent sellHolder = new TitledComponent(sellSlide, "Sell profit: " + getSellProfit() + "%");

        mypanel.add(openHolder);
        mypanel.add(closeHolder);
        mypanel.add(buyHolder);
        mypanel.add(sellHolder);
        mypanel.setBorder(new LineBorder(Color.gray, 1));


        JPanel rpanel = new JPanel(true);
        rpanel.setLayout(new BorderLayout());
        rpanel.add(mypanel, BorderLayout.CENTER);
        JLabel label = new JLabel(
                "This mobile will sell the equipment selected in it's inventory.");
        rpanel.add(label, BorderLayout.SOUTH);

        openSlide.setValue(getOpenHour());
        closeSlide.setValue(getCloseHour());
        buySlide.setValue(getBuyProfit());
        sellSlide.setValue(getSellProfit());

        openSlide.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                //if (!source.getValueIsAdjusting())
                {
                    setOpenHour((int) source.getValue());
                    openSlide.setValue(getOpenHour());
                    closeSlide.setValue(getCloseHour());
                    openHolder.setText("Opens at: " + getOpenHour());
                    closeHolder.setText("Closes at: " + getCloseHour());
                }
            }
        });

        closeSlide.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                //if (!source.getValueIsAdjusting())
                {
                    setCloseHour((int) source.getValue());
                    openSlide.setValue(getOpenHour());
                    closeSlide.setValue(getCloseHour());
                    openHolder.setText("Opens at: " + getOpenHour());
                    closeHolder.setText("Closes at: " + getCloseHour());
                }
            }
        });

        buySlide.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                // if (!source.getValueIsAdjusting())
                {
                    setBuyProfit((int) source.getValue());
                    buySlide.setValue(getBuyProfit());
                    buyHolder.setText("Buy profit: " + getBuyProfit() + "%");
                }
            }
        });

        sellSlide.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                //if (!source.getValueIsAdjusting())
                {
                    setSellProfit((int) source.getValue());
                    sellSlide.setValue(getSellProfit());
                    sellHolder.setText("Sell profit: " + getSellProfit() + "%");
                }
            }
        });

        return rpanel;
    }

    class TitledComponent extends JPanel {
    JLabel label;

    public TitledComponent(Component j, String title) {
        super();
        this.setLayout(new GridLayout(2, 1));
        label = new JLabel(" " + title);
        this.add(label);
        this.add(j);
    }

    public void setText(String newTitle) {
        label.setText(" " + newTitle);
    }

}
}
