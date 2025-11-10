package practical.task.userservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import practical.task.userservice.dtos.requests.paymentCardDtos.CreatePaymentCardDto;
import practical.task.userservice.dtos.requests.paymentCardDtos.UpdatePaymentCardDto;
import practical.task.userservice.dtos.responses.PaymentCardResponse;
import practical.task.userservice.models.PaymentCard;

public interface PaymentCardService {

    Page<PaymentCardResponse> getAll(Specification<PaymentCard> spec, Pageable pageable);
    PaymentCardResponse getOneById(Long id);
    PaymentCardResponse createPaymentCard(CreatePaymentCardDto createPaymentCardDto);
    PaymentCardResponse updatePaymentCardById(Long id, UpdatePaymentCardDto updatePaymentCardDto);
    void deletePaymentCardById(Long id);

}
