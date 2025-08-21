package es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.mapper;

import java.util.List;

import es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.models.BaseEntity;

public abstract class AbstractMapper<T extends BaseEntity, R extends Object>
{

    abstract R toDto(T entity);
    abstract T toEntity(R dto);

    public List<R> toDtoList(List<T> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    public List<T> toEntityList(List<R> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }
}

