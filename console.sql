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
    in p_email varchar(255),
    in p_password varchar(255),
    out p_result int,
    out p_role varchar(50)
)
begin
    declare v_account_id int default null;
    declare v_role varchar(50);
    declare v_status varchar(50);

    select id, role, status
    into v_account_id, v_role, v_status
    from account
    where email = p_email and password = p_password
    limit 1;

    if v_account_id is not null then
        if v_status = 'BLOCKED' then
            set p_result = -1;
            set p_role = null;
        else
            if v_status = 'INACTIVE' then
                update account
                set status = 'ACTIVE'
                where id = v_account_id;
            end if;
            set p_result = v_account_id;
            set p_role = v_role;
        end if;
    else
        set p_result = 0;
        set p_role = null;
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
    out p_result int
)
begin
    -- cập nhật status của khóa học
    update course
    set status = 'INACTIVE'
    where id = p_id and status = 'ACTIVE';

    -- kiểm tra cập nhật thành công
    if row_count() > 0 then
        set p_result = 1;
    else
        set p_result = 0;
    end if;
end //
DELIMITER //

-- Tìm kiếm khóa học theo tên (hiển thị khoá học có status là ACTIVE)
DELIMITER //
create procedure search_course(
    in p_name varchar(255),
    in p_instructor varchar(255),
    in p_page int,
    in p_page_size int,
    out p_total_pages int
)
begin
    declare v_offset int;
    -- tính offset
    set v_offset = (p_page - 1) * p_page_size;

    -- tính tổng số bản ghi
    select count(*) into @total_records
    from course
    where status = 'ACTIVE'
      and (p_name is null or name like concat('%', p_name, '%'))
      and (p_instructor is null or instructor like concat('%', p_instructor, '%'));
    -- tính tổng số trang
    set p_total_pages = ceil(@total_records / p_page_size);
    -- lấy danh sách khóa học
    select *
    from course
    where status = 'ACTIVE'
      and (p_name is null or name like concat('%', p_name, '%'))
      and (p_instructor is null or instructor like concat('%', p_instructor, '%'))
    limit p_page_size offset v_offset;
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
        insert into Account (email, password, role, status)
        values (p_email, p_password, 'STUDENT', 'INACTIVE');
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
    in p_name varchar(255),
    in p_dob date,
    in p_email varchar(255),
    in p_sex boolean,
    in p_phone varchar(255),
    in p_password varchar(255),
    out p_result int
)
begin
    -- cập nhật thông tin học viên
    update student
    set name = p_name,
        dob = p_dob,
        email = p_email,
        sex = p_sex,
        phone = p_phone
    where id = p_id and status = 'ACTIVE';

    -- kiểm tra cập nhật thành công
    if row_count() > 0 then
        -- nếu có mật khẩu mới, cập nhật hoặc thêm vào bảng account
        if p_password is not null then
            if exists (select 1 from account where email = p_email) then
                update account
                set password = p_password
                where email = p_email and status = 'ACTIVE';
            else
                insert into account (email, password, role, status)
                values (p_email, p_password, 'STUDENT', 'ACTIVE');
            end if;
        end if;
        set p_result = 1;
    else
        set p_result = 0;
    end if;
end //
DELIMITER //

-- Xoá sinh viên (chuyển status thành INACTIVE)
DELIMITER //
create procedure delete_student(
    in p_id int,
    out p_result int
)
begin
    declare v_email varchar(255);

    -- lấy email của học viên
    select email into v_email
    from student
    where id = p_id and status = 'ACTIVE';

    -- cập nhật status của học viên
    update student
    set status = 'INACTIVE'
    where id = p_id and status = 'ACTIVE';

    -- kiểm tra cập nhật thành công
    if row_count() > 0 then
        -- cập nhật status của tài khoản liên quan
        update account
        set status = 'INACTIVE'
        where email = v_email and status = 'ACTIVE';
        set p_result = 1;
    else
        set p_result = 0;
    end if;
end //
DELIMITER //

-- Tìm kiếm học viên theo tên, email hoặc id (Tương đối, chỉ hiện những sinh viên có status là ACTIVE)
DELIMITER //
create procedure search_student(
    in p_name varchar(255),
    in p_email varchar(255),
    in p_id int,
    in p_page int,
    in p_page_size int,
    out p_total_pages int
)
begin
    declare v_offset int;

    -- tính offset
    set v_offset = (p_page - 1) * p_page_size;

    -- tính tổng số bản ghi
    select count(*) into @total_records
    from student
    where status = 'ACTIVE'
      and (p_name is null or name like concat('%', p_name, '%'))
      and (p_email is null or email like concat('%', p_email, '%'))
      and (p_id = 0 or id = p_id);

    -- tính tổng số trang
    set p_total_pages = ceil(@total_records / p_page_size);

    -- lấy danh sách học viên
    select *
    from student
    where status = 'ACTIVE'
      and (p_name is null or name like concat('%', p_name, '%'))
      and (p_email is null or email like concat('%', p_email, '%'))
      and (p_id = 0 or id = p_id)
    limit p_page_size offset v_offset;
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
    where e.course_id = p_course_id and s.status = 'ACTIVE' and e.status = 'CONFIRM';

    set total_pages = ceil(total_records / p_page_size);

    if total_records > 0 then
        select s.* from student s
                            join enrollment e on s.id = e.student_id
        where e.course_id = p_course_id and s.status = 'ACTIVE' and e.status = 'CONFIRM'
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
        update enrollment
        set status = 'DENIED' where student_id = p_student_id and course_id = p_course_id;
#         delete from enrollment where student_id = p_student_id and course_id = p_course_id;
        set return_code = 1; -- Xoá học viên khỏi khoá học thành công
    else
        set return_code = 0; -- Học viên không tồn tại trong khoá học
    end if;
end //
DELIMITER //

-- Hiển thị những sinh viên đăng ký khoá học có trạng thái trong bảng enrollment là WAITING
DELIMITER //
create procedure view_waiting_students(
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
    where e.course_id = p_course_id and e.status = 'WAITING';

    set total_pages = ceil(total_records / p_page_size);

    if total_records > 0 then
        select s.* from student s
                            join enrollment e on s.id = e.student_id
        where e.course_id = p_course_id and e.status = 'WAITING'
        limit p_page_size offset offset_val;

        set return_code = 1; -- hiển thị sinh viên đang chờ duyệt thành công
    else
        set return_code = 0; -- không có sinh viên nào đang chờ duyệt
    end if;
end //
DELIMITER //

-- Duyệt sinh viên đăng ký khoá học (chuyển status thành CONFIRM nếu đồng ý còn nếu không thì chuyển thành DENIED)
DELIMITER //
create procedure approve_student_registration(
    in p_student_id int,
    in p_course_id int,
    in p_status varchar(20),
    out return_code int
)
begin
    declare enrollment_count int;
    select count(*) into enrollment_count from enrollment where student_id = p_student_id and course_id = p_course_id and status = 'WAITING';
    if enrollment_count = 1 then
        update enrollment
        set status = p_status where student_id = p_student_id and course_id = p_course_id;
        set return_code = 1; -- Duyệt sinh viên thành công
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
    select count(*) into student_count from enrollment where course_id = p_course_id and status = 'CONFIRM';
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
    declare enrollment_status varchar(100);

    select count(*) into enrollment_count
    from enrollment
    where student_id = p_student_id and course_id = p_course_id;

    if enrollment_count = 0 then
        insert into enrollment (student_id, course_id, registered_at, status)
        values (p_student_id, p_course_id, now(), 'WAITING');
        set return_code = 1; -- đăng ký thành công

    else
        select status into enrollment_status
        from enrollment
        where student_id = p_student_id and course_id = p_course_id;

        if enrollment_status = 'CANCER' then
            update enrollment
            set status = 'WAITING'
            where student_id = p_student_id and course_id = p_course_id;
            set return_code = 1; -- cập nhật lại trạng thái thành waiting
        else
            set return_code = 0; -- học viên đã đăng ký rồi hoặc không được phép đăng ký
        end if;

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
        select c.*, e.status as enrollment_status from course c
                                                           join enrollment e on c.id = e.course_id
        where e.student_id = p_student_id and c.status = 'ACTIVE'
        limit p_page_size offset offset_val;

        set return_code = 1; -- hiển thị khóa học đã đăng ký thành công
    else
        set return_code = 0; -- không có khóa học nào đã đăng ký
    end if;
end //
DELIMITER //
SET @total_pages = 0;
SET @return_code = 0;
CALL view_registered_courses(2, 1, 1, @total_pages, @return_code);
SELECT @total_pages AS total_pages, @return_code AS return_code;

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
        update enrollment
        set status = 'CANCER' where student_id = p_student_id and course_id = p_course_id;
#         delete from enrollment where student_id = p_student_id and course_id = p_course_id;
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
    where email = p_email and status = 'ACTIVE';
end //
DELIMITER //

DELIMITER //
create procedure get_student_name_by_email(
    in p_email varchar(100),
    out p_student_name varchar(100)
)
begin

    select name into p_student_name
    from student
    where email = p_email and status = 'ACTIVE';
end //
DELIMITER //