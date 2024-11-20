package com.oreo.finalproject_5re5_be.member.repository;

import com.oreo.finalproject_5re5_be.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query( "SELECT m " +
            "FROM Member m " +
            "WHERE m.seq = :memberSeq " )
    public Member findBySeq(Long memberSeq);
    public Member findByEmail(String email);
    public Member findById(String id);
    public boolean existsByEmail(String email);
    public boolean existsById(String id);
    @Query( "SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
            "FROM Member m " +
            "WHERE m.seq <> :memberSeq " +
            "AND m.id = :id" )
    public boolean existsByIdNotContainingMemberSeq(@Param("memberSeq") Long memberSeq, @Param("id") String id);

    @Query( "SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
            "FROM Member m " +
            "WHERE m.seq <> :memberSeq " +
            "AND m.email = :email" )
    public boolean existsByEmailNotContainingMemberSeq(@Param("memberSeq")Long memberSeq, @Param("email") String email);


}
