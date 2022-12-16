package dao;

import entities.FacultyAndCourse;
import entities.Student;

import java.util.*;
import java.util.stream.Collectors;

public class StudentsDAO {
    private List<Student> students;

    public StudentsDAO() {
        students = new ArrayList<>(Arrays.asList(new Student(0, "A", "A", "A", new Date(2003, Calendar.APRIL, 17), "A", "+0663333334", "F1", 3, "A"),
                new Student(1, "B", "B", "B", new Date(2004, Calendar.APRIL, 17), "B", "+0957777777", "F2", 2, "B"),
                new Student(2, "C", "C", "C", new Date(2003, Calendar.MARCH, 19), "C", "+0957777777", "F1", 3, "A"),
                new Student(3, "D", "D", "D", new Date(2005, Calendar.JANUARY, 12 ), "D", "+0957777777", "F2", 1, "D")));
    }

    public List<Student> studentsByFaculty(String faculty) {
        return students.stream().filter(student -> Objects.equals(student.getFaculty(), faculty)).collect(Collectors.toList());
    }

    public Map<FacultyAndCourse, List<Student>> studentsByFacultiesAndCourses() {
        Map<FacultyAndCourse, List<Student>> map = new HashMap<>();
        List<String> faculties = students.stream().map(Student::getFaculty).distinct().toList();
        for (String faculty : faculties) {
            List<Integer> courses = students.stream().filter(student -> Objects.equals(student.getFaculty(), faculty)).map(Student::getCourse).distinct().toList();
            for (Integer course : courses) {
                map.put(new FacultyAndCourse(faculty, course), students.stream().filter(student -> Objects.equals(student.getFaculty(), faculty) && student.getCourse() == course).collect(Collectors.toList()));
            }
        }
        return map;
    }

    public List<Student> studentsBornAfterYear(int year) {
        return students.stream().filter(student -> student.getBirthDate().getYear() > year).collect(Collectors.toList());
    }

    public List<Student> studentsGroupList(String group) {
        return students.stream().filter(student -> Objects.equals(student.getGroup(), group)).sorted(Comparator.comparing(Student::getSurname)).collect(Collectors.toList());
    }
}
