package ra.edu.business.model;

import java.time.LocalDate;

public class Course {
    private int id;
    private String name;
    private int duration;
    private String instructor;
    private LocalDate createAt;
    private String status;

    public Course() {}

    public Course(int id, String name, int duration, String instructor, LocalDate createAt, String status) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.instructor = instructor;
        this.createAt = createAt;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public LocalDate getCreateAt() { return createAt; }
    public void setCreateAt(LocalDate createAt) { this.createAt = createAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}