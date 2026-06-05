package com.devknowledge.knowledgeService.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notes")
@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false,length = 200)
    private String title;

    @Column(columnDefinition = "TEXT",nullable = false)

    private String content;

    @Column(length = 100)
    private String category;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "note_tags",
            joinColumns = @JoinColumn(name = "note_id")
    )
    private List<String> tags = new ArrayList<>();

//    private String AiSummary;

    @Column(updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }


}
