package com.example.TeamUP.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Team team;

    @Column(name = "tag_name")
    private String tagName;

    @Builder
    public Tag(Long id, Team team, String tagName) {
        this.id = id;
        this.team = team;
        this.tagName = tagName;
    }

    public Tag() {

    }
}
