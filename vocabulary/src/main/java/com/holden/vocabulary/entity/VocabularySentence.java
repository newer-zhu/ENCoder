package com.holden.vocabulary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "vocabulary_sentence")
@Getter
@Setter
@ToString
public class VocabularySentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String sentence;

    @Column(columnDefinition = "TEXT")
    private String translation;

    @Column(length = 64)
    private String source;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public VocabularySentence() {}

    public VocabularySentence(Vocabulary vocabulary, String sentence, String translation, String source) {
        this.vocabulary = vocabulary;
        this.sentence = sentence;
        this.translation = translation;
        this.source = source;
    }
}
