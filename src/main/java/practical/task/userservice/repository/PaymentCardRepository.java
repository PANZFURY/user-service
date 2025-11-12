package practical.task.userservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practical.task.userservice.models.PaymentCard;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {

    List<PaymentCard> findByUserId(Long userId);

    @Query("SELECT pc FROM PaymentCard pc WHERE pc.id = :id")
    Optional<PaymentCard> findPaymentCardById(@Param("id") Long id);

    @Query(value = "SELECT COUNT(*) FROM payment_cards WHERE user_id = :userId", nativeQuery = true)
    long countByUserId(@Param("userId") Long userId);

    Page<PaymentCard> findAll(Specification<PaymentCard> spec, Pageable pageable);

    @Query("SELECT pc FROM PaymentCard pc WHERE pc.number LIKE :number")
    Optional<PaymentCard> findPaymentCardByNumber(String number);

    @Modifying
    @Transactional
    @Query("UPDATE PaymentCard pc SET pc.active = :active WHERE pc.id = :id")
    void updateActiveStatus(@Param("id") Long id, @Param("active") boolean active);

}

