package com.example.BMS.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @CreatedDate
    @Column(nullable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @LastModifiedDate
    @Column(nullable = false,updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}
