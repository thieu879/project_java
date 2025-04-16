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
        set return_code = 1; -- Đăng nhập thành công
    else
        set return_code = 0; -- Đăng nhập thất bại
    end if;
end //
DELIMITER //

