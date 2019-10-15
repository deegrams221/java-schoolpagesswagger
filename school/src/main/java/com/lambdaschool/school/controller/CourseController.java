package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.Course;
import com.lambdaschool.school.model.ErrorDetail;
import com.lambdaschool.school.service.CourseService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/courses")
public class CourseController
{
    @Autowired
    private CourseService courseService;

    // Adding custom swagger documentation for list all courses with paging
    @ApiOperation(value = "Return all Courses", response = Course.class,
            responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string",
                    paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})

    // paging and sorting
    // localhost:2019/courses/courses/paging/?page=1&size=10
    @GetMapping(value = "/courses/paging",
            produces = {"application/json"})
    public ResponseEntity<?> ListAllCoursesByPage(@PageableDefault(page = 0,
            size = 5) Pageable pageable)
    {                        // findAllPageable(pageable.unpaged()) <- returns everything
        List<Course> myCourses = courseService.findAllPageable(pageable);
        return new ResponseEntity<>(myCourses, HttpStatus.OK);
    }

    // Adding custom swagger documentation for list all courses
    @ApiOperation(value = "Return all Courses", response = Course.class,
            responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Course List Found",
            response = Course.class),
            @ApiResponse(code = 404,
                    message = "Course List Not Found",
                    response = ErrorDetail.class)})

    @GetMapping(value = "/courses", produces = {"application/json"})
    public ResponseEntity<?> listAllCourses()
    {
        ArrayList<Course> myCourses = courseService.findAll();
        return new ResponseEntity<>(myCourses, HttpStatus.OK);
    }

    // Adding custom swagger documentation for Count of Students in Courses
    @ApiOperation(value = "Return a Count of Students in Courses",
            response = Course.class,
            responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Count Found",
            response = Course.class),
            @ApiResponse(code = 404,
                    message = "Count Not Found",
                    response = ErrorDetail.class)})

    @GetMapping(value = "/studcount", produces = {"application/json"})
    public ResponseEntity<?> getCountStudentsInCourses()
    {
        return new ResponseEntity<>(courseService.getCountStudentsInCourse(), HttpStatus.OK);
    }

    // Adding custom swagger documentation for delete course by id
    @ApiOperation(value = "Delete a course",
            response = void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Course Deleted",
            response = void.class),
            @ApiResponse(code = 500,
                    message = "Could Not Delete Course",
                    response = ErrorDetail.class)})

    @DeleteMapping("/courses/{courseid}")
    public ResponseEntity<?> deleteCourseById(
            // Adding custom swagger params for delete course
            @ApiParam(value = "Course Id",
                    required = true,
                    example = "1")
            @PathVariable long courseid)
    {
        courseService.delete(courseid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
