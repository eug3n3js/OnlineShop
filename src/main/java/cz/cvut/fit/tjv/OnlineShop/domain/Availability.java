package cz.cvut.fit.tjv.OnlineShop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Availability")
@Getter
@Setter
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "offer_id")
    private Long id;
    @Column(name = "quantity")
    @DecimalMin(value = "0")
    @NotNull
    private int quantity;
    @Column(name = "price")
    @Positive
    @NotNull
    private float price;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Product.class)
    @JoinColumn(name = "product_offer")
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = OfflineShop.class)
    @JoinColumn(name = "shop_offer")
    private OfflineShop offlineShop;
    @ManyToMany(targetEntity = Order.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_item",
            joinColumns = @JoinColumn(name = "custom_id"),
            inverseJoinColumns = @JoinColumn(name = "offer_id")
    )
    private List<Order> customs;

    public Availability() {
    }

    public Availability(Long id, int quantity, float price, Product product, OfflineShop offlineShop, List<Order> customs) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
        this.offlineShop = offlineShop;
        this.customs = customs;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) return false;
        Availability availability = (Availability) obj;
        return Objects.equals(id, availability.id);
    }

}
