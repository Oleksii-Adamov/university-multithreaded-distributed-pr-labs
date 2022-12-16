package client.rmi;

import entities.FacultyAndCourse;
import entities.Student;
import server.rmi.RMIServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientRmiTask1 {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        RMIServer server = (RMIServer) Naming.lookup("//127.0.0.1/Task1");
        System.out.println("Connected to server");
        System.out.println("Students By Faculty F1:");
        System.out.println(Arrays.toString(server.studentsByFaculty("F1").toArray()));
        System.out.println("Students By Faculties And Courses:");
        Map<FacultyAndCourse, List<Student>> map = server.studentsByFacultiesAndCourses();
        System.out.println(map.keySet().stream().map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}")));
        System.out.println("Students Born After Year 2003:");
        System.out.println(Arrays.toString(server.studentsBornAfterYear(2003).toArray()));
        System.out.println("Students Group \"A\" List:");
        System.out.println(Arrays.toString(server.studentsGroupList("A").toArray()));
    }
}
