/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BUS.deThiBUS;
import BUS.lopBUS1;
import BUS.monBUS1;
import DTO.nguoiDungDTO;
import BUS.nguoiDungBUS;
import DTO.deThiDTO;
import DTO.lopDTO;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author E7250
 */
public class PnDsDeThiDaTao extends JPanel implements ActionListener {

    private DefaultTableModel model;
    private JTextField txt_mon;
    private JComboBox<String> cbb_trangThai;
    private JTextField txt_maDe;
    private String maTK;
    private JTable table;
    private deThiBUS deThi;
    private JPanel pn_btn;

    public PnDsDeThiDaTao(String maTK) throws SQLException {
        deThi = new deThiBUS();
        this.maTK = maTK;
        init();
        cbb_trangThai.setSelectedIndex(0);
    }

    public JComboBox<String> getCbb_trangThai() {
        return cbb_trangThai;
    }

    public void setCbb_trangThai(JComboBox<String> cbb_trangThai) {
        this.cbb_trangThai = cbb_trangThai;
    }

    public void init() {
        this.setLayout(new BorderLayout());
        JPanel pn_header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel lb_maDe = new JLabel("Mã đề:");
        txt_maDe = new JTextField(10);
        JLabel lb_mon = new JLabel("Tên môn:");
        txt_mon = new JTextField(10);
        cbb_trangThai = new JComboBox<>(new String[]{"Sắp diễn ra", "Đang diễn ra", "Đã diễn ra"});
        cbb_trangThai.setPreferredSize(new Dimension(100, cbb_trangThai.getPreferredSize().height));
        cbb_trangThai.addActionListener(this);
        pn_header.add(lb_maDe);
        pn_header.add(txt_maDe);

        pn_header.add(lb_mon);
        pn_header.add(txt_mon);

        pn_header.add(cbb_trangThai);

        JPanel pn_table = new JPanel(new BorderLayout());

        Object[][] data = {};
        Object[] columns = {"Mã đề", "Tên đề", "Môn", "Nhóm lớp", "Ngày làm bài", "Thời gian bắt đầu", "Số lượng câu hỏi", "Thời gian làm bài"};

        model = new DefaultTableModel(data, columns);

        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane_table = new JScrollPane(table);
        pn_table.add(scrollPane_table, BorderLayout.CENTER);

        pn_btn = new JPanel(new FlowLayout(1, 10, 10));
        JButton btn_ketThucDe = new JButton("Kết thúc đề");
        pn_btn.add(btn_ketThucDe);
        btn_ketThucDe.addActionListener(this);

        this.add(pn_header, BorderLayout.NORTH);
        this.add(pn_table, BorderLayout.CENTER);
        this.add(pn_btn, BorderLayout.SOUTH);

    }

    public void loadData(int trangThai) throws SQLException {
        ArrayList<deThiDTO> arr = this.deThi.layDSDeThiDaTao(maTK, trangThai);
        for (deThiDTO dt : arr) {
            lopBUS1 lopBUS = new lopBUS1();
            lopDTO lop = lopBUS.layLopBangMaDe(dt.getMaDT());
            monBUS1 monBUS = new monBUS1();
            String tenMon = monBUS.layTenMonBangMaMon(lop.getMaMon());
            model.addRow(new Object[]{dt.getMaDT(), dt.getTenDeThi(), tenMon, lop.getNhomLop(), dt.getNgayThi(), dt.getThoiGianBatDauThi(), dt.getSLCauHoi(), dt.getThoiGianLamBai()});
        }
        table.setModel(model);
    }

    public static void main(String[] args) throws SQLException {
        JFrame f = new JFrame();
        f.setSize(800, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.getContentPane().add(new PnDsDeThiDaTao("TK4"));
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String btn = e.getActionCommand();
        String selectedOption = (String) this.getCbb_trangThai().getSelectedItem();
        if (btn.equals("Kết thúc đề")) {
            int[] selectedRows = table.getSelectedRows();
            for (int row : selectedRows) {
                Object maDeThi = table.getValueAt(row, 0);
                try {
                    deThi.updateTrangThaiDeThi(maDeThi.toString().trim());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            try {
                loadData(1);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (selectedOption.equals("Sắp diễn ra")) {
            model.setRowCount(0);
            try {
                this.loadData(0);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            pn_btn.setVisible(false);
        } else if (selectedOption.equals("Đang diễn ra")) {
            model.setRowCount(0);
            try {
                this.loadData(1);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            pn_btn.setVisible(true);
        } else if (selectedOption.equals("Đã diễn ra")) {
            model.setRowCount(0);
            try {
                this.loadData(2);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            pn_btn.setVisible(false);
        }
    }

}
