package com.oreo.finalproject_5re5_be.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.oreo.finalproject_5re5_be.member.entity.MemberTermsCondition;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberTermConditionRepositoryTest {

    @Autowired
    private MemberTermConditionRepository memberTermConditionRepository;

    private List<MemberTermsCondition> dummy = new ArrayList<>();

    @BeforeEach
    void setUp() {
        assertNotNull(memberTermConditionRepository);
        memberTermConditionRepository.deleteAll();

        // 더미 생성

        // 더미 저장
    }

    private void createDummy() {

    }
}