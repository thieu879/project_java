package ra.edu.business.dao.enrollment;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Course;
import ra.edu.business.model.Student;
import ra.edu.exception.DatabaseException;
import ra.edu.utils.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAOImp implements IEnrollmentDAO {
    @Override
    public int addStudentToCourse(int studentId, int courseId) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL add_student_to_course(?,?,?)}");
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return stmt.getInt(3);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi thêm học viên vào khóa học: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public int removeStudentFromCourse(int studentId, int courseId) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL remove_student_from_course(?,?,?)}");
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return stmt.getInt(3);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi xóa học viên khỏi khóa học: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Student> displayStudentsByCourse(int courseId, int page, int pageSize, int[] totalPages) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Student> students = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL display_students_by_course(?,?,?,?,?)}");
            stmt.setInt(1, courseId);
            stmt.setInt(2, page);
            stmt.setInt(3, pageSize);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.registerOutParameter(5, Types.INTEGER);
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
            totalPages[0] = stmt.getInt(4);
            conn.commit();
            return students;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi hiển thị học viên theo khóa học: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public int registerCourse(int studentId, int courseId) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL register_course(?,?,?)}");
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return stmt.getInt(3);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi đăng ký khóa học: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Pair<Course, String>> viewRegisteredCourses(int studentId, int page, int pageSize, int[] totalPages) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Pair<Course, String>> courses = new ArrayList<>();

        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL view_registered_courses(?,?,?,?,?)}");
            stmt.setInt(1, studentId);
            stmt.setInt(2, page);
            stmt.setInt(3, pageSize);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.registerOutParameter(5, Types.INTEGER);

            boolean hasResults = stmt.execute();
            if (hasResults) {
                rs = stmt.getResultSet();
                while (rs.next()) {
                    Course course = new Course();
                    course.setId(rs.getInt("id"));
                    course.setName(rs.getString("name"));
                    course.setDuration(rs.getInt("duration"));
                    course.setInstructor(rs.getString("instructor"));
                    course.setCreateAt(rs.getDate("create_at").toLocalDate());
                    course.setStatus(rs.getString("status"));
                    String enrollmentStatus = rs.getString("enrollment_status");
                    courses.add(Pair.of(course, enrollmentStatus));
                }
            }

            totalPages[0] = stmt.getInt(4);
            int returnCode = stmt.getInt(5);

            if (returnCode == 0) {
                System.out.println("Không có khóa học nào đã đăng ký.");
            }

            conn.commit();
            return courses;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi xem khóa học đã đăng ký: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public int cancelRegistration(int studentId, int courseId) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL cancel_registration(?,?,?)}");
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return stmt.getInt(3);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi hủy đăng ký: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public int[] countCoursesAndStudents() throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL count_courses_and_students(?,?)}");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return new int[]{stmt.getInt(1), stmt.getInt(2)};
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi thống kê: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public int countStudentsByCourse(int courseId) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL count_students_by_course(?,?)}");
            stmt.setInt(1, courseId);
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
            throw new DatabaseException("Lỗi khi thống kê học viên theo khóa học: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Object[]> top5CoursesByStudents() throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Object[]> result = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL top_5_courses_by_students()}");
            rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new Object[]{rs.getString("name"), rs.getInt("student_count")});
            }
            conn.commit();
            return result;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi lấy top 5 khóa học: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Object[]> coursesWithMoreThan10Students() throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Object[]> result = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL courses_with_more_than_10_students()}");
            rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new Object[]{rs.getString("name"), rs.getInt("student_count")});
            }
            conn.commit();
            return result;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi lấy khóa học có trên 10 học viên: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Student> displayWaitingStudentsByCourse(int courseId, int page, int pageSize, int[] totalPages) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Student> students = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            stmt = conn.prepareCall("{CALL view_waiting_students(?,?,?,?,?)}");
            stmt.setInt(1, courseId);
            stmt.setInt(2, page);
            stmt.setInt(3, pageSize);
            stmt.registerOutParameter(4, java.sql.Types.INTEGER);
            stmt.registerOutParameter(5, java.sql.Types.INTEGER);
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

            totalPages[0] = stmt.getInt(4);
            int returnCode = stmt.getInt(5);
            if (returnCode == 0 && students.isEmpty()) {
                throw new DatabaseException("Không có sinh viên nào đang chờ duyệt cho khóa học này.");
            }
            return students;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi hiển thị sinh viên đang chờ duyệt: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public void approveStudentEnrollment(int courseId, int studentId, String status) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL approve_student_registration(?,?,?,?)}");
            stmt.setInt(2, courseId);
            stmt.setInt(1, studentId);
            stmt.setString(3, status);
            stmt.registerOutParameter(4, java.sql.Types.INTEGER);
            stmt.execute();
            if (stmt.getInt(4) <= 0) {
                throw new DatabaseException("Duyệt sinh viên thất bại.");
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi duyệt sinh viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }
}
