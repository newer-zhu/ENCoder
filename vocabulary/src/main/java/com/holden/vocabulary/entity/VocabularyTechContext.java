package com.holden.vocabulary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author: Hodor_Zhu
 * @description
 * @date: 2025/12/14 18:00
 */
@Entity
@Table(name = "vocabulary_tech_context",
        uniqueConstraints = @UniqueConstraint(columnNames = {"vocabulary_id", "domain"}))
@Getter
@Setter
@ToString
public class VocabularyTechContext {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    @Column(length = 64)
    private String domain;

    @Column(columnDefinition = "TEXT")
    private String techTranslation;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public VocabularyTechContext() {}

    public VocabularyTechContext(Vocabulary vocabulary, String domain, String techTranslation, String explanation) {
        this.vocabulary = vocabulary;
        this.domain = domain;
        this.techTranslation = techTranslation;
        this.explanation = explanation;
    }
}
