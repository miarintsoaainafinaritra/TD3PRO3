package hei.school.td3.service;

import hei.school.td3.entity.Student;
import hei.school.td3.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public String welcome(String name) {
        return "Welcome " + name;
    }

    public List<Student> addStudents(List<Student> newStudents) {
        studentRepository.saveAll(newStudents);
        return newStudents;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public String getAllStudentNames() {
        return studentRepository.findAll().stream()
                .map(student -> student.getFirstName() + " " + student.getLastName())
                .collect(Collectors.joining(", "));
    }
}