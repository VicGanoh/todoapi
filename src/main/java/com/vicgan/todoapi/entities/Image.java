package com.vicgan.todoapi.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@Data
@Entity
@Table(name = "images", indexes = {
        @Index(name = "index_images_on_name", columnList = "name")
})
public class Image implements Serializable {

    @Id
    @UuidGenerator
    @GenericGenerator(name = "UUID", type = org.hibernate.id.uuid.UuidGenerator.class)
    @GeneratedValue(generator = "UUID")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "image")
    private byte[] image;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
