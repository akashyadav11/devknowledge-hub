package com.devknowledge.knowledgeService.exception;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(String id){
        super("note not found"+id);
    }
}
