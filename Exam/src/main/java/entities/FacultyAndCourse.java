package entities;

import java.io.Serializable;

public class FacultyAndCourse implements Serializable {
    private String faculty;
    private int course;

    public FacultyAndCourse(String faculty, int course) {
        this.faculty = faculty;
        this.course = course;
    }

    public FacultyAndCourse() {

    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "FacultyAndCourse{" +
                "faculty='" + faculty + '\'' +
                ", course=" + course +
                '}';
    }
}