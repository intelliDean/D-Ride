//package dean.project.Dride.controller.CourseController;
//
//import dean.project.Dride.data.Course;
//import dean.project.Dride.services.CourseService;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class CourseControllerTest {
//    CourseService courseService = new CourseService();
//    CourseController courseController = new CourseController();
//
//    @Test
//    void getCourseByName() {
//        List<Course> list = List.of(
//                new Course("jv", "Jav", "First Love"),
//                new Course("db", "Database", "Select * from"),
//                new Course("fe", "Frontend", "HTML, CSS, JS and React")
//        );
//        //Course course = courseController.getCourseByName("Java");
//        Course course = courseService.getByName("Jav");
//        assertEquals("First Love", course.getDescription());
//    }
//}