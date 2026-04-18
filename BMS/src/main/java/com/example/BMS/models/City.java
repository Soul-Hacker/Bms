package com.example.BMS.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class City extends BaseModel {
    private String name;

}
