package eunoospring.learningtest.concurrency;

public class DbConcurrencyLearningTest {

    /**
     * 동시성 문제 해결법 : 비관적 락, 낙관적 락, DB 유니크 제약
     *
     * 비관적 락:
     * 트랜잭션 시작 시 필요한 데이터에 x-lock을 건다.
     *
     * 낙관적 락:
     * 락을 걸지 않는다. 대신에 version 또는 조건을 통해 데이터의 정합성이 깨지지 않았을 때만 update한다.
     * 데이터의 정합성이 깨져서 update 실패한 경우에 대한 처리가 필요하다. (exception 던지기 또는 재시도)
     */
}
