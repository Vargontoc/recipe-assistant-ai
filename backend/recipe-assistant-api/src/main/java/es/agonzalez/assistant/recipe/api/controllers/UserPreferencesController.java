package es.agonzalez.assistant.recipe.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.agonzalez.assistant.recipe.api.dtos.PreferencesResponse;
import es.agonzalez.assistant.recipe.api.dtos.PreferencesUpdateRequest;
import es.agonzalez.assistant.recipe.api.services.UserPreferencesService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/preferences")
public class UserPreferencesController
{
    @Autowired
    private UserPreferencesService service;
    
    @GetMapping
    public PreferencesResponse get() {
        return service.getOrCreate();
    }
    
    @PutMapping()
    public PreferencesResponse update(@Valid @RequestBody PreferencesUpdateRequest request) {
        return service.update(request);
    }
    
}
