package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.ErrorDetail;
import com.lambdaschool.school.model.Student;
import com.lambdaschool.school.service.StudentService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController
{
    @Autowired
    private StudentService studentService;

    // Adding custom swagger documentation for list all students
    @ApiOperation(value = "Return all students",
            response = Student.class,
            responseContainer = "List")
    @ApiImplicitParams({@ApiImplicitParam(name = "page",
                                dataType = "integer", paramType = "query",
                                value = "Results page you want to retrieve (0..N)"),
                        @ApiImplicitParam(name = "size", dataType = "integer",
                                paramType = "query", value = "Number of records per page."),
                        @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string",
                                 paramType = "query", value = "Sorting criteria in for format: property(,asc|desc.). " +
                                                                "Default sort order is ascending. " +
                                                                "Multiple sort criteria are supported.")})

    @GetMapping(value = "/students", produces = {"application/json"})
    public ResponseEntity<?> listAllStudents()
    {
        List<Student> myStudents = studentService.findAll();
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }


    // Adding custom swagger documentation for get student by id
    @ApiOperation(value = "Retrieves a student associate with the Id",
            response = Student.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Student Found",
            response = Student.class),
            @ApiResponse(code = 404,
                    message = "Student Not Found",
                    response = ErrorDetail.class)})

    @GetMapping(value = "/Student/{StudentId}",
                produces = {"application/json"})
    public ResponseEntity<?> getStudentById(
            // Adding custom swagger params for get student by id
            @ApiParam(value = "Student Id",
                    required = true,
                    example = "1") // example is always set as a string
            @PathVariable
                    Long StudentId)
    {
        Student r = studentService.findStudentById(StudentId);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    // Adding custom swagger documentation for get student by name containing
    @ApiOperation(value = "Retrieves a student",
            response = Student.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Student Found",
            response = Student.class),
            @ApiResponse(code = 404,
                    message = "Student Not Found",
                    response = ErrorDetail.class)})

    @GetMapping(value = "/student/namelike/{name}",
                produces = {"application/json"})
    public ResponseEntity<?> getStudentByNameContaining(
            // Adding custom swagger params for get student by name containing
            @ApiParam(value = "Student Name", required = true, example = "Alice%20Cooper")
            @PathVariable String name)
    {
        List<Student> myStudents = studentService.findStudentByNameLike(name);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }

    // Adding custom swagger documentation for add new student
    @ApiOperation(value = "Add a new student",
            notes = "New student Id will be sent to the location header",
            response = void.class)
    @ApiResponses(value = {@ApiResponse(code = 201,
            message = "Student Added",
            response = void.class),
            @ApiResponse(code = 500,
                    message = "Error: Could Not Add Student",
                    response = ErrorDetail.class)})

    @PostMapping(value = "/Student",
                 consumes = {"application/json"},
                 produces = {"application/json"})
    public ResponseEntity<?> addNewStudent(
            @Valid
            @RequestBody Student newStudent) throws URISyntaxException
    {
        newStudent = studentService.save(newStudent);
        newStudent = studentService.save(newStudent);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{Studentid}").buildAndExpand(newStudent.getStudid()).toUri();
        responseHeaders.setLocation(newStudentURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    // Adding custom swagger documentation for update student
    @ApiOperation(value = "Update a student",
            response = void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Student Information Updated",
            response = void.class),
            @ApiResponse(code = 500,
                    message = "Could Not Update Student",
                    response = ErrorDetail.class)})

    @PutMapping(value = "/Student/{Studentid}")
    public ResponseEntity<?> updateStudent(
            // Adding custom swagger params for update student
            @ApiParam(value = "Student Id",
                    required = true,
                    example = "1")
            @RequestBody
                    Student updateStudent,
            @PathVariable
                    long Studentid)
    {
        studentService.update(updateStudent, Studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Adding custom swagger documentation for delete student
    @ApiOperation(value = "Delete a student",
            response = void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Student Deleted",
            response = void.class),
            @ApiResponse(code = 500,
                    message = "Could Not Delete Student",
                    response = ErrorDetail.class)})

    @DeleteMapping("/Student/{Studentid}")
    public ResponseEntity<?> deleteStudentById(
            // Adding custom swagger params for delete student
            @ApiParam(value = "Student Id",
                    required = true,
                    example = "1")
            @PathVariable
                    long Studentid)
    {
        studentService.delete(Studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
