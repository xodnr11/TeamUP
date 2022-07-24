package com.example.TeamUP.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Team team;

    @Column(name = "date")
    private Date date;

    @Column(name = "content")
    private String content;


}
