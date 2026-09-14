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
        course = prepareCourse();
    }

    @Test
    void submitForReview() {
        var courseForReview = coursePublisher.submitForReview(course.getId());

        assertThat(courseForReview.getStatus()).isEqualTo(CourseStatus.IN_REVIEW);
    }
    @Test
    void publish() {
        coursePublisher.submitForReview(course.getId());

        var courseForPublish = coursePublisher.publish(course.getId());

        assertThat(courseForPublish.getStatus()).isEqualTo(CourseStatus.PUBLISHED);
        assertThat(courseForPublish.getDetail().getPublishedAt()).isNotNull();
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