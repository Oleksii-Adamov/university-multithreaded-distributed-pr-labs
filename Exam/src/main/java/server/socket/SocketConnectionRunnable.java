package server.socket;

import dao.StudentsDAO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketConnectionRunnable implements Runnable {
    Socket socket;
    StudentsDAO studentsDAO;

    SocketConnectionRunnable(Socket socket, StudentsDAO studentsDAO) {
        this.socket = socket;
        this.studentsDAO = studentsDAO;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client connected, socket info: " + socket.toString());
            ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());
            Object objectFromClient = fromClient.readObject();
            while (objectFromClient != null) {
                String command = (String) objectFromClient;
                switch (command) {
                    case "studentsByFaculty" -> {
                        String faculty = (String) fromClient.readObject();
                        toClient.writeObject(studentsDAO.studentsByFaculty(faculty));
                    }
                    case "studentsByFacultiesAndCourses" ->
                            toClient.writeObject(studentsDAO.studentsByFacultiesAndCourses());
                    case "studentsBornAfterYear" -> {
                        int year = (Integer) fromClient.readObject();
                        toClient.writeObject(studentsDAO.studentsBornAfterYear(year));
                    }
                    case "studentsGroupList" -> {
                        String group = (String) fromClient.readObject();
                        toClient.writeObject(studentsDAO.studentsGroupList(group));
                    }
                }
                objectFromClient = fromClient.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

