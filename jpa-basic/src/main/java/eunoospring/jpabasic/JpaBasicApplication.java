package eunoospring.jpabasic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

/**
 * exam 패키지는 persistence.xml 기반 순수 JPA 학습용이므로 엔티티 스캔 대상에서 제외
 * @EntityScan을 선언하면 기본 스캔 범위(@SpringBootApplication이 위치한 패키지 이하 전체)를 대체하므로,
 * Boot가 관리할 엔티티 패키지는 여기에 모두 나열해야 한다.
 */
@EntityScan(basePackages = "eunoospring.jpabasic.domain")
@SpringBootApplication
public class JpaBasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaBasicApplication.class, args);
    }

}
