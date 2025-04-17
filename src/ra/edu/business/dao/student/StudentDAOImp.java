package ra.edu.business.dao.student;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Student;
import ra.edu.exception.DatabaseException;
import ra.edu.exception.InvalidInputException;
import ra.edu.exception.NotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImp implements StudentDAO {
    @Override
    public Student login(String email, String password) throws Exception {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL log_in_student(?,?,?)}");
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.execute();
            int returnCode = stmt.getInt(3);
            if (returnCode == 1) {
                rs = stmt.getResultSet();
                if (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setEmail(email);
                    student.setPassword(password);
                    conn.commit();
                    return student;
                }
            }
            conn.rollback();
            return null;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new DatabaseException("Lỗi khi đăng nhập học viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public void addStudent(Student student) throws Exception {
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
            stmt.setString(6, student.getPassword());
            stmt.registerOutParameter(7, Types.INTEGER);
            stmt.execute();
            int returnCode = stmt.getInt(7);
            if (returnCode != 1) {
                conn.rollback();
                throw new DatabaseException("Không thể thêm học viên. Email đã tồn tại.");
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new DatabaseException("Lỗi khi thêm học viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public void updateStudent(Student student) throws Exception {
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
            stmt.setString(7, student.getPassword());
            stmt.registerOutParameter(8, Types.INTEGER);
            stmt.execute();
            int returnCode = stmt.getInt(8);
            if (returnCode != 1) {
                conn.rollback();
                throw new NotFoundException("Học viên không tồn tại.");
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new DatabaseException("Lỗi khi cập nhật học viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public void deleteStudent(int id) throws Exception {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL delete_student(?,?)}");
            stmt.setInt(1, id);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            int returnCode = stmt.getInt(2);
            if (returnCode != 1) {
                conn.rollback();
                throw new NotFoundException("Học viên không tồn tại.");
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new DatabaseException("Lỗi khi xóa học viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Student> getAllStudents() throws Exception {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Student> students = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL display_students(?)}");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.execute();
            int returnCode = stmt.getInt(1);
            if (returnCode == 1) {
                rs = stmt.getResultSet();
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setDob(rs.getDate("dob").toLocalDate());
                    student.setEmail(rs.getString("email"));
                    student.setSex(rs.getBoolean("sex"));
                    student.setPhone(rs.getString("phone"));
                    students.add(student);
                }
                conn.commit();
            } else {
                conn.rollback();
            }
            return students;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new DatabaseException("Lỗi khi lấy danh sách học viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Student> searchStudent(String name, String email, int id) throws Exception {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Student> students = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL search_student(?,?,?,?)}");
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, id);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.execute();
            int returnCode = stmt.getInt(4);
            if (returnCode == 1) {
                rs = stmt.getResultSet();
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setDob(rs.getDate("dob").toLocalDate());
                    student.setEmail(rs.getString("email"));
                    student.setSex(rs.getBoolean("sex"));
                    student.setPhone(rs.getString("phone"));
                    students.add(student);
                }
                conn.commit();
            } else {
                conn.rollback();
            }
            return students;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new DatabaseException("Lỗi khi tìm kiếm học viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Student> sortStudent(int sortBy) throws Exception {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Student> students = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL sort_student(?,?)}");
            stmt.setInt(1, sortBy);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            int returnCode = stmt.getInt(2);
            if (returnCode == 1) {
                rs = stmt.getResultSet();
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setDob(rs.getDate("dob").toLocalDate());
                    student.setEmail(rs.getString("email"));
                    student.setSex(rs.getBoolean("sex"));
                    student.setPhone(rs.getString("phone"));
                    students.add(student);
                }
                conn.commit();
            } else {
                conn.rollback();
            }
            return students;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new DatabaseException("Lỗi khi sắp xếp học viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Student> getStudentsByCourse(int courseId) throws Exception {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Student> students = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL display_students_by_course(?,?)}");
            stmt.setInt(1, courseId);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            int returnCode = stmt.getInt(2);
            if (returnCode == 1) {
                rs = stmt.getResultSet();
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setDob(rs.getDate("dob").toLocalDate());
                    student.setEmail(rs.getString("email"));
                    student.setSex(rs.getBoolean("sex"));
                    student.setPhone(rs.getString("phone"));
                    students.add(student);
                }
                conn.commit();
            } else {
                conn.rollback();
            }
            return students;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new DatabaseException("Lỗi khi lấy danh sách học viên theo khóa học: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public void changePassword(int studentId, String oldPassword, String newPassword) throws Exception {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL change_password(?,?,?,?)}");
            stmt.setInt(1, studentId);
            stmt.setString(2, oldPassword);
            stmt.setString(3, newPassword);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.execute();
            int returnCode = stmt.getInt(4);
            if (returnCode != 1) {
                conn.rollback();
                throw new InvalidInputException("Mật khẩu cũ không đúng.");
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new DatabaseException("Lỗi khi đổi mật khẩu: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }
}
