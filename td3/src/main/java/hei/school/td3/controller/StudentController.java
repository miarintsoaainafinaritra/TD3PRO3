package hei.school.td3.controller;

import hei.school.td3.entity.Student;
import hei.school.td3.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("/welcome")
    public ResponseEntity<String> welcome(@RequestParam(required = false) String name) {
        try {
            if (name == null || name.isBlank()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Le paramètre 'name' est requis");
            }
            String message = studentService.welcome(name);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type", "text/plain")
                    .body(message);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }


    @PostMapping("/students")
    public ResponseEntity<List<Student>> addStudents(@RequestBody List<Student> students) {
        try {
            List<Student> allStudents = studentService.addStudents(students);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(allStudents);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GetMapping("/students")
    public ResponseEntity<?> getStudents(@RequestHeader(value = "Accept", required = false) String acceptHeader) {
        try {
            if (acceptHeader == null || acceptHeader.isBlank()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("L'en-tête 'Accept' est requis");
            }

            if (acceptHeader.contains("application/json") || acceptHeader.contains("*/*")) {
                List<Student> students = studentService.getAllStudents();
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(students);
            }
            else if (acceptHeader.contains("text/plain")) {
                String studentNames = studentService.getAllStudentNames();
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header("Content-Type", "text/plain")
                        .body(studentNames);
            }
            else {
                return ResponseEntity
                        .status(HttpStatus.NOT_IMPLEMENTED)
                        .header("Content-Type", "text/plain")
                        .body("Format non supporté. Les formats acceptés sont : text/plain, application/json");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }
}