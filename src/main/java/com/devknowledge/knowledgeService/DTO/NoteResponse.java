package com.devknowledge.knowledgeService.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class NoteResponse {
    private String id ;

    private String title;

    private String content;

    private String category;

    private List<String> tags;

    private String AiSummary;

    private String practiceQuestion;
    private Instant createdAt;

    private Instant updatedAt;
}
