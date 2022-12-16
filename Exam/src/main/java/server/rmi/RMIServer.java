package server.rmi;

import entities.FacultyAndCourse;
import entities.Student;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface RMIServer extends Remote {
    List<Student> studentsByFaculty(String faculty) throws RemoteException;
    Map<FacultyAndCourse, List<Student>> studentsByFacultiesAndCourses() throws RemoteException;
    List<Student> studentsBornAfterYear(int year) throws RemoteException;
    List<Student> studentsGroupList(String group) throws RemoteException;
}