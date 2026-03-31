package com.klearn.controller;

import com.klearn.dto.ApiResponse;
import com.klearn.model.Course;
import com.klearn.model.Lesson;
import com.klearn.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseApiController {

    private final CourseService courseService;

    // Allow guests to view all courses
    @GetMapping
    public ResponseEntity<ApiResponse<List<Course>>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(ApiResponse.success("Success", courses));
    }

    // Allow guests to view course details
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success("Success", course));
    }

    // Allow guests to view lessons in a course
    @GetMapping("/{id}/lessons")
    public ResponseEntity<ApiResponse<List<Lesson>>> getCourseLessons(@PathVariable Long id) {
        List<Lesson> lessons = courseService.getLessonsByCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Success", lessons));
    }
}
