package eunoospring.splearn.application.course.provided;

import static org.assertj.core.api.Assertions.assertThat;

import eunoospring.splearn.domain.course.Course;
import eunoospring.splearn.domain.course.CourseStatus;
import eunoospring.splearn.support.ApplicationServiceTest;
import eunoospring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@ApplicationServiceTest
@RequiredArgsConstructor
class CoursePublisherTest extends BaseApplicationServiceTest {

    final private CoursePublisher coursePublisher;

    private Course course;

    @BeforeEach
    void setUp() {
        course = prepareCourseWithCurriculum();
    }

    @Test
    void submitForReview() {

        coursePublisher.submitForReview(course.getId());

        assertThat(course.getStatus()).isEqualTo(CourseStatus.IN_REVIEW);
    }

    @Test
    void publish() {
        coursePublisher.submitForReview(course.getId());

        coursePublisher.publish(course.getId());

        assertThat(course.getStatus()).isEqualTo(CourseStatus.PUBLISHED);
    }

    @Test
    void archive() {
        coursePublisher.submitForReview(course.getId());
        coursePublisher.publish(course.getId());

        var courseForArchive = coursePublisher.archive(course.getId());

        assertThat(courseForArchive.getStatus()).isEqualTo(CourseStatus.ARCHIVED);
        assertThat(courseForArchive.getDetail().getArchivedAt()).isNotNull();
        }

}