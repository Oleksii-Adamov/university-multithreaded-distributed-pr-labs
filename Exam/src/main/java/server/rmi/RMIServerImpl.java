package server.rmi;

import dao.StudentsDAO;
import entities.FacultyAndCourse;
import entities.Student;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class RMIServerImpl extends UnicastRemoteObject implements RMIServer {

    private StudentsDAO studentsDAO;

    protected RMIServerImpl() throws RemoteException {
        studentsDAO = new StudentsDAO();
    }

    @Override
    public List<Student> studentsByFaculty(String faculty) throws RemoteException {
        return studentsDAO.studentsByFaculty(faculty);
    }

    @Override
    public Map<FacultyAndCourse, List<Student>> studentsByFacultiesAndCourses() throws RemoteException {
        return studentsDAO.studentsByFacultiesAndCourses();
    }

    @Override
    public List<Student> studentsBornAfterYear(int year) throws RemoteException {
        return studentsDAO.studentsBornAfterYear(year);
    }

    @Override
    public List<Student> studentsGroupList(String group) throws RemoteException {
        return studentsDAO.studentsGroupList(group);
    }
}
