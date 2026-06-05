package com.devknowledge.knowledgeService.controller;


import com.devknowledge.knowledgeService.DTO.NoteRequest;
import com.devknowledge.knowledgeService.DTO.NoteResponse;
import com.devknowledge.knowledgeService.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;



    // POST /api/v1/notes
    @PostMapping
    public ResponseEntity<NoteResponse> create(
            @Valid @RequestBody NoteRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noteService.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getById(@PathVariable String id){
         return ResponseEntity.ok(noteService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getAll(){
        return ResponseEntity.ok(noteService.getAll());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<NoteResponse>> getByCategory(@PathVariable String category){
        return ResponseEntity.ok(noteService.getByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<NoteResponse>> search(
            @RequestParam String q,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(noteService.search(q, pageable));
    }

    // PUT /api/v1/notes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> update(
            @PathVariable String id,
            @Valid @RequestBody NoteRequest req) {
        return ResponseEntity.ok(noteService.update(id, req));
    }

    // DELETE /api/v1/notes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
