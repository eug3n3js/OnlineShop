package cz.cvut.fit.tjv.OnlineShop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Custom")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "custom_id")
    private Long id;
    @Column(name = "time")
    @PastOrPresent
    @NotNull
    private LocalDateTime time;
    @Column(name = "is_completed")
    @NotNull
    private boolean isCompleted;
    @ManyToMany(mappedBy = "customs", fetch = FetchType.EAGER)
    private List<Availability> items;

    public Order() {
    }

    public Order(Long id, LocalDateTime time, boolean isCompleted, List<Availability> items) {
        this.id = id;
        this.time = time;
        this.isCompleted = isCompleted;
        this.items = items;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return Objects.equals(id, order.id);
    }
}
