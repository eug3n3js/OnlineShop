package cz.cvut.fit.tjv.OnlineShop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Product")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "product_id")
    private Long id;
    @Column(name = "product_name")
    @Size(max = 20)
    @NotBlank
    private String name;
    @Column(name = "description")
    @NotBlank
    private String description;
    @OneToMany(targetEntity = Availability.class, mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Availability> presence;

    public Product() {
    }

    public Product(Long id, String name, String description, List<Availability> presence) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.presence = presence;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return Objects.equals(id, product.id);
    }
}
