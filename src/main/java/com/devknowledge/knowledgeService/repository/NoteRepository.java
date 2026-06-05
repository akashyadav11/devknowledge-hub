package com.devknowledge.knowledgeService.repository;


import java.util.*;

import com.devknowledge.knowledgeService.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note,String> {

    List<Note>  findByCategoryOrderByCreatedAtDesc(String Category);

    @Query("""
        SELECT n FROM Note n
        WHERE LOWER(n.title)   LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(n.content) LIKE LOWER(CONCAT('%', :q, '%'))
        ORDER BY n.createdAt DESC
        """)
    Page<Note> search(@Param("q") String q,Pageable pageable);
}
