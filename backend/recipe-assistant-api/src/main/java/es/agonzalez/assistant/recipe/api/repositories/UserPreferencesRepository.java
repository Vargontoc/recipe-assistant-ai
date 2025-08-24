package es.agonzalez.assistant.recipe.api.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.agonzalez.assistant.recipe.api.models.UserPreferences;

@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, UUID>{}
