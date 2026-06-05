package com.devknowledge.knowledgeService.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
@Data
public class NoteRequest {

    @NotBlank(message = "Content should not be blank")
    private String content;

    @NotBlank(message = "Title must not be blank")
    @Size(max = 200,message = "title must be less than 200")
    private String title;

    @Size(max = 100)
    private String category;

    private List<String> tags;

}
