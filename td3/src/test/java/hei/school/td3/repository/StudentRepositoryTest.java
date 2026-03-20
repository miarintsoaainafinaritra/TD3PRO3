package hei.school.td3.repository;

import hei.school.td3.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryTest {

    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository = new StudentRepository();
    }

    @Test
    void shouldFindAll() {
        Student s1 = Student.builder().reference("REF1").firstName("John").lastName("Doe").age(20).build();
        Student s2 = Student.builder().reference("REF2").firstName("Jane").lastName("Doe").age(22).build();
        studentRepository.saveAll(List.of(s1, s2));

        List<Student> result = studentRepository.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void shouldFindByReference() {
        Student s1 = Student.builder().reference("REF1").firstName("John").lastName("Doe").age(20).build();
        studentRepository.save(s1);

        Optional<Student> result = studentRepository.findByReference("REF1");
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void shouldDeleteByReference() {
        Student s1 = Student.builder().reference("REF1").firstName("John").lastName("Doe").age(20).build();
        studentRepository.save(s1);
        studentRepository.deleteByReference("REF1");

        Optional<Student> result = studentRepository.findByReference("REF1");
        assertFalse(result.isPresent());
    }
}
