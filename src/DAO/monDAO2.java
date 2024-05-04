/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DTO.monDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class monDAO2 {

    private MyConnection conn;

    public monDAO2() {
        try {
            conn = new MyConnection();
        } catch (SQLException e) {
            System.err.println("ket noi that bat" + e.getMessage());
        }
    }

    public ArrayList<monDTO> listMon() {
        ArrayList<monDTO> list = new ArrayList<>();
        try {
            conn.Connect();
            String sql = "SELECT * FROM MON";
            try (PreparedStatement pre = conn.preparedStatement(sql)) {
                ResultSet rs = pre.executeQuery();
                while (rs.next()) {
                    monDTO item = new monDTO(rs.getString("MaMon"), rs.getString("TenMon"));
                    list.add(item);
                }
            }
            conn.disconnect();
        } catch (SQLException e) {
            System.err.println("Lay danh sach mon that bai");
        }
        return list;
    }

    public String getMaMonByName(String name) {
        String MaMon = "";
        try {
            conn.Connect();
            String sql = "SELECT MaMon FROM MON WHERE TenMon=?";
            try (PreparedStatement pre = conn.preparedStatement(sql)) {
                pre.setString(1, name);
                ResultSet rs = pre.executeQuery();
                if (rs.next()) {
                    MaMon = rs.getString("MaMon");
                }
            }
            conn.disconnect();
        } catch (SQLException e) {
            System.err.println("Doi ma mon that bai" + e.getMessage());
        }
        return MaMon;
    }

    public String getNameByMaMon(String MaMon) {
        String TenMon = "";
        try {
            conn.Connect();
            String sql = "SELECT TenMon FROM MON WHERE MaMon=?";
            try (PreparedStatement pre = conn.preparedStatement(sql)) {
                pre.setString(1, MaMon);
                ResultSet rs = pre.executeQuery();
                if (rs.next()) {
                    TenMon = rs.getString("TenMon");
                }
            }
            conn.disconnect();
        } catch (SQLException e) {
            System.err.println("Doi ma mon that bai" + e.getMessage());
        }
        return TenMon;
    }
    
    public boolean ThemMon(monDTO m){
        boolean success = false;
        try{
            conn.Connect();
            String check = "SELECT COUNT(*) FROM MON WHERE TenMon=?";
            PreparedStatement preCheck = conn.preparedStatement(check);
            preCheck.setString(1, m.getTenMon());
            ResultSet rs = preCheck.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if(count > 0){
                System.out.println("Môn đã tồn tại trong csdl");
                return false;
            }
            String sql = "INSERT INTO MON(MaMon,TenMon) VALUES(?,?)";
            PreparedStatement pre = conn.preparedStatement(sql);
            pre.setString(1, m.getMaMon());
            pre.setString(2, m.getTenMon());
            success = pre.executeUpdate() > 0;
            conn.disconnect();
        }catch(SQLException e){
            System.out.println("Them mon that bai" + e.getMessage());
        }
        return success;
    }
    
    

    public static void main(String[] args) {
        monDAO2 dao = new monDAO2();
        ArrayList<monDTO> arr = dao.listMon();
        for (monDTO x : arr) {
            System.out.println(x.getTenMon());
        }
    }
}
