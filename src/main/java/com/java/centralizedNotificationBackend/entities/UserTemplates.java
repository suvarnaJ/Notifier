package com.java.centralizedNotificationBackend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserTemplates {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userTemplate;

    @ManyToOne
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private Date updatedAt;
}
