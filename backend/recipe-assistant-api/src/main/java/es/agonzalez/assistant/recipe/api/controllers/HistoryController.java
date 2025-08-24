package es.agonzalez.assistant.recipe.api.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.agonzalez.assistant.recipe.api.dtos.HistoryDetail;
import es.agonzalez.assistant.recipe.api.dtos.HistoryItem;
import es.agonzalez.assistant.recipe.api.services.SuggestionHistoryService;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    @Autowired
    private SuggestionHistoryService service;

    @GetMapping
    public Page<HistoryItem> list(
        @RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="10") int size
    ) {
        return service.list(page, size);
    }

    @GetMapping("/{id}")
    public HistoryDetail get(@PathVariable UUID id) {
        return service.getDetail(id);
    }

}
