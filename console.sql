create database COURSE_AND_STUDENT_MANAGEMENT;
use COURSE_AND_STUDENT_MANAGEMENT;
create table Account(
    id int primary key auto_increment,
    email varchar(100) not null unique,
    password varchar(255) not null,
    role enum('ADMIN','STUDENT'),
    status enum('ACTIVE','INACTIVE', 'BLOCKED')
);
create table Student(
    id int primary key auto_increment,
    name varchar(100) not null,
    dob date not null,
    email varchar(100) not null unique,
    sex bit not null,
    phone varchar(20) null,
    create_at date default (now()),
    status enum('ACTIVE','INACTIVE') default 'ACTIVE'
);
create table Course(
    id int primary key auto_increment,
    name varchar(100) not null,
    duration int not null,
    instructor varchar(100) not null,
    create_at date default (now()),
    status enum('ACTIVE','INACTIVE') default 'ACTIVE'
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
insert into Account(email, password, role, status) values
('admin@example.com', 'admin123', 'ADMIN', 'ACTIVE'),
('student1@example.com', 'pass123', 'STUDENT', 'ACTIVE'),
('student2@example.com', 'pass123', 'STUDENT', 'INACTIVE'),
('student3@example.com', 'pass123', 'STUDENT', 'BLOCKED');

insert into Student(name, dob, email, sex, phone, status) values
('Nguyen Van A', '2000-05-12', 'student1@example.com', 1, '0909123456', 'ACTIVE'),
('Tran Thi B', '1999-08-23', 'student2@example.com', 0, '0912345678', 'ACTIVE'),
('Le Van C', '2001-01-15', 'student3@example.com', 1, '0987654321', 'INACTIVE');

insert into Course(name, duration, instructor) values
('Lập trình C cơ bản', 30, 'Thầy Hùng'),
('Cơ sở dữ liệu', 40, 'Cô Lan'),
('Thiết kế Web', 35, 'Thầy Minh'),
('Python cho người mới', 25, 'Cô Thảo');

insert into enrollment(student_id, course_id, status) values
(1, 1, 'CONFIRM'),
(1, 2, 'WAITING'),
(2, 2, 'CONFIRM'),
(2, 3, 'DENIED'),
(3, 4, 'CANCER');

-- Đăng ký tài khoản
DELIMITER //
create procedure register_account(
    in p_email varchar(100),
    in p_password varchar(255),
    in p_role varchar(10),
    out return_code int
)
begin
    declare account_count int;
    declare student_count int;
    select count(*) into student_count from Student where email = p_email;
    if student_count = 0 then
        select count(*) into account_count from Account where email = p_email;
        if account_count = 0 then
            insert into Account (email, password, role, status) values (p_email, p_password, p_role, 'ACTIVE');
            insert into Student(email) values (p_email);
            set return_code = 1; -- Đăng ký tài khoản thành công
        else
            set return_code = 0; -- Tài khoản đã tồn tại
        end if;
    else
        set return_code = -1; -- Email đã được sử dụng
    end if;
end //
DELIMITER //

-- Đăng nhập tài khoản
DELIMITER //
create procedure login_account(
    in p_email varchar(100),
    in p_password varchar(255),
    out return_code int,
    OUT user_role VARCHAR(10),
    OUT user_email VARCHAR(100)
)
begin
    declare account_count int;
    select count(*) into account_count from Account where email = p_email and password = p_password;
    if account_count = 1 then
        select role into user_role from Account where email = p_email;
        if user_role = 'ADMIN' then
            set return_code = 1; -- Đăng nhập thành công với quyền ADMIN
        else
            select email into user_email from Student where email = p_email;
            set return_code = 2; -- Đăng nhập thành công với quyền STUDENT
        end if;
    else
        set return_code = 0; -- Đăng nhập thất bại
    end if;
end //
DELIMITER //

-- Đăng xuất tài khoản (chuyển status thành INACTIVE)
DELIMITER //
create procedure logout_account(
    in p_email varchar(100),
    out return_code int
)
begin
    declare account_count int;
    select count(*) into account_count from Account where email = p_email;
    if account_count = 1 then
        update Account set status = 'INACTIVE' where email = p_email;
        set return_code = 1; -- Đăng xuất thành công
    else
        set return_code = 0; -- Tài khoản không tồn tại
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
        insert into Course (name, duration, instructor, create_at, status) values (p_name, p_duration, p_instructor, CURDATE(), 'ACTIVE');
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

-- Xoá khoá học (chuyển status thành INACTIVE)
DELIMITER //
create procedure delete_course(
    in p_id int,
    out return_code int
)
begin
    declare course_count int;
    select count(*) into course_count from Course where id = p_id;
    if course_count = 1 then
        update Course set status = 'INACTIVE' where id = p_id;
        set return_code = 1; -- Xoá khoá học thành công
    else
        set return_code = 0; -- Khoá học không tồn tại
    end if;
end //
DELIMITER //

-- Tìm kiếm khóa học theo tên (hiển thị khoá học có status là ACTIVE)
DELIMITER //
create procedure search_course(
    in p_name varchar(100),
    in p_page int,
    in p_page_size int,
    out total_pages int,
    out return_code int
)
begin
    declare total_records int;
    declare offset_val int;
    set offset_val = (p_page - 1) * p_page_size;

    select count(*) into total_records
    from course
    where name like concat('%', p_name, '%') and status = 'ACTIVE';

    set total_pages = ceil(total_records / p_page_size);

    if total_records > 0 then
        select * from course
        where name like concat('%', p_name, '%') and status = 'ACTIVE'
        limit p_page_size offset offset_val;
        set return_code = 1; -- tìm thấy khóa học
    else
        set return_code = 0; -- không tìm thấy khóa học
    end if;
end //
DELIMITER //

-- Sắp xếp theo tên khoá học hoặc id (Tăng/giảm dần, chỉ hiển thị khoá học có status là ACTIVE)
DELIMITER //
create procedure sort_course(
    in p_sort_by int,
    in p_is_asc bit,
    in p_page int,
    in p_page_size int,
    out total_pages int,
    out return_code int
)
begin
    declare total_records int;
    declare offset_val int;
    set offset_val = (p_page - 1) * p_page_size;

    select count(*) into total_records from course where status = 'ACTIVE';
    set total_pages = ceil(total_records / p_page_size);

    if p_sort_by = 1 then -- sắp xếp theo tên
        if p_is_asc = 1 then
            select * from course where status = 'ACTIVE'
            order by name asc
            limit p_page_size offset offset_val;
        else
            select * from course where status = 'ACTIVE'
            order by name desc
            limit p_page_size offset offset_val;
        end if;
        set return_code = 1; -- sắp xếp theo tên thành công
    elseif p_sort_by = 2 then -- sắp xếp theo id
        if p_is_asc = 1 then
            select * from course where status = 'ACTIVE'
            order by id asc
            limit p_page_size offset offset_val;
        else
            select * from course where status = 'ACTIVE'
            order by id desc
            limit p_page_size offset offset_val;
        end if;
        set return_code = 1; -- sắp xếp theo id thành công
    else
        set return_code = 0; -- không hợp lệ
    end if;
end //

DELIMITER //

-- Hiển thị danh sách khóa học (phân trang)
DELIMITER //
create procedure display_courses(
    in p_page int,
    in p_page_size int,
    out total_pages int,
    out return_code int
)
begin
    declare total_records int;
    declare offset_val int;
    set offset_val = (p_page - 1) * p_page_size;

    select count(*) into total_records from course where status = 'ACTIVE';
    set total_pages = ceil(total_records / p_page_size);

    if total_records > 0 then
        select * from course
        where status = 'ACTIVE'
        limit p_page_size offset offset_val;
        set return_code = 1; -- hiển thị danh sách khóa học thành công
    else
        set return_code = 0; -- không có khóa học nào
    end if;
end //

DELIMITER //

-- Hiển thị danh sách sinh viên (chỉ hiển thị sinh viên có status là ACTIVE)
DELIMITER //
create procedure display_students(
    in p_page int,
    in p_page_size int,
    out total_pages int,
    out return_code int
)
begin
    declare total_records int;
    declare offset_val int;
    set offset_val = (p_page - 1) * p_page_size;

    select count(*) into total_records from student where status = 'ACTIVE';
    set total_pages = ceil(total_records / p_page_size);

    if total_records > 0 then
        select * from student
            where status = 'ACTIVE'
            limit p_page_size offset offset_val;
        set return_code = 1; -- hiển thị danh sách sinh viên thành công
    else
        set return_code = 0; -- không có sinh viên nào
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
        insert into Student (name, dob, email, sex, phone, status)
            values (p_name, p_dob, p_email, p_sex, p_phone, 'ACTIVE');
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
        update Student set name = p_name , dob = p_dob, email = p_email, sex = p_sex, phone = p_phone where id = p_id;
        set return_code = 1; -- Cập nhật sinh viên thành công
    else
        set return_code = 0; -- Sinh viên không tồn tại
    end if;
end //
DELIMITER //

-- Xoá sinh viên (chuyển status thành INACTIVE)
DELIMITER //
create procedure delete_student(
    in p_id int,
    out return_code int
)
begin
    declare student_count int;
    select count(*) into student_count from Student where id = p_id;
    if student_count = 1 then
        update Student set status = 'INACTIVE' where id = p_id;
        set return_code = 1; -- Xoá sinh viên thành công
    else
        set return_code = 0; -- Sinh viên không tồn tại
    end if;
end //
DELIMITER //

-- Tìm kiếm học viên theo tên, email hoặc id (Tương đối, chỉ hiện những sinh viên có status là ACTIVE)
DELIMITER //
create procedure search_student(
    in p_name varchar(100),
    in p_email varchar(100),
    in p_id int,
    in p_page int,
    in p_page_size int,
    out total_pages int,
    out return_code int
)
begin
    declare total_records int;
    declare offset_val int;
    set offset_val = (p_page - 1) * p_page_size;

    select count(*) into total_records
    from student
    where status = 'ACTIVE'
      and (
        name like concat('%', p_name, '%')
            or email like concat('%', p_email, '%')
            or id = p_id or p_id = 0
        );

    set total_pages = ceil(total_records / p_page_size);

    if total_records > 0 then
        select * from student
        where status = 'ACTIVE'
          and (
            name like concat('%', p_name, '%')
                or email like concat('%', p_email, '%')
                or id = p_id or p_id = 0
            )
        limit p_page_size offset offset_val;
        set return_code = 1; -- tìm thấy sinh viên
    else
        set return_code = 0; -- không tìm thấy sinh viên
    end if;
end //

DELIMITER //

-- Sắp xếp theo tên hoặc id (Tăng hoặc giảm dần, chỉ hiện những sinh viên có status là ACTIVE)
DELIMITER //
create procedure sort_student(
    in p_sort_by int,
    in p_is_asc bit,
    in p_page int,
    in p_page_size int,
    out total_pages int,
    out return_code int
)
begin
    declare total_records int;
    declare offset_val int;
    set offset_val = (p_page - 1) * p_page_size;

    select count(*) into total_records from student where status = 'ACTIVE';
    set total_pages = ceil(total_records / p_page_size);

    if p_sort_by = 1 then -- sắp xếp theo tên
        if p_is_asc = 1 then
            select * from student where status = 'ACTIVE'
            order by name asc
            limit p_page_size offset offset_val;
    else
    select * from student where status = 'ACTIVE'
        order by name desc
        limit p_page_size offset offset_val;
    end if;
    set return_code = 1; -- sắp xếp theo tên thành công
    elseif p_sort_by = 2 then -- sắp xếp theo id
        if p_is_asc = 1 then
        select * from student where status = 'ACTIVE'
            order by id asc
            limit p_page_size offset offset_val;
        else
            select * from student where status = 'ACTIVE'
            order by id desc
            limit p_page_size offset offset_val;
        end if;
            set return_code = 1; -- sắp xếp theo id thành công
    else
        set return_code = 0; -- không hợp lệ
    end if;
end //

DELIMITER //

-- Hiển thị sinh viên theo từng khoá học (chỉ hiện những sinh viên và khoá học có status là ACTIVE)
DELIMITER //
create procedure display_students_by_course(
    in p_course_id int,
    in p_page int,
    in p_page_size int,
    out total_pages int,
    out return_code int
)
begin
    declare total_records int;
    declare offset_val int;
    set offset_val = (p_page - 1) * p_page_size;

    select count(*) into total_records
    from enrollment e
             join student s on e.student_id = s.id
    where e.course_id = p_course_id and s.status = 'ACTIVE';

    set total_pages = ceil(total_records / p_page_size);

    if total_records > 0 then
        select s.* from student s
                            join enrollment e on s.id = e.student_id
        where e.course_id = p_course_id and s.status = 'ACTIVE'
        limit p_page_size offset offset_val;

        set return_code = 1; -- hiển thị sinh viên theo khóa học thành công
    else
        set return_code = 0; -- không có sinh viên nào trong khóa học này
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
    in p_page int,
    in p_page_size int,
    out total_pages int,
    out return_code int
)
begin
    declare total_records int;
    declare offset_val int;
    set offset_val = (p_page - 1) * p_page_size;

    select count(*) into total_records
    from enrollment e
             join course c on e.course_id = c.id
    where e.student_id = p_student_id and c.status = 'ACTIVE';

    set total_pages = ceil(total_records / p_page_size);

    if total_records > 0 then
        select c.* from course c
                            join enrollment e on c.id = e.course_id
        where e.student_id = p_student_id and c.status = 'ACTIVE'
        limit p_page_size offset offset_val;

        set return_code = 1; -- hiển thị khóa học đã đăng ký thành công
    else
        set return_code = 0; -- không có khóa học nào đã đăng ký
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

-- Đổi mật khẩu sinh viên trong bản account
DELIMITER //
create procedure change_student_password(
    in p_email varchar(100),
    in p_old_password varchar(255),
    in p_new_password varchar(255),
    out return_code int
)
begin
    declare account_count int;
    select count(*) into account_count from Account where email = p_email and password = p_old_password;
    if account_count = 1 then
        update Account set password = p_new_password where email = p_email;
        set return_code = 1; -- Đổi mật khẩu thành công
    else
        set return_code = 0; -- Tài khoản không tồn tại hoặc mật khẩu cũ không đúng
    end if;
end //
DELIMITER //

-- lấy mã sinh viên bằng email
DELIMITER //
create procedure get_student_id_by_email(
    in p_email varchar(100),
    out p_student_id int
)
begin
    declare continue handler for not found set p_student_id = 0;

    select id into p_student_id
    from student
    where email = p_email and status = 'active';
end //
DELIMITER //
