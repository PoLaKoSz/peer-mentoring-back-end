package com.codecool.peermentoringbackend.repository;

import com.codecool.peermentoringbackend.entity.TechnologyEntity;
import com.codecool.peermentoringbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    UserEntity findDistinctById(Long id);

    UserEntity findDistinctByUsername(String username);

    UserEntity findDistinctByEmail(String email);

    @Query("SELECT DISTINCT u FROM UserEntity u WHERE u.technologyTags is not empty OR u.projectTags is not empty")
    List<UserEntity> getIfHasProjectOrTechTags();


    @Query("SELECT u from UserEntity u join fetch u.questions q where q.id = :questionId ")
    UserEntity findUserEntityByQuestionId(@Param("questionId") Long questionId);

    @Query("SELECT u from UserEntity u join fetch u.answers a where a.id = :answerId ")
    UserEntity findUserEntityByAnswerId(@Param("answerId") Long answerId);
}