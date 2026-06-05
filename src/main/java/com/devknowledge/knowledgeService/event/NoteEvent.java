package com.devknowledge.knowledgeService.event;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NoteEvent {

    private String eventType;
    private String noteId;
    private String title;
    private String content;
    private String category;
    private List<String> tags;
    private String correlationId;
}
