package client.socket;

import entities.FacultyAndCourse;
import entities.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientSocketTask1 {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            System.out.println("Connecting to server...");
            socket = new Socket("localhost", 12345);
            System.out.println("Connected");
            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
            List<Student> studentList;
            System.out.println("Students By Faculty F1:");
            toServer.writeObject("studentsByFaculty");
            toServer.writeObject("F1");
            studentList = (List<Student>) fromServer.readObject();
            System.out.println(Arrays.toString(studentList.toArray()));

            System.out.println("Students By Faculties And Courses:");
            toServer.writeObject("studentsByFacultiesAndCourses");
            Map<FacultyAndCourse, List<Student>> map = (Map<FacultyAndCourse, List<Student>>) fromServer.readObject();
            System.out.println(map.keySet().stream().map(key -> key + "=" + map.get(key))
                    .collect(Collectors.joining(", ", "{", "}")));

            System.out.println("Students Born After Year 2003:");
            toServer.writeObject("studentsBornAfterYear");
            toServer.writeObject(2003);
            studentList = (List<Student>) fromServer.readObject();
            System.out.println(Arrays.toString(studentList.toArray()));

            System.out.println("Students Group \"A\" List:");
            toServer.writeObject("studentsGroupList");
            toServer.writeObject("A");
            studentList = (List<Student>) fromServer.readObject();
            System.out.println(Arrays.toString(studentList.toArray()));

            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
