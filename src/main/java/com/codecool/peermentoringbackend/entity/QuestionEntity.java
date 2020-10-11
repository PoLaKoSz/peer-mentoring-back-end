package com.codecool.peermentoringbackend.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class QuestionEntity {

    @Id
    @GeneratedValue
    private Long id;


    @Column(columnDefinition="text")
    private String title;

    @Column(columnDefinition="text")
    private String description;

    @Column
    private LocalDateTime submissionTime;

    @Column
    private Long vote;

    @Column
    private boolean anonym;

    @NonNull
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private UserEntity user;

    @Singular
    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    Set<AnswerEntity> answers = new HashSet<>();


    @Transient
    private Long userId_;

    @Transient
    private String username;

    @ManyToMany
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<TechnologyEntity> technologyTags = new HashSet<>();

    @JsonIgnore
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private VoteEntity voteEntity;

    public void setUserData() {
        this.userId_ = user.getId();
        this.username = user.getUsername();
    }

    public void setTransientData() {
        this.userId_ = user.getId();
        this.username = user.getUsername();
    }
}
