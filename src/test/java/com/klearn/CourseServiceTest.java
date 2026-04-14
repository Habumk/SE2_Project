package com.klearn.service;

import com.klearn.exception.ResourceNotFoundException;
import com.klearn.model.Course;
import com.klearn.model.Lesson;
import com.klearn.repository.CourseRepository;
import com.klearn.repository.LessonRepository;
import com.klearn.repository.UserProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CourseService
 */
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private UserProgressRepository userProgressRepository;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;
    private Lesson testLesson;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setCourseId(1L);
        testCourse.setTitle("Tiếng Hàn Sơ cấp 1");
        testCourse.setLevel("Sơ cấp 1");

        testLesson = new Lesson();
        testLesson.setLessonId(1L);
        testLesson.setTitle("Giới thiệu");
        testLesson.setCourse(testCourse);
    }

    @Test
    void getAllCourses_ShouldReturnAllCourses() {
        when(courseRepository.findAll()).thenReturn(List.of(testCourse));

        List<Course> courses = courseService.getAllCourses();

        assertEquals(1, courses.size());
        assertEquals("Tiếng Hàn Sơ cấp 1", courses.get(0).getTitle());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getCourseById_ShouldReturnCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        Course course = courseService.getCourseById(1L);

        assertNotNull(course);
        assertEquals("Tiếng Hàn Sơ cấp 1", course.getTitle());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void getCourseById_ShouldThrowException_WhenCourseNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById(99L));
        verify(courseRepository, times(1)).findById(99L);
    }

    @Test
    void getLessonsByCourse_ShouldReturnLessonList() {
        when(lessonRepository.findByCourse_CourseIdOrderByOrderIndexAsc(1L))
            .thenReturn(List.of(testLesson));

        List<Lesson> lessons = courseService.getLessonsByCourse(1L);

        assertEquals(1, lessons.size());
        assertEquals("Giới thiệu", lessons.get(0).getTitle());
        verify(lessonRepository, times(1)).findByCourse_CourseIdOrderByOrderIndexAsc(1L);
    }

    @Test
    void getLessonsByCourse_ShouldReturnEmptyList_WhenNoLessons() {
        when(lessonRepository.findByCourse_CourseIdOrderByOrderIndexAsc(1L))
            .thenReturn(List.of());

        List<Lesson> lessons = courseService.getLessonsByCourse(1L);

        assertTrue(lessons.isEmpty());
    }
}
