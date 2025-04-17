package ra.edu.business.dao.course;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Course;
import ra.edu.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImp implements ICourseDAO {
    @Override
    public int addCourse(Course course) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL add_course(?,?,?,?)}");
            stmt.setString(1, course.getName());
            stmt.setInt(2, course.getDuration());
            stmt.setString(3, course.getInstructor());
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return stmt.getInt(4);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi thêm khóa học: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public int updateCourse(Course course) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL update_course(?,?,?,?,?)}");
            stmt.setInt(1, course.getId());
            stmt.setString(2, course.getName());
            stmt.setInt(3, course.getDuration());
            stmt.setString(4, course.getInstructor());
            stmt.registerOutParameter(5, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return stmt.getInt(5);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi cập nhật khóa học: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public int deleteCourse(int id) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL delete_course(?,?)}");
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
            throw new DatabaseException("Lỗi khi xóa khóa học: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Course> searchCourse(String name, int page, int pageSize, int[] totalPages) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Course> courses = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL search_course(?,?,?,?,?)}");
            stmt.setString(1, name);
            stmt.setInt(2, page);
            stmt.setInt(3, pageSize);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.registerOutParameter(5, Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setName(rs.getString("name"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreateAt(rs.getDate("create_at").toLocalDate());
                course.setStatus(rs.getString("status"));
                courses.add(course);
            }
            totalPages[0] = stmt.getInt(4);
            conn.commit();
            return courses;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi tìm kiếm khóa học: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Course> sortCourse(int sortBy, boolean isAsc, int page, int pageSize, int[] totalPages) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Course> courses = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL sort_course(?,?,?,?,?,?)}");
            stmt.setInt(1, sortBy);
            stmt.setBoolean(2, isAsc);
            stmt.setInt(3, page);
            stmt.setInt(4, pageSize);
            stmt.registerOutParameter(5, Types.INTEGER);
            stmt.registerOutParameter(6, Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setName(rs.getString("name"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreateAt(rs.getDate("create_at").toLocalDate());
                course.setStatus(rs.getString("status"));
                courses.add(course);
            }
            totalPages[0] = stmt.getInt(5);
            conn.commit();
            return courses;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi sắp xếp khóa học: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public List<Course> displayCourses(int page, int pageSize, int[] totalPages) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Course> courses = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL display_courses(?,?,?,?)}");
            stmt.setInt(1, page);
            stmt.setInt(2, pageSize);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.registerOutParameter(4, Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setName(rs.getString("name"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreateAt(rs.getDate("create_at").toLocalDate());
                course.setStatus(rs.getString("status"));
                courses.add(course);
            }
            totalPages[0] = stmt.getInt(3);
            conn.commit();
            return courses;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi hiển thị khóa học: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            ConnectionDB.closeConnection(conn, stmt);
        }
    }
}
