package com.example.BMS.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
public class User extends BaseModel{

    private String name;

    private String email;

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets;
}