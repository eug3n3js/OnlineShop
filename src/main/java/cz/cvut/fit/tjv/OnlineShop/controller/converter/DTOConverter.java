package cz.cvut.fit.tjv.OnlineShop.controller.converter;

public interface DTOConverter<DTO, Entity> {
    DTO toDTO(Entity entity);
    Entity toEntity(DTO dto) throws  IllegalArgumentException;
}
