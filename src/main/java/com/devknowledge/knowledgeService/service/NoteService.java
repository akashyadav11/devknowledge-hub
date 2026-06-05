package com.devknowledge.knowledgeService.service;


import com.devknowledge.knowledgeService.DTO.NoteRequest;
import com.devknowledge.knowledgeService.DTO.NoteResponse;
import com.devknowledge.knowledgeService.domain.Note;
import com.devknowledge.knowledgeService.event.NoteEvent;
import com.devknowledge.knowledgeService.exception.NoteNotFoundException;
import com.devknowledge.knowledgeService.repository.NoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository repo;
    private final KafkaTemplate<String, NoteEvent> kafkaTemplate;

    @Transactional
    @CacheEvict(value = "notes-list", allEntries = true)
    public NoteResponse create(NoteRequest req) {
        Note note = Note.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .category(req.getCategory())
                .tags(req.getTags() != null ? req.getTags() : List.of())
                .build();

        note = repo.save(note);

        // publish to Kafka → AI enrichment service consumes this
//        publishEvent("NOTE_CREATED", note);
//
        log.info("Note created id={} category={}", note.getId(), note.getCategory());

        return toResponse(note);
    }


//    @Transactional(readOnly = true)
    @Cacheable(value = "notes", key = "#id")
    public NoteResponse getById(String id){
        return toResponse(findOrThrow(id));
    }

    public List<NoteResponse> getAll(){
        return repo.findAll().stream().map(this::toResponse).toList();

    }


    @Caching(
            put    = { @CachePut(value = "notes", key = "#id") },
            evict  = { @CacheEvict(value = "notes-list",   allEntries = true),
                    @CacheEvict(value = "notes-search",  allEntries = true) }
    )
    public NoteResponse update(String id, NoteRequest req)
    {
        Note note=findOrThrow(id);
        note.setTitle(req.getTitle());
        note.setContent(req.getContent());
        note.setCategory(req.getCategory());
        if (req.getTags() != null) note.setTags(req.getTags());

        // no repo.save() needed — Hibernate dirty checking handles it

//        publishEvent("NOTE_UPDATED", note);
        log.info("Note updated id={}", id);
        return toResponse(note);
    }

    @Cacheable(value = "notes-list", key = "'category-' + #category")
    public List<NoteResponse> getByCategory(String category){
        return repo.findByCategoryOrderByCreatedAtDesc(category).stream().map(this::toResponse).toList();
    }

    @Cacheable(value = "notes-search", key = "#q + '-' + #pageable.pageNumber")
    public Page<NoteResponse> search(String q, Pageable pageable){
        return repo.search(q,pageable).map(this::toResponse);
    }


    @Caching(evict = {
            @CacheEvict(value = "notes",        key = "#id"),
            @CacheEvict(value = "notes-list",   allEntries = true),
            @CacheEvict(value = "notes-search", allEntries = true)
    })
    public void delete(String id){
        Note note=findOrThrow(id);
        repo.delete(note);
//        publishEvent("NOTE_DELETED", note);
        log.info("Note deleted id={}", id);
    }

    private NoteResponse toResponse(Note n) {
        return NoteResponse.builder()
                .id(n.getId  ())
                .title(n.getTitle())
                .content(n.getContent())
                .category(n.getCategory())
                .tags(n.getTags())
//                .aiSummary(n.getAiSummary())
//                .practiceQuestion(n.getPracticeQuestion())
                .createdAt(n.getCreatedAt())
                .updatedAt(n.getUpdatedAt())
                .build();
    }

    private Note findOrThrow(String id){
        return repo.findById(id).orElseThrow(()->new NoteNotFoundException((id)));
    }

//    public void publishEvent(String eventType,Note note){
//        NoteEvent event = NoteEvent.builder()
//                .eventType(eventType)
//                .noteId(note.getId())
//                .title(note.getTitle())
//                .content(note.getContent())
//                .category(note.getCategory())
//                .tags(note.getTags())
//                .correlationId(UUID.randomUUID().toString()) // unique per event
//                .build();
//
//        CompletableFuture<SendResult<String, NoteEvent>> future =
//                kafkaTemplate.send("note-events", note.getId(), event);
//
//        future.whenComplete((result, ex) -> {
//            if (ex != null) {
//                log.error("Failed to publish {} for noteId={}",
//                        eventType, note.getId(), ex);
//            } else {
//                log.info("Published {} noteId={} partition={} offset={}",
//                        eventType,
//                        note.getId(),
//                        result.getRecordMetadata().partition(),
//                        result.getRecordMetadata().offset());
//            }
//        });
//    }

}


