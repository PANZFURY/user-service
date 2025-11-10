package practical.task.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import practical.task.userservice.dtos.requests.paymentCardDtos.CreatePaymentCardDto;
import practical.task.userservice.dtos.requests.paymentCardDtos.UpdatePaymentCardDto;
import practical.task.userservice.dtos.responses.PaymentCardResponse;
import practical.task.userservice.mappers.PaymentCardMapper;
import practical.task.userservice.models.PaymentCard;
import practical.task.userservice.repositories.PaymentCardRepository;

@Service
public class PaymentCardServiceImpl implements PaymentCardService{

    private final PaymentCardRepository paymentCardRepository;
    private final PaymentCardMapper paymentCardMapper;

    @Autowired
    public PaymentCardServiceImpl(PaymentCardRepository paymentCardRepository, PaymentCardMapper paymentCardMapper) {
        this.paymentCardRepository = paymentCardRepository;
        this.paymentCardMapper = paymentCardMapper;
    }

    @Override
    public Page<PaymentCardResponse> getAll(Specification<PaymentCard> spec, Pageable pageable) {
        return paymentCardRepository
                .findAll(spec, pageable)
                .map(paymentCardMapper::toPaymentCardResponse);
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
