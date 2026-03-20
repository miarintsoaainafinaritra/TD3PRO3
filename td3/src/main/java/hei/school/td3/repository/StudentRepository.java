package hei.school.td3.repository;

import hei.school.td3.entity.Student;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepository {
    private final List<Student> students = new ArrayList<>();

    public List<Student> findAll() {
        return new ArrayList<>(students);
    }

    public Optional<Student> findByReference(String reference) {
        return students.stream()
                .filter(student -> student.getReference().equals(reference))
                .findFirst();
    }

    public void save(Student student) {
        students.add(student);
    }

    public void saveAll(List<Student> newStudents) {
        students.addAll(newStudents);
    }

    public void deleteByReference(String reference) {
        students.removeIf(student -> student.getReference().equals(reference));
    }
}
