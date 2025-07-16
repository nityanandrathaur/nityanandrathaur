package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

import ch.qos.logback.core.status.Status;

@Controller
public class StudentController {

	@Autowired
	StudentRepository studentRepo;

	@GetMapping("/home")
	public String getHello() {
		String message = "Hello, this is your message!";
		return "index.html";
	}

	@PostMapping("/saveStudent")
	public ResponseEntity<Map<String, Object>> saveStudentData(@RequestBody Student student) {
		studentRepo.save(student);
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Student Records saved successfully!");
		response.put("student", student); // Add the saved student to the response
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/studentList")
	public ResponseEntity<List<Student>> getStudent() {
		List<Student> students = studentRepo.findAll();
		return ResponseEntity.ok(students);
	}

	@PutMapping("/updateStudent/{id}")
	public ResponseEntity<Map<String, Object>> updateStudent(@PathVariable Integer id, @RequestBody Student studentDetails) {
		Optional<Student> studentOptional = studentRepo.findById(id);
		Map<String, Object> response = new HashMap<>();
		if (!studentOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // If student not found
		}

		// Updating the student information
		Student student = studentOptional.get();
		student.setName(studentDetails.getName());
		student.setCollege(studentDetails.getCollege());
		student.setAddress(studentDetails.getAddress());

		studentRepo.save(student);
		response.put("message", "Student Records Updated successfully!");

		return  new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/deleteStudent/{id}")
	public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable Integer id) {
	    Optional<Student> studentOptional = studentRepo.findById(id);
	    Map<String, Object> response = new HashMap<>();

	    if (!studentOptional.isPresent()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Student not found
	    }

	    studentRepo.deleteById(id);
	    response.put("message", "Student Records Deleted successfully!");
	    
	    // Use HttpStatus.OK or another status instead of NO_CONTENT
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
