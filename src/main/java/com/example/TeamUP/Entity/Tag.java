package com.example.TeamUP.Entity;

import lombok.Data;

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
}
