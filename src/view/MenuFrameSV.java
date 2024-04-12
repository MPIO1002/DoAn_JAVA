/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.Menu_SV_Listener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.ImageUtils;
import static view.base.cobalt_blue;
import static view.base.dark_green;
import static view.base.font14;

/**
 *
 * @author E7250
 */
public class MenuFrameSV extends JFrame {

    private JPanel cards;
    private CardLayout cardLayout;
    private JLabel lblHeader, lblName;
    private JPanel pnLeft, pnCenter, pnTop;
    private String[] title = {"Thông tin", "Vào thi", "Cá nhân"};
    private String[] nameImage = {"taoCauHoi_icon.png", "taoDeThi_icon.png", "passwd_icon.png"};
    private JButton[] buttons = new JButton[title.length];

    public MenuFrameSV() {
        init();
        initComponents();
        card();
        this.setVisible(true);
    }

    public JPanel getCards() {
        return cards;
    }

    public void setCards(JPanel cards) {
        this.cards = cards;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public void setCardLayout(CardLayout cardLayout) {
        this.cardLayout = cardLayout;
    }

    public JLabel getLb_Header() {
        return lblHeader;
    }

    private void init() {
        this.setSize(1000, 600);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pnLeft = new JPanel();

        pnLeft.setLayout(new BoxLayout(pnLeft, BoxLayout.Y_AXIS));
        pnLeft.setBackground(cobalt_blue);
        pnLeft.setPreferredSize(new Dimension(150, 0));

        pnCenter = new JPanel(new BorderLayout());
        pnCenter.setBackground(Color.CYAN);

        pnTop = new JPanel(new BorderLayout());
        pnTop.setBackground(dark_green);
        pnTop.setPreferredSize(new Dimension(0, 35));

        this.add(pnTop, BorderLayout.NORTH);
        this.add(pnCenter, BorderLayout.CENTER);
        this.add(pnLeft, BorderLayout.WEST);

    }

    private void initComponents() {
        lblName = new JLabel("Hello Word");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14) {
        });
        lblName.setForeground(Color.decode("#ffffff"));
        JPanel pnName = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnName.add(lblName);
        pnName.setBackground(dark_green);
        pnName.setPreferredSize(new Dimension(150, 0));
        lblHeader = new JLabel("Thông tin");
        lblHeader.setForeground(Color.WHITE);
        JPanel pnHead = new JPanel();
        pnHead.setBackground(dark_green);
        pnHead.add(lblHeader);
        lblHeader.setFont(font14);
        pnTop.add(pnName, BorderLayout.WEST);
        pnTop.add(pnHead, BorderLayout.CENTER);

        Dimension buttonDimension = pnLeft.getPreferredSize();
        buttonDimension.height = 35;
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = createButton(title[i], nameImage[i], buttonDimension);
            pnLeft.add(buttons[i]);
        }
        setupMenuActions();

    }

    private JButton createButton(String name, String img, Dimension size) {
        JButton btn = new JButton(name);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        ImageIcon icon = ImageUtils.createResizedIcon(MenuFrameAd.class, "..//image//" + img, 20, 20);
        btn.setIcon(icon);
        btn.setFont(font14);
        btn.setMaximumSize(size);
        btn.setBackground(dark_green);
        btn.setForeground(Color.WHITE);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(dark_green);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!btn.isSelected()) {
                    btn.setBackground(cobalt_blue);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Thiết lập lại màu nền của tất cả các JButton
                for (JButton button : buttons) {
                    button.setSelected(false);
                    button.setBackground(cobalt_blue);
                }
                // Thiết lập màu nền của JButton được chọn
                btn.setSelected(true);
                btn.setBackground(dark_green);
            }
        });
        if (name.equals("Quản lý")) {
            btn.setBackground(dark_green);
            btn.setSelected(true);
        } else {
            btn.setBackground(cobalt_blue);
        }
        return btn;
    }

    public void setupMenuActions() {
        Menu_SV_Listener menuActionListener = new Menu_SV_Listener(this);
        for (JButton button : buttons) {
            button.addActionListener(menuActionListener);
        }
    }

    private void card() {
        cards = new JPanel();
        pnCenter.add(cards, BorderLayout.CENTER);
        cardLayout = new CardLayout();
        cards.setLayout(cardLayout);

        JPanel pn_thongtin = new PnthongTinThi();
        cards.add(pn_thongtin, "PnThongtin");

        JPanel pn_Thi = new PnThongTinAdTr();
        cards.add(pn_Thi, "pnCTAD");

        JPanel pn_Info = new PnThi();
        cards.add(pn_Info, "PnThi");

    }

    public static void main(String[] args) {
        new MenuFrameSV();
    }
}