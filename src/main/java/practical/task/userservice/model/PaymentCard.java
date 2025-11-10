package practical.task.userservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Entity
@Table(name = "payment_cards")
@NoArgsConstructor
@Data
public class PaymentCard extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NonNull
    @Column(unique = true)
    private String number;

    private String holder;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    private boolean active;
}
