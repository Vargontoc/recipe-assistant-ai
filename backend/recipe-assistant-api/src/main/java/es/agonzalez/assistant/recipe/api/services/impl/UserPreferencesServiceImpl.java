package es.agonzalez.assistant.recipe.api.services.impl;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.agonzalez.assistant.recipe.api.dtos.PreferencesResponse;
import es.agonzalez.assistant.recipe.api.dtos.PreferencesUpdateRequest;
import es.agonzalez.assistant.recipe.api.models.UserPreferences;
import es.agonzalez.assistant.recipe.api.repositories.UserPreferencesRepository;
import es.agonzalez.assistant.recipe.api.services.UserPreferencesService;

@Service
public class UserPreferencesServiceImpl implements UserPreferencesService {
    
    @Autowired
    private UserPreferencesRepository repository;

    @Override
    public PreferencesResponse getOrCreate() {
        var entity = repository.findAll().stream().findFirst().orElseGet(() -> repository.save(new UserPreferences()));
        return toResponse(entity);
    }

    @Override
    public PreferencesResponse update(PreferencesUpdateRequest request) {
       var entity = repository.findAll().stream().findFirst().orElseGet(() -> repository.save(new UserPreferences()));
       entity.setAllergns(request.allergns());
       entity.setDiet(request.diet());
       entity.setExcludeIngredients(normalize(request.excludes()));

       return toResponse(repository.save(entity));
    }

    private Set<String> normalize(Set<String> excludes) {
        if(excludes == null) return Set.of();
        return excludes.stream().filter(s -> s != null && !s.isBlank()).map(s -> s.trim().toLowerCase()).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private PreferencesResponse toResponse(UserPreferences entity) {
        return new PreferencesResponse(entity.getId(), entity.getDiet(), entity.getExcludeIngredients(), entity.getAllergns());
    }
}
