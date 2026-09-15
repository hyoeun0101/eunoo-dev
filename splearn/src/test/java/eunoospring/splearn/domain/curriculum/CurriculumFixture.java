package eunoospring.splearn.domain.curriculum;

import eunoospring.splearn.domain.course.CourseFixture;

public class CurriculumFixture {
    public static Curriculum createCurriculum() {
        return new Curriculum(CourseFixture.createCourse());
    }
}
