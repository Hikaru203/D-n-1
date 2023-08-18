/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.phones.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.PasswordAuthentication;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import com.phones.DAO.BaoCaoDao;
import com.phones.DAO.BaoHanhDao;
import com.phones.DAO.HangMayDao;
import com.phones.DAO.HoaDonDao;
import com.phones.DAO.KhachHangDao;
import com.phones.DAO.LinhKienDao;
import com.phones.DAO.NhaCungCapDao;
import com.phones.DAO.NhanVienDAO;
import com.phones.DAO.PhieuNhapDao;
import com.phones.DAO.SuaChuaDao;
import com.phones.DAO.ThongTinMayDao;
import com.phones.helper.DBConnection;
import com.phones.helper.DateHelper;
import com.phones.helper.DialogHelper;
import com.phones.helper.JdbcHelper;
import com.phones.helper.XimageHelper;
import com.phones.model.BaoHanh;
import com.phones.model.HangMay;
import com.phones.model.HoaDon;
import com.phones.model.KhachHang;
import com.phones.model.LinhKien;
import com.phones.model.NhaCungCap;
import com.phones.model.NhanVien;
import com.phones.model.PhieuNhap;
import com.phones.model.SuaChua;
import com.phones.model.ThongTinMay;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author PC
 */
public class Main extends javax.swing.JFrame {

    public static DBConnection conn;

    public Main() {

        initComponents();
        setResizable(false);
        getContentPane().setBackground(new Color(250, 250, 250));

        tableEdit();
        setstatusSC(true);
        conn = new DBConnection();
        //changed();
        changedTK();
        //lblTrangThai.setSelectedIndex(0);
        lblTrangThai.setEnabled(false);
        txtKyThuatVien.setVisible(false);
        txtKH.setVisible(false);
        txtMaLK.setVisible(false);
        txtMaMay1.setVisible(false);
        txtMNCC.setVisible(false);
        txtMNCC1.setVisible(false);
        jButton49.setVisible(false);
        jButton53.setVisible(false);
        textField44.setVisible(false);
        txtRam1.setVisible(false);
        getTime();
        SinhMa();
        anLich();
        setstatusSua(true);
    }
    Timer timer;

    //SET THỜI GIAN HIỆN TẠI CHO LBL THỜI GIAN
    public void getTime() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDateTime time = LocalDateTime.now();
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");
                String formattedDate = time.format(timeFormat);
                lnlThoiGian.setText(formattedDate);
            }
        };
        timer = new Timer(1000, actionListener);
        timer.setInitialDelay(0);
        timer.start();
    }

    void anLich() {
        date1.addEventDateChooser(new EventDateChooser() {
            @Override
            public void dateSelected(SelectedAction action, SelectedDate date) {
                if (action.getAction() == SelectedAction.DAY_SELECTED) {
                    date1.hidePopup();
                    txtNgayTraMayThucTe.setText(txtNgayTraDuKien.getText());
                }
            }
        });
    }

    public void tableEdit() {

    }

    void setstatusSua(boolean status) {
        jButton12.setEnabled(!status);
        jButton48.setEnabled(!status);
        jButton64.setEnabled(!status);
        btnSuaNCC.setEnabled(!status);
        jButton44.setEnabled(!status);
        btnSuaNH.setEnabled(!status);
        jButton42.setEnabled(!status);
        btnXoaNH.setEnabled(!status);
        btnXoaNV.setEnabled(!status);
        btnXoaNCC.setEnabled(!status);
        jButton65.setEnabled(!status);
        jButton13.setEnabled(!status);
    }

    void SinhMa() {
        // TODO add your handling code here:
        loadHD();
        loadSC();
        loadHM();
        loadTTM();
        loadNCC();
        loadLK();
        loadKH();
        textField44.setText("HD" + (tblHoaDon.getRowCount() + 1));
        txtNgayNhap.setText(DateHelper.toString(DateHelper.now()));
        txtMaNhanVien.setText("NV" + (tblThongTinPhieuNhap.getRowCount() + 1));
        txtMaHang.setText("MH" + (tblThongTinBaoHanh2.getRowCount() + 1));
        txtMaMay.setText("MM" + (tblThongTinBaoHanh1.getRowCount() + 1));
        txtMaNhaCungCap.setText("NCC" + (tblNhaCungCap.getRowCount() + 1));
        txtMaLinhKien.setText("LK" + (tblKhoHang.getRowCount() + 1));
        txtMaKhachHang.setText("KH" + (tblKhachHang.getRowCount() + 1));
        txtMaSuaChua.setText("SC" + (tblDichVu.getRowCount() + 1));

    }

    //CODE PHÂN QUYỀN
    void PhanQuyen() {
        if (XimageHelper.USER.getChucVu().equals("Nhân viên")) {
            try {
                NhanVien model3 = nvdao.findById(XimageHelper.USER.getMaNhanVien());
                if (model3 != null) {
                    this.setModelNV(model3);
                    txtNhanVienNhanMay1.setText(model3.getMaNhanVien());
                    txtNhanVienNhanMay.setText(model3.getHoTen());
                }
            } catch (Exception e) {
                DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
            }
            jPanel4.setVisible(false);
            jPanel6.setVisible(false);
            tabNhanVien.setEnabledAt(0, false);
            tabNhanVien.setSelectedIndex(1);
            btnThemNV.setEnabled(false);
            btnXoaNV.setEnabled(false);
            btnThemNCC.setEnabled(false);
            btnXoaNCC.setEnabled(false);
            btnSuaNCC.setEnabled(false);
            btnLamMoiNCC.setEnabled(false);
            btnThemNH.setEnabled(false);
            btnXoaNH.setEnabled(false);
            btnSuaNH.setEnabled(false);
            btnLamMoiNH.setEnabled(false);
            txtMaNhanVien.setEditable(false);
            txtHoTenNV.setEditable(false);
            txtEmailNV.setEditable(false);
            txtSoDienThoaiNV.setEditable(false);
            txtDiaChiNV.setEditable(false);
            txtLuongNV.setEditable(false);
            cboChucVu.setEnabled(false);
            btnLamMoiNV.setEnabled(false);
            lblNhanVien.setVisible(false);
            tblThongTinPhieuNhap.setVisible(false);
            lblKhoHang.setVisible(false);
            lblDoanhThu.setVisible(false);
            tableDark1.setVisible(false);
            tableDark3.setVisible(false);
            txtPhiSuaChua.setEnabled(false);
            btnSuaNV.setEnabled(true);
        } else if (XimageHelper.USER.getChucVu().equals("Nhân viên kỹ thuật")) {
            try {
                NhanVien model3 = nvdao.findById(XimageHelper.USER.getMaNhanVien());
                if (model3 != null) {
                    this.setModelNV(model3);
                }
            } catch (Exception e) {
                DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
            }
            txtMaNhanVien.setEditable(false);
            txtHoTenNV.setEditable(false);
            txtEmailNV.setEditable(false);
            txtSoDienThoaiNV.setEditable(false);
            txtDiaChiNV.setEditable(false);
            txtLuongNV.setEditable(false);
            cboChucVu.setEnabled(false);
            tabNhanVien.setEnabledAt(0, false);
            tabNhanVien.setSelectedIndex(1);
            btnThemNV.setEnabled(false);
            btnXoaNV.setEnabled(false);
            btnSuaNV.setEnabled(false);
            btnLamMoiNV.setEnabled(false);

            tblThongTinPhieuNhap.setVisible(false);
            lblKhachHang.setVisible(false);
            lblKhoHang.setVisible(false);
            lblNhanVien.setVisible(false);
            lblThongTinBanHang.setVisible(false);
            lblHoaDon.setVisible(false);
            lblThongTinMay.setVisible(false);
            lblNhangHang.setVisible(false);
            lblDoanhThu.setVisible(false);
            btnSuaNV.setEnabled(true);
        } else if (XimageHelper.USER.getChucVu().equals("Quản Lý")) {
            try {
                NhanVien model3 = nvdao.findById(XimageHelper.USER.getMaNhanVien());
                if (model3 != null) {
                    this.setModelNV(model3);
                    txtNhanVienNhanMay1.setText(model3.getMaNhanVien());
                    txtNhanVienNhanMay.setText(model3.getHoTen());
                }
            } catch (Exception e) {
                DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
            }
            jPanel4.setVisible(false);
            jPanel6.setVisible(false);
            jLabel9.setVisible(false);
            tableDark1.setVisible(false);
            tableDark3.setVisible(false);
            txtPhiSuaChua.setEnabled(false);
        }

    }

    //CODE SET ẢNH
    void selectImage() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (XimageHelper.saveLogo(file)) {
                // Hiển thị hình lên form
                lblHinhAnh.setIcon(XimageHelper.readLogo(file.getName()));
                lblHinhAnh.setToolTipText(file.getName());

            }
        }
    }
    private static final String PATTERN = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";
    private static final String PATTERNSDT = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";
    KhachHangDao khdao = new KhachHangDao();
    HangMayDao hmdao = new HangMayDao();
    ThongTinMayDao ttmdao = new ThongTinMayDao();
    NhanVienDAO nvdao = new NhanVienDAO();
    NhaCungCapDao nccdao = new NhaCungCapDao();
    LinhKienDao lkdao = new LinhKienDao();
    PhieuNhapDao pndao = new PhieuNhapDao();
    SuaChuaDao scdao = new SuaChuaDao();
    HoaDonDao hddao = new HoaDonDao();
    BaoHanhDao bhdao = new BaoHanhDao();
    BaoCaoDao bcd = new BaoCaoDao();

    public String MaHang;

    //CODE CHECK TRÙNG MÃ LINH KIỆN
    public boolean checkTrungMLK() throws SQLException {
        //check null
        if (txtMaLinhKien.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Mã linh kiện không được rỗng");
            txtMaLinhKien.requestFocus();
            return false;
        }
        String sql = "select MaLinhKien from LinhKien";
        ResultSet result = JdbcHelper.executeQuery(sql);
        while (result.next()) {
            String str = result.getString(1);
            if (str.equalsIgnoreCase(txtMaLinhKien.getText())) {
                DialogHelper.alert(this, "Mã Linh kiên đã tồn tại");
                txtMaLinhKien.requestFocus();
                return false;
            }
        }

        return true;
    }

    public boolean checkvalidateLK() {
        if (txtTenLinhKien.getText().equals("")) {
            DialogHelper.alert(this, "Tên linh kiện không được rỗng");
            txtTenLinhKien.requestFocus();
            return false;
        }
        if (txtSoLuong.getText().equals("")) {
            DialogHelper.alert(this, "Số lượng không được rỗng");
            txtSoLuong.requestFocus();
            return false;
        }
        try {
            Integer.parseInt(txtSoLuong.getText());
        } catch (Exception e) {
            DialogHelper.alert(this, "Số lượng phải nhập số");
            txtSoLuong.requestFocus();
        }
        if (Integer.parseInt(txtSoLuong.getText()) < 0) {
            DialogHelper.alert(this, "Số lượng phải lớn hơn 0");
            txtSoLuong.requestFocus();
            return false;
        }
        if (txtGiaBan.getText().equals("")) {
            DialogHelper.alert(this, "Giá bán không được rỗng");
            txtGiaBan.requestFocus();
            return false;
        }
        try {
            Integer.parseInt(txtGiaBan.getText());
        } catch (Exception e) {
            DialogHelper.alert(this, "Giá bán phải nhập số");
            txtGiaBan.requestFocus();
        }
        if (Integer.parseInt(txtGiaBan.getText()) < 0) {
            DialogHelper.alert(this, "Giá bán phải lớn hơn 0");
            txtGiaBan.requestFocus();
            return false;
        }
        return true;
    }

    //CODE CHECK TRÙNG KHÁCH HÀNG
    public boolean checkTrungKH() throws SQLException {
        String sql = "select MaKhachHang from KhachHang";
        ResultSet result = JdbcHelper.executeQuery(sql);
        while (result.next()) {
            String str = result.getString(1);
            if (str.equalsIgnoreCase(txtMaKhachHang.getText())) {
                DialogHelper.alert(this, "Mã khách hàng đã tồn tại");
                txtMaKhachHang.requestFocus();
                return false;
            }
        }

        return true;
    }

    //CODE CHECK TRÙNG SỬA CHỮA
    public boolean checkTrungSC() throws SQLException {
        String sql = "select SuaChua.MaSuaChua,SuaChua_LinhKien.MaLinhKien from SuaChua inner join SuaChua_LinhKien on SuaChua.MaSuaChua=SuaChua_LinhKien.MaSuaChua";
        ResultSet result = JdbcHelper.executeQuery(sql);
        while (result.next()) {
            String str = result.getString(1);
            String str1 = result.getString(2);
            if (str.equalsIgnoreCase(txtMaSuaChua.getText()) && str1.equalsIgnoreCase(txtMaLK.getText())) {
                DialogHelper.alert(this, "Linh kiện trên đã được thêm vào sửa chữa máy này");
                return false;
            }
        }

        return true;
    }

    //CODE CHECK RỖNG SỬA CHỮA
    boolean checkSC() {
        if (txtMaSuaChua.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Mã sửa chữa không được rỗng");
            txtMaSuaChua.requestFocus();
            return false;
        }

        if (txtNgayTraDuKien.getText().equals("")) {
            DialogHelper.alert(this, "Ngày trả máy dự kiến không được rỗng");
            txtNgayTraDuKien.requestFocus();
            return false;
        }
        if (txtMoTaLoi.getText().equals("")) {
            DialogHelper.alert(this, "Mô tả lỗi không được rỗng");
            txtMoTaLoi.requestFocus();
            return false;
        }
        if (txtTinhTrangMay.getText().equals("")) {
            DialogHelper.alert(this, "Tình trạng máy không được rỗng");
            txtTinhTrangMay.requestFocus();
            return false;
        }
        if (txtNgayTraMayThucTe.getText().equals("")) {
            txtNgayTraMayThucTe.setText("");
        }
        return true;
    }

    //CODE CHECK TRÙNG NHÀ CUNG CẤP
    public boolean checkTrungNCC() throws SQLException {
        String sql = "select MaNhaCungCap from NhaCungCap";
        ResultSet result = JdbcHelper.executeQuery(sql);
        while (result.next()) {
            String str = result.getString(1);
            if (str.equalsIgnoreCase(txtMaNhaCungCap.getText())) {
                DialogHelper.alert(this, "Mã nhà cung cấp đã tồn tại");
                txtMaNhaCungCap.requestFocus();
                return false;
            }
        }

        return true;
    }

    //CODE CHECK TRÙNG NHÂN VIÊN
    public boolean checkTrungNV() throws SQLException {
        String sql = "select MaNhanVien from NhanVien";
        ResultSet result = JdbcHelper.executeQuery(sql);
        while (result.next()) {
            String str = result.getString(1);
            if (str.equalsIgnoreCase(txtMaNhanVien.getText())) {
                DialogHelper.alert(this, "Mã nhân viên đã tồn tại");
                txtMaNhanVien.requestFocus();
                return false;
            }
        }

        return true;
    }

    //CODE CHECK TRÙNG THÔNG TIN MÁY
    public boolean checkTrungTTM() throws SQLException {
        String sql = "select MaMay from May";
        ResultSet result = JdbcHelper.executeQuery(sql);
        while (result.next()) {
            String str = result.getString(1);
            if (str.equalsIgnoreCase(txtMaMay.getText())) {
                DialogHelper.alert(this, "Mã máy đã tồn tại");
                txtMaMay.requestFocus();
                return false;
            }
        }

        return true;
    }

    //CODE CHECK TRÙNG HẢNG MÁY
    public boolean checkTrungHM() throws SQLException {
        String sql = "select MaHang from Hang";
        ResultSet result = JdbcHelper.executeQuery(sql);
        while (result.next()) {
            String str = result.getString(1);
            if (str.equalsIgnoreCase(txtMaHang.getText())) {
                DialogHelper.alert(this, "Mã nhãn hàng đã tồn tại");
                txtMaHang.requestFocus();
                return false;
            }
        }

        return true;
    }

    //CODE CHECK TRÙNG PHIẾU NHẬP
    public boolean checkTrungPN() throws SQLException {
        String sql = "select MaPhieuNhap from PhieuNhap";
        ResultSet result = JdbcHelper.executeQuery(sql);
        while (result.next()) {
            String str = result.getString(1);
            if (str.equalsIgnoreCase(txtMaPhieuNhap.getText())) {
                DialogHelper.alert(this, "Mã phiếu nhập đã tồn tại");
                txtMaPhieuNhap.requestFocus();
                return false;
            }
        }

        if (txtNgayNhap.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Ngày nhập không được rỗng");
            txtNgayNhap.requestFocus();
            return false;
        }
        return true;
    }

    //CODE CHECK TRÙNG LINH KIỆN
//    public boolean checkTrungPN2() throws SQLException {
//        String sql = "select TenHang from LinhKien";
//        ResultSet result = JdbcHelper.executeQuery(sql);
//        while (result.next()) {
//            String str = result.getString(1);
//            if (str.equalsIgnoreCase(txtTenLinhKienNhap.getText())) {
//                return false;
//            }
//        }
//        return true;
//    }
    public boolean checkTTSC(String maSuaChua) throws SQLException {
        String sql = "select TrangThai,MaSuaChua from SuaChua where MaSuaChua=?";
        ResultSet result = JdbcHelper.executeQuery(sql, maSuaChua);
        while (result.next()) {
            String str = result.getString(1);
            String str1 = result.getString(2);
            if (str.equalsIgnoreCase("Đã giao máy")) {
                DialogHelper.alert(this, "Máy này đã giao cho khách rùi!");
                return false;
            } else if (!str.equalsIgnoreCase("Đã Sửa")) {
                DialogHelper.alert(this, "Máy này hiện chưa sửa xong vui lòng kiểm tra lại");
                return false;
            } else {
                SuaChua model = getModelSC1(textField43.getText(), "Đã Sửa");
                try {
                    scdao.updateSL1(model);
                    this.loadHD();
                    DialogHelper.alert(this, "Nhận máy thành công!");
                    break;
                } catch (Exception e) {
                    DialogHelper.alert(this, "Nhận máy thất bại!");
                    return false;
                }
            }
        }
        return true;
    }

    //Tìm kiếm
    //***Khách hàng
    //Load khách hàng lên bảng
    public boolean checkKhachHang() {
        if (txtMaKhachHang.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Mã khách hàng không được rỗng");
            txtMaKhachHang.requestFocus();
            return false;
        }
        if (txtTenKhachHang.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Tên khách hàng không được rỗng");
            txtTenKhachHang.requestFocus();
            return false;
        }

        if (txtEmail.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Email không được rỗng");
            txtEmail.requestFocus();
            return false;
        }
        Matcher matcher = Pattern.compile(PATTERN).matcher(txtEmail.getText());
        if (!matcher.matches()) {
            DialogHelper.alert(this, "Email sai định dạng");
            return false;
        }
        if (txtDiaChi.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Địa chỉ không được rỗng");
            txtDiaChi.requestFocus();
            return false;
        }
        if (txtSoDienThoai.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Số điện thoại không được rỗng");
            txtSoDienThoai.requestFocus();
            return false;
        }
        Matcher matcherSDT = Pattern.compile(PATTERNSDT).matcher(txtSoDienThoai.getText());
        if (!matcherSDT.matches()) {
            DialogHelper.alert(this, "Số điện thoại sai định dạng");
            txtSoDienThoaiNV.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validafromNV() {
        if (txtMaNhanVien.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Mã nhân viên không được rỗng");
            txtMaNhanVien.requestFocus();
            return false;
        }
        if (txtHoTenNV.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Tên nhà cung cấp không được rỗng");
            txtHoTenNV.requestFocus();
            return false;
        }
        if (txtTaiKhoanNV.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Tài khoản không được để rỗng");
            txtTaiKhoanNV.requestFocus();
            return false;
        }
        if (txtMatkhauNV.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Mật khẩu được để rỗng");
            txtMatkhauNV.requestFocus();
            return false;
        }

        if (txtEmailNV.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Email không được rỗng");
            txtEmailNV.requestFocus();
            return false;
        }
        Matcher matcher = Pattern.compile(PATTERN).matcher(txtEmailNV.getText());
        if (!matcher.matches()) {
            DialogHelper.alert(this, "Email sai định dạng");
            return false;
        }

        if (txtSoDienThoaiNV.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Số điện thoại không được rỗng");
            txtSoDienThoaiNV.requestFocus();
            return false;
        }
        try {
            Integer.valueOf(txtSoDienThoaiNV.getText());
        } catch (NumberFormatException e) {
            DialogHelper.alert(this, "Số điện thoại không nhập chữ");
            return false;
        }
        Matcher matcherSDT = Pattern.compile(PATTERNSDT).matcher(txtSoDienThoaiNV.getText());
        if (!matcherSDT.matches()) {
            DialogHelper.alert(this, "Số điện thoại sai định dạng");
            txtSoDienThoaiNV.requestFocus();
            return false;
        }
        if (txtDiaChiNV.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Địa chỉ không được rỗng");
            txtDiaChiNV.requestFocus();
            return false;
        }
        if (txtLuongNV.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "lương không được rỗng");
            txtLuongNV.requestFocus();
            return false;
        }
        try {
            double getLuongNV = Double.parseDouble(txtLuongNV.getText().replace(",", ""));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lương phải là một số ");
            txtLuongNV.requestFocus();
            return false;
        }
        if (Double.parseDouble(txtLuongNV.getText().replace(",", "")) < 100000) {
            DialogHelper.alert(this, "lương phải lớn hơn 1.000.00");
            txtLuongNV.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validafromMay() {
        if (txtMaMay.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Mã máy không được rỗng");
            txtMaMay.requestFocus();
            return false;
        }
        if (txtTenMay.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Tên máy không được rỗng");
            txtTenMay.requestFocus();
            return false;
        }
        if (txtRam.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Ram không được rỗng");
            txtRam.requestFocus();
            return false;
        }
        try {
            Integer.parseInt(txtRam.getText());
        } catch (NumberFormatException e) {
            DialogHelper.alert(this, "Ram không nhập chữ");
            return false;
        }
        if (txtRom.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Rom không được rỗng");
            txtRom.requestFocus();
            return false;
        }
        try {
            Integer.parseInt(txtRom.getText());
        } catch (NumberFormatException e) {
            DialogHelper.alert(this, "Rom không nhập chữ");
            return false;
        }
        if (txtChip.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Chip không được rỗng");
            txtChip.requestFocus();
            return false;
        }

        return true;
    }

    void loadKH() {
        DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtTimKiem.getText();
            List<KhachHang> list = khdao.selectByKeyword(keyword);
            for (KhachHang kh : list) {
                Object[] row = {
                    kh.getMaKhachHanh(),
                    kh.getTenKhachHang(),
                    kh.getDiaChi(),
                    kh.getEmail(),
                    kh.getSoDienThoai()};
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //Lấy thông tin khách hàng
    void editKH() {
        try {
            String MaKhachHanh = (String) tblKhachHang.getValueAt(this.index, 0);
            KhachHang model = khdao.findById(MaKhachHanh);
            if (model != null) {
                this.setModel(model);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //chuyển thông tin khách hàng từ bảng qua form
    void setModel(KhachHang model) {
        txtMaKhachHang.setText(model.getMaKhachHanh());
        txtTenKhachHang.setText(model.getTenKhachHang());
        txtEmail.setText(String.valueOf(model.getEmail()));
        txtDiaChi.setText(String.valueOf(model.getDiaChi()));
        txtSoDienThoai.setText(model.getSoDienThoai());
    }

    KhachHang getModelKH() {
        KhachHang model = new KhachHang();
        //if (checkKhachHang()) {
        model.setMaKhachHanh(txtMaKhachHang.getText());
        model.setTenKhachHang(txtTenKhachHang.getText());
        model.setEmail(txtEmail.getText());
        model.setDiaChi(txtDiaChi.getText());
        model.setSoDienThoai(txtSoDienThoai.getText());
        //}
        return model;
    }

    void insertKH() {
        KhachHang model = getModelKH();
        try {
            if (model.getMaKhachHanh().length() > 5) {
                DialogHelper.alert(this, "Mã Khách Hàng Không Được Quá 5 Ký Tự");
                return;
            }
            khdao.insert(model);
            this.loadKH();
            this.clearKH();
            DialogHelper.alert(this, "Thêm Thành Công");
        } catch (Exception e) {
            DialogHelper.alert(this, "Thêm Thất bại");
        }

    }

    //LÀM MỚI FROM KHÁCH HÀNG
    void clearKH() {
        KhachHang model = new KhachHang();
        this.setModelKH(model);
        txtMaKhachHang.setText("KH" + (tblKhachHang.getRowCount() + 1));
        setstatusSua(true);
    }

    void setModelKH(KhachHang model) {
        txtMaKhachHang.setText(model.getMaKhachHanh());
        txtTenKhachHang.setText(model.getTenKhachHang());
        txtDiaChi.setText(model.getDiaChi());
        txtEmail.setText(model.getEmail());
        txtSoDienThoai.setText(model.getSoDienThoai());
    }

    //CẬP NHẬT KHÁCH HÀNG
    void updateKH() {
        KhachHang model = getModelKH();
        try {
            if (model.getMaKhachHanh().length() > 5) {
                DialogHelper.alert(this, "Mã Khách Hàng Không Được Quá 5 Ký Tự");
                return;
            }
            khdao.update(model);
            this.loadKH();
            DialogHelper.alert(this, "Cập nhật thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Cập nhật thất bại!");
        }

    }

    //XÓA KHÁCH HÀNG
    void deleteKH() {
        if (DialogHelper.confirm(this, "Bạn có muốn xóa hay không?")) {
//            if (checkDeleteChuyenDe()) {
            String makh = txtMaKhachHang.getText();

            khdao.delete(makh);
            this.loadKH();
            this.clearKH();
            DialogHelper.alert(this, "Xóa thành công!");

//            }
        }
    }

    //***Nhân viên
    //load thông tin máy lên bảng nhân viên
    void loadNV() {
        DefaultTableModel model = (DefaultTableModel) tblThongTinPhieuNhap.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtTimKiem.getText();
            List<NhanVien> list = nvdao.selectByKeyword(keyword);
            for (NhanVien nv : list) {
                Object[] row = {
                    nv.getMaNhanVien(),
                    nv.getHoTen(),
                    nv.getTaiKhoan(),
                    nv.getEmail(),
                    nv.getSoDienThoai(),
                    nv.getDiaChi(),
                    nv.getLuong(),
                    nv.getChucVu(),};
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //chỉnh sửa thông tin nhân viên
    void editNV() {
        try {
            String maNhanVien = (String) tblThongTinPhieuNhap.getValueAt(this.index, 0);
            NhanVien model3 = nvdao.findById(maNhanVien);
            if (model3 != null) {
                this.setModelNV(model3);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //Chuyển thông tin nhân viên từ bảng vào form
    void setModelNV(NhanVien model) {
        txtMaNhanVien.setText(model.getMaNhanVien());
        txtHoTenNV.setText(model.getHoTen());
        txtTaiKhoanNV.setText(String.valueOf(model.getTaiKhoan()));
        txtMatkhauNV.setText(String.valueOf(model.getMatKhau()));
        txtEmailNV.setText(model.getEmail());
        txtSoDienThoaiNV.setText(String.valueOf(model.getSoDienThoai()));
        txtDiaChiNV.setText(model.getDiaChi());
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        txtLuongNV.setText(formatter.format(model.getLuong()));
        cboChucVu.setSelectedItem(String.valueOf(model.getChucVu()));
        if (model.getHinhAnh() != null) {
            lblHinhAnh.setIcon(XimageHelper.readLogo(model.getHinhAnh()));
        }
    }

    NhanVien getModelNV() {
        NhanVien model = new NhanVien();
//        if (validafromChuyenDe()) {
        model.setMaNhanVien(txtMaNhanVien.getText());
        model.setHoTen(txtHoTenNV.getText());
        model.setTaiKhoan(txtTaiKhoanNV.getText());
        model.setMatKhau(txtMatkhauNV.getText());
        model.setEmail(txtEmailNV.getText());
        model.setSoDienThoai(txtSoDienThoaiNV.getText());
        model.setDiaChi(txtDiaChiNV.getText());
        model.setLuong(Float.valueOf(txtLuongNV.getText().replace(",", "")));
        model.setChucVu(String.valueOf(cboChucVu.getSelectedItem()));
        model.setHinhAnh(lblHinhAnh.getToolTipText());
//        }
        return model;
    }

    void clearNV() {
        NhanVien model = new NhanVien();
        this.setModelNV(model);
        cboChucVu.setSelectedIndex(0);
        lblHinhAnh.setIcon(null);
        txtMaNhanVien.setText("NV" + (tblThongTinPhieuNhap.getRowCount() + 1));
        txtTaiKhoanNV.setText("");
        txtMatkhauNV.setText("");
        txtSoDienThoaiNV.setText("");
        setstatusSua(true);
    }

    void insertNV() {
        NhanVien model = getModelNV();
        try {
            if (model.getMaNhanVien().length() > 5) {
                DialogHelper.alert(this, "Mã Nhân Viên Không Được Quá 5 Ký Tự");
                return;
            }
            nvdao.insert(model);
            this.loadNV();
            this.clearNV();
            DialogHelper.alert(this, "Thêm Thành Công");
        } catch (Exception e) {
            System.out.println("Lỗi!");
        }

    }

    void updateNV() {
        NhanVien model = getModelNV();
        try {
            nvdao.update(model);
            this.loadNV();
            DialogHelper.alert(this, "Cập nhật thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Cập nhật thất bại!");
        }

    }

    void deleteNV() {
        if (DialogHelper.confirm(this, "Bạn có muốn xóa hay không?")) {
//            if (checkDeleteChuyenDe()) {
            String maNV = txtMaNhanVien.getText();
            nvdao.delete(maNV);
            this.loadNV();
            this.clearNV();
            DialogHelper.alert(this, "Xóa thành công!");

//            }
        }
    }

    //***thông tin hảng máy
    //load thông tin hảng máy lên bảng
    void loadHM() {
        DefaultTableModel model = (DefaultTableModel) tblThongTinBaoHanh2.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtTimKiem.getText();
            List<HangMay> list = hmdao.selectByKeyword(keyword);
            for (HangMay hm : list) {
                Object[] row = {
                    hm.getMaHang(),
                    hm.getTenHang()};
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //chỉnh sửa thông tin hảng máy
    void editHM() {
        try {
            String MaHang = (String) tblThongTinBaoHanh2.getValueAt(this.index, 0);
            HangMay model2 = hmdao.findById(MaHang);
            if (model2 != null) {
                this.setModelHM(model2);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //chuyển thông tin hảng máy từ bảng qua form
    void setModelHM(HangMay model) {
        txtMaHang.setText(model.getMaHang());
        txtTenHang.setText(model.getTenHang());
    }

    //hiện combo box hảng trên trên form máy
    void fillComBoBoxHang() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboHangMay.getModel();
        model.removeAllElements();
        List<HangMay> list = hmdao.select();
        for (HangMay hm : list) {
            index++;
            model.addElement(hm);
        }
        cboHangMay.setSelectedIndex(0);
    }

    //Lấy giá trị của người dùng nhập vào đưa vào Hảng máy
    HangMay getModelHM() {
        HangMay model = new HangMay();

        model.setMaHang(txtMaHang.getText());
        model.setTenHang(txtTenHang.getText());

        return model;
    }

    //Thêm dữ liệu vào database
    void insertHM() {
        HangMay model = getModelHM();
        try {
            if (model.getMaHang().length() > 5) {
                DialogHelper.alert(this, "Mã Chuyên Đề Không Được Quá 5 Ký Tự");
                return;
            }
            hmdao.insert(model);
            this.loadHM();
            this.clearHM();
            DialogHelper.alert(this, "Thêm mới thành công!");
        } catch (Exception e) {

        }
    }

    //chỉnh sửa thông tin hảng máy
    void updateHM() {
        HangMay model = getModelHM();
        try {
            hmdao.update(model);
            this.loadHM();
            DialogHelper.alert(this, "Cập nhật thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Cập nhật thất bại!");
        }

    }

    void deleteHM() {
        if (DialogHelper.confirm(this, "Bạn có muốn xóa hay không?")) {
            String MaHang = txtMaHang.getText();
            try {
                hmdao.delete(MaHang);
                this.loadHM();
                this.clearHM();
                DialogHelper.alert(this, "Xóa thành công!");
            } catch (Exception e) {
                System.out.println("Lỗi!");

            }
        }
    }

    //Làm mới form nhập
    void clearHM() {
        this.setModelHM(new HangMay());
        txtMaHang.setText("MH" + (tblThongTinBaoHanh2.getRowCount() + 1));
        setstatusSua(true);

    }

    public boolean validafromHangMay() {
        if (txtMaHang.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Mã hảng không được rỗng");
            txtMaHang.requestFocus();
            return false;
        }
        if (txtTenHang.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Tên hảng không được rỗng");
            txtTenHang.requestFocus();
            return false;
        }
        return true;
    }

    //***thông tin máy
    //load thông tin máy lên bảng máy
    void loadTTM() {
        DefaultTableModel model = (DefaultTableModel) tblThongTinBaoHanh1.getModel();
        model.setRowCount(0);
        try {

            String keyword = txtTimKiem.getText();
            List<ThongTinMay> list = ttmdao.selectByKeyword(keyword);
            for (ThongTinMay ttm : list) {
                Object[] row = {
                    ttm.getMaMay(),
                    ttm.getTenMay(),
                    ttm.getTenHang(),
                    ttm.getRam(),
                    ttm.getRom(),
                    ttm.getChip(),};
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //chỉnh sửa thông tin hảng máy
    void editTTM() {
        try {
            String MaMay = (String) tblThongTinBaoHanh1.getValueAt(this.index, 0);
            ThongTinMay model7 = ttmdao.findById(MaMay);
            if (model7 != null) {
                this.setModelTTM(model7);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //chuyển thông tin hảng máy từ bảng qua form
    void setModelTTM(ThongTinMay model) {
        txtMaMay.setText(model.getMaMay());
        txtTenMay.setText(model.getTenMay());
        cboHangMay.setSelectedItem(model.getTenHang());
        txtRam.setText(String.valueOf(model.getRam()));
        txtRom.setText(String.valueOf(model.getRom()));
        txtChip.setText(model.getChip());
    }

    ThongTinMay getModelTTM() {
        ThongTinMay model = new ThongTinMay();
        model.setMaMay(txtMaMay.getText());
        model.setTenMay(txtTenMay.getText());
        model.setMaHang(txtRam1.getText());
        model.setTenHang(cboHangMay.getSelectedItem().toString());
        model.setRam(Integer.parseInt(txtRam.getText()));
        model.setRom(Integer.parseInt(txtRom.getText()));
        model.setChip(txtChip.getText());
        return model;
    }

    //Thêm dữ liệu vào database
    void insertTTM() {
        ThongTinMay model = getModelTTM();
        try {
            if (model.getMaMay().length() > 5) {
                DialogHelper.alert(this, "Mã máy Không Được Quá 5 Ký Tự");
                return;
            }
            ttmdao.insert(model);
            this.loadTTM();
            this.clearTTM();
            DialogHelper.alert(this, "Thêm mới thành công!");
        } catch (Exception e) {

        }
    }

    void updateTTM() {
        ThongTinMay model = getModelTTM();
        if (validafromMay()) {
            try {
                ttmdao.update(model);
                this.loadTTM();
                DialogHelper.alert(this, "Cập nhật thành công!");
            } catch (Exception e) {
                DialogHelper.alert(this, "Cập nhật thất bại!");
            }
        } else {
            DialogHelper.alert(this, "Cập nhật thất bại!");
        }

    }

    void deleteTTM() {
        if (DialogHelper.confirm(this, "Bạn có muốn xóa hay không?")) {
            String MaMay = txtMaMay.getText();
            try {
                ttmdao.delete(MaMay);
                this.loadTTM();
                this.clearTTM();
                DialogHelper.alert(this, "Xóa thành công!");
            } catch (Exception e) {
                System.out.println("Lỗi!");

            }
        }
    }

    void clearTTM() {
        this.setModelTTM(new ThongTinMay());
        txtMaMay.setText("MM" + (tblThongTinBaoHanh1.getRowCount() + 1));
        setstatusSua(true);
    }

    //***Nhà cung cấp
    void loadNCC() {
        DefaultTableModel model = (DefaultTableModel) tblNhaCungCap.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtTimKiem.getText();
            List<NhaCungCap> list = nccdao.selectByKeyword(keyword);
            for (NhaCungCap ncc : list) {
                Object[] row = {
                    ncc.getMaNhaCungCap(),
                    ncc.getTenNhaCungCap(),
                    ncc.getDiaChi(),
                    ncc.getEmail(),
                    ncc.getSoDienThoai(),};
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //chỉnh sửa thông tin nhà cung cấp
    void editNCC() {
        try {
            String MaNhaCungCap = (String) tblNhaCungCap.getValueAt(this.index, 0);
            NhaCungCap model4 = nccdao.findById(MaNhaCungCap);
            if (model4 != null) {
                this.setModelNCC(model4);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    public boolean validafromNCC() {
        if (txtMaNhaCungCap.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Mã nhà cung cấp không được để rỗng");
            txtMaNhaCungCap.requestFocus();
            return false;
        }
        if (txtTenNhaCungCap.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Tên nhà cung cấp không được rỗng");
            txtTenNhaCungCap.requestFocus();
            return false;
        }
        if (txtDiaChiNCC.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Địa chỉ không được rỗng");
            txtDiaChiNCC.requestFocus();
            return false;
        }
        if (txtEmailNCC.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Email không được rỗng");
            txtEmailNCC.requestFocus();
            return false;
        }
        Matcher matcher = Pattern.compile(PATTERN).matcher(txtEmailNCC.getText());
        if (!matcher.matches()) {
            DialogHelper.alert(this, "email sai định dạng");
            return false;
        }
        if (txtSoDienThoaiNCC.getText().equalsIgnoreCase("")) {
            DialogHelper.alert(this, "Số điện thoại không được rỗng");
            txtSoDienThoaiNCC.requestFocus();
            return false;
        }
        try {
            Integer.valueOf(txtSoDienThoaiNCC.getText());
        } catch (NumberFormatException e) {
            DialogHelper.alert(this, "Số điện thoại không nhập chữ");
            return false;
        }
        Matcher matcherSDT = Pattern.compile(PATTERNSDT).matcher(txtSoDienThoaiNCC.getText());
        if (!matcherSDT.matches()) {
            DialogHelper.alert(this, "Số điện thoại sai định dạng");
            txtSoDienThoaiNCC.requestFocus();
            return false;
        }

        return true;
    }

    //chuyển thông tin nhà cung cấp lên form
    void setModelNCC(NhaCungCap model) {
        txtMaNhaCungCap.setText(model.getMaNhaCungCap());
        txtTenNhaCungCap.setText(model.getTenNhaCungCap());
        txtEmailNCC.setText(String.valueOf(model.getEmail()));
        txtDiaChiNCC.setText(String.valueOf(model.getDiaChi()));
        txtSoDienThoaiNCC.setText(model.getSoDienThoai());
    }

    NhaCungCap getModelNCC() {
        NhaCungCap model = new NhaCungCap();
        //if (validafromNCC()) {
        model.setMaNhaCungCap(txtMaNhaCungCap.getText());
        model.setTenNhaCungCap(txtTenNhaCungCap.getText());
        model.setDiaChi(txtDiaChiNCC.getText());
        model.setEmail(txtEmailNCC.getText());
        model.setSoDienThoai(txtSoDienThoaiNCC.getText());
        //}
        return model;
    }

    void insertNCC() {
        NhaCungCap model = getModelNCC();
        String comfirm = new String(txtMaNhaCungCap.getText());
        try {
            if (model.getMaNhaCungCap().length() > 5) {
                DialogHelper.alert(this, "Mã nhà cung cấp Không Được Quá 5 Ký Tự");
                return;
            }
//            if (comfirm.equals(nccdao.check()) ){
//               DialogHelper.alert(this, "Mã nhà cung cấp không được trùng");
//               return;
//            }
            nccdao.insert(model);
            this.loadNCC();
            DialogHelper.alert(this, "Thêm mới thành công!");

        } catch (Exception e) {
            System.out.println("Lỗi!");
        }

    }
    //clear nhà cung cấp

    void clearNCC() {
        this.setModelNCC(new NhaCungCap());
        txtMaNhaCungCap.setText("NCC" + (tblNhaCungCap.getRowCount() + 1));
        txtDiaChiNCC.setText("");
        txtEmailNCC.setText("");
        setstatusSua(true);
        //this.setStatus(true);
    }

    void deleteNCC() {
        if (DialogHelper.confirm(this, "Bạn có muốn xóa hay không?")) {
            //        if (checkDelteChuyenDe()) {
            String MaNhaCungCap = txtMaNhaCungCap.getText();
            try {
                nccdao.delete(MaNhaCungCap);
                this.loadNCC();
                this.clearNCC();
                DialogHelper.alert(this, "Xóa thành công!");
            } catch (Exception e) {

                //           }
            }
        }
    }

    void updateNCC() {
        NhaCungCap model = getModelNCC();
        try {
            nccdao.update(model);
            this.loadNCC();
            DialogHelper.alert(this, "Cập nhật thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Cập nhật thất bại!");
        }

    }

    //***thông tin linh kiện
    //load thông tin linh kiện lên bảng
    void loadLK() {
        DefaultTableModel model = (DefaultTableModel) tblKhoHang.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtTimKiem.getText();
            List<LinhKien> list = lkdao.selectByKeyword(keyword);
            for (LinhKien lk : list) {
                Object[] row = {
                    lk.getMaLinhKien(),
                    lk.getTenHang(),
                    lk.getSoLuong(),
                    lk.getGiaBan(),
                    lk.getMoTa(),};
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //chỉnh sửa thông tin linh kiện
    void editLK() {
        try {
            String MaLinhKien = (String) tblKhoHang.getValueAt(this.index, 0);
            LinhKien model5 = lkdao.findById(MaLinhKien);
            if (model5 != null) {
                this.setModelLK(model5);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //chuyển thông tin linh kiện máy từ bảng qua form
    void setModelLK(LinhKien model) {
        txtMaLinhKien.setText(model.getMaLinhKien());
        txtTenLinhKien.setText(model.getTenHang());
        txtSoLuong.setText(String.valueOf(model.getSoLuong()));
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        txtGiaBan.setText(formatter.format(model.getGiaBan()));
        txtMoTa.setText(model.getMoTa());
    }

    void setModelLK2(LinhKien model) {
        txtMNCC1.setText(model.getMaLinhKien());
    }

    void insertLK() {
        LinhKien model = getModelLK();
        try {
            lkdao.insert(model);
            this.loadLK();
            this.lamMoiLK();
            DialogHelper.alert(this, "Thêm mới thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Thêm mới thất bại!");
        }
    }

    public void suaLK() {
        LinhKien model = getModelLK();
        try {
            lkdao.update(model);
            this.loadLK();
            this.lamMoiLK();
            DialogHelper.alert(this, "Cập nhật thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Cập nhật thất bại!");
        }
    }

    public void xoaLK() {
        if (DialogHelper.confirm(this, "Bạn thực sự muốn xóa linh kiện này?")) {
            String maLK = txtMaLinhKien.getText();
            try {
                lkdao.delete(maLK);
                this.loadLK();
                this.lamMoiLK();
                DialogHelper.alert(this, "Xóa thành công!");
            } catch (Exception e) {
                DialogHelper.alert(this, "Xóa thất bại!");
            }
        }
    }

    public void lamMoiLK() {
        txtTenLinhKien.setText("");
        txtSoLuong.setText("");
        txtGiaBan.setText("");
        txtMoTa.setText("");
        txtMaLinhKien.setText("LK" + (tblKhoHang.getRowCount() + 1));
        setstatusSua(true);
    }

    LinhKien getModelLK() {
        LinhKien model = new LinhKien();
        model.setMaLinhKien(txtMaLinhKien.getText());
        model.setTenHang(txtTenLinhKien.getText());
        model.setSoLuong(Integer.valueOf(txtSoLuong.getText()));
        model.setGiaBan(Float.valueOf(txtGiaBan.getText().replace(",", "")));
        model.setMoTa(txtMoTa.getText());
        return model;
    }

    LinhKien getModelLK2(String maLinhKien, String tenLinhKien) {
        LinhKien model = new LinhKien();
        model.setMaLinhKien(maLinhKien);
        model.setTenHang(tenLinhKien);
        model.setSoLuong(0);
        return model;
    }

    //***thông tin phiếu nhập
    //load thông tin phiếu nhập lên bảng
    void loadPN() {
        DefaultTableModel model = (DefaultTableModel) tblPhieuNhap.getModel();
        model.setRowCount(0);
        try {
            List<PhieuNhap> list = pndao.selectByKeyword();
            for (PhieuNhap pn : list) {
                Object[] row = {
                    pn.getMaPhieuNhap(),
                    pn.getTenNhaCungCap(),
                    DateHelper.toString(pn.getNgayNhap()),
                    pn.getMoTa()};
                model.addRow(row);
            }
            loadTatCaLinhKien();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void loadTatCaLinhKien() {
        DefaultTableModel model1 = (DefaultTableModel) tableDark2.getModel();
        model1.setRowCount(0);
        try {
            List<LinhKien> list = lkdao.select();
            int i = 1;
            for (LinhKien lk : list) {
                Object[] row = {
                    lk.getMaLinhKien(),
                    lk.getTenHang(),
                    lk.getSoLuong()};
                model1.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void fillComBoBoxNhaCungCap() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboNhaCungCap.getModel();
        model.removeAllElements();
        List<NhaCungCap> list = nccdao.select();
        for (NhaCungCap nccdao : list) {
            model.addElement(nccdao);
        }

        cboHangMay.setSelectedIndex(0);
    }

    //chỉnh sửa thông tin phieuNhap
    void editPN() {
        try {
            String maPhieuNhap = (String) tblPhieuNhap.getValueAt(this.index, 0);
            PhieuNhap model6 = pndao.findById(maPhieuNhap);
            if (model6 != null) {
                this.setModelPN(model6);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //chuyển thông tin hảng máy từ bảng qua form
    void setModelPN(PhieuNhap model) {
        txtMaPhieuNhap.setText(model.getMaPhieuNhap());
        cboNhaCungCap.setSelectedItem(model.getTenNhaCungCap());
        txtNgayNhap.setText(DateHelper.toString(model.getNgayNhap()));
        txtMoTaNhap.setText(String.valueOf(model.getMoTa()));
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        txtMNCC.setText(model.getMaNhaCungCap());
        txtMNCC1.setText(model.getMaLinhKien());

        DefaultTableModel model1 = (DefaultTableModel) tableDark4.getModel();
        model1.setRowCount(0);
        try {
            List<PhieuNhap> list = pndao.select1(txtMaPhieuNhap.getText());
            //List<LinhKien> list1 = lkdao.select();
            for (PhieuNhap pn : list) {
                Object[] row = {
                    pn.getMaLinhKien(),
                    pn.getTenLinhKien(),
                    pn.getSoLuong(),
                    pn.getGiaThanh()};
                model1.addRow(row);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void setModelPN2(PhieuNhap model) {
        txtMNCC1.setText(model.getMaLinhKien());
    }

    PhieuNhap getModelPN() {
        PhieuNhap model = new PhieuNhap();
        model.setMaPhieuNhap(txtMaPhieuNhap.getText());
        model.setMaNhaCungCap(txtMNCC.getText());
        model.setTenNhaCungCap(String.valueOf(cboNhaCungCap.getSelectedItem()));
        model.setNgayNhap(DateHelper.toDate(txtNgayNhap.getText()));
        model.setMoTa(txtMoTaNhap.getText());
        model.setMaPhieuNhap(txtMaPhieuNhap.getText());
        model.setMaLinhKien("");
        return model;
    }

    PhieuNhap getModelPN2(String MaLinhKien, String tenLinhKien, int soLuong, String thanhTien) {
        PhieuNhap model = new PhieuNhap();
        model.setMaPhieuNhap(txtMaPhieuNhap.getText());
        model.setMaLinhKien(MaLinhKien);
        model.setTenLinhKien(tenLinhKien);
        model.setSoLuong(soLuong);
        model.setGiaThanh(Float.valueOf(thanhTien.replace(",", "")));
        return model;
    }

    void insertPN() {
        PhieuNhap model = getModelPN();
        try {
            if (model.getMaPhieuNhap().length() > 5) {
                DialogHelper.alert(this, "Mã phiếu nhập Không Được Quá 5 Ký Tự");
                return;
            }
            pndao.insert(model);
            this.loadPN();
            DialogHelper.alert(this, "Thêm mới thành công!");
            loadLK();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void insertLKPN(String MaLinhKien, String tenLinhKien, int soLuong, String thanhTien) {
        PhieuNhap model2 = getModelPN2(MaLinhKien, tenLinhKien, soLuong, thanhTien);
        try {
            pndao.insert2(model2);
            this.loadPN();
            DialogHelper.alert(this, "Thêm Thành Công");
            loadLK();
            pndao.updateSL(model2);
        } catch (Exception e) {
        }

    }

    void insertLKMoi(String tenLinhKien) {
        LinhKien model2 = getModelLK2("LK" + (tblKhoHang.getRowCount() + 1), tenLinhKien);
        try {
            lkdao.insertNhap(model2);
            this.loadPN();
            DialogHelper.alert(this, "Thêm Thành Công");
        } catch (Exception e) {
            DialogHelper.alert(this, "Linh kiện đã có trong sửa chữa");
        }

    }

    void clearPN() {
        this.setModelPN(new PhieuNhap());
        cboNhaCungCap.setSelectedIndex(0);
        txtMoTaNhap.setText("");
        txtNgayNhap.setText(DateHelper.toString(DateHelper.now()));

    }

    //***thông tin sửa chữa
    //load thông tin phiếu nhập lên bảng
    void setstatusSC(boolean status) {
        jButton47.setEnabled(status);
        txtNhanVienNhanMay1.setEnabled(!status);
        txtNhanVienNhanMay.setEnabled(!status);
        jButton49.setEnabled(!status);
        jButton53.setEnabled(!status);
        cboTenKhach.setEnabled(status);
        List<NhanVien> list = nvdao.select();
        List<SuaChua> list1 = scdao.select();
        for (int i = 0; i < list.size(); i++) {
            for (NhanVien nv : list) {
                if (nv.getTaiKhoan().equalsIgnoreCase(LoginFrom.taiKhoan)) {

                    for (SuaChua suaChua : list1) {
                        if (lblTrangThai.getText().equals("Đã giao máy")) {
                            tableDark3.setVisible(false);
                            tableDark1.setVisible(false);
                            jButton53.setEnabled(false);
                            jButton47.setEnabled(false);
                            jButton48.setEnabled(false);
                            jButton56.setEnabled(false);
                            jButton50.setEnabled(false);
                            jButton49.setEnabled(false);
                            txtNgayTraDuKien.setEnabled(false);
                        } else {
                            tableDark3.setVisible(true);
                            tableDark1.setVisible(true);
                            jButton53.setEnabled(true);
                            jButton47.setEnabled(true);
                            jButton48.setEnabled(true);
                            jButton56.setEnabled(true);
                            jButton50.setEnabled(true);
                            jButton49.setEnabled(true);
                            txtNgayTraDuKien.setEnabled(true);
                        }
                        break;
                    }
                    for (SuaChua suaChua : list1) {
                        if (nv.getChucVu().equalsIgnoreCase("Nhân viên kỹ thuật")) {
                            loadSC1();
                            jButton49.setVisible(true);
                            jButton53.setVisible(true);
                            jButton49.setEnabled(true);
                            jButton53.setEnabled(false);
                            jButton47.setEnabled(false);
                        } else {
                            tableDark3.setVisible(false);
                            tableDark1.setVisible(false);
                        }
                        break;
                    }

                }
            }
            break;
        }
    }

    void loadSC() {
        DefaultTableModel model = (DefaultTableModel) tblDichVu.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtTimKiem.getText();
            List<SuaChua> list1 = scdao.selectByKeyword(keyword);
            for (SuaChua sc : list1) {
                Object[] row = {
                    sc.getMaSuaChua(),
                    sc.getTenKhachHang(),
                    sc.getNhanVienNhanMay(),
                    sc.getKyThuatVien(),
                    sc.getTinhTrangMay(),
                    sc.getTrangThai(),
                    sc.getMaLinhKien()};
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
        try {
            List<NhanVien> list = nvdao.select();
            List<SuaChua> list1 = scdao.select();
            for (int i = 0; i < list.size(); i++) {
                for (NhanVien nv : list) {
                    if (nv.getTaiKhoan().equalsIgnoreCase(LoginFrom.taiKhoan)) {
                        //txtNhanVienNhanMay1.setText(nv.getMaNhanVien());
                        //txtNhanVienNhanMay.setText(nv.getHoTen());
                        for (SuaChua suaChua : list1) {
                            if (nv.getChucVu().equalsIgnoreCase("Nhân viên kỹ thuật")) {
                                loadSC1();
                                jButton49.setVisible(true);
                                jButton53.setVisible(true);
                                jButton49.setEnabled(true);
                                jButton53.setEnabled(false);
                                jButton47.setEnabled(false);
                            }
                            break;
                        }

                    }
                }
                break;
            }
        } catch (Exception e) {
            System.out.println("Lỗi!");
        }
        loadSC2();
    }

    void loadSC2() {
        DefaultTableModel model1 = (DefaultTableModel) tableDark1.getModel();
        model1.setRowCount(0);
        try {
            List<LinhKien> list = lkdao.select();
            int i = 1;
            for (LinhKien lk : list) {
                Object[] row = {
                    lk.getMaLinhKien(),
                    lk.getTenHang(),
                    lk.getSoLuong(),
                    lk.getGiaBan()};
                model1.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void loadSC1() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblDichVu.getModel();
            model.setRowCount(0);
            List<NhanVien> list = nvdao.select();
            for (int i = 0; i < list.size(); i++) {
                for (NhanVien nv : list) {
                    String keyword = nv.getMaNhanVien();
                    List<SuaChua> list1 = scdao.selectByKeywordNVKT(keyword);

                    if (nv.getTaiKhoan().equalsIgnoreCase(LoginFrom.taiKhoan) || nv.getChucVu().equalsIgnoreCase(list1.toString())) {
                        for (SuaChua sc : list1) {
                            Object[] row = {
                                sc.getMaSuaChua(),
                                sc.getTenKhachHang(),
                                sc.getNhanVienNhanMay(),
                                sc.getKyThuatVien(),
                                sc.getTinhTrangMay(),
                                sc.getTrangThai(),
                                sc.getMaLinhKien()};
                            model.addRow(row);
                        }
                    }
                }
                break;
            }
        } catch (Exception e) {
            System.out.println("Lỗi!");
        }
    }

    //hiện combo box hảng trên trên form sửa chữa
    void fillComBoBoxHang1() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboHang1.getModel();
        model.removeAllElements();
        List<HangMay> list = hmdao.select();
        for (HangMay hm : list) {
            model.addElement(hm);
        }

        cboHang1.setSelectedIndex(0);
    }

    //hiện combo box hảng trên trên form sửa chữa
    void fillComBoBoxMay() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboMay.getModel();
        model.removeAllElements();
        try {
            List<ThongTinMay> list = ttmdao.selectByCourse(String.valueOf(cboHang1.getSelectedItem()));
            for (ThongTinMay ttm : list) {
                model.addElement(ttm);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //hiện combo box hảng trên trên form sửa chữa
//    void fillComBoBoxLinhKien() {
//        DefaultComboBoxModel model = (DefaultComboBoxModel) cboLinhKien.getModel();
//        model.removeAllElements();
//        model.addElement("Linh kiện");
//        List<LinhKien> list = lkdao.select();
//        for (LinhKien lk : list) {
//            model.addElement(lk);
//        }
//
//        cboLinhKien.setSelectedIndex(0);
//    }
    void fillComBoBoxNVSua() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboKyThuatVien.getModel();
        model.removeAllElements();
        List<NhanVien> list = nvdao.select();
        for (NhanVien nv : list) {
            if (nv.getChucVu().equalsIgnoreCase("Nhân viên kỹ thuật")) {
                model.addElement(nv);
            }
        }
    }

    //hiện combo box hảng trên trên form sửa chữa
    void fillComBoBoxKhachHang() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboTenKhach.getModel();
        model.removeAllElements();

        List<KhachHang> list = khdao.select();
        for (KhachHang kh : list) {
            model.addElement(kh);
        }
        cboTenKhach.setSelectedIndex(0);
    }

    //chỉnh sửa thông tin linh kiện
    void editSC() {
        try {
            String maSuaChua = (String) tblDichVu.getValueAt(this.index, 0);
            SuaChua model8 = scdao.findById(maSuaChua);
            if (model8 != null) {
                this.setModelSC(model8);
                phi = model8.getPhiSuaChua();
                tien = model8.getT();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //chuyển thông tin hảng máy từ bảng qua form
    void setModelSC(SuaChua model) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        txtMaSuaChua.setText(model.getMaSuaChua());

        cboHang1.setSelectedItem(model.getTenHang());
        cboMay.setSelectedItem(model.getTenMay());
        txtMoTaLoi.setText(model.getMoTaLoi());
        txtTinhTrangMay.setText(model.getTinhTrangMay());
        txtMatKhau.setText(model.getMatkhauMay());
        txtNgayNhanMay.setText(DateHelper.toString(model.getNgayNhanMay()));
        txtNhanVienNhanMay.setText(model.getNhanVienNhanMay());
        txtNhanVienNhanMay1.setText(model.getMaNhanVienNhan());
        cboKyThuatVien.setSelectedItem(model.getKyThuatVien());

        txtPhiSuaChua.setText(formatter.format(model.getPhiSuaChua()));
        txtMota.setText(model.getMoTa());
        txtNgayTraDuKien.setText(DateHelper.toString(model.getNgayTraMayDuKien()));
        lblTrangThai.setText(model.getTrangThai());
        txtNgayTraMayThucTe.setText(DateHelper.toString(model.getNgayTraMayThucTe()));
        cboTenKhach.setSelectedItem(model.getTenKhachHang());

        DefaultTableModel model1 = (DefaultTableModel) tableDark3.getModel();
        model1.setRowCount(0);
        try {
            List<SuaChua> list = scdao.select1(txtMaSuaChua.getText());
            List<LinhKien> list1 = lkdao.select();
            int i = 1;
            for (SuaChua SC : list) {
                Object[] row = {
                    SC.getMaLinhKien(),
                    SC.getTenLinhKien()};
                model1.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void Tong1() {
        try {
            String masc;
            int Row = tblDichVu.getSelectedRow();
            masc = (String) tblDichVu.getValueAt(Row, 0);
            SuaChua model22 = scdao.select2(masc);
            if (model22 != null) {
                this.setModelSUM(model22);
                tien=model22.getT();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void Tong2() {
        try {
            String masc;
            masc = txtMaSuaChua.getText();
            SuaChua model22 = scdao.select2(masc);
            if (model22 != null) {
                this.setModelSUM(model22);
                tien=model22.getT();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void setModelSUM(SuaChua model) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        lblTong.setText(formatter.format(model.getT()));
    }

    SuaChua getModelSC() {
        SuaChua model = new SuaChua();
        model.setMaSuaChua(txtMaSuaChua.getText());
        model.setMaMay(txtMaMay1.getText());
        model.setMoTaLoi(txtMoTaLoi.getText());
        model.setTinhTrangMay(txtTinhTrangMay.getText());

        model.setMatkhauMay(txtMatKhau.getText());
        model.setNgayNhanMay(DateHelper.toDate(txtNgayNhanMay.getText()));
        model.setMaNhanVienNhan(txtNhanVienNhanMay.getText());
        model.setMaNhanVienSua(txtKyThuatVien.getText());
        model.setPhiSuaChua(Float.valueOf(txtPhiSuaChua.getText().replace(",", "")));
        model.setMoTa(txtMota.getText());
        model.setNgayTraMayDuKien(DateHelper.toDate(txtNgayTraDuKien.getText()));
        model.setTrangThai(lblTrangThai.getText());
        model.setNgayTraMayThucTe(DateHelper.toDate(txtNgayTraMayThucTe.getText()));
        model.setMaKhachHang(txtKH.getText());
        model.setMaNhanVienNhan(txtNhanVienNhanMay1.getText());
        return model;
    }

    SuaChua getModelThem() {
        SuaChua model = new SuaChua();
        model.setMaSuaChua("SC" + (tblDichVu.getRowCount() + 1));
        model.setMaMay(txtMaMay1.getText());
        model.setMoTaLoi(txtMoTaLoi.getText());
        model.setTinhTrangMay(txtTinhTrangMay.getText());

        model.setMatkhauMay(txtMatKhau.getText());
        model.setNgayNhanMay(DateHelper.toDate(txtNgayNhanMay.getText()));
        model.setMaNhanVienNhan(txtNhanVienNhanMay.getText());
        model.setMaNhanVienSua(txtKyThuatVien.getText());

        model.setNgayTraMayDuKien(DateHelper.toDate(txtNgayTraDuKien.getText()));
        model.setTrangThai(lblTrangThai.getText());
        model.setNgayTraMayThucTe(DateHelper.toDate(txtNgayTraMayThucTe.getText()));
        model.setMaKhachHang(txtKH.getText());
        model.setMaNhanVienNhan(txtNhanVienNhanMay1.getText());
        return model;
    }

    SuaChua getModelSC1(String MaSuaChua, String trangThai) {
        SuaChua model = new SuaChua();
        model.setMaSuaChua(MaSuaChua);
        model.setTrangThai(trangThai);
        //cboTrangThai.setSelectedIndex(trangThai);
        //model.setTrangThai(cboTrangThai.getSelectedItem().toString());
        return model;
    }

    SuaChua getModelSC3(String MaSuaChua) {
        SuaChua model = new SuaChua();
        model.setNgayTraMayThucTe(DateHelper.now());
        model.setMaSuaChua(MaSuaChua);
        return model;
    }

    SuaChua getModelSC2(String MaSuaChua, String maSuaChua) {
        SuaChua model = new SuaChua();
        model.setMaSuaChua(MaSuaChua);
        model.setMaLinhKien(maSuaChua);
        return model;
    }

    void insertSC() {
        SuaChua model = getModelThem();
        try {
            if (model.getMaSuaChua().length() > 5) {
                DialogHelper.alert(this, "Mã Sửa chữa Không Được Quá 5 Ký Tự");
                return;
            }
            scdao.insert(model);
            this.loadSC();
            this.clearSC();
            DialogHelper.alert(this, "Thêm mới thành công!");
            scdao.updateSL(model);
            loadLK();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void insertSC1() {
        SuaChua model = getModelSC();
        try {
            if (model.getMaSuaChua().length() > 5) {
                DialogHelper.alert(this, "Mã Sửa chữa Không Được Quá 5 Ký Tự");
                return;
            }
            scdao.insert1(model);
            this.loadSC();
            this.clearSC();
            DialogHelper.alert(this, "Thêm mới thành công!");
            scdao.updateSL(model);
            loadLK();
        } catch (Exception e) {
            System.out.println("Lỗi!");
        }

    }

    void insertSC2(String maLinhKien) {
        SuaChua model2 = getModelSC2(txtMaSuaChua.getText(), maLinhKien);
        try {
            scdao.insert1(model2);
            this.loadSC();
            DialogHelper.alert(this, "Thêm Thành Công");
            scdao.updateSL(model2);
        } catch (Exception e) {
            DialogHelper.alert(this, "Linh kiện đã có trong sửa chữa");
        }

    }

    void insertSC3(String maLinhKien) {
        SuaChua model2 = getModelSC2(txtMaSuaChua.getText(), maLinhKien);
        try {
            scdao.delete1(maLinhKien);
            System.out.println(maLinhKien);
            DialogHelper.alert(this, "Xóa thành công");
            scdao.updateSL2(model2);
        } catch (Exception e) {
            System.out.println("Lỗi!");
        }

    }

    void updateSC() {
        SuaChua model = getModelSC();
        try {
            scdao.update(model);
            //scdao.update1(model);
            this.loadSC();
            DialogHelper.alert(this, "Cập nhật thành công!");
        } catch (Exception e) {
            System.out.println("Lỗi!");
        }

    }

    void deleteSC() {
        if (DialogHelper.confirm(this, "Bạn có muốn xóa hay không?")) {
            String masc = txtMaSuaChua.getText();
            try {
                scdao.delete(masc);
                this.loadSC();
                this.clearSC();
                DialogHelper.alert(this, "Xóa thành công!");
            } catch (Exception e) {
                DialogHelper.alert(this, "chuyên đề còn tồn trong khóa học!");
            }
        }
    }

    void clearSC() {
        this.setModelSC(new SuaChua());
        txtNgayNhanMay.setText(DateHelper.toString(DateHelper.now()));
        //cboTrangThai.setSelectedIndex(0);
        cboHang1.setSelectedIndex(0);
        cboMay.setSelectedIndex(0);
        //cboLinhKien.setSelectedIndex(0);
        cboKyThuatVien.setSelectedIndex(0);
        cboTenKhach.setSelectedIndex(0);
        txtNgayTraDuKien.setText("");
        txtNgayTraMayThucTe.setText(DateHelper.toString(DateHelper.now()));
        try {
            List<NhanVien> list = nvdao.select();
            for (int i = 0; i < list.size(); i++) {
                for (NhanVien nv : list) {
                    if (nv.getTaiKhoan().equalsIgnoreCase(LoginFrom.taiKhoan)) {

                    }
                }
                break;
            }
        } catch (Exception e) {
            System.out.println("Lỗi!");
        }
        lblTrangThai.setText("Đã Nhận");
        txtMaSuaChua.setText("SC" + (tblDichVu.getRowCount() + 1));

    }

    //***thông tin Hóa đơn
    //load thông tin phiếu nhập lên bảng
    void loadHD() {
        DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtTimKiem.getText();
            List<HoaDon> list = hddao.selectByKeyword(keyword);
            for (HoaDon hd : list) {
                Object[] row = {
                    hd.getMaSuaChua(),
                    hd.getThoiGianLap(),
                    hd.getThanhTien(),
                    hd.getThoiGianBaoHanh(),
                    hd.getNgayBatDau(),
                    hd.getMoTa()};
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void editHD() {
        try {
            String MaLinhKien = (String) tblHoaDon.getValueAt(this.index, 0);
            HoaDon model8 = hddao.findById(MaLinhKien);
            if (model8 != null) {
                this.setModelHD(model8);
            }
        } catch (Exception e) {
            System.out.println("Lỗi!");
        }
    }

    void insertHD() {
        HoaDon model = getModelHD1();
        try {
            if (model.getMaHoaDon().length() > 5) {
                DialogHelper.alert(this, "Mã hóa đơn Không Được Quá 5 Ký Tự");
                return;
            }
            hddao.insert1(model);
            hddao.insert(model);
            SuaChua model1 = getModelSC1(textField43.getText(), "Đã giao máy");
            scdao.updateSL1(model1);
            SuaChua model2 = getModelSC3(textField43.getText());
            scdao.update1(model2);
            this.loadHD();
            //this.clearSC();
            DialogHelper.alert(this, "Thêm mới thành công!");
        } catch (Exception e) {
            System.out.println("Lỗi!");
        }

    }

    void setModelHD(HoaDon model) {
        textField43.setText(model.getMaSuaChua());
        textField45.setText(model.getTenKhachHang());
        textField46.setText(model.getNhanVienTiepNhan());
        textField47.setText(model.getNhanVienKyThuat());
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        textField48.setText(formatter.format(model.getThanhTien()));
        textField49.setText(DateHelper.toString(model.getThoiGianLap()));
        textArea8.setText(String.valueOf(model.getMoTa()));
        comboBoxSuggestion2.setSelectedItem(model.getThoiGianBaoHanh());
    }

    HoaDon getModelHD() {
        HoaDon model = new HoaDon();
        model.setMaSuaChua(textField43.getText());
        model.setTenKhachHang(textField45.getText());
        model.setNhanVienTiepNhan(textField46.getText());
        model.setNhanVienKyThuat(textField47.getText());
        model.setThanhTien(Float.valueOf(textField48.getText().replace(",", "")));
        model.setNgayBatDau(DateHelper.now());
        model.setMoTa(textArea8.getText());
        model.setThoiGianBaoHanh(Integer.parseInt((String) comboBoxSuggestion2.getSelectedItem()));
        return model;
    }

    HoaDon getModelHD1() {
        HoaDon model = new HoaDon();
        model.setMaSuaChua(textField43.getText());
        model.setMaHoaDon(textField44.getText());
        model.setThoiGianLap(DateHelper.now());
        model.setThanhTien(Float.valueOf(textField48.getText().replace(",", "")));
        model.setNgayBatDau(DateHelper.now());
        model.setThoiGianBaoHanh(Integer.parseInt((String) comboBoxSuggestion2.getSelectedItem()));
        return model;
    }

    //***thông tin bảo hành
    //load thông tin phiếu nhập lên bảng
    void loadBH() {
        DefaultTableModel model = (DefaultTableModel) tblBaoHanh.getModel();
        model.setRowCount(0);
        try {

            String keyword = txtTimKiem.getText();
            List<BaoHanh> list = bhdao.selectByKeyword(keyword);
            int i = 1;
            for (BaoHanh bh : list) {
                Object[] row = {
                    i++,
                    bh.getMaSuaChua(),
                    bh.getThoiGianBaoHanh(),
                    bh.getNgayBatDau()};
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    //***thông tin Thống kê
    void fillTableDoanhThu() {
        DefaultTableModel model = (DefaultTableModel) tblDoanhThu.getModel();
        model.setRowCount(0);
        List<Object[]> list = bcd.getDoanhThu();
        int i = 1;
        for (Object[] row : list) {
            model.addRow(row);
        }
    }

    void fillTableDoanhThuLuong() {
        DefaultTableModel model = (DefaultTableModel) tblDoanhThuLuong.getModel();
        model.setRowCount(0);
        List<Object[]> list = bcd.getLuong();
        for (Object[] row : list) {
            model.addRow(row);
        }
    }

    public void changedTK() {
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                loadKH();
                loadSC();
                loadLK();
                loadPN();
                loadNCC();
                loadNV();
                loadHD();
                loadTTM();
                loadHM();
                loadBH();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                loadKH();
                loadSC();
                loadLK();
                loadPN();
                loadNCC();
                loadNV();
                loadHD();
                loadTTM();
                loadHM();
                loadBH();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                loadKH();
                loadSC();
                loadLK();
                loadPN();
                loadNCC();
                loadNV();
                loadHD();
                loadTTM();
                loadHM();
                loadBH();
            }

        }
        );
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        fileChooser = new javax.swing.JFileChooser();
        date1 = new com.raven.datechooser.DateChooser();
        pnlMenu = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblKhachHang = new javax.swing.JLabel();
        lblDichVu = new javax.swing.JLabel();
        lblKhoHang = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();
        lblThongTinBanHang = new javax.swing.JLabel();
        lblThongTinSua = new javax.swing.JLabel();
        lblHoaDon = new javax.swing.JLabel();
        lblThongTinMay = new javax.swing.JLabel();
        lblNhangHang = new javax.swing.JLabel();
        lblBaoHanh = new javax.swing.JLabel();
        lblDoanhThu = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        panelRound4 = new com.phones.jar.PanelRound();
        lnlThoiGian = new javax.swing.JLabel();
        txtTimKiem = new com.phones.jar.TextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jpnMain = new javax.swing.JPanel();
        tabKhachHang = new com.phones.jar.MaterialTabbed();
        jpnDanhSachKhachHang = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKhachHang = new com.phones.jar.TableDark();
        jpnCapNhatKhachHang = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        txtMaKhachHang = new com.phones.jar.TextField();
        txtTenKhachHang = new com.phones.jar.TextField();
        txtEmail = new com.phones.jar.TextField();
        txtSoDienThoai = new com.phones.jar.TextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtDiaChi = new javax.swing.JTextArea();
        jButton51 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        tabSuaChua = new com.phones.jar.MaterialTabbed();
        jpnDanhSachSuaChua = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDichVu = new com.phones.jar.TableDark();
        jpnCapNhatSuaChua = new javax.swing.JPanel();
        txtKH = new javax.swing.JTextField();
        txtMaLK = new javax.swing.JTextField();
        txtMaMay1 = new javax.swing.JTextField();
        txtKyThuatVien = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        textAreaScroll4 = new com.phones.jar.TextAreaScroll();
        txtMoTaLoi = new com.phones.jar.TextArea();
        txtMatKhau = new com.phones.jar.TextField();
        txtNhanVienNhanMay = new com.phones.jar.TextField();
        cboKyThuatVien = new com.phones.jar.ComboBoxSuggestion();
        cboTenKhach = new com.phones.jar.ComboBoxSuggestion();
        txtNgayNhanMay = new com.phones.jar.TextField();
        txtNhanVienNhanMay1 = new com.phones.jar.TextField();
        jPanel14 = new javax.swing.JPanel();
        textAreaScroll6 = new com.phones.jar.TextAreaScroll();
        txtTinhTrangMay = new com.phones.jar.TextArea();
        txtMaSuaChua = new com.phones.jar.TextField();
        cboMay = new com.phones.jar.ComboBoxSuggestion();
        cboHang1 = new com.phones.jar.ComboBoxSuggestion();
        txtNgayTraDuKien = new com.phones.jar.TextField();
        txtNgayTraMayThucTe = new com.phones.jar.TextField();
        lblTrangThai = new javax.swing.JLabel();
        txtPhiSuaChua = new com.phones.jar.TextField();
        textAreaScroll7 = new com.phones.jar.TextAreaScroll();
        txtMota = new com.phones.jar.TextArea();
        jLabel9 = new javax.swing.JLabel();
        lblTong = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jButton47 = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        jButton56 = new javax.swing.JButton();
        jButton50 = new javax.swing.JButton();
        jButton53 = new javax.swing.JButton();
        jButton49 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        tableDark3 = new com.phones.jar.TableDark();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableDark1 = new com.phones.jar.TableDark();
        tabLinhKien = new com.phones.jar.MaterialTabbed();
        jpnDanhSachKhoHang = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblKhoHang = new com.phones.jar.TableDark();
        jButton8 = new javax.swing.JButton();
        jpnCapNhatKhoHang = new javax.swing.JPanel();
        txtMaLinhKien = new com.phones.jar.TextField();
        txtTenLinhKien = new com.phones.jar.TextField();
        txtSoLuong = new com.phones.jar.TextField();
        txtGiaBan = new com.phones.jar.TextField();
        textAreaScroll2 = new com.phones.jar.TextAreaScroll();
        txtMoTa = new com.phones.jar.TextArea();
        jButton63 = new javax.swing.JButton();
        jButton64 = new javax.swing.JButton();
        jButton65 = new javax.swing.JButton();
        jButton66 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        tabPhieuNhap = new com.phones.jar.MaterialTabbed();
        jpnDanhSachPhieuNhap = new javax.swing.JPanel();
        jButton20 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblPhieuNhap = new com.phones.jar.TableDark();
        jPanel5 = new javax.swing.JPanel();
        txtMNCC = new javax.swing.JTextField();
        txtMNCC1 = new javax.swing.JTextField();
        txtMaPhieuNhap = new com.phones.jar.TextField();
        txtNgayNhap = new com.phones.jar.TextField();
        textAreaScroll1 = new com.phones.jar.TextAreaScroll();
        txtMoTaNhap = new com.phones.jar.TextArea();
        jButton17 = new javax.swing.JButton();
        jButton58 = new javax.swing.JButton();
        cboNhaCungCap = new com.phones.jar.ComboBoxSuggestion();
        jButton5 = new javax.swing.JButton();
        jScrollPane15 = new javax.swing.JScrollPane();
        tableDark2 = new com.phones.jar.TableDark();
        jScrollPane17 = new javax.swing.JScrollPane();
        tableDark4 = new com.phones.jar.TableDark();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jpnNhaCungCap = new javax.swing.JPanel();
        panelRound6 = new com.phones.jar.PanelRound();
        txtMaNhaCungCap = new com.phones.jar.TextField();
        txtTenNhaCungCap = new com.phones.jar.TextField();
        textAreaScroll3 = new com.phones.jar.TextAreaScroll();
        txtDiaChiNCC = new com.phones.jar.TextArea();
        txtEmailNCC = new com.phones.jar.TextField();
        txtSoDienThoaiNCC = new com.phones.jar.TextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblNhaCungCap = new com.phones.jar.TableDark();
        btnThemNCC = new javax.swing.JButton();
        btnXoaNCC = new javax.swing.JButton();
        btnLamMoiNCC = new javax.swing.JButton();
        btnSuaNCC = new javax.swing.JButton();
        tabNhanVien = new com.phones.jar.MaterialTabbed();
        jpnThongTinSuaChua = new javax.swing.JPanel();
        jButton34 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblThongTinPhieuNhap = new com.phones.jar.TableDark();
        jPanel7 = new javax.swing.JPanel();
        txtMaNhanVien = new com.phones.jar.TextField();
        txtHoTenNV = new com.phones.jar.TextField();
        txtTaiKhoanNV = new com.phones.jar.TextField();
        txtMatkhauNV = new com.phones.jar.TextField();
        txtEmailNV = new com.phones.jar.TextField();
        txtSoDienThoaiNV = new com.phones.jar.TextField();
        txtLuongNV = new com.phones.jar.TextField();
        textAreaScroll5 = new com.phones.jar.TextAreaScroll();
        txtDiaChiNV = new com.phones.jar.TextArea();
        cboChucVu = new com.phones.jar.ComboBoxSuggestion();
        jPanel8 = new javax.swing.JPanel();
        lblHinhAnh = new javax.swing.JLabel();
        btnThemNV = new javax.swing.JButton();
        btnSuaNV = new javax.swing.JButton();
        btnXoaNV = new javax.swing.JButton();
        btnLamMoiNV = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        tabHoaDon = new com.phones.jar.MaterialTabbed();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblHoaDon = new com.phones.jar.TableDark();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        textField43 = new com.phones.jar.TextField();
        textField45 = new com.phones.jar.TextField();
        textField46 = new com.phones.jar.TextField();
        textField47 = new com.phones.jar.TextField();
        textField48 = new com.phones.jar.TextField();
        textField49 = new com.phones.jar.TextField();
        comboBoxSuggestion2 = new com.phones.jar.ComboBoxSuggestion();
        jButton6 = new javax.swing.JButton();
        textAreaScroll8 = new com.phones.jar.TextAreaScroll();
        textArea8 = new com.phones.jar.TextArea();
        jButton14 = new javax.swing.JButton();
        textField44 = new com.phones.jar.TextField();
        jpnThongTinMay = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        txtTenMay = new com.phones.jar.TextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblThongTinBaoHanh1 = new com.phones.jar.TableDark();
        jButton41 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        txtRam = new com.phones.jar.TextField();
        txtRom = new com.phones.jar.TextField();
        txtChip = new com.phones.jar.TextField();
        cboHangMay = new com.phones.jar.ComboBoxSuggestion();
        txtMaMay = new com.phones.jar.TextField();
        txtRam1 = new com.phones.jar.TextField();
        jpnNhangHang = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        txtMaHang = new com.phones.jar.TextField();
        jScrollPane11 = new javax.swing.JScrollPane();
        tblThongTinBaoHanh2 = new com.phones.jar.TableDark();
        btnThemNH = new javax.swing.JButton();
        btnSuaNH = new javax.swing.JButton();
        btnXoaNH = new javax.swing.JButton();
        btnLamMoiNH = new javax.swing.JButton();
        txtTenHang = new com.phones.jar.TextField();
        jpnBaoHanh = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblBaoHanh = new com.phones.jar.TableDark();
        tabDoanhThu = new com.phones.jar.MaterialTabbed();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tblDoanhThu = new com.phones.jar.TableDark();
        jButton36 = new javax.swing.JButton();
        txtTongDoanhThu = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tblDoanhThuLuong = new com.phones.jar.TableDark();
        jButton74 = new javax.swing.JButton();

        date1.setDateFormat("dd/MM/yyyy");
        date1.setTextRefernce(txtNgayTraDuKien);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlMenu.setBackground(new java.awt.Color(255, 255, 255));
        pnlMenu.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 50, 20));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("CORE MODULES");
        pnlMenu.add(jLabel3);

        lblKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKhachHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/phones/icon/customer.png"))); // NOI18N
        lblKhachHang.setText("  Khách hàng");
        lblKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblKhachHangMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblKhachHangMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblKhachHangMouseExited(evt);
            }
        });
        pnlMenu.add(lblKhachHang);

        lblDichVu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDichVu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/phones/icon/suaChua.png"))); // NOI18N
        lblDichVu.setText("    Sửa chữa");
        lblDichVu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDichVuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblDichVuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblDichVuMouseExited(evt);
            }
        });
        pnlMenu.add(lblDichVu);

        lblKhoHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKhoHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/phones/icon/linhKien.png"))); // NOI18N
        lblKhoHang.setText("    Linh kiện");
        lblKhoHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblKhoHangMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblKhoHangMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblKhoHangMouseExited(evt);
            }
        });
        pnlMenu.add(lblKhoHang);

        lblNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/phones/icon/thongtinbanhang.png"))); // NOI18N
        lblNhanVien.setText("   Phiếu nhập");
        lblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblNhanVienMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblNhanVienMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblNhanVienMouseExited(evt);
            }
        });
        pnlMenu.add(lblNhanVien);

        lblThongTinBanHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblThongTinBanHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/phones/icon/nhaCungCap.png"))); // NOI18N
        lblThongTinBanHang.setText("    Nhà cung cấp");
        lblThongTinBanHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThongTinBanHangMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblThongTinBanHangMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblThongTinBanHangMouseExited(evt);
            }
        });
        pnlMenu.add(lblThongTinBanHang);

        lblThongTinSua.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblThongTinSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/phones/icon/nhanvien.png"))); // NOI18N
        lblThongTinSua.setText("    Nhân viên");
        lblThongTinSua.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThongTinSuaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblThongTinSuaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblThongTinSuaMouseExited(evt);
            }
        });
        pnlMenu.add(lblThongTinSua);

        lblHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/phones/icon/thongtinbanhang.png"))); // NOI18N
        lblHoaDon.setText("   Hóa đơn");
        lblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHoaDonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblHoaDonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblHoaDonMouseExited(evt);
            }
        });
        pnlMenu.add(lblHoaDon);

        lblThongTinMay.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblThongTinMay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/phones/icon/thonTinMay.png"))); // NOI18N
        lblThongTinMay.setText("   Thông tin máy");
        lblThongTinMay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThongTinMayMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblThongTinMayMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblThongTinMayMouseExited(evt);
            }
        });
        pnlMenu.add(lblThongTinMay);

        lblNhangHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblNhangHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/phones/icon/nhangHang.png"))); // NOI18N
        lblNhangHang.setText("  Nhãn hàng");
        lblNhangHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblNhangHangMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblNhangHangMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblNhangHangMouseExited(evt);
            }
        });
        pnlMenu.add(lblNhangHang);

        lblBaoHanh.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblBaoHanh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/phones/icon/BaoHanh.png"))); // NOI18N
        lblBaoHanh.setText("    Bảo hành");
        lblBaoHanh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBaoHanhMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblBaoHanhMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblBaoHanhMouseExited(evt);
            }
        });
        pnlMenu.add(lblBaoHanh);

        lblDoanhThu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDoanhThu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/phones/icon/doanhThu.png"))); // NOI18N
        lblDoanhThu.setText("    Doanh thu");
        lblDoanhThu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDoanhThuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblDoanhThuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblDoanhThuMouseExited(evt);
            }
        });
        pnlMenu.add(lblDoanhThu);

        getContentPane().add(pnlMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 230, 710));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        panelRound4.setBackground(new java.awt.Color(186, 232, 232));
        panelRound4.setRoundBottomLeft(30);
        panelRound4.setRoundBottomRight(30);
        panelRound4.setRoundTopLeft(30);
        panelRound4.setRoundTopRight(30);
        panelRound4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lnlThoiGian.setText("8:00 PM");
        panelRound4.add(lnlThoiGian, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        txtTimKiem.setLabelText("Tìm Theo Tên");
        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(panelRound4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(212, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(panelRound4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 920, 80));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/phones/icon/logo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/phones/icon/menu.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 10, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 80));

        jpnMain.setLayout(new java.awt.CardLayout());

        jpnDanhSachKhachHang.setBackground(new java.awt.Color(255, 255, 255));
        jpnDanhSachKhachHang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setBackground(new java.awt.Color(186, 232, 232));
        jButton2.setText(">");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jpnDanhSachKhachHang.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 590, 55, 37));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setText("Khách Hàng");
        jpnDanhSachKhachHang.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 221, -1));

        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã khách hàng", "Tên khách hàng", "Địa Chỉ", "Email", "Số điện thoại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhachHangMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblKhachHangMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblKhachHang);
        if (tblKhachHang.getColumnModel().getColumnCount() > 0) {
            tblKhachHang.getColumnModel().getColumn(0).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(1).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(2).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(3).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(4).setResizable(false);
        }

        jpnDanhSachKhachHang.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 730, 530));

        tabKhachHang.addTab("Danh sách", jpnDanhSachKhachHang);

        jpnCapNhatKhachHang.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Mã khách hàng");

        jLabel5.setText("Tên khách hàng");

        jLabel6.setText("Email");

        jLabel7.setText("Địa chỉ");

        jLabel8.setText("Số điện thoại");

        jButton11.setBackground(new java.awt.Color(7, 128, 128));
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Thêm ");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(7, 128, 128));
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Sửa");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(7, 128, 128));
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("Xóa");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        txtMaKhachHang.setEditable(false);
        txtMaKhachHang.setLabelText("Mã khách hàng");

        txtTenKhachHang.setLabelText("Tên khách hàng");

        txtEmail.setLabelText("Email");

        txtSoDienThoai.setLabelText("Số điện thoại");

        txtDiaChi.setColumns(20);
        txtDiaChi.setRows(5);
        jScrollPane10.setViewportView(txtDiaChi);

        jButton51.setBackground(new java.awt.Color(7, 128, 128));
        jButton51.setForeground(new java.awt.Color(255, 255, 255));
        jButton51.setText("Làm mới");
        jButton51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton51ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(186, 232, 232));
        jButton3.setText("<");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnCapNhatKhachHangLayout = new javax.swing.GroupLayout(jpnCapNhatKhachHang);
        jpnCapNhatKhachHang.setLayout(jpnCapNhatKhachHangLayout);
        jpnCapNhatKhachHangLayout.setHorizontalGroup(
            jpnCapNhatKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCapNhatKhachHangLayout.createSequentialGroup()
                .addGroup(jpnCapNhatKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnCapNhatKhachHangLayout.createSequentialGroup()
                        .addGroup(jpnCapNhatKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpnCapNhatKhachHangLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jpnCapNhatKhachHangLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(jpnCapNhatKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jpnCapNhatKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(31, 31, 31)
                        .addGroup(jpnCapNhatKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTenKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSoDienThoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)))
                    .addGroup(jpnCapNhatKhachHangLayout.createSequentialGroup()
                        .addGap(180, 180, 180)
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton51)))
                .addContainerGap(413, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnCapNhatKhachHangLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(146, 146, 146))
        );
        jpnCapNhatKhachHangLayout.setVerticalGroup(
            jpnCapNhatKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCapNhatKhachHangLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(jpnCapNhatKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpnCapNhatKhachHangLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(54, 54, 54)
                        .addComponent(jLabel7)
                        .addGap(75, 75, 75)
                        .addComponent(jLabel8)
                        .addGap(98, 98, 98))
                    .addGroup(jpnCapNhatKhachHangLayout.createSequentialGroup()
                        .addGroup(jpnCapNhatKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtMaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jpnCapNhatKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82)))
                .addGroup(jpnCapNhatKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11)
                    .addComponent(jButton12)
                    .addComponent(jButton13)
                    .addComponent(jButton51))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        tabKhachHang.addTab("Cập nhật", jpnCapNhatKhachHang);

        jpnMain.add(tabKhachHang, "tabKhachHang");

        jpnDanhSachSuaChua.setBackground(new java.awt.Color(255, 255, 255));

        tblDichVu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sửa chữa", "Tên khách hàng", "Tên nhân viên nhận máy", "Tên kỹ thuật viên", "Trạng thái máy", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDichVu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDichVuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblDichVuMouseEntered(evt);
            }
        });
        jScrollPane2.setViewportView(tblDichVu);
        if (tblDichVu.getColumnModel().getColumnCount() > 0) {
            tblDichVu.getColumnModel().getColumn(0).setResizable(false);
            tblDichVu.getColumnModel().getColumn(1).setResizable(false);
            tblDichVu.getColumnModel().getColumn(2).setResizable(false);
            tblDichVu.getColumnModel().getColumn(3).setResizable(false);
            tblDichVu.getColumnModel().getColumn(4).setResizable(false);
            tblDichVu.getColumnModel().getColumn(5).setResizable(false);
        }

        javax.swing.GroupLayout jpnDanhSachSuaChuaLayout = new javax.swing.GroupLayout(jpnDanhSachSuaChua);
        jpnDanhSachSuaChua.setLayout(jpnDanhSachSuaChuaLayout);
        jpnDanhSachSuaChuaLayout.setHorizontalGroup(
            jpnDanhSachSuaChuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDanhSachSuaChuaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 884, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jpnDanhSachSuaChuaLayout.setVerticalGroup(
            jpnDanhSachSuaChuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDanhSachSuaChuaLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        tabSuaChua.addTab("Sửa chữa", jpnDanhSachSuaChua);

        jpnCapNhatSuaChua.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Thông Tin"));

        textAreaScroll4.setBackground(new java.awt.Color(255, 255, 255));
        textAreaScroll4.setLabelText("Mô tả lỗi");

        txtMoTaLoi.setColumns(20);
        txtMoTaLoi.setRows(5);
        textAreaScroll4.setViewportView(txtMoTaLoi);

        txtMatKhau.setLabelText("Mật khẩu máy");

        txtNhanVienNhanMay.setLabelText("Nhân viên nhận máy");

        cboKyThuatVien.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboKyThuatVienItemStateChanged(evt);
            }
        });
        cboKyThuatVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboKyThuatVienMouseClicked(evt);
            }
        });

        cboTenKhach.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboTenKhachItemStateChanged(evt);
            }
        });
        cboTenKhach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTenKhachActionPerformed(evt);
            }
        });

        txtNgayNhanMay.setEditable(false);
        txtNgayNhanMay.setLabelText("Ngày nhận máy");

        txtNhanVienNhanMay1.setLabelText("Mã");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textAreaScroll4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboKyThuatVien, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNgayNhanMay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboTenKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtNhanVienNhanMay, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNhanVienNhanMay1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNhanVienNhanMay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNhanVienNhanMay1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(cboKyThuatVien, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cboTenKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNgayNhanMay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(textAreaScroll4, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Thông Tin Sửa Chữa"));
        jPanel14.setPreferredSize(new java.awt.Dimension(400, 381));

        textAreaScroll6.setBackground(new java.awt.Color(255, 255, 255));
        textAreaScroll6.setLabelText("Tình trạng máy");

        txtTinhTrangMay.setColumns(20);
        txtTinhTrangMay.setRows(5);
        textAreaScroll6.setViewportView(txtTinhTrangMay);

        txtMaSuaChua.setEditable(false);
        txtMaSuaChua.setLabelText("Mã sửa chữa");
        txtMaSuaChua.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtMaSuaChuaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtMaSuaChuaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtMaSuaChuaMouseReleased(evt);
            }
        });
        txtMaSuaChua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaSuaChuaActionPerformed(evt);
            }
        });

        cboMay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboMayItemStateChanged(evt);
            }
        });
        cboMay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboMayMouseClicked(evt);
            }
        });

        cboHang1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboHang1ItemStateChanged(evt);
            }
        });
        cboHang1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboHang1MouseClicked(evt);
            }
        });

        txtNgayTraDuKien.setLabelText("Ngày trả máy dự kiến");

        txtNgayTraMayThucTe.setEditable(false);
        txtNgayTraMayThucTe.setLabelText("Ngày trả máy thực tế");

        lblTrangThai.setText("Đã Nhận");

        txtPhiSuaChua.setLabelText("Phí sửa chữa");

        textAreaScroll7.setBackground(new java.awt.Color(255, 255, 255));
        textAreaScroll7.setLabelText("Mô tả");

        txtMota.setColumns(20);
        txtMota.setRows(5);
        textAreaScroll7.setViewportView(txtMota);

        jLabel9.setText("Tổng Tiền");

        lblTong.setText("0");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(txtMaSuaChua, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboHang1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboMay, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(textAreaScroll6, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNgayTraDuKien, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(20, 20, 20))
                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                        .addComponent(lblTong, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                                        .addComponent(txtPhiSuaChua, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(textAreaScroll7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(txtNgayTraMayThucTe, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(lblTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboHang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboMay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtMaSuaChua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNgayTraDuKien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNgayTraMayThucTe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(lblTong)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(txtPhiSuaChua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(textAreaScroll7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textAreaScroll6, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 5));

        jButton47.setBackground(new java.awt.Color(186, 232, 232));
        jButton47.setText("Thêm sửa chữa");
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton47);

        jButton48.setBackground(new java.awt.Color(186, 232, 232));
        jButton48.setText("Sửa sửa chữa");
        jButton48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton48ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton48);

        jButton56.setBackground(new java.awt.Color(186, 232, 232));
        jButton56.setText("In giấy hẹn");
        jButton56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton56ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton56);

        jButton50.setBackground(new java.awt.Color(186, 232, 232));
        jButton50.setText("Làm mới sửa chữa");
        jButton50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton50);

        jButton53.setBackground(new java.awt.Color(186, 232, 232));
        jButton53.setText("Đã sửa máy");
        jButton53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton53ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton53);

        jButton49.setBackground(new java.awt.Color(186, 232, 232));
        jButton49.setText("Nhận máy");
        jButton49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton49ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton49);

        tableDark3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mã linh kiện", "Tên linh kiện"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableDark3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDark3MouseClicked(evt);
            }
        });
        jScrollPane16.setViewportView(tableDark3);
        if (tableDark3.getColumnModel().getColumnCount() > 0) {
            tableDark3.getColumnModel().getColumn(1).setResizable(false);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );

        tableDark1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã linh kiện", "Tên linh kiện", "Số lượng", "Giá"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableDark1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDark1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tableDark1MouseEntered(evt);
            }
        });
        jScrollPane6.setViewportView(tableDark1);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jpnCapNhatSuaChuaLayout = new javax.swing.GroupLayout(jpnCapNhatSuaChua);
        jpnCapNhatSuaChua.setLayout(jpnCapNhatSuaChuaLayout);
        jpnCapNhatSuaChuaLayout.setHorizontalGroup(
            jpnCapNhatSuaChuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCapNhatSuaChuaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jpnCapNhatSuaChuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnCapNhatSuaChuaLayout.createSequentialGroup()
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 872, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnCapNhatSuaChuaLayout.createSequentialGroup()
                        .addGroup(jpnCapNhatSuaChuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jpnCapNhatSuaChuaLayout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(53, 53, 53)
                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jpnCapNhatSuaChuaLayout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(12, 12, 12)
                                .addGroup(jpnCapNhatSuaChuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMaMay1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaLK, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtKyThuatVien, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtKH, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(31, 31, 31))))
        );
        jpnCapNhatSuaChuaLayout.setVerticalGroup(
            jpnCapNhatSuaChuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCapNhatSuaChuaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jpnCapNhatSuaChuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(8, 8, 8)
                .addGroup(jpnCapNhatSuaChuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpnCapNhatSuaChuaLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(txtMaMay1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(txtMaLK, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(txtKyThuatVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(txtKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabSuaChua.addTab("Cập nhật", jpnCapNhatSuaChua);

        jpnMain.add(tabSuaChua, "tabSuaChua");

        jpnDanhSachKhoHang.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Showing 1 of 1");

        jButton9.setBackground(new java.awt.Color(186, 232, 232));
        jButton9.setText("<");

        jButton10.setBackground(new java.awt.Color(186, 232, 232));
        jButton10.setText(">");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel29.setText("Kho hàng");

        tblKhoHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã linh kiện", "Tên linh kiện", "Số lượng", "Giá bán", "Mô tả"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhoHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhoHangMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblKhoHang);
        if (tblKhoHang.getColumnModel().getColumnCount() > 0) {
            tblKhoHang.getColumnModel().getColumn(0).setResizable(false);
            tblKhoHang.getColumnModel().getColumn(1).setResizable(false);
            tblKhoHang.getColumnModel().getColumn(2).setResizable(false);
            tblKhoHang.getColumnModel().getColumn(3).setResizable(false);
            tblKhoHang.getColumnModel().getColumn(4).setResizable(false);
        }

        jButton8.setBackground(new java.awt.Color(186, 232, 232));
        jButton8.setText(">");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnDanhSachKhoHangLayout = new javax.swing.GroupLayout(jpnDanhSachKhoHang);
        jpnDanhSachKhoHang.setLayout(jpnDanhSachKhoHangLayout);
        jpnDanhSachKhoHangLayout.setHorizontalGroup(
            jpnDanhSachKhoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDanhSachKhoHangLayout.createSequentialGroup()
                .addGroup(jpnDanhSachKhoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnDanhSachKhoHangLayout.createSequentialGroup()
                        .addGap(710, 710, 710)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnDanhSachKhoHangLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(617, 617, 617)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnDanhSachKhoHangLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jpnDanhSachKhoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jpnDanhSachKhoHangLayout.setVerticalGroup(
            jpnDanhSachKhoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDanhSachKhoHangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92)
                .addGroup(jpnDanhSachKhoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );

        tabLinhKien.addTab("Danh sách", jpnDanhSachKhoHang);

        jpnCapNhatKhoHang.setBackground(new java.awt.Color(255, 255, 255));

        txtMaLinhKien.setEditable(false);
        txtMaLinhKien.setLabelText("Mã linh kiện");

        txtTenLinhKien.setLabelText("Tên hàng");

        txtSoLuong.setLabelText("Số lượng");

        txtGiaBan.setLabelText("Giá bán");

        textAreaScroll2.setLabelText("Trạng thái");

        txtMoTa.setColumns(20);
        txtMoTa.setRows(5);
        textAreaScroll2.setViewportView(txtMoTa);

        jButton63.setBackground(new java.awt.Color(186, 232, 232));
        jButton63.setText("Thêm ");
        jButton63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton63ActionPerformed(evt);
            }
        });

        jButton64.setBackground(new java.awt.Color(186, 232, 232));
        jButton64.setText("Sửa");
        jButton64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton64ActionPerformed(evt);
            }
        });

        jButton65.setBackground(new java.awt.Color(186, 232, 232));
        jButton65.setText("Xóa");
        jButton65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton65ActionPerformed(evt);
            }
        });

        jButton66.setBackground(new java.awt.Color(186, 232, 232));
        jButton66.setText("Làm mới");
        jButton66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton66ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(186, 232, 232));
        jButton16.setText("<");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnCapNhatKhoHangLayout = new javax.swing.GroupLayout(jpnCapNhatKhoHang);
        jpnCapNhatKhoHang.setLayout(jpnCapNhatKhoHangLayout);
        jpnCapNhatKhoHangLayout.setHorizontalGroup(
            jpnCapNhatKhoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnCapNhatKhoHangLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jpnCapNhatKhoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textAreaScroll2, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                    .addComponent(txtMaLinhKien, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTenLinhKien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGiaBan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(46, 46, 46)
                .addGroup(jpnCapNhatKhoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(201, 201, 201))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnCapNhatKhoHangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(145, 145, 145))
        );
        jpnCapNhatKhoHangLayout.setVerticalGroup(
            jpnCapNhatKhoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnCapNhatKhoHangLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(txtMaLinhKien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jpnCapNhatKhoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnCapNhatKhoHangLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtTenLinhKien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnCapNhatKhoHangLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jButton63)
                        .addGap(18, 18, 18)
                        .addComponent(jButton64)
                        .addGap(18, 18, 18)
                        .addComponent(jButton65)
                        .addGap(18, 18, 18)
                        .addComponent(jButton66)))
                .addGap(19, 19, 19)
                .addComponent(textAreaScroll2, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        tabLinhKien.addTab("Cập nhật", jpnCapNhatKhoHang);

        jpnMain.add(tabLinhKien, "tabLinhKien");

        jpnDanhSachPhieuNhap.setBackground(new java.awt.Color(255, 255, 255));

        jButton20.setBackground(new java.awt.Color(7, 128, 128));
        jButton20.setText(">");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        tblPhieuNhap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã phiếu nhập", "Nhà cung cấp", "Ngày nhập", "Mô tả"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPhieuNhap.setShowGrid(false);
        tblPhieuNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPhieuNhapMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblPhieuNhap);
        if (tblPhieuNhap.getColumnModel().getColumnCount() > 0) {
            tblPhieuNhap.getColumnModel().getColumn(0).setResizable(false);
            tblPhieuNhap.getColumnModel().getColumn(1).setResizable(false);
            tblPhieuNhap.getColumnModel().getColumn(2).setResizable(false);
            tblPhieuNhap.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout jpnDanhSachPhieuNhapLayout = new javax.swing.GroupLayout(jpnDanhSachPhieuNhap);
        jpnDanhSachPhieuNhap.setLayout(jpnDanhSachPhieuNhapLayout);
        jpnDanhSachPhieuNhapLayout.setHorizontalGroup(
            jpnDanhSachPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnDanhSachPhieuNhapLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 895, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jpnDanhSachPhieuNhapLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jpnDanhSachPhieuNhapLayout.setVerticalGroup(
            jpnDanhSachPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnDanhSachPhieuNhapLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabPhieuNhap.addTab("Danh sách", jpnDanhSachPhieuNhap);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        txtMNCC.setText("jTextField1");
        txtMNCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMNCCActionPerformed(evt);
            }
        });

        txtMNCC1.setText("jTextField1");
        txtMNCC1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMNCC1ActionPerformed(evt);
            }
        });

        txtMaPhieuNhap.setEditable(false);
        txtMaPhieuNhap.setLabelText("Mã phiếu nhập");
        txtMaPhieuNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaPhieuNhapActionPerformed(evt);
            }
        });

        txtNgayNhap.setEditable(false);
        txtNgayNhap.setLabelText("Ngày nhập");

        textAreaScroll1.setBackground(new java.awt.Color(255, 255, 255));
        textAreaScroll1.setLabelText("Mô tả");

        txtMoTaNhap.setColumns(20);
        txtMoTaNhap.setRows(5);
        textAreaScroll1.setViewportView(txtMoTaNhap);

        jButton17.setBackground(new java.awt.Color(7, 128, 128));
        jButton17.setForeground(new java.awt.Color(255, 255, 255));
        jButton17.setText("Thêm ");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton58.setBackground(new java.awt.Color(7, 128, 128));
        jButton58.setForeground(new java.awt.Color(255, 255, 255));
        jButton58.setText("Làm Mới");
        jButton58.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton58MouseClicked(evt);
            }
        });
        jButton58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton58ActionPerformed(evt);
            }
        });

        cboNhaCungCap.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nhà cung cấp" }));
        cboNhaCungCap.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboNhaCungCapItemStateChanged(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(7, 128, 128));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("IN PHIẾU");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        tableDark2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã linh kiện", "Tên linh kiện", "Số lượng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableDark2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDark2MouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(tableDark2);
        if (tableDark2.getColumnModel().getColumnCount() > 0) {
            tableDark2.getColumnModel().getColumn(0).setResizable(false);
            tableDark2.getColumnModel().getColumn(1).setResizable(false);
            tableDark2.getColumnModel().getColumn(2).setResizable(false);
        }

        tableDark4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã linh kiện", "Tên linh kiện", "Số lượng", "Giá nhập"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane17.setViewportView(tableDark4);
        if (tableDark4.getColumnModel().getColumnCount() > 0) {
            tableDark4.getColumnModel().getColumn(0).setResizable(false);
            tableDark4.getColumnModel().getColumn(1).setResizable(false);
            tableDark4.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel10.setText("Linh Kiện");

        jLabel13.setText("Danh Sách");

        jButton15.setBackground(new java.awt.Color(7, 128, 128));
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setText("Thêm linh kiện");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton19.setBackground(new java.awt.Color(7, 128, 128));
        jButton19.setText("<");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(53, 53, 53)
                                .addComponent(jButton58)
                                .addGap(52, 52, 52)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtMaPhieuNhap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNgayNhap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboNhaCungCap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(textAreaScroll1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
                                    .addComponent(jButton15, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(48, 48, 48))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(104, 104, 104)
                                .addComponent(txtMNCC1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtMNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                                .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60, 60, 60))))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton15)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 13, Short.MAX_VALUE)
                        .addComponent(textAreaScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMNCC1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtMaPhieuNhap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNgayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(cboNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton17)
                            .addComponent(jButton58)
                            .addComponent(jButton5))))
                .addGap(12, 12, 12))
        );

        tabPhieuNhap.addTab("Cập nhật", jPanel5);

        jpnMain.add(tabPhieuNhap, "tabPhieuNhap");

        jpnNhaCungCap.setBackground(new java.awt.Color(255, 255, 255));

        panelRound6.setBackground(new java.awt.Color(186, 232, 232));
        panelRound6.setRoundBottomLeft(20);
        panelRound6.setRoundBottomRight(20);
        panelRound6.setRoundTopLeft(20);
        panelRound6.setRoundTopRight(20);

        txtMaNhaCungCap.setEditable(false);
        txtMaNhaCungCap.setBackground(new java.awt.Color(186, 232, 232));
        txtMaNhaCungCap.setLabelText("Mã nhà cung cấp");

        txtTenNhaCungCap.setBackground(new java.awt.Color(186, 232, 232));
        txtTenNhaCungCap.setLabelText("Tên nhà cung cấp");

        textAreaScroll3.setBackground(new java.awt.Color(186, 232, 232));
        textAreaScroll3.setLabelText("Địa chỉ");

        txtDiaChiNCC.setBackground(new java.awt.Color(186, 232, 232));
        txtDiaChiNCC.setColumns(20);
        txtDiaChiNCC.setRows(5);
        textAreaScroll3.setViewportView(txtDiaChiNCC);

        txtEmailNCC.setBackground(new java.awt.Color(186, 232, 232));
        txtEmailNCC.setLabelText("Email");

        txtSoDienThoaiNCC.setBackground(new java.awt.Color(186, 232, 232));
        txtSoDienThoaiNCC.setLabelText("Số điện thoại");

        javax.swing.GroupLayout panelRound6Layout = new javax.swing.GroupLayout(panelRound6);
        panelRound6.setLayout(panelRound6Layout);
        panelRound6Layout.setHorizontalGroup(
            panelRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaNhaCungCap, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                    .addComponent(txtTenNhaCungCap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(textAreaScroll3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtEmailNCC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSoDienThoaiNCC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelRound6Layout.setVerticalGroup(
            panelRound6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(txtMaNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtTenNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(textAreaScroll3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtEmailNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtSoDienThoaiNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        tblNhaCungCap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã nhà cung cấp", "Tên nhà cung cấp", "Địa chỉ", "Email", "Số điện thoại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhaCungCap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhaCungCapMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblNhaCungCap);
        if (tblNhaCungCap.getColumnModel().getColumnCount() > 0) {
            tblNhaCungCap.getColumnModel().getColumn(0).setResizable(false);
            tblNhaCungCap.getColumnModel().getColumn(1).setResizable(false);
            tblNhaCungCap.getColumnModel().getColumn(2).setResizable(false);
            tblNhaCungCap.getColumnModel().getColumn(3).setResizable(false);
            tblNhaCungCap.getColumnModel().getColumn(4).setResizable(false);
        }

        btnThemNCC.setBackground(new java.awt.Color(186, 232, 232));
        btnThemNCC.setText("Thêm");
        btnThemNCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNCCActionPerformed(evt);
            }
        });

        btnXoaNCC.setBackground(new java.awt.Color(186, 232, 232));
        btnXoaNCC.setText("Xóa");
        btnXoaNCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNCCActionPerformed(evt);
            }
        });

        btnLamMoiNCC.setBackground(new java.awt.Color(186, 232, 232));
        btnLamMoiNCC.setText("Làm mới");
        btnLamMoiNCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiNCCActionPerformed(evt);
            }
        });

        btnSuaNCC.setBackground(new java.awt.Color(186, 232, 232));
        btnSuaNCC.setText("Sửa");
        btnSuaNCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaNCCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnNhaCungCapLayout = new javax.swing.GroupLayout(jpnNhaCungCap);
        jpnNhaCungCap.setLayout(jpnNhaCungCapLayout);
        jpnNhaCungCapLayout.setHorizontalGroup(
            jpnNhaCungCapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnNhaCungCapLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(panelRound6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jpnNhaCungCapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpnNhaCungCapLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(btnThemNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnSuaNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(btnXoaNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnLamMoiNCC)))
                .addGap(0, 0, 0))
        );
        jpnNhaCungCapLayout.setVerticalGroup(
            jpnNhaCungCapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnNhaCungCapLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jpnNhaCungCapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelRound6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpnNhaCungCapLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jpnNhaCungCapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnThemNCC)
                            .addComponent(btnSuaNCC)
                            .addComponent(btnXoaNCC)
                            .addComponent(btnLamMoiNCC))))
                .addGap(167, 167, 167))
        );

        jpnMain.add(jpnNhaCungCap, "cardNhaCungCap");

        jpnThongTinSuaChua.setBackground(new java.awt.Color(255, 255, 255));
        jpnThongTinSuaChua.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton34.setBackground(new java.awt.Color(186, 232, 232));
        jButton34.setText(">");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });
        jpnThongTinSuaChua.add(jButton34, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 600, 50, 30));

        tblThongTinPhieuNhap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã nhân viên", "Họ tên", "Tài khoản", "Email", "Số điện thoại", "Đia chỉ", "Lương", "Chức vụ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThongTinPhieuNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblThongTinPhieuNhapMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblThongTinPhieuNhap);
        if (tblThongTinPhieuNhap.getColumnModel().getColumnCount() > 0) {
            tblThongTinPhieuNhap.getColumnModel().getColumn(0).setResizable(false);
            tblThongTinPhieuNhap.getColumnModel().getColumn(1).setResizable(false);
            tblThongTinPhieuNhap.getColumnModel().getColumn(2).setResizable(false);
            tblThongTinPhieuNhap.getColumnModel().getColumn(3).setResizable(false);
            tblThongTinPhieuNhap.getColumnModel().getColumn(4).setResizable(false);
            tblThongTinPhieuNhap.getColumnModel().getColumn(5).setResizable(false);
            tblThongTinPhieuNhap.getColumnModel().getColumn(6).setResizable(false);
            tblThongTinPhieuNhap.getColumnModel().getColumn(7).setResizable(false);
        }

        jpnThongTinSuaChua.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 820, 580));

        tabNhanVien.addTab("Danh sách", jpnThongTinSuaChua);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        txtMaNhanVien.setEditable(false);
        txtMaNhanVien.setLabelText("Mã nhân viên");

        txtHoTenNV.setLabelText("Họ tên");

        txtTaiKhoanNV.setLabelText("Tài khoản");

        txtMatkhauNV.setLabelText("Mật khẩu");

        txtEmailNV.setLabelText("Email");

        txtSoDienThoaiNV.setLabelText("Số điện thoại");

        txtLuongNV.setLabelText("Lương");

        textAreaScroll5.setLabelText("Địa chỉ");

        txtDiaChiNV.setColumns(20);
        txtDiaChiNV.setRows(5);
        textAreaScroll5.setViewportView(txtDiaChiNV);

        cboChucVu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nhân viên", "Quản Lý", "Nhân viên kỹ thuật" }));

        lblHinhAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHinhAnhMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblHinhAnh, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblHinhAnh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btnThemNV.setBackground(new java.awt.Color(7, 128, 128));
        btnThemNV.setForeground(new java.awt.Color(255, 255, 255));
        btnThemNV.setText("Thêm ");
        btnThemNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNVActionPerformed(evt);
            }
        });

        btnSuaNV.setBackground(new java.awt.Color(7, 128, 128));
        btnSuaNV.setForeground(new java.awt.Color(255, 255, 255));
        btnSuaNV.setText("Sửa");
        btnSuaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaNVActionPerformed(evt);
            }
        });

        btnXoaNV.setBackground(new java.awt.Color(7, 128, 128));
        btnXoaNV.setForeground(new java.awt.Color(255, 255, 255));
        btnXoaNV.setText("Xóa");
        btnXoaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNVActionPerformed(evt);
            }
        });

        btnLamMoiNV.setBackground(new java.awt.Color(7, 128, 128));
        btnLamMoiNV.setForeground(new java.awt.Color(255, 255, 255));
        btnLamMoiNV.setText("Làm mới");
        btnLamMoiNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiNVActionPerformed(evt);
            }
        });

        jButton46.setBackground(new java.awt.Color(186, 232, 232));
        jButton46.setText("<");
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtHoTenNV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTaiKhoanNV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtMatkhauNV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtEmailNV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 178, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(txtSoDienThoaiNV, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(txtLuongNV, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(textAreaScroll5, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(btnThemNV)
                                    .addGap(6, 6, 6)
                                    .addComponent(btnSuaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(12, 12, 12)
                                    .addComponent(btnXoaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(12, 12, 12)
                                    .addComponent(btnLamMoiNV))))
                        .addContainerGap())))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtHoTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtTaiKhoanNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(txtSoDienThoaiNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(textAreaScroll5, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLuongNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(126, 126, 126)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(txtMatkhauNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnThemNV)
                            .addComponent(btnSuaNV)
                            .addComponent(btnXoaNV)
                            .addComponent(btnLamMoiNV))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmailNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        tabNhanVien.addTab("Cập nhật", jPanel7);

        jpnMain.add(tabNhanVien, "CardNhanVien");

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sửa chữa", "Ngày lập", "Thành tiền", "Thời gian bảo hành", "Ngày bắt đầu", "Mô tả"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblHoaDon);
        if (tblHoaDon.getColumnModel().getColumnCount() > 0) {
            tblHoaDon.getColumnModel().getColumn(0).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(1).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(2).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(3).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(4).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(5).setResizable(false);
        }

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 773, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(126, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(104, Short.MAX_VALUE))
        );

        tabHoaDon.addTab("Danh sách", jPanel9);

        jPanel10.setBackground(new java.awt.Color(204, 204, 204));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        textField43.setLabelText("Mã sửa chữa");

        textField45.setEditable(false);
        textField45.setLabelText("Tên khách hàng");

        textField46.setEditable(false);
        textField46.setLabelText("Nhân viên tiếp nhập");
        textField46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textField46ActionPerformed(evt);
            }
        });

        textField47.setEditable(false);
        textField47.setLabelText("Nhân viên kỹ thuật");

        textField48.setEditable(false);
        textField48.setLabelText("Thành tiền");

        textField49.setEditable(false);
        textField49.setLabelText("Thời gian lập");

        comboBoxSuggestion2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3", "6", "9", "12" }));

        jButton6.setBackground(new java.awt.Color(7, 128, 128));
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("In hóa đơn");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        textAreaScroll8.setBackground(new java.awt.Color(255, 255, 255));
        textAreaScroll8.setLabelText("Mô tả");

        textArea8.setEditable(false);
        textArea8.setColumns(20);
        textArea8.setRows(5);
        textAreaScroll8.setViewportView(textArea8);

        jButton14.setBackground(new java.awt.Color(7, 128, 128));
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setLabel("Tìm kiếm");
        jButton14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton14MouseClicked(evt);
            }
        });
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        textField44.setLabelText("Mã sửa chữa");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(176, Short.MAX_VALUE)
                .addComponent(comboBoxSuggestion2, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(171, 171, 171))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(textField44, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(269, 269, 269))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(textAreaScroll8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(textField46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(textField45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addComponent(textField43, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textField49, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textField48, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textField47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(39, 39, 39))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textField43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textField47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textField45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textField48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textField46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textField49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textAreaScroll8, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comboBoxSuggestion2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(textField44, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38))))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(180, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );

        tabHoaDon.addTab("Cập nhật", jPanel10);

        jpnMain.add(tabHoaDon, "cardHoaDon");

        jpnThongTinMay.setBackground(new java.awt.Color(255, 255, 255));

        txtTenMay.setLabelText("Tên máy");

        tblThongTinBaoHanh1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã máy", "Tên máy", "Tên hãng", "Ram", "Rom", "Chip"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThongTinBaoHanh1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblThongTinBaoHanh1MouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tblThongTinBaoHanh1);
        if (tblThongTinBaoHanh1.getColumnModel().getColumnCount() > 0) {
            tblThongTinBaoHanh1.getColumnModel().getColumn(0).setResizable(false);
            tblThongTinBaoHanh1.getColumnModel().getColumn(1).setResizable(false);
            tblThongTinBaoHanh1.getColumnModel().getColumn(2).setResizable(false);
            tblThongTinBaoHanh1.getColumnModel().getColumn(3).setResizable(false);
            tblThongTinBaoHanh1.getColumnModel().getColumn(4).setResizable(false);
            tblThongTinBaoHanh1.getColumnModel().getColumn(5).setResizable(false);
        }

        jButton41.setBackground(new java.awt.Color(186, 232, 232));
        jButton41.setText("Thêm");
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });

        jButton44.setBackground(new java.awt.Color(186, 232, 232));
        jButton44.setText("Sửa");
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        jButton42.setBackground(new java.awt.Color(186, 232, 232));
        jButton42.setText("Xóa");
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });

        jButton43.setBackground(new java.awt.Color(186, 232, 232));
        jButton43.setText("Làm mới");
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });

        txtRam.setLabelText("Ram");

        txtRom.setLabelText("Rom");

        txtChip.setLabelText("Chip");

        cboHangMay.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tên Hãng" }));
        cboHangMay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboHangMayItemStateChanged(evt);
            }
        });

        txtMaMay.setEditable(false);
        txtMaMay.setLabelText("Mã máy");

        txtRam1.setLabelText("Ram");

        javax.swing.GroupLayout jpnThongTinMayLayout = new javax.swing.GroupLayout(jpnThongTinMay);
        jpnThongTinMay.setLayout(jpnThongTinMayLayout);
        jpnThongTinMayLayout.setHorizontalGroup(
            jpnThongTinMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnThongTinMayLayout.createSequentialGroup()
                .addGroup(jpnThongTinMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnThongTinMayLayout.createSequentialGroup()
                        .addGap(350, 350, 350)
                        .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jButton42, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jButton43)
                        .addGap(0, 208, Short.MAX_VALUE))
                    .addGroup(jpnThongTinMayLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jpnThongTinMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jpnThongTinMayLayout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(jLabel52))
                            .addComponent(txtRom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtRam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboHangMay, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                            .addComponent(txtTenMay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtChip, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMaMay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane9)))
                .addContainerGap())
            .addGroup(jpnThongTinMayLayout.createSequentialGroup()
                .addGap(571, 571, 571)
                .addComponent(txtRam1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(136, Short.MAX_VALUE))
        );
        jpnThongTinMayLayout.setVerticalGroup(
            jpnThongTinMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnThongTinMayLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jpnThongTinMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnThongTinMayLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(txtMaMay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTenMay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(cboHangMay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(txtRam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtRom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtChip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jpnThongTinMayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton42)
                    .addComponent(jButton43)
                    .addComponent(jButton41)
                    .addComponent(jButton44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRam1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(115, Short.MAX_VALUE))
        );

        jpnMain.add(jpnThongTinMay, "cardThongTinMay");

        jpnNhangHang.setBackground(new java.awt.Color(255, 255, 255));

        txtMaHang.setEditable(false);
        txtMaHang.setLabelText("Mã nhãn hàng");
        txtMaHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaHangActionPerformed(evt);
            }
        });

        tblThongTinBaoHanh2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã nhãn hàng", "Tên nhãn hàng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThongTinBaoHanh2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblThongTinBaoHanh2MouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(tblThongTinBaoHanh2);
        if (tblThongTinBaoHanh2.getColumnModel().getColumnCount() > 0) {
            tblThongTinBaoHanh2.getColumnModel().getColumn(0).setResizable(false);
            tblThongTinBaoHanh2.getColumnModel().getColumn(1).setResizable(false);
        }

        btnThemNH.setBackground(new java.awt.Color(186, 232, 232));
        btnThemNH.setText("Thêm");
        btnThemNH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNHActionPerformed(evt);
            }
        });

        btnSuaNH.setBackground(new java.awt.Color(186, 232, 232));
        btnSuaNH.setText("Sửa");
        btnSuaNH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaNHActionPerformed(evt);
            }
        });

        btnXoaNH.setBackground(new java.awt.Color(186, 232, 232));
        btnXoaNH.setText("Xóa");
        btnXoaNH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNHActionPerformed(evt);
            }
        });

        btnLamMoiNH.setBackground(new java.awt.Color(186, 232, 232));
        btnLamMoiNH.setText("Làm Mới");
        btnLamMoiNH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiNHActionPerformed(evt);
            }
        });

        txtTenHang.setLabelText("Tên nhãn hàng");

        javax.swing.GroupLayout jpnNhangHangLayout = new javax.swing.GroupLayout(jpnNhangHang);
        jpnNhangHang.setLayout(jpnNhangHangLayout);
        jpnNhangHangLayout.setHorizontalGroup(
            jpnNhangHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnNhangHangLayout.createSequentialGroup()
                .addGroup(jpnNhangHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnNhangHangLayout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(txtMaHang, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnNhangHangLayout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jLabel53))
                    .addGroup(jpnNhangHangLayout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(txtTenHang, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnNhangHangLayout.createSequentialGroup()
                        .addGap(180, 180, 180)
                        .addComponent(btnThemNH, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(btnSuaNH, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(btnXoaNH, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnLamMoiNH))
                    .addGroup(jpnNhangHangLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 251, Short.MAX_VALUE))
        );
        jpnNhangHangLayout.setVerticalGroup(
            jpnNhangHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnNhangHangLayout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addComponent(jLabel53)
                .addGap(17, 17, 17)
                .addComponent(txtTenHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jpnNhangHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThemNH)
                    .addComponent(btnSuaNH)
                    .addComponent(btnXoaNH)
                    .addComponent(btnLamMoiNH))
                .addGap(87, 87, 87))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnNhangHangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtMaHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(547, 547, 547))
        );

        jpnMain.add(jpnNhangHang, "cardNhangHang");

        jpnBaoHanh.setBackground(new java.awt.Color(255, 255, 255));
        jpnBaoHanh.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jpnBaoHanh.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 133, -1, -1));

        tblBaoHanh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Số thứ tự", "Mã sửa chữa", "Thời gian bảo hành", "Ngày bắt đầu"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBaoHanh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBaoHanhMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tblBaoHanh);
        if (tblBaoHanh.getColumnModel().getColumnCount() > 0) {
            tblBaoHanh.getColumnModel().getColumn(0).setResizable(false);
            tblBaoHanh.getColumnModel().getColumn(1).setResizable(false);
            tblBaoHanh.getColumnModel().getColumn(2).setResizable(false);
            tblBaoHanh.getColumnModel().getColumn(3).setResizable(false);
        }

        jpnBaoHanh.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 790, 580));

        jpnMain.add(jpnBaoHanh, "cardBaoHanh");

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        tblDoanhThu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Số thứ tự", "Ngày", "Nội dung", "Tổng tiền", "Trạng thái"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane13.setViewportView(tblDoanhThu);
        if (tblDoanhThu.getColumnModel().getColumnCount() > 0) {
            tblDoanhThu.getColumnModel().getColumn(0).setResizable(false);
            tblDoanhThu.getColumnModel().getColumn(1).setResizable(false);
            tblDoanhThu.getColumnModel().getColumn(2).setResizable(false);
            tblDoanhThu.getColumnModel().getColumn(3).setResizable(false);
            tblDoanhThu.getColumnModel().getColumn(4).setResizable(false);
        }

        jButton36.setBackground(new java.awt.Color(186, 232, 232));
        jButton36.setText(">");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(710, 710, 710)
                        .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 773, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(126, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabDoanhThu.addTab("Daonh thu", jPanel12);

        jPanel13.setBackground(new java.awt.Color(204, 204, 204));

        tblDoanhThuLuong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Họ tên", "Lương", "Chức vụ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane14.setViewportView(tblDoanhThuLuong);
        if (tblDoanhThuLuong.getColumnModel().getColumnCount() > 0) {
            tblDoanhThuLuong.getColumnModel().getColumn(1).setResizable(false);
            tblDoanhThuLuong.getColumnModel().getColumn(2).setResizable(false);
            tblDoanhThuLuong.getColumnModel().getColumn(3).setResizable(false);
        }

        jButton74.setBackground(new java.awt.Color(186, 232, 232));
        jButton74.setText("<");
        jButton74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton74ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(812, Short.MAX_VALUE)
                .addComponent(jButton74, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 773, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(121, Short.MAX_VALUE)))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(610, Short.MAX_VALUE)
                .addComponent(jButton74, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addGap(64, 64, 64)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(95, Short.MAX_VALUE)))
        );

        tabDoanhThu.addTab("Lương NV", jPanel13);

        jpnMain.add(tabDoanhThu, "cardThongKe");

        getContentPane().add(jpnMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, 920, 710));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblKhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblKhachHangMouseClicked
        CardLayout layout = (CardLayout) jpnMain.getLayout();
        layout.show(jpnMain, "tabKhachHang");
        txtTimKiem.setText("");

        this.loadKH();
    }//GEN-LAST:event_lblKhachHangMouseClicked

    private void lblKhachHangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblKhachHangMouseEntered
        lblKhachHang.setForeground(Color.YELLOW);
    }//GEN-LAST:event_lblKhachHangMouseEntered

    private void lblKhachHangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblKhachHangMouseExited
        lblKhachHang.setForeground(Color.BLACK);
    }//GEN-LAST:event_lblKhachHangMouseExited

    private void lblDichVuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDichVuMouseClicked
        CardLayout layout = (CardLayout) jpnMain.getLayout();
        layout.show(jpnMain, "tabSuaChua");
        txtTimKiem.setText("");

        this.loadSC();
    }//GEN-LAST:event_lblDichVuMouseClicked

    private void lblDichVuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDichVuMouseEntered
        lblDichVu.setForeground(Color.YELLOW);
    }//GEN-LAST:event_lblDichVuMouseEntered

    private void lblDichVuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDichVuMouseExited
        lblDichVu.setForeground(Color.BLACK);
    }//GEN-LAST:event_lblDichVuMouseExited

    private void lblKhoHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblKhoHangMouseClicked
        CardLayout layout = (CardLayout) jpnMain.getLayout();
        layout.show(jpnMain, "tabLinhKien");
        txtTimKiem.setText("");
        this.loadLK();
    }//GEN-LAST:event_lblKhoHangMouseClicked

    private void lblKhoHangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblKhoHangMouseEntered
        lblKhoHang.setForeground(Color.YELLOW);
    }//GEN-LAST:event_lblKhoHangMouseEntered

    private void lblKhoHangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblKhoHangMouseExited
        lblKhoHang.setForeground(Color.BLACK);
    }//GEN-LAST:event_lblKhoHangMouseExited

    private void lblNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNhanVienMouseClicked
        CardLayout layout = (CardLayout) jpnMain.getLayout();
        layout.show(jpnMain, "tabPhieuNhap");
        txtTimKiem.setText("");
        this.loadPN();
    }//GEN-LAST:event_lblNhanVienMouseClicked

    private void lblNhanVienMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNhanVienMouseEntered
        lblNhanVien.setForeground(Color.YELLOW);
    }//GEN-LAST:event_lblNhanVienMouseEntered

    private void lblNhanVienMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNhanVienMouseExited
        lblNhanVien.setForeground(Color.BLACK);
    }//GEN-LAST:event_lblNhanVienMouseExited

    private void lblThongTinBanHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongTinBanHangMouseClicked
        CardLayout layout = (CardLayout) jpnMain.getLayout();
        layout.show(jpnMain, "cardNhaCungCap");
        txtTimKiem.setText("");
        this.loadNCC();
    }//GEN-LAST:event_lblThongTinBanHangMouseClicked

    private void lblThongTinBanHangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongTinBanHangMouseEntered
        lblThongTinBanHang.setForeground(Color.YELLOW);
    }//GEN-LAST:event_lblThongTinBanHangMouseEntered

    private void lblThongTinBanHangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongTinBanHangMouseExited
        lblThongTinBanHang.setForeground(Color.BLACK);
    }//GEN-LAST:event_lblThongTinBanHangMouseExited

    private void lblThongTinSuaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongTinSuaMouseClicked
        CardLayout layout = (CardLayout) jpnMain.getLayout();
        layout.show(jpnMain, "CardNhanVien");
        txtTimKiem.setText("");
        this.loadNV();
    }//GEN-LAST:event_lblThongTinSuaMouseClicked

    private void lblThongTinSuaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongTinSuaMouseEntered
        lblThongTinSua.setForeground(Color.YELLOW);
    }//GEN-LAST:event_lblThongTinSuaMouseEntered

    private void lblThongTinSuaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongTinSuaMouseExited
        lblThongTinSua.setForeground(Color.BLACK);
    }//GEN-LAST:event_lblThongTinSuaMouseExited
    int index = 0;
    private void tblKhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhachHangMouseClicked
        if (evt.getClickCount() == 2) {
            this.index = tblKhachHang.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                this.editKH();
                jButton12.setEnabled(true);
                jButton13.setEnabled(true);
                tabKhachHang.setSelectedIndex(1);
            }
        }
    }//GEN-LAST:event_tblKhachHangMouseClicked

    private void tblKhachHangMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhachHangMouseReleased
        try {
            if (this.tblKhachHang.getCellEditor() != null) {
                this.tblKhachHang.getCellEditor().cancelCellEditing();
            }
        } catch (Exception e) {
        }
        //Chua co sql nen loi
    }//GEN-LAST:event_tblKhachHangMouseReleased

    private void tblKhoHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhoHangMouseClicked
        if (evt.getClickCount() == 2) {
            this.index = tblKhoHang.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                this.editLK();
                jButton64.setEnabled(true);
                jButton65.setEnabled(true);
                tabLinhKien.setSelectedIndex(1);
            }
        }
    }//GEN-LAST:event_tblKhoHangMouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (!txtMaPhieuNhap.getText().equals("")) {
            try {
                String MaPhieuNhap = txtMaPhieuNhap.getText();
                Hashtable map = new Hashtable();
                JasperReport report = JasperCompileManager.compileReport("src\\com\\phones\\ui\\InPhieuNhap.jrxml");
                map.put("MaPhieuNhap", MaPhieuNhap);
                JasperPrint p = JasperFillManager.fillReport(report, map, DBConnection.conn);
                JasperViewer.viewReport(p, false);
                //JasperExportManager.exportReportToPdfFile(p, "test.pdf");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        } else {
            DialogHelper.alert(this, "Mã phiêu nhập không được để trống");
            return;
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        // TODO add your handling code here:
        tabNhanVien.setSelectedIndex(1);

    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        // TODO add your handling code here:
        tabNhanVien.setSelectedIndex(0);
    }//GEN-LAST:event_jButton46ActionPerformed

    private void lblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHoaDonMouseClicked
        CardLayout layout = (CardLayout) jpnMain.getLayout();
        layout.show(jpnMain, "cardHoaDon");
        txtTimKiem.setText("");
        this.loadHD();
    }//GEN-LAST:event_lblHoaDonMouseClicked

    private void lblHoaDonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHoaDonMouseEntered
        lblHoaDon.setForeground(Color.YELLOW);
    }//GEN-LAST:event_lblHoaDonMouseEntered

    private void lblHoaDonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHoaDonMouseExited
        lblHoaDon.setForeground(Color.BLACK);
    }//GEN-LAST:event_lblHoaDonMouseExited

    private void lblThongTinMayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongTinMayMouseClicked
        CardLayout layout = (CardLayout) jpnMain.getLayout();
        layout.show(jpnMain, "cardThongTinMay");
        txtTimKiem.setText("");
        this.loadTTM();
    }//GEN-LAST:event_lblThongTinMayMouseClicked

    private void lblThongTinMayMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongTinMayMouseEntered
        lblThongTinMay.setForeground(Color.YELLOW);
    }//GEN-LAST:event_lblThongTinMayMouseEntered

    private void lblThongTinMayMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongTinMayMouseExited
        lblThongTinMay.setForeground(Color.BLACK);
    }//GEN-LAST:event_lblThongTinMayMouseExited

    private void lblNhangHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNhangHangMouseClicked
        CardLayout layout = (CardLayout) jpnMain.getLayout();
        layout.show(jpnMain, "cardNhangHang");
        txtTimKiem.setText("");
        this.loadHM();
    }//GEN-LAST:event_lblNhangHangMouseClicked

    private void lblNhangHangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNhangHangMouseEntered
        lblNhangHang.setForeground(Color.YELLOW);
    }//GEN-LAST:event_lblNhangHangMouseEntered

    private void lblNhangHangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNhangHangMouseExited
        lblNhangHang.setForeground(Color.BLACK);
    }//GEN-LAST:event_lblNhangHangMouseExited

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        PhanQuyen();
        loadKH();
        loadHM();
        loadTTM();
        loadNV();
        loadNCC();
        loadLK();
        loadPN();
        loadSC();
        loadHD();
        loadBH();

        fillComBoBoxHang();
        fillComBoBoxNhaCungCap();

        fillComBoBoxHang1();

        fillComBoBoxMay();
        //fillComBoBoxLinhKien();
        fillComBoBoxKhachHang();
        fillComBoBoxNVSua();

        fillTableDoanhThu();
        fillTableDoanhThuLuong();
        //txtRam1.setVisible(false);
        txtMNCC1.setText("MLK" + (tblKhoHang.getRowCount() + 1));
        txtNgayNhanMay.setText(DateHelper.toString(DateHelper.now()));
        jButton15.setEnabled(false);
    }//GEN-LAST:event_formWindowOpened

    private void tblThongTinBaoHanh2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblThongTinBaoHanh2MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.index = tblThongTinBaoHanh2.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                this.editHM();
                tabKhachHang.setSelectedIndex(0);
                btnSuaNH.setEnabled(true);
                btnXoaNH.setEnabled(true);
            }
        }
    }//GEN-LAST:event_tblThongTinBaoHanh2MouseClicked

    private void lblHinhAnhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHinhAnhMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.selectImage();
        }
    }//GEN-LAST:event_lblHinhAnhMouseClicked

    private void tblThongTinPhieuNhapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblThongTinPhieuNhapMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.index = tblThongTinPhieuNhap.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                this.editNV();
                tabNhanVien.setSelectedIndex(1);
                btnXoaNV.setEnabled(true);
            }
        }
    }//GEN-LAST:event_tblThongTinPhieuNhapMouseClicked

    private void tblNhaCungCapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhaCungCapMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.index = tblNhaCungCap.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                this.editNCC();
                btnSuaNCC.setEnabled(true);
                btnXoaNCC.setEnabled(true);

            }
        }
    }//GEN-LAST:event_tblNhaCungCapMouseClicked

    private void lblBaoHanhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBaoHanhMouseClicked
        // TODO add your handling code here:
        CardLayout layout = (CardLayout) jpnMain.getLayout();
        layout.show(jpnMain, "cardBaoHanh");
        txtTimKiem.setText("");
        this.loadBH();
    }//GEN-LAST:event_lblBaoHanhMouseClicked

    private void lblBaoHanhMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBaoHanhMouseEntered
        // TODO add your handling code here:
        lblBaoHanh.setForeground(Color.YELLOW);
    }//GEN-LAST:event_lblBaoHanhMouseEntered

    private void lblBaoHanhMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBaoHanhMouseExited
        // TODO add your handling code here:
        lblBaoHanh.setForeground(Color.BLACK);
    }//GEN-LAST:event_lblBaoHanhMouseExited

    private void tblBaoHanhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBaoHanhMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblBaoHanhMouseClicked

    private void tblPhieuNhapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPhieuNhapMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.index = tblPhieuNhap.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                this.editPN();
                tabPhieuNhap.setSelectedIndex(1);
            }
        }
    }//GEN-LAST:event_tblPhieuNhapMouseClicked

    private void tblThongTinBaoHanh1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblThongTinBaoHanh1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.index = tblThongTinBaoHanh1.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                this.editTTM();
                jButton44.setEnabled(true);
                jButton42.setEnabled(true);

            }
        }
    }//GEN-LAST:event_tblThongTinBaoHanh1MouseClicked
    float phi = 0;
    float tien = 0;

    private void tblDichVuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDichVuMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.index = tblDichVu.rowAtPoint(evt.getPoint());
            txtMatKhau.setEnabled(false);
            if (this.index >= 0) {
                this.editSC();
                Tong1();
                setstatusSC(false);
                
                txtNhanVienNhanMay1.setEnabled(false);
                txtNhanVienNhanMay.setEnabled(false);
                cboTenKhach.setEnabled(false);
                tabSuaChua.setSelectedIndex(1);
                if (tblDichVu.getValueAt(index, 5).equals("Đã Nhận")) {
                    jButton49.setEnabled(true);
                } else {
                    jButton49.setEnabled(false);
                }
                if (tblDichVu.getValueAt(index, 5).equals("Đang Sửa")) {
                    jButton53.setEnabled(true);
                } else {
                    jButton53.setEnabled(false);
                }

            }
        }
    }//GEN-LAST:event_tblDichVuMouseClicked

    private void cboHang1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboHang1MouseClicked
        // TODO add your handling code here:
        try {
            //fillComBoBoxMay();

        } catch (Exception e) {
        }
    }//GEN-LAST:event_cboHang1MouseClicked

    private void cboHang1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboHang1ItemStateChanged
        // TODO add your handling code here:
        try {
            fillComBoBoxMay();

        } catch (Exception e) {
        }
    }//GEN-LAST:event_cboHang1ItemStateChanged

    private void lblDoanhThuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDoanhThuMouseClicked
        // TODO add your handling code here:
        CardLayout layout = (CardLayout) jpnMain.getLayout();
        layout.show(jpnMain, "cardThongKe");
    }//GEN-LAST:event_lblDoanhThuMouseClicked

    private void lblDoanhThuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDoanhThuMouseEntered
        lblDoanhThu.setForeground(Color.YELLOW);
    }//GEN-LAST:event_lblDoanhThuMouseEntered

    private void lblDoanhThuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDoanhThuMouseExited
        lblDoanhThu.setForeground(Color.BLACK);

    }//GEN-LAST:event_lblDoanhThuMouseExited

    private void jButton74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton74ActionPerformed
        // TODO add your handling code here:
        tabDoanhThu.setSelectedIndex(0);

    }//GEN-LAST:event_jButton74ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        // TODO add your handling code here:
        tabDoanhThu.setSelectedIndex(1);

    }//GEN-LAST:event_jButton36ActionPerformed

    private void cboTenKhachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTenKhachActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTenKhachActionPerformed

    private void cboTenKhachItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboTenKhachItemStateChanged
        // TODO add your handling code here:
        //LẤY MÃ KHÁCH HÀNG THEO INDEX CBOTENKHACH VÀ THAY ĐỔI MÃ KHÁCH HÀNG KHI THAY ĐỔI CHỌN TÊN KHÁCH HÀNG
        try {
            int indexKH = 0;
            indexKH = cboTenKhach.getSelectedIndex();
            List<KhachHang> list = khdao.select();
            txtKH.setText(list.get(indexKH).getMaKhachHanh());
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cboTenKhachItemStateChanged

    private void cboMayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboMayItemStateChanged
        // TODO add your handling code here:
        try {
            List<ThongTinMay> list = ttmdao.select();
            for (int i = 0; i < list.size(); i++) {
                for (ThongTinMay thongTinMay : list) {
                    if (thongTinMay.getTenMay().equalsIgnoreCase(String.valueOf(cboMay.getSelectedItem()))) {
                        txtMaMay1.setText(String.valueOf(thongTinMay.getMaMay()));
                    }
                }
                break;
            }
        } catch (Exception e) {
            System.out.println("Lỗi!");
        }
    }//GEN-LAST:event_cboMayItemStateChanged

    private void cboMayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboMayMouseClicked

    }//GEN-LAST:event_cboMayMouseClicked

    private void btnThemNHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNHActionPerformed
        // TODO add your handling code here:
        try {
            if (validafromHangMay()) {
                if (checkTrungHM()) {
                    this.insertHM();
                }
            }
        } catch (Exception e) {
        }
        fillComBoBoxHang1();
        fillComBoBoxHang();
    }//GEN-LAST:event_btnThemNHActionPerformed

    private void btnSuaNHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaNHActionPerformed
        // TODO add your handling code here:
        if (validafromHangMay()) {
            this.updateHM();
        }
        fillComBoBoxHang1();
        fillComBoBoxHang();
    }//GEN-LAST:event_btnSuaNHActionPerformed

    private void btnXoaNHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNHActionPerformed
        // TODO add your handling code here:
        this.deleteHM();
        fillComBoBoxHang1();
        fillComBoBoxHang();
    }//GEN-LAST:event_btnXoaNHActionPerformed

    private void btnLamMoiNHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiNHActionPerformed
        // TODO add your handling code here:
        clearHM();
    }//GEN-LAST:event_btnLamMoiNHActionPerformed

    private void cboHangMayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboHangMayItemStateChanged
        // TODO add your handling code here:
        try {
            int indexKH = 0;
            indexKH = cboHangMay.getSelectedIndex();
            List<HangMay> list = hmdao.select();
            txtRam1.setText(list.get(indexKH).getMaHang());

        } catch (Exception e) {
        }
    }//GEN-LAST:event_cboHangMayItemStateChanged

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        // TODO add your handling code here:
        try {
            if (validafromMay()) {
                if (checkTrungTTM()) {
                    insertTTM();
                }
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        // TODO add your handling code here:
        if (validafromMay()) {
            updateTTM();
        }
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        // TODO add your handling code here:
        deleteTTM();
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        // TODO add your handling code here:
        clearTTM();
    }//GEN-LAST:event_jButton43ActionPerformed

    private void cboKyThuatVienItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboKyThuatVienItemStateChanged
        // TODO add your handling code here:
        try {
            List<NhanVien> list = nvdao.select();
            for (int i = 0; i < list.size(); i++) {
                for (NhanVien nhanVien : list) {
                    if (nhanVien.getHoTen().equalsIgnoreCase(String.valueOf(cboKyThuatVien.getSelectedItem()))) {
                        txtKyThuatVien.setText(String.valueOf(nhanVien.getMaNhanVien()));
                    }
                }
                break;
            }
        } catch (Exception e) {
            System.out.println("Lỗi!");
        }
    }//GEN-LAST:event_cboKyThuatVienItemStateChanged

    private void cboKyThuatVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboKyThuatVienMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cboKyThuatVienMouseClicked

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        // TODO add your handling code here:
        //try {
        if (checkSC()) {
            //if (checkTrungSC()) {
            insertSC();
            //insertSC1();
        }
        //}
//        } catch (Exception e) {
//            System.out.println(e);
//        }

    }//GEN-LAST:event_jButton47ActionPerformed

    private void jButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50ActionPerformed
        // TODO add your handling code here:
        clearSC();
        SinhMa();
        setstatusSC(true);

    }//GEN-LAST:event_jButton50ActionPerformed

    private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed
        // TODO add your handling code here:
        updateSC();

        clearSC();
    }//GEN-LAST:event_jButton48ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        try {
            if (checkKhachHang()) {
                if (checkTrungKH()) {
                    insertKH();
                }
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        if (checkKhachHang()) {
            updateKH();
        }

    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        deleteKH();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton51ActionPerformed
        // TODO add your handling code here:
        clearKH();
    }//GEN-LAST:event_jButton51ActionPerformed

    private void btnThemNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNVActionPerformed
        // TODO add your handling code here:
        try {
            if (validafromNV()) {
                if (checkTrungNV()) {
                    insertNV();
                }
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_btnThemNVActionPerformed

    private void btnSuaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaNVActionPerformed
        // TODO add your handling code here:
        if (validafromNV()) {
            updateNV();
            clearNV();
        }
    }//GEN-LAST:event_btnSuaNVActionPerformed

    private void btnXoaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNVActionPerformed
        // TODO add your handling code here:
        deleteNV();
    }//GEN-LAST:event_btnXoaNVActionPerformed

    private void btnLamMoiNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiNVActionPerformed
        // TODO add your handling code here:
        clearNV();
    }//GEN-LAST:event_btnLamMoiNVActionPerformed

    private void jButton63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton63ActionPerformed
        // TODO add your handling code here:
        try {
            if (checkvalidateLK()) {
                if (!checkTrungMLK()) {
                    return;
                }
                insertLK();
            }

        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton63ActionPerformed

    private void jButton64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton64ActionPerformed
        // TODO add your handling code here:
        if (checkvalidateLK()) {
            suaLK();
        }

    }//GEN-LAST:event_jButton64ActionPerformed

    private void jButton65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton65ActionPerformed
        // TODO add your handling code here:
        xoaLK();

    }//GEN-LAST:event_jButton65ActionPerformed

    private void jButton66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton66ActionPerformed
        // TODO add your handling code here:
        lamMoiLK();
    }//GEN-LAST:event_jButton66ActionPerformed

    private void btnThemNCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNCCActionPerformed
        // TODO add your handling code here:
        if (!validafromNCC()) {
            return;
        }
        try {
            if (checkTrungNCC()) {
                insertNCC();
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_btnThemNCCActionPerformed

    private void btnSuaNCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaNCCActionPerformed
        // TODO add your handling code here:
        if (!validafromNCC()) {
            return;
        }
        updateNCC();
    }//GEN-LAST:event_btnSuaNCCActionPerformed

    private void btnXoaNCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNCCActionPerformed
        // TODO add your handling code here:
        if (!validafromNCC()) {
            return;
        }
        deleteNCC();
    }//GEN-LAST:event_btnXoaNCCActionPerformed

    private void btnLamMoiNCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiNCCActionPerformed
        // TODO add your handling code here:
        clearNCC();
    }//GEN-LAST:event_btnLamMoiNCCActionPerformed

    int indexAnHien;
    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        if (indexAnHien == 0) {
            pnlMenu.setSize(0, 680);
            jpnMain.setBounds(120, 80, 820, 680);
            indexAnHien++;
            return;
        }
        if (indexAnHien != 0) {
            pnlMenu.setSize(230, 680);
            jpnMain.setBounds(230, 80, 820, 680);
            indexAnHien--;
            return;
        }

    }//GEN-LAST:event_jLabel1MouseClicked

    private void txtMNCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMNCCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMNCCActionPerformed

    private void cboNhaCungCapItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboNhaCungCapItemStateChanged
        // TODO add your handling code here:
        try {
            int indexKH = 0;
            indexKH = cboNhaCungCap.getSelectedIndex();
            List<NhaCungCap> list = nccdao.select();
            txtMNCC.setText(list.get(indexKH).getMaNhaCungCap());
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cboNhaCungCapItemStateChanged

    private void txtMNCC1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMNCC1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMNCC1ActionPerformed

    private void jButton58MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton58MouseClicked
        // TODO add your handling code here:
        clearPN();
        txtMNCC1.setText("MLK" + (tblKhoHang.getRowCount() + 1));
    }//GEN-LAST:event_jButton58MouseClicked

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        try {
            // TODO add your handling code here:
            if (checkTrungPN()) {
                txtMaPhieuNhap.setText("PN" + (tblPhieuNhap.getRowCount() + 1));
                insertPN();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }//GEN-LAST:event_jButton17ActionPerformed

    private void txtMaPhieuNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaPhieuNhapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaPhieuNhapActionPerformed

    private void jButton58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton58ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton58ActionPerformed

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        // TODO add your handling code here:
        SuaChua model = getModelSC1(txtMaSuaChua.getText(), "Đang Sửa");
        try {
            scdao.updateSL1(model);
            this.loadSC();
            DialogHelper.alert(this, "Nhận máy thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Nhận máy thất bại!");
        }
    }//GEN-LAST:event_jButton49ActionPerformed

    private void jButton53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton53ActionPerformed
        // TODO add your handling code here:

        SuaChua model = getModelSC1(txtMaSuaChua.getText(), "Đã Sửa");
        try {
            scdao.updateSL1(model);
            this.loadSC();

            DialogHelper.alert(this, "sửa trạng thái thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "sửa trạng thái thất bại!");
        }

        String keyword = txtTimKiem.getText();
        List<KhachHang> list = khdao.selectByKeyword(keyword);
        for (KhachHang kh : list) {
            final String username = "dangneen@gmail.com";
            final String password = "biarqepgefqywyiv";

            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true"); //TLS

            Session session = Session.getInstance(prop,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                if (kh.getTenKhachHang().equals(cboTenKhach.getSelectedItem())) {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("dangneen@gmail.com"));
                    message.setRecipients(
                            Message.RecipientType.TO,
                            InternetAddress.parse(kh.getEmail())
                    );
                    System.out.print(tien);
                    message.setSubject("Thông báo nhận máy");
                    message.setText("Thông báo khách hàng:" + cboTenKhach.getSelectedItem()
                            + "\nĐến nhận máy"
                            + "\nKhi đi vui lòng mang theo giấy hẹn và kiểm tra máy trước lúc ra về"
                            + "\nXin cảm ơn"
                            + "\nKhi đi vui lòng mang theo:"+(tien+phi));

                    Transport.send(message);
                    System.out.println("Done");
                    break;
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }


    }//GEN-LAST:event_jButton53ActionPerformed

    private void txtMaSuaChuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaSuaChuaActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtMaSuaChuaActionPerformed

    private void txtMaSuaChuaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMaSuaChuaMouseExited
        // TODO add your handling code here:

    }//GEN-LAST:event_txtMaSuaChuaMouseExited

    private void txtMaSuaChuaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMaSuaChuaMousePressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtMaSuaChuaMousePressed

    private void txtMaSuaChuaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMaSuaChuaMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaSuaChuaMouseReleased

    private void jButton56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton56ActionPerformed
        // TODO add your handling code here:
        if (!txtMaSuaChua.getText().equals("")) {
            try {
                String MaSuaChua = txtMaSuaChua.getText();
                Hashtable map = new Hashtable();
                JasperReport report = JasperCompileManager.compileReport("src/com/phones/ui/GiayHen.jrxml");

                map.put("MaSuaChua", MaSuaChua);
                final JasperPrint p = JasperFillManager.fillReport(report, map, DBConnection.conn);
                JasperViewer.viewReport(p, false);
                //JasperExportManager.exportReportToPdfFile(p, "test.pdf");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        } else {
            DialogHelper.alert(this, "Mã sửa chữa không được để trống");
            return;
        }
    }//GEN-LAST:event_jButton56ActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        loadKH();
        loadSC();
        loadLK();
        loadPN();
        loadNCC();
        loadNV();
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        loadKH();
        loadSC();
        loadLK();
        loadPN();
        loadNCC();
        loadNV();

        loadHM();
        loadTTM();
        loadHD();
        loadBH();
    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            if (checkTTSC(textField43.getText())) {
                insertHD();
                if (!textField43.getText().equals("")) {
                    try {
                        String MaSuaChua = textField43.getText();
                        Hashtable map = new Hashtable();
                        JasperReport report = JasperCompileManager.compileReport("src/com/phones/ui/InHoaDon.jrxml");

                        map.put("maSuaChua", MaSuaChua);
                        final JasperPrint p = JasperFillManager.fillReport(report, map, DBConnection.conn);
                        JasperViewer.viewReport(p, false);
                        //JasperExportManager.exportReportToPdfFile(p, "test.pdf");
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                } else {
                    DialogHelper.alert(this, "Mã sửa chữa không được để trống");
                    return;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        try {
            for (int i = 0; i < tblDichVu.getRowCount(); i++) {
                String MaLinhKien = (String) tblDichVu.getValueAt(i, 0);
                if (textField43.getText().equals(MaLinhKien)) {
                    textField43.setText(tblDichVu.getValueAt(i, 0).toString());
                    textField45.setText(tblDichVu.getValueAt(i, 1).toString());
                    textField46.setText(tblDichVu.getValueAt(i, 2).toString());
                    textField47.setText(tblDichVu.getValueAt(i, 3).toString());
                    textField49.setText(DateHelper.toString(DateHelper.now()));
                    textArea8.setText(tblDichVu.getValueAt(i, 4).toString());
                    break;
                }
            }
        } catch (Exception e) {
        }
        String keyword = textField43.getText();
        if (hddao.select1(keyword).size() != 0) {
            List<HoaDon> list = hddao.select1(keyword);
            DecimalFormat formatter = new DecimalFormat("###,###,###.##");
            textField48.setText(formatter.format(list.get(0).getTongTien()));
        } else {
            DialogHelper.alert(this, "sửa chữa chưa có linh kiện nào");
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.index = tblHoaDon.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                this.editHD();
                tabHoaDon.setSelectedIndex(1);

            }
        }
    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void jButton14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton14MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14MouseClicked

    private void tableDark1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDark1MouseClicked
        if (txtMaSuaChua.getText().equals("")) {
            DialogHelper.alert(this, "Vui lòng chọn mã sửa chữa");
        } else {
            if (evt.getClickCount() == 1) {
                this.index = tableDark1.rowAtPoint(evt.getPoint());
                if (this.index >= 0) {
                    if (tableDark1.getValueAt(index, 0).equals(tblKhoHang.getValueAt(index, 0))) {

                        int SoLuong = (int) tableDark1.getValueAt(index, 2);
                        System.out.println(SoLuong);
                        try {

                            if (SoLuong <= 0) {
                                DialogHelper.alert(this, "Linh kiện hiện đã hết");
                            } else if (SoLuong > 0) {

                                insertSC2(tblKhoHang.getValueAt(index, 0).toString());
                                DefaultTableModel model1 = (DefaultTableModel) tableDark3.getModel();
                                model1.setRowCount(0);
                                List<SuaChua> list = scdao.select1(txtMaSuaChua.getText());
                                List<LinhKien> list1 = lkdao.select();
                                for (SuaChua SC : list) {
                                    Object[] row = {
                                        SC.getMaLinhKien(),
                                        SC.getTenLinhKien()};
                                    model1.addRow(row);
                                }
                            }
                            Tong2();
                        } catch (Exception e) {
                            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
                        }
                        //this.editSC();
                        this.loadSC2();
                    }

                }
            }
        }
    }//GEN-LAST:event_tableDark1MouseClicked

    private void tableDark1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDark1MouseEntered

    }//GEN-LAST:event_tableDark1MouseEntered

    private void tableDark3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDark3MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {
            this.index = tableDark3.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                for (int i = 0; i <= tblKhoHang.getRowCount(); i++) {
                    if (tableDark3.getValueAt(index, 0).equals(tblKhoHang.getValueAt(i, 0))) {
                        insertSC3(tblKhoHang.getValueAt(i, 0).toString());
                        break;
                    }
                }
                DefaultTableModel model1 = (DefaultTableModel) tableDark3.getModel();
                model1.setRowCount(0);
                try {

                    List<SuaChua> list = scdao.select1(txtMaSuaChua.getText());
                    int j = 1;
                    for (SuaChua SC : list) {
                        Object[] row = {
                            SC.getMaLinhKien(),
                            SC.getTenLinhKien()};
                        model1.addRow(row);
                    }
                    Tong2();
                } catch (Exception e) {
                    DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
                }
                //this.editSC();
                this.loadSC2();

            }
        }
    }//GEN-LAST:event_tableDark3MouseClicked

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        String tenLinhKien = JOptionPane.showInputDialog("Mời nhập tên linh kiện");
        if (tenLinhKien != null) {
            int xacNhan = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thêm mới linh kiện này", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (xacNhan == JOptionPane.YES_OPTION) {
                insertLKMoi(tenLinhKien);
            }
        }

    }//GEN-LAST:event_jButton15ActionPerformed

    private void tableDark2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDark2MouseClicked
        // TODO add your handling code here:
        loadLK();
        loadPN();
        if (txtMaPhieuNhap.getText().equals("")) {
            DialogHelper.alert(this, "Vui lòng chọn mã Phiếu nhập");
        } else {
            if (evt.getClickCount() == 1) {
                this.index = tableDark2.rowAtPoint(evt.getPoint());
                if (this.index >= 0) {
                    System.out.println(tableDark2.getValueAt(index, 0));
                    if (tableDark2.getValueAt(index, 0).equals(tblKhoHang.getValueAt(index, 0))) {
                        String SoLuong1 = JOptionPane.showInputDialog("Mời nhập số lượng");
                        String SoLuong2 = JOptionPane.showInputDialog("Mời nhập giá tiền");
                        if (SoLuong1 != null && SoLuong2 != null) {
                            int SoLuong = Integer.parseInt(SoLuong1);
                            try {
                                insertLKPN(tblKhoHang.getValueAt(index, 0).toString(), tblKhoHang.getValueAt(index, 1).toString(), SoLuong, SoLuong2);
                                DefaultTableModel model1 = (DefaultTableModel) tableDark4.getModel();
                                model1.setRowCount(0);
                                List<PhieuNhap> list = pndao.select1(txtMaPhieuNhap.getText());
                                List<LinhKien> list1 = lkdao.select();
                                for (PhieuNhap pn : list) {
                                    Object[] row = {
                                        pn.getMaLinhKien(),
                                        pn.getTenLinhKien(),
                                        pn.getSoLuong(),
                                        pn.getGiaThanh()};
                                    model1.addRow(row);
                                }

                            } catch (Exception e) {
                                DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
                            }
                        } else {
                            DialogHelper.alert(this, "Lỗi chưa nhập");
                        }
                        //this.editSC();
                        this.loadPN();
                        loadLK();
                    }

                }
            }
        }
    }//GEN-LAST:event_tableDark2MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        tabKhachHang.setSelectedIndex(1);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        tabKhachHang.setSelectedIndex(0);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        tabLinhKien.setSelectedIndex(1);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        tabLinhKien.setSelectedIndex(0);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        tabPhieuNhap.setSelectedIndex(1);

    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        tabPhieuNhap.setSelectedIndex(0);

    }//GEN-LAST:event_jButton19ActionPerformed

    private void txtMaHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaHangActionPerformed

    private void tblDichVuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDichVuMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDichVuMouseEntered

    private void textField46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textField46ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textField46ActionPerformed

    public static void main(String args[]) {
        try {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new LoginFrom().setVisible(true);
                }
            });
        } catch (Exception e) {
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoiNCC;
    private javax.swing.JButton btnLamMoiNH;
    private javax.swing.JButton btnLamMoiNV;
    private javax.swing.JButton btnSuaNCC;
    private javax.swing.JButton btnSuaNH;
    private javax.swing.JButton btnSuaNV;
    private javax.swing.JButton btnThemNCC;
    private javax.swing.JButton btnThemNH;
    private javax.swing.JButton btnThemNV;
    private javax.swing.JButton btnXoaNCC;
    private javax.swing.JButton btnXoaNH;
    private javax.swing.JButton btnXoaNV;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.phones.jar.ComboBoxSuggestion cboChucVu;
    private com.phones.jar.ComboBoxSuggestion cboHang1;
    private com.phones.jar.ComboBoxSuggestion cboHangMay;
    private com.phones.jar.ComboBoxSuggestion cboKyThuatVien;
    private com.phones.jar.ComboBoxSuggestion cboMay;
    private com.phones.jar.ComboBoxSuggestion cboNhaCungCap;
    private com.phones.jar.ComboBoxSuggestion cboTenKhach;
    private com.phones.jar.ComboBoxSuggestion comboBoxSuggestion2;
    private com.raven.datechooser.DateChooser date1;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton53;
    private javax.swing.JButton jButton56;
    private javax.swing.JButton jButton58;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton63;
    private javax.swing.JButton jButton64;
    private javax.swing.JButton jButton65;
    private javax.swing.JButton jButton66;
    private javax.swing.JButton jButton74;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPanel jpnBaoHanh;
    private javax.swing.JPanel jpnCapNhatKhachHang;
    private javax.swing.JPanel jpnCapNhatKhoHang;
    private javax.swing.JPanel jpnCapNhatSuaChua;
    private javax.swing.JPanel jpnDanhSachKhachHang;
    private javax.swing.JPanel jpnDanhSachKhoHang;
    private javax.swing.JPanel jpnDanhSachPhieuNhap;
    private javax.swing.JPanel jpnDanhSachSuaChua;
    private javax.swing.JPanel jpnMain;
    private javax.swing.JPanel jpnNhaCungCap;
    private javax.swing.JPanel jpnNhangHang;
    private javax.swing.JPanel jpnThongTinMay;
    private javax.swing.JPanel jpnThongTinSuaChua;
    private javax.swing.JLabel lblBaoHanh;
    private javax.swing.JLabel lblDichVu;
    private javax.swing.JLabel lblDoanhThu;
    private javax.swing.JLabel lblHinhAnh;
    private javax.swing.JLabel lblHoaDon;
    private javax.swing.JLabel lblKhachHang;
    private javax.swing.JLabel lblKhoHang;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblNhangHang;
    private javax.swing.JLabel lblThongTinBanHang;
    private javax.swing.JLabel lblThongTinMay;
    private javax.swing.JLabel lblThongTinSua;
    private javax.swing.JLabel lblTong;
    private javax.swing.JLabel lblTrangThai;
    private javax.swing.JLabel lnlThoiGian;
    private com.phones.jar.PanelRound panelRound4;
    private com.phones.jar.PanelRound panelRound6;
    private javax.swing.JPanel pnlMenu;
    private com.phones.jar.MaterialTabbed tabDoanhThu;
    private com.phones.jar.MaterialTabbed tabHoaDon;
    private com.phones.jar.MaterialTabbed tabKhachHang;
    private com.phones.jar.MaterialTabbed tabLinhKien;
    private com.phones.jar.MaterialTabbed tabNhanVien;
    private com.phones.jar.MaterialTabbed tabPhieuNhap;
    private com.phones.jar.MaterialTabbed tabSuaChua;
    private com.phones.jar.TableDark tableDark1;
    private com.phones.jar.TableDark tableDark2;
    private com.phones.jar.TableDark tableDark3;
    private com.phones.jar.TableDark tableDark4;
    private com.phones.jar.TableDark tblBaoHanh;
    private com.phones.jar.TableDark tblDichVu;
    private com.phones.jar.TableDark tblDoanhThu;
    private com.phones.jar.TableDark tblDoanhThuLuong;
    private com.phones.jar.TableDark tblHoaDon;
    private com.phones.jar.TableDark tblKhachHang;
    private com.phones.jar.TableDark tblKhoHang;
    private com.phones.jar.TableDark tblNhaCungCap;
    private com.phones.jar.TableDark tblPhieuNhap;
    private com.phones.jar.TableDark tblThongTinBaoHanh1;
    private com.phones.jar.TableDark tblThongTinBaoHanh2;
    private com.phones.jar.TableDark tblThongTinPhieuNhap;
    private com.phones.jar.TextArea textArea8;
    private com.phones.jar.TextAreaScroll textAreaScroll1;
    private com.phones.jar.TextAreaScroll textAreaScroll2;
    private com.phones.jar.TextAreaScroll textAreaScroll3;
    private com.phones.jar.TextAreaScroll textAreaScroll4;
    private com.phones.jar.TextAreaScroll textAreaScroll5;
    private com.phones.jar.TextAreaScroll textAreaScroll6;
    private com.phones.jar.TextAreaScroll textAreaScroll7;
    private com.phones.jar.TextAreaScroll textAreaScroll8;
    private com.phones.jar.TextField textField43;
    private com.phones.jar.TextField textField44;
    private com.phones.jar.TextField textField45;
    private com.phones.jar.TextField textField46;
    private com.phones.jar.TextField textField47;
    private com.phones.jar.TextField textField48;
    private com.phones.jar.TextField textField49;
    private com.phones.jar.TextField txtChip;
    private javax.swing.JTextArea txtDiaChi;
    private com.phones.jar.TextArea txtDiaChiNCC;
    private com.phones.jar.TextArea txtDiaChiNV;
    private com.phones.jar.TextField txtEmail;
    private com.phones.jar.TextField txtEmailNCC;
    private com.phones.jar.TextField txtEmailNV;
    private com.phones.jar.TextField txtGiaBan;
    private com.phones.jar.TextField txtHoTenNV;
    private javax.swing.JTextField txtKH;
    private javax.swing.JTextField txtKyThuatVien;
    private com.phones.jar.TextField txtLuongNV;
    private javax.swing.JTextField txtMNCC;
    private javax.swing.JTextField txtMNCC1;
    private com.phones.jar.TextField txtMaHang;
    private com.phones.jar.TextField txtMaKhachHang;
    private javax.swing.JTextField txtMaLK;
    private com.phones.jar.TextField txtMaLinhKien;
    private com.phones.jar.TextField txtMaMay;
    private javax.swing.JTextField txtMaMay1;
    private com.phones.jar.TextField txtMaNhaCungCap;
    private com.phones.jar.TextField txtMaNhanVien;
    private com.phones.jar.TextField txtMaPhieuNhap;
    private com.phones.jar.TextField txtMaSuaChua;
    private com.phones.jar.TextField txtMatKhau;
    private com.phones.jar.TextField txtMatkhauNV;
    private com.phones.jar.TextArea txtMoTa;
    private com.phones.jar.TextArea txtMoTaLoi;
    private com.phones.jar.TextArea txtMoTaNhap;
    private com.phones.jar.TextArea txtMota;
    private com.phones.jar.TextField txtNgayNhanMay;
    private com.phones.jar.TextField txtNgayNhap;
    private com.phones.jar.TextField txtNgayTraDuKien;
    private com.phones.jar.TextField txtNgayTraMayThucTe;
    private com.phones.jar.TextField txtNhanVienNhanMay;
    private com.phones.jar.TextField txtNhanVienNhanMay1;
    private com.phones.jar.TextField txtPhiSuaChua;
    private com.phones.jar.TextField txtRam;
    private com.phones.jar.TextField txtRam1;
    private com.phones.jar.TextField txtRom;
    private com.phones.jar.TextField txtSoDienThoai;
    private com.phones.jar.TextField txtSoDienThoaiNCC;
    private com.phones.jar.TextField txtSoDienThoaiNV;
    private com.phones.jar.TextField txtSoLuong;
    private com.phones.jar.TextField txtTaiKhoanNV;
    private com.phones.jar.TextField txtTenHang;
    private com.phones.jar.TextField txtTenKhachHang;
    private com.phones.jar.TextField txtTenLinhKien;
    private com.phones.jar.TextField txtTenMay;
    private com.phones.jar.TextField txtTenNhaCungCap;
    private com.phones.jar.TextField txtTimKiem;
    private com.phones.jar.TextArea txtTinhTrangMay;
    private javax.swing.JLabel txtTongDoanhThu;
    // End of variables declaration//GEN-END:variables
}
