package cz.cvut.fit.tjv.OnlineShop.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderDTO {
    private final Long id;
    private final LocalDateTime time;
    private final boolean isCompleted;
    private final List<Long> itemIds;
}

