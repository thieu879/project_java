create database COURSE_AND_STUDENT_MANAGEMENT;
use COURSE_AND_STUDENT_MANAGEMENT;
create table Admin(
    id int primary key auto_increment,
    username varchar(50) not null unique,
    password varchar(255) not null
);
create table Student(
    id int primary key auto_increment,
    name varchar(100) not null,
    dob date not null,
    email varchar(100) not null unique,
    sex bit not null,
    phone varchar(20) null,
    password varchar(255) not null,
    create_at date default (now())
);
create table Course(
    id int primary key auto_increment,
    name varchar(100) not null,
    duration int not null,
    instructor varchar(100) not null,
    create_at date default (now())
);
create table enrollment(
    id int primary key auto_increment,
    student_id int,
    foreign key (student_id) references Student(id),
    course_id int,
    foreign key (course_id) references Course(id),
    registered_at datetime default current_timestamp,
    status enum('WAITING','DENIED','CANCER','CONFIRM') default 'WAITING'
);
INSERT INTO Admin (username, password) VALUES
('admin1', '123456789'),
('admin2', '123456789');
INSERT INTO Student (name, dob, email, sex, phone, password, create_at) VALUES
('Nguyen Van A', '2000-01-15', 'a@example.com', 1, '0123456789', 'pass123a', CURDATE()),
('Tran Thi B', '2001-05-20', 'b@example.com', 0, '0987654321', 'pass123b', CURDATE()),
('Le Van C', '1999-09-09', 'c@example.com', 1, NULL, 'pass123c', CURDATE());
INSERT INTO Course (name, duration, instructor, create_at) VALUES
('Lập trình Web', 30, 'Thầy Nam', CURDATE()),
('Phân tích dữ liệu', 45, 'Cô Hoa', CURDATE()),
('Thiết kế UI/UX', 25, 'Thầy Hùng', CURDATE());
INSERT INTO Enrollment (student_id, course_id, registered_at, status) VALUES
(1, 1, NOW(), 'CONFIRM'),
(1, 2, NOW(), 'WAITING'),
(2, 2, NOW(), 'CANCER'),
(3, 3, NOW(), 'DENIED');

-- Đăng nhập với admin
DELIMITER //
create procedure log_in_admin(
    in p_username varchar(50),
    in p_password varchar(255),
    out return_code int
)
begin
    declare admin_count int;
    select count(*) into admin_count from Admin where username = p_username and password = p_password;
    if admin_count = 1 then
        select id from Admin where username = p_username and password = p_password;
        set return_code = 1; -- Đăng nhập thành công
    else
        set return_code = 0; -- Đăng nhập thất bại
    end if;
end //
DELIMITER //

-- Đăng nhâp với sinh viên
DELIMITER //
create procedure log_in_student(
    in p_email varchar(100),
    in p_password varchar(255),
    out return_code int
)
begin
    declare student_count int;
    select count(*) into student_count from Student where email = p_email and password = p_password;
    if student_count = 1 then
        select id from Student where email = p_email and password = p_password;
        set return_code = 1; -- Đăng nhập thành công
    else
        set return_code = 0; -- Đăng nhập thất bại
    end if;
end //
DELIMITER //

-- Thêm mới khoá học
DELIMITER //
create procedure add_course(
    in p_name varchar(100),
    in p_duration int,
    in p_instructor varchar(100),
    out return_code int
)
begin
    declare course_count int;
    select count(*) into course_count from Course where name = p_name;
    if course_count = 0 then
        insert into Course (name, duration, instructor, create_at) values (p_name, p_duration, p_instructor, CURDATE());
        set return_code = 1; -- Thêm khoá học thành công
    else
        set return_code = 0; -- Khoá học đã tồn tại
    end if;
end //
DELIMITER //

-- Chỉnh sửa thông tin khoá học (Hiển thị menu chọn thuộc tính cần sửa)
DELIMITER //
create procedure update_course(
    in p_id int,
    in p_name varchar(100),
    in p_duration int,
    in p_instructor varchar(100),
    out return_code int
)
begin
    declare course_count int;
    select count(*) into course_count from Course where id = p_id;
    if course_count = 1 then
        update Course set name = p_name, duration = p_duration, instructor = p_instructor where id = p_id;
        set return_code = 1; -- Cập nhật khoá học thành công
    else
        set return_code = 0; -- Khoá học không tồn tại
    end if;
end //
DELIMITER //

-- Xoá khoá học
DELIMITER //
create procedure delete_course(
    in p_id int,
    out return_code int
)
begin
    declare course_count int;
    select count(*) into course_count from Course where id = p_id;
    if course_count = 1 then
        delete from Course where id = p_id;
        set return_code = 1; -- Xoá khoá học thành công
    else
        set return_code = 0; -- Khoá học không tồn tại
    end if;
end //
DELIMITER //

-- Tìm kiếm khóa học theo tên
DELIMITER //
create procedure search_course_by_name(
    in p_name varchar(100),
    out return_code int
)
begin
    declare course_count int;
    select count(*) into course_count from Course where name like concat('%', p_name, '%');
    if course_count > 0 then
        set return_code = 1; -- Tìm thấy khoá học
    else
        set return_code = 0; -- Không tìm thấy khoá học
    end if;
