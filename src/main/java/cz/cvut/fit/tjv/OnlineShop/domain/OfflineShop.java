package cz.cvut.fit.tjv.OnlineShop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "OfflineShop")
@Getter
@Setter
public class OfflineShop {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "shop_id")
    private Long id;
    @Column(name = "shop_name")
    @Size(max = 20)
    @NotBlank
    private String name;
    @Column(name = "address")
    @NotBlank
    private String address;
    @OneToMany(targetEntity = Availability.class, mappedBy = "offlineShop", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Availability> offers;

    public OfflineShop() {
    }

    public OfflineShop(Long id, String name, String address, List<Availability> offers) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.offers = offers;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) return false;
        OfflineShop offlineShop = (OfflineShop) obj;
        return Objects.equals(id, offlineShop.id);
    }
}
