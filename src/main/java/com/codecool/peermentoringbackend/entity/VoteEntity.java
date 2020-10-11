package com.codecool.peermentoringbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class VoteEntity {
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @OneToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private QuestionEntity question;


    @NonNull
    @OneToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private UserEntity user;

    @Column(columnDefinition = "boolean default false")
    private boolean voted;


}