end //
DELIMITER //

-- Sắp xếp theo tên hoặc id (Tăng/giảm dần)
DELIMITER //
create procedure sort_course(
    in p_sort_by int,
    out return_code int
)
begin
    declare course_count int;
    if p_sort_by = 1 then -- Sắp xếp theo tên
        select * from Course order by name;
        set return_code = 1; -- Sắp xếp theo tên thành công
    elseif p_sort_by = 2 then -- Sắp xếp theo id
        select * from Course order by id;
        set return_code = 1; -- Sắp xếp theo id thành công
    else
        set return_code = 0; -- Không hợp lệ
    end if;
end //
DELIMITER //

-- Hiển thị danh sách khoá học dưới dạng bảng
DELIMITER //
create procedure display_courses(
    out return_code int
)
begin
    declare course_count int;
    select count(*) into course_count from Course;
    if course_count > 0 then
        select * from Course;
        set return_code = 1; -- Hiển thị danh sách thành công
    else
        set return_code = 0; -- Không có khoá học nào
    end if;
end //
DELIMITER //

-- Hiển thị danh sách sinh viên
DELIMITER //
create procedure display_students(
    out return_code int
)
begin
    declare student_count int;
    select count(*) into student_count from Student;
    if student_count > 0 then
        select * from Student;
        set return_code = 1; -- Hiển thị danh sách thành công
    else
        set return_code = 0; -- Không có sinh viên nào
    end if;
end //
DELIMITER //

-- Thêm mới sinh viên
DELIMITER //
create procedure add_student(
    in p_name varchar(100),
    in p_dob date,
    in p_email varchar(100),
    in p_sex bit,
    in p_phone varchar(20),
    in p_password varchar(255),
    out return_code int
)
begin
    declare student_count int;
    select count(*) into student_count from Student where email = p_email;
    if student_count = 0 then
        insert into Student (name, dob, email, sex, phone, password)
            values (p_name, p_dob, p_email, p_sex, p_phone, p_password);
        set return_code = 1; -- Thêm sinh viên thành công
    elseIf student_count > 0 then
        set return_code = 0; -- Sinh viên đã tồn tại
    else
        set return_code = -1; -- Lỗi không xác định
    end if;
end //
DELIMITER //

-- Sửa thông tin sinh viên
DELIMITER //
create procedure update_student(
    in p_id int,
    in p_name varchar(100),
    in p_dob date,
    in p_email varchar(100),
    in p_sex bit,
    in p_phone varchar(20),
    in p_password varchar(255),
    out return_code int
)
begin
    declare student_count int;
    select count(*) into student_count from Student where id = p_id;
    if student_count = 1 then
        update Student set name = p_name , dob = p_dob, email = p_email, sex = p_sex, phone = p_phone, password = p_password where id = p_id;
        set return_code = 1; -- Cập nhật sinh viên thành công
    else
        set return_code = 0; -- Sinh viên không tồn tại
    end if;
end //
DELIMITER //

-- Xoá sinh viên
DELIMITER //
create procedure delete_student(
    in p_id int,
    out return_code int
)
begin
    declare student_count int;
    select count(*) into student_count from Student where id = p_id;
    if student_count = 1 then
        delete from Student where id = p_id;
        set return_code = 1; -- Xoá sinh viên thành công
    else
        set return_code = 0; -- Sinh viên không tồn tại
    end if;
end //
DELIMITER //

-- Tìm kiếm học viên theo tên, email hoặc id (Tương đối)
DELIMITER //
create procedure search_student(
    in p_name varchar(100),
    in p_email varchar(100),
    in p_id int,
    out return_code int
)
begin
    declare student_count int;
    select count(*) into student_count from Student where name like concat('%', p_name, '%') or email like concat('%', p_email, '%') or id = p_id;
    if student_count > 0 then
        set return_code = 1; -- Tìm thấy sinh viên
    else
        set return_code = 0; -- Không tìm thấy sinh viên
    end if;
end //
DELIMITER //

-- Sắp xếp theo tên hoặc id (Tăng hoặc giảm dần)
DELIMITER //
create procedure sort_student(
    in p_sort_by int,
    out return_code int
)
begin
    declare student_count int;
    if p_sort_by = 1 then -- Sắp xếp theo tên
        select * from Student order by name;
        set return_code = 1; -- Sắp xếp theo tên thành công
    elseif p_sort_by = 2 then -- Sắp xếp theo id
        select * from Student order by id;
        set return_code = 1; -- Sắp xếp theo id thành công
    else
        set return_code = 0; -- Không hợp lệ
    end if;
end //
DELIMITER //

-- Hiển thị sinh viên theo từng khoá học
DELIMITER //
create procedure display_students_by_course(
    in p_course_id int,
    out return_code int
)
begin
    declare student_count int;
    select count(*) into student_count from enrollment where course_id = p_course_id;
    if student_count > 0 then
        select s.* from Student s
        join enrollment e on s.id = e.student_id
        where e.course_id = p_course_id;
        set return_code = 1; -- Hiển thị sinh viên theo khoá học thành công
    else
        set return_code = 0; -- Không có sinh viên nào trong khoá học này
    end if;
end //
DELIMITER //

-- thêm học viên vào khoá học
DELIMITER //
create procedure add_student_to_course(
    in p_student_id int,
    in p_course_id int,
    out return_code int
)
begin
    declare enrollment_count int;
    select count(*) into enrollment_count from enrollment where student_id = p_student_id and course_id = p_course_id;
    if enrollment_count = 0 then
        insert into enrollment (student_id, course_id, registered_at, status) values (p_student_id, p_course_id, NOW(), 'WAITING');
        set return_code = 1; -- Thêm học viên vào khoá học thành công
    else
        set return_code = 0; -- Học viên đã tồn tại trong khoá học
    end if;
end //
DELIMITER //

-- Xoá học viên khỏi khoá học
DELIMITER //
create procedure remove_student_from_course(
    in p_student_id int,
    in p_course_id int,
    out return_code int
)
begin
    declare enrollment_count int;
    select count(*) into enrollment_count from enrollment where student_id = p_student_id and course_id = p_course_id;
    if enrollment_count = 1 then
        delete from enrollment where student_id = p_student_id and course_id = p_course_id;
        set return_code = 1; -- Xoá học viên khỏi khoá học thành công
    else
        set return_code = 0; -- Học viên không tồn tại trong khoá học
    end if;
end //
DELIMITER //

-- Thống k số lượng khoá học và sinh viên
DELIMITER //
create procedure count_courses_and_students(
    out course_count int,
    out student_count int
)
begin
    select count(*) into course_count from Course;
    select count(*) into student_count from Student;
end //
DELIMITER //

-- Thống kê học viên theo từng khoá học
DELIMITER //
create procedure count_students_by_course(
    in p_course_id int,
    out student_count int
)
begin
    select count(*) into student_count from enrollment where course_id = p_course_id;
end //
DELIMITER //

-- top 5 khoá học có nhiều sinh viên nhất
DELIMITER //
create procedure top_5_courses_by_students()
begin
    select c.name, count(e.student_id) as student_count
    from Course c
             left join enrollment e on c.id = e.course_id
    group by c.id
    order by student_count desc
    limit 5;
end //
DELIMITER //

-- Liệt kê khoá học có trên 10 học viên
DELIMITER //
create procedure courses_with_more_than_10_students()
begin
    select c.name, count(e.student_id) as student_count
    from Course c
             left join enrollment e on c.id = e.course_id
    group by c.id
    having student_count > 10;
end //
DELIMITER //

-- Đăng ký khoá học
DELIMITER //
create procedure register_course(
    in p_student_id int,
    in p_course_id int,
    out return_code int
)
begin
    declare enrollment_count int;
    select count(*) into enrollment_count from enrollment where student_id = p_student_id and course_id = p_course_id;
    if enrollment_count = 0 then
        insert into enrollment (student_id, course_id, registered_at, status) values (p_student_id, p_course_id, NOW(), 'WAITING');
        set return_code = 1; -- Đăng ký khoá học thành công
    else
        set return_code = 0; -- Học viên đã đăng ký khoá học này
    end if;
end //
DELIMITER //

-- Xem khoá học đã đăng ký
DELIMITER //
create procedure view_registered_courses(
    in p_student_id int,
    out return_code int
)
begin
    declare enrollment_count int;
    select count(*) into enrollment_count from enrollment where student_id = p_student_id;
    if enrollment_count > 0 then
        select c.* from Course c
        join enrollment e on c.id = e.course_id
        where e.student_id = p_student_id;
        set return_code = 1; -- Hiển thị khoá học đã đăng ký thành công
    else
        set return_code = 0; -- Không có khoá học nào đã đăng ký
    end if;
end //
DELIMITER //

-- Huỷ đăng ký khoá học (Nếu chưa bắt đầu)
DELIMITER //
create procedure cancel_registration(
    in p_student_id int,
    in p_course_id int,
    out return_code int
)
begin
    declare enrollment_count int;
    select count(*) into enrollment_count from enrollment where student_id = p_student_id and course_id = p_course_id;
    if enrollment_count = 1 then
        delete from enrollment where student_id = p_student_id and course_id = p_course_id;
        set return_code = 1; -- Huỷ đăng ký khoá học thành công
    else
        set return_code = 0; -- Không có khoá học nào đã đăng ký
    end if;
end //
DELIMITER //

-- Đổi mật khẩu sinh viên
DELIMITER //
create procedure change_password(
    in p_student_id int,
    in p_old_password varchar(255),
    in p_new_password varchar(255),
    out return_code int
)
begin
    declare student_count int;
    select count(*) into student_count from Student where id = p_student_id and password = p_old_password;
    if student_count = 1 then
        update Student set password = p_new_password where id = p_student_id;
        set return_code = 1; -- Đổi mật khẩu thành công
    else
        set return_code = 0; -- Mật khẩu cũ không đúng
    end if;
end //
DELIMITER //

