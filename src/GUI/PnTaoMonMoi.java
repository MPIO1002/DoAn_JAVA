/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BUS.khoCauHoiBUS2;
import BUS.monBUS2;
import DAO.khoCauHoiDAO2;
import DTO.monDTO;
import static GUI.BASE.dark_green;
import static GUI.BASE.font14;
import static GUI.BASE.font14b;
import static GUI.BASE.gray_bg;
import XULY.ShowDiaLog;
import java.awt.BorderLayout;
import static java.awt.Color.white;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PnTaoMonMoi extends JPanel {

    private JTextField tfTimKiem, tfTenMon;
    private DefaultTableModel model;
    private JTable table;
    private JButton btnThem, btnXoa;
    private JPanel pnTop, pnCenter, pnBottom;
    private monBUS2 busMon = new monBUS2();
    private monDTO dtoMon = new monDTO();
    private khoCauHoiBUS2 busKho = new khoCauHoiBUS2();

    public PnTaoMonMoi() {
        init();
        initComponents();
        loadData();
    }

    public void init() {
        this.setLayout(new BorderLayout());
        pnTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnTop.setBackground(gray_bg);
        pnCenter = new JPanel(new BorderLayout());
        pnBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnBottom.setBackground(gray_bg);
        this.add(pnTop, BorderLayout.NORTH);
        this.add(pnCenter, BorderLayout.CENTER);
        this.add(pnBottom, BorderLayout.SOUTH);
    }

    public void initComponents() {
        JLabel lblTimKiem, lblTenMon;
        lblTimKiem = new JLabel("Tìm kiếm");
        lblTimKiem.setFont(font14);
        tfTimKiem = new JTextField(20);
        pnTop.add(lblTimKiem);
        pnTop.add(tfTimKiem);

        Object[] columns = {"Mã môn", "Tên môn"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        setTableFont(table);
        JScrollPane scrlTable = new JScrollPane(table);
        pnCenter.add(scrlTable);

        lblTenMon = new JLabel("Tên môn:");
        lblTenMon.setFont(font14);
        tfTenMon = new JTextField(20);
        btnThem = new JButton("Thêm");
        btnThem.setBackground(dark_green);
        btnThem.setFont(font14b);
        btnThem.setForeground(white);
        btnThem.setBorderPainted(false);
        btnThem.setFocusPainted(false);
        btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThem.setMaximumSize(new Dimension(120, 20));

        btnXoa = new JButton("Xóa");
        btnXoa.setBackground(dark_green);
        btnXoa.setFont(font14b);
        btnXoa.setForeground(white);
        btnXoa.setBorderPainted(false);
        btnXoa.setFocusPainted(false);
        btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnXoa.setMaximumSize(new Dimension(120, 20));

        pnBottom.add(lblTenMon);
        pnBottom.add(tfTenMon);
        pnBottom.add(btnThem);
        pnBottom.add(btnXoa);

        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String TenMon = tfTenMon.getText();

                if (TenMon.isEmpty()) {
                    new ShowDiaLog("Bạn chưa nhập tên môn", ShowDiaLog.ERROR_DIALONG);

                } else if (TenMon.matches(".*\\d.*")) {
                    new ShowDiaLog("Tên môn không được chứa số", ShowDiaLog.ERROR_DIALONG);

                } else {
                    String ten = TenMon.trim().replaceAll("\\s+", " ");
                    ten = ten.substring(0, 1).toUpperCase() + ten.substring(1);
                    String[] words = ten.split("\\s+");
                    String MaMon = "M";
                    String MaKho = "K";
                    for (String word : words) {
                        if (!word.isEmpty()) {
                            MaMon += Character.toUpperCase(word.charAt(0));
                            MaKho += Character.toUpperCase(word.charAt(0));
                        }
                    }
                    ThemMon(MaMon, ten);
                    ThemKho(MaKho, MaMon);
                }
            }
        });

    }

    private void setTableFont(JTable table) {
        table.setFont(font14);

        JTableHeader header = table.getTableHeader();
        header.setFont(font14);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setFont(font14);
        table.setDefaultRenderer(Object.class, renderer);
    }

    private void loadData() {
        model.setRowCount(0);
        ArrayList<monDTO> list = busMon.getList();
        for (monDTO m : list) {
            Object[] row = {m.getMaMon(), m.getTenMon()};
            model.addRow(row);
        }
    }

    private void ThemMon(String MaMon, String TenMon) {
        boolean result = busMon.ThemMon(MaMon, TenMon);
        if (result) {
            loadData();
            tfTenMon.setText("");
        }
    }

    private void ThemKho(String MaKho, String MaMon) {
        busKho.ThemKho(MaKho, MaMon, null);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(900, 400);
        PnTaoMonMoi p = new PnTaoMonMoi();
        f.add(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

}
