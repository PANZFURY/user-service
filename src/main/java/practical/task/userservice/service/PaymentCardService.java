package practical.task.userservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import practical.task.userservice.dto.request.paymentCardDto.CreatePaymentCardDto;
import practical.task.userservice.dto.request.paymentCardDto.UpdatePaymentCardDto;
import practical.task.userservice.dto.response.PaymentCardResponse;
import practical.task.userservice.model.PaymentCard;

public interface PaymentCardService {

    Page<PaymentCardResponse> getAll(Specification<PaymentCard> spec, Pageable pageable);
    PaymentCardResponse getOneById(Long id);
    PaymentCardResponse createPaymentCard(CreatePaymentCardDto createPaymentCardDto);
    PaymentCardResponse updatePaymentCardById(Long id, UpdatePaymentCardDto updatePaymentCardDto);
    void deletePaymentCardById(Long id);

}
