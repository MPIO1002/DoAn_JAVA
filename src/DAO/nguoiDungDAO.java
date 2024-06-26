/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DTO.nguoiDungDTO;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author PHUNG
 */
public class nguoiDungDAO {

    private final MyConnection conn;
    private nguoiDungDTO ng;

    public nguoiDungDAO() throws SQLException {
        conn = new MyConnection();
        conn.Connect();
    }
//    private final Connection conn = connect.Connect();

    public ArrayList<nguoiDungDTO> getNguoiDung() throws SQLException {
        ArrayList<nguoiDungDTO> arr = new ArrayList<>();
        String sql = "SELECT * FROM nguoidung";
        PreparedStatement stmt = conn.preparedStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            nguoiDungDTO user = new nguoiDungDTO();
            user.setMaUser(rs.getString(1));
            user.setHoTen(rs.getString(2));
            user.setNgSinh(rs.getString(3));
            arr.add(user);
        }
        return arr;
    }

    public int getSoLuongNguoiDung() throws SQLException {
        String sql = "SELECT COUNT(MaUser) FROM nguoidung";
        PreparedStatement stmt = conn.preparedStatement(sql);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return Integer.parseInt(rs.getString(1));
        }
        return 0;
    }

    public boolean addNguoiDung(nguoiDungDTO a) throws SQLException {
        String sql = "INSERT INTO nguoidung(MaUser, HoTen, NgSinh) VALUES (?,?,?)";
        PreparedStatement stmt = conn.preparedStatement(sql);
        stmt.setString(1, a.getMaUser());
        stmt.setString(2, a.getHoTen());
        stmt.setString(3, a.getNgSinh());
        int ketQua = stmt.executeUpdate();
        if (ketQua > 0) {
            return true;
        }
        return false;

    }

    public boolean deleteNguoiDung(String maUser) throws SQLException {
        String sql = "DELETE FROM nguoidung WHERE MaUser=?";
        PreparedStatement stmt = conn.preparedStatement(sql);
        stmt.setString(1, maUser);
        int rs = stmt.executeUpdate();
        if (rs > 0) {
            return true;
        }
        return false;
    }

    public boolean updateNguoiDung(String hoTen, String ngSinh, String maUser) throws SQLException {
        String sql = "UPDATE nguoidung SET HoTen=?, NgSinh=? WHERE MaUser=?";
        PreparedStatement stmt = conn.preparedStatement(sql);
        stmt.setString(1, hoTen);
        stmt.setString(2, ngSinh);
        stmt.setString(3, maUser);
        int rs = stmt.executeUpdate();
        if (rs > 0) {
            return true;
        }
        return false;
    }

    public nguoiDungDTO layNguoiDung(String maTK) {
        nguoiDungDTO user = new nguoiDungDTO();
        try {
            String query = "SELECT MaUser, HoTen, NgSinh FROM TAIKHOAN JOIN NGUOIDUNG ON TAIKHOAN.TenDN = NGUOIDUNG.MaUser WHERE MaTK ='" + maTK + "'";

            PreparedStatement pre = conn.preparedStatement(query);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                user.setMaUser(rs.getString("MaUser"));
                user.setHoTen(rs.getString("HoTen"));
                user.setNgSinh(rs.getString("NgSinh"));
                return user;
            } else {
                // Xử lý trường hợp không có dữ liệu phù hợp với điều kiện
                System.out.println("Không có dữ liệu phù hợp với điều kiện.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String layTenNguoiDungTheoMaTK(String maTK) {
        try {
            String query = "SELECT HoTen FROM TAIKHOAN JOIN NGUOIDUNG ON TAIKHOAN.TenDN = NGUOIDUNG.MaUser WHERE MaTK ='" + maTK + "'";

            PreparedStatement pre = conn.preparedStatement(query);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                // Xử lý trường hợp không có dữ liệu phù hợp với điều kiện
                System.out.println("Không có dữ liệu phù hợp với điều kiện.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String layMaUserTheoMaTK(String maTK) {
        try {
            String query = "SELECT MaUser FROM TAIKHOAN JOIN NGUOIDUNG ON TAIKHOAN.TenDN = NGUOIDUNG.MaUser WHERE MaTK ='" + maTK + "'";

            PreparedStatement pre = conn.preparedStatement(query);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                // Xử lý trường hợp không có dữ liệu phù hợp với điều kiện
                System.out.println("Không có dữ liệu phù hợp với điều kiện.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<nguoiDungDTO> getThongTinSV(int Nam, int HocKy, String TenMon, String MaLop) {
        ArrayList<nguoiDungDTO> list = new ArrayList<>();
        try {
            conn.Connect();
            String sql = "SELECT ND.* "
                    + "FROM NGUOIDUNG ND "
                    + "JOIN TAIKHOAN TK ON ND.MaUser = TK.TenDN "
                    + "WHERE TK.MaQuyen = 'QSV' AND TK.TrangThai = 1 AND NOT EXISTS ( "
                    + "    SELECT 1 "
                    + "    FROM CHITIETLOP CT "
                    + "    JOIN LOP L ON L.MaLop = CT.MaLop "
                    + "    JOIN MON M ON L.MaMon = M.MaMon "
                    + "    WHERE CT.MaSV = TK.MaTK "
                    + "      AND L.Nam = ? "
                    + "      AND L.HocKy = ? "
                    + "      AND M.TenMon = ? "
                    + "      AND CT.MaLop = ?"
                    + ");";
            try (PreparedStatement pre = conn.preparedStatement(sql)) {
                pre.setInt(1, Nam);
                pre.setInt(2, HocKy);
                pre.setString(3, TenMon);
                pre.setString(4, MaLop);
                ResultSet rs = pre.executeQuery();
                while (rs.next()) {
                    ng = new nguoiDungDTO(rs.getString("MaUser"), rs.getString("HoTen"), rs.getString("NgSinh"));
                    list.add(ng);
                }
            }
            conn.disconnect();
        } catch (SQLException e) {
            System.err.println("Lay thong tin sv that bai" + e.getMessage());
        }
        return list;
    }

    public ArrayList<nguoiDungDTO> TimKiem(int Nam, int HocKy, String TenMon, String MaLop, String key) {
        ArrayList<nguoiDungDTO> list = new ArrayList<>();
        try {
            conn.Connect();
            String sql = "SELECT ND.* "
                    + "FROM NGUOIDUNG ND "
                    + "JOIN TAIKHOAN TK ON ND.MaUser = TK.TenDN "
                    + "WHERE TK.MaQuyen = 'QSV' AND TK.TrangThai = 1 AND ND.HoTen LIKE ? AND NOT EXISTS ( "
                    + "    SELECT 1 "
                    + "    FROM CHITIETLOP CT "
                    + "    JOIN LOP L ON L.MaLop = CT.MaLop "
                    + "    JOIN MON M ON L.MaMon = M.MaMon "
                    + "    WHERE CT.MaSV = TK.MaTK "
                    + "      AND L.Nam = ? "
                    + "      AND L.HocKy = ? "
                    + "      AND M.TenMon = ? "
                    + "      AND CT.MaLop = ?"
                    + ");";
            try (PreparedStatement pre = conn.preparedStatement(sql)) {
                String keyword = "%" + key + "%";
                pre.setString(1, keyword);
                pre.setInt(2, Nam);
                pre.setInt(3, HocKy);
                pre.setString(4, TenMon);
                pre.setString(5, MaLop);
                ResultSet rs = pre.executeQuery();
                while (rs.next()) {
                    ng = new nguoiDungDTO(rs.getString("MaUser"), rs.getString("HoTen"), rs.getString("NgSinh"));
                    list.add(ng);
                }
            }
            conn.disconnect();
        } catch (SQLException e) {
            System.err.println("Lay thong tin sv that bai" + e.getMessage());
        }
        return list;
    }

    public ArrayList<nguoiDungDTO> DSTenGV(String TenMon) {
        ArrayList<nguoiDungDTO> list = new ArrayList<>();
        try {
            conn.Connect();
            String sql = "SELECT ND.HoTen "
                    + "FROM TAIKHOAN TK "
                    + "JOIN NGUOIDUNG ND ON TK.TenDN = ND.MaUser "
                    + "JOIN CHITIETMON CTM ON TK.MaTK = CTM.MaGV "
                    + "JOIN Mon M ON CTM.MaMon = M.MaMon "
                    + "WHERE TK.MaQuyen = 'QGV' "
                    + "AND TK.TrangThai = 1 "
                    + "AND M.TenMon = ?";
            try (PreparedStatement pre = conn.preparedStatement(sql)) {
                pre.setString(1, TenMon);
                ResultSet rs = pre.executeQuery();
                while (rs.next()) {
                    ng = new nguoiDungDTO(rs.getString("HoTen"));
                    list.add(ng);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lay danh sach gv that bai" + e.getMessage());
        }
        return list;
    }

    public ArrayList<nguoiDungDTO> DSGiaoVien() {
        ArrayList<nguoiDungDTO> list = new ArrayList<>();
        try {
            conn.Connect();
            String sql = "SELECT ND.MaUser, ND.HoTen, ND.NgSinh FROM NGUOIDUNG ND JOIN TAIKHOAN TK ON TK.TenDN = ND.MaUser WHERE TK.MaQuyen ='QGV' AND TK.TrangThai = 1";
            try (PreparedStatement pre = conn.preparedStatement(sql)) {
                ResultSet rs = pre.executeQuery();
                while (rs.next()) {
                    ng = new nguoiDungDTO(rs.getString("MaUser"), rs.getString("HoTen"), rs.getString("NgSinh"));
                    list.add(ng);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lay danh sach gv that bai" + e.getMessage());
        }
        return list;
    }

}
