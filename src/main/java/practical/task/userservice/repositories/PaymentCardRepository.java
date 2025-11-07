package practical.task.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import practical.task.userservice.models.PaymentCard;

import java.util.List;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {

    List<PaymentCard> findByUserId(Long userId);

    long countByUserId(Long userId);

    void updateActiveStatus(Long id, boolean active);

}

