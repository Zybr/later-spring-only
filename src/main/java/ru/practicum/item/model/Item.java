package ru.practicum.item.model;

import com.github.javafaker.Bool;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private Long userId;

    @Column()
    private String url;

    @Column(name = "resolved_url")
    private String resolvedUrl;

    @Column(name = "mime_type")
    private String mimeType;

    @Column
    private String title;

    @Column(name = "has_image")
    private Boolean hasImage;

    @Column(name = "has_video")
    private Boolean hasVideo;

    @Column(name = "date_resolved")
    private LocalDate dateResolved;
}
