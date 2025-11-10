package practical.task.userservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import practical.task.userservice.dtos.requests.paymentCardDtos.CreatePaymentCardDto;
import practical.task.userservice.dtos.requests.paymentCardDtos.UpdatePaymentCardDto;
import practical.task.userservice.dtos.responses.PaymentCardResponse;
import practical.task.userservice.models.PaymentCard;

public class PaymentCardServiceImpl implements PaymentCardService{
    @Override
    public Page<PaymentCardResponse> getAll(Specification<PaymentCard> spec, Pageable pageable) {
        return null;
    }

    @Override
    public PaymentCardResponse getOneById(Long id) {
        return null;
    }

    @Override
    public PaymentCardResponse createPaymentCard(CreatePaymentCardDto createPaymentCardDto) {
        return null;
    }

    @Override
    public PaymentCardResponse updatePaymentCardById(Long id, UpdatePaymentCardDto updatePaymentCardDto) {
        return null;
    }

    @Override
    public void deletePaymentCardById(Long id) {

    }
}
