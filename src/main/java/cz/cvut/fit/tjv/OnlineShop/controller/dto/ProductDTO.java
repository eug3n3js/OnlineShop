package cz.cvut.fit.tjv.OnlineShop.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProductDTO {
    private final Long id;
    private final String name;
    private final String description;
    private final List<Long> presenceIds;
}

