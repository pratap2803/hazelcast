package com.hazelcast.Controller;

import com.hazelcast.Entity.Student;
import com.hazelcast.Service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@CacheConfig(cacheNames = "students")
@Slf4j
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    public Student getStudentById(@PathVariable Long id) {
        log.info("fetching the student with id " + id + " from DB");
        return studentService.getStudentById(id).orElse(null);
    }

    @PostMapping
    public Student saveStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @PutMapping("/{id}")
    @CachePut(key = "#id")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        return studentService.saveStudent(student);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(key = "#id")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}