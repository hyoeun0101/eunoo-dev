package eunoospring.splearn.application.required;

import eunoospring.splearn.domain.Email;
import eunoospring.splearn.domain.Member;
import java.util.Optional;
import org.springframework.data.repository.Repository;

/**
 * 회원 정보를 저장하거나 조회한다
 */
public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);

    Optional<Member> findByEmail(Email email);
}
