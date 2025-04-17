package ra.edu.business.dao.student;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Student;
import ra.edu.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImp implements IStudentDAO {
    @Override
    public int addStudent(Student student, String password) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL add_student(?,?,?,?,?,?,?)}");
            stmt.setString(1, student.getName());
            stmt.setDate(2, Date.valueOf(student.getDob()));
            stmt.setString(3, student.getEmail());
            stmt.setBoolean(4, student.isSex());
            stmt.setString(5, student.getPhone());
            stmt.setString(6, password);
            stmt.registerOutParameter(7, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return stmt.getInt(7);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi thêm học viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public int updateStudent(Student student, String password) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL update_student(?,?,?,?,?,?,?,?)}");
            stmt.setInt(1, student.getId());
            stmt.setString(2, student.getName());
            stmt.setDate(3, Date.valueOf(student.getDob()));
            stmt.setString(4, student.getEmail());
            stmt.setBoolean(5, student.isSex());
            stmt.setString(6, student.getPhone());
            stmt.setString(7, password);
            stmt.registerOutParameter(8, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return stmt.getInt(8);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi cập nhật học viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public int deleteStudent(int id) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL delete_student(?,?)}");
            stmt.setInt(1, id);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return stmt.getInt(2);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi xóa học viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Student> displayStudents(int page, int pageSize, int[] totalPages) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Student> students = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL display_students(?,?,?,?)}");
            stmt.setInt(1, page);
            stmt.setInt(2, pageSize);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.registerOutParameter(4, Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setDob(rs.getDate("dob").toLocalDate());
                student.setEmail(rs.getString("email"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setCreateAt(rs.getDate("create_at").toLocalDate());
                student.setStatus(rs.getString("status"));
                students.add(student);
            }
            totalPages[0] = stmt.getInt(3);
            conn.commit();
            return students;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi hiển thị học viên: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Student> searchStudent(String name, String email, int id, int page, int pageSize, int[] totalPages) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Student> students = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL search_student(?,?,?,?,?,?,?)}");
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, id);
            stmt.setInt(4, page);
            stmt.setInt(5, pageSize);
            stmt.registerOutParameter(6, Types.INTEGER);
            stmt.registerOutParameter(7, Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setDob(rs.getDate("dob").toLocalDate());
                student.setEmail(rs.getString("email"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setCreateAt(rs.getDate("create_at").toLocalDate());
                student.setStatus(rs.getString("status"));
                students.add(student);
            }
            totalPages[0] = stmt.getInt(6);
            conn.commit();
            return students;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi tìm kiếm học viên: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Student> sortStudent(int sortBy, boolean isAsc, int page, int pageSize, int[] totalPages) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Student> students = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL sort_student(?,?,?,?,?,?)}");
            stmt.setInt(1, sortBy);
            stmt.setBoolean(2, isAsc);
            stmt.setInt(3, page);
            stmt.setInt(4, pageSize);
            stmt.registerOutParameter(5, Types.INTEGER);
            stmt.registerOutParameter(6, Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setDob(rs.getDate("dob").toLocalDate());
                student.setEmail(rs.getString("email"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setCreateAt(rs.getDate("create_at").toLocalDate());
                student.setStatus(rs.getString("status"));
                students.add(student);
            }
            totalPages[0] = stmt.getInt(5);
            conn.commit();
            return students;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi sắp xếp học viên: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public int getStudentIdByEmail(String email) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL get_student_id_by_email(?,?)}");
            stmt.setString(1, email);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return stmt.getInt(2);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi lấy mã học viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }
}
