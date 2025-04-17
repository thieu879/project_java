package ra.edu.business.model;

import java.time.LocalDate;

public class Student {
    private int id;
    private String name;
    private LocalDate dob;
    private String email;
    private boolean sex;
    private String phone;
    private LocalDate createAt;
    private String status;

    public Student() {}

    public Student(int id, String name, LocalDate dob, String email, boolean sex, String phone, LocalDate createAt, String status) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.sex = sex;
        this.phone = phone;
        this.createAt = createAt;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isSex() { return sex; }
    public void setSex(boolean sex) { this.sex = sex; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDate getCreateAt() { return createAt; }
    public void setCreateAt(LocalDate createAt) { this.createAt = createAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}