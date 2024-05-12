package cz.cvut.fit.tjv.OnlineShop.application;

import cz.cvut.fit.tjv.OnlineShop.domain.Availability;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface AvailabilityServiceInterface {
    Availability getAvailabilityById(Long id);

    List<Availability> getAvailabilities();

    Availability createAvailability(Availability availability) throws IllegalArgumentException;

    Availability updateAvailability(Availability availability) throws IllegalArgumentException, EntityNotFoundException;

    void deleteAvailabilityById(Long availability_id) throws EntityNotFoundException;

    List<Availability> checkAvailability(Long order_id) throws EntityNotFoundException;

    void addAvailabilityToOrder(Long order_id, Long availability_id) throws EntityNotFoundException;

    void deleteAvailabilityFromOrder(Long order_id, Long availability_id) throws IllegalArgumentException, EntityNotFoundException;
}

