package hei.school.td3.controller;

import hei.school.td3.entity.Student;
import hei.school.td3.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void welcome_shouldReturn200_whenNameIsProvided() throws Exception {
        when(studentService.welcome("John")).thenReturn("Welcome John");

        mockMvc.perform(get("/welcome").param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/plain"))
                .andExpect(content().string("Welcome John"));
    }

    @Test
    void welcome_shouldReturn400_whenNameIsMissing() throws Exception {
        mockMvc.perform(get("/welcome"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addStudents_shouldReturn201_afterCreation() throws Exception {
        Student s1 = Student.builder().reference("REF1").firstName("John").lastName("Doe").age(20).build();
        List<Student> students = List.of(s1);
        when(studentService.addStudents(anyList())).thenReturn(students);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"reference\":\"REF1\", \"firstName\":\"John\", \"lastName\":\"Doe\", \"age\":20}]"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$[0].reference").value("REF1"));
    }

    @Test
    void addStudents_shouldReturn500_onException() throws Exception {
        when(studentService.addStudents(anyList())).thenThrow(new RuntimeException("Server error"));

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getStudents_shouldReturn400_whenAcceptHeaderIsMissing() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStudents_shouldReturn200_whenAcceptIsTextPlain() throws Exception {
        when(studentService.getAllStudentNames()).thenReturn("John Doe");

        mockMvc.perform(get("/students").header("Accept", "text/plain"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/plain"))
                .andExpect(content().string("John Doe"));
    }

    @Test
    void getStudents_shouldReturn200_whenAcceptIsApplicationJson() throws Exception {
        Student s1 = Student.builder().reference("REF1").firstName("John").lastName("Doe").age(20).build();
        when(studentService.getAllStudents()).thenReturn(List.of(s1));

        mockMvc.perform(get("/students").header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void getStudents_shouldReturn501_whenAcceptIsUnsupported() throws Exception {
        mockMvc.perform(get("/students").header("Accept", "application/xml"))
                .andExpect(status().isNotImplemented());
    }

    @Test
    void getStudents_shouldReturn500_onException() throws Exception {
        mockMvc.perform(get("/students").header("Accept", "text/plain"))
                .andExpect(status().isInternalServerError());
    }
}
