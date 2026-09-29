package com.elearning.courses.service;

import com.elearning.common.dto.PageResponse;
import com.elearning.common.error.ForbiddenException;
import com.elearning.common.error.NotFoundException;
import com.elearning.common.security.AuthenticatedUser;
import com.elearning.common.security.SecurityUtils;
import com.elearning.courses.dto.CourseRequest;
import com.elearning.courses.dto.CourseResponse;
import com.elearning.courses.entity.Course;
import com.elearning.courses.repository.CourseRepository;
import com.elearning.enrollments.repository.EnrollmentRepository;
import com.elearning.lessons.repository.LessonRepository;
import com.elearning.users.entity.User;
import com.elearning.users.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository,
                         EnrollmentRepository enrollmentRepository, LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User instructor = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));

        Course course = new Course();
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setCategory(request.category());
        course.setPrice(request.price());
        course.setLevel(request.level());
        course.setInstructor(instructor);
        course.setStatus("DRAFT");

        courseRepository.save(course);
        return toResponse(course);
    }

    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = getCourseEntity(id);
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();

        if (!course.getInstructor().getId().equals(currentUser.id()) && !currentUser.role().equals("ADMIN")) {
            throw new ForbiddenException("Not authorized to update this course");
        }

        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setCategory(request.category());
        course.setPrice(request.price());
        course.setLevel(request.level());

        courseRepository.save(course);
        return toResponse(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = getCourseEntity(id);
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();

        if (!course.getInstructor().getId().equals(currentUser.id()) && !currentUser.role().equals("ADMIN")) {
            throw new ForbiddenException("Not authorized to delete this course");
        }

        long enrollmentCount = enrollmentRepository.countByCourse(course);
        if (enrollmentCount > 0) {
            throw new ForbiddenException("Cannot delete course with " + enrollmentCount + " active enrollments");
        }

        courseRepository.delete(course);
    }

    public CourseResponse getCourse(Long id) {
        Course course = getCourseEntity(id);
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();

        if (!"PUBLISHED".equals(course.getStatus())) {
            boolean isOwner = course.getInstructor().getId().equals(currentUser.id());
            boolean isAdmin = currentUser.role().equals("ADMIN");
            if (!isOwner && !isAdmin) {
                throw new NotFoundException("Course not found");
            }
        }

        return toResponse(course);
    }

    public PageResponse<CourseResponse> listCourses(String search, String category, int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Course> coursePage;
        if (search != null && !search.isBlank()) {
            coursePage = courseRepository.findByTitleContainingIgnoreCaseAndStatus(search, "PUBLISHED", pageable);
        } else if (category != null && !category.isBlank()) {
            coursePage = courseRepository.findByCategoryAndStatus(category, "PUBLISHED", pageable);
        } else {
            coursePage = courseRepository.findByStatus("PUBLISHED", pageable);
        }

        List<CourseResponse> content = coursePage.getContent().stream().map(this::toResponse).toList();
        return PageResponse.of(content, coursePage.getNumber(), coursePage.getSize(), coursePage.getTotalElements());
    }

    public List<CourseResponse> getInstructorCourses() {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User instructor = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));
        return courseRepository.findByInstructor(instructor).stream().map(this::toResponse).toList();
    }

    @Transactional
    public CourseResponse publishCourse(Long id) {
        Course course = getCourseEntity(id);
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();

        if (!course.getInstructor().getId().equals(currentUser.id()) && !currentUser.role().equals("ADMIN")) {
            throw new ForbiddenException("Not authorized to publish this course");
        }

        course.setStatus("PUBLISHED");
        courseRepository.save(course);
        return toResponse(course);
    }

    @Transactional
    public CourseResponse rejectCourse(Long id) {
        Course course = getCourseEntity(id);
        course.setStatus("REJECTED");
        courseRepository.save(course);
        return toResponse(course);
    }

    private Course getCourseEntity(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Course not found"));
    }

    private CourseResponse toResponse(Course course) {
        long lessonCount = lessonRepository.countByCourse(course);
        long enrollmentCount = enrollmentRepository.countByCourse(course);
        return new CourseResponse(
            course.getId(), course.getTitle(), course.getDescription(), course.getCategory(),
            course.getPrice(), course.getStatus(), course.getLevel(), course.getThumbnailUrl(),
            course.getInstructor().getId(), course.getInstructor().getName(),
            course.getCreatedAt(), lessonCount, enrollmentCount
        );
    }
}
