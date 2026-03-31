package com.klearn.controller;

import com.klearn.model.Course;
import com.klearn.security.UserDetailsImpl;
import com.klearn.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * UC-04: Course listing + lesson listing with progress.
 * @Controller → returns Thymeleaf view name (NOT JSON).
 */
@Controller
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/courses")
    public String viewCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("currentPage", "courses");
        return "courses/index";
    }

    @GetMapping("/courses/{id}/lessons")
    public String viewCourseLessons(@PathVariable Long id, Authentication authentication, Model model) {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return "redirect:/courses";
        }

        model.addAttribute("course", course);
        model.addAttribute("lessons", courseService.getLessonsByCourse(id));
        model.addAttribute("currentPage", "courses");

        // Per-lesson progress status – via Service (not direct repo call)
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl principal) {
            model.addAttribute("statusByLessonId", courseService.getLessonStatusByUserId(principal.getUserId()));
        }

        return "lessons/list";
    }

    @GetMapping("/courses/{id}")
    public String viewCourseDetails(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        if (course == null) return "redirect:/courses";

        model.addAttribute("course", course);
        model.addAttribute("lessons", courseService.getLessonsByCourse(id));
        model.addAttribute("currentPage", "courses");
        return "pages/course-details";
    }
}
