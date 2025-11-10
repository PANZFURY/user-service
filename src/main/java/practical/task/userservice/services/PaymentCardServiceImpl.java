package practical.task.userservice.services;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
import practical.task.userservice.models.User;
import practical.task.userservice.repositories.PaymentCardRepository;
import practical.task.userservice.repositories.UserRepository;

@Service
public class PaymentCardServiceImpl implements PaymentCardService{

    private final PaymentCardRepository paymentCardRepository;
    private final PaymentCardMapper paymentCardMapper;
    private final UserRepository userRepository;

    @Autowired
    public PaymentCardServiceImpl(PaymentCardRepository paymentCardRepository,
                                  PaymentCardMapper paymentCardMapper,
                                  UserRepository userRepository) {
        this.paymentCardRepository = paymentCardRepository;
        this.paymentCardMapper = paymentCardMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Page<PaymentCardResponse> getAll(Specification<PaymentCard> spec, Pageable pageable) {
        return paymentCardRepository
                .findAll(spec, pageable)
                .map(paymentCardMapper::toPaymentCardResponse);
    }

    @Override
    public PaymentCardResponse getOneById(Long id) {
        PaymentCard paymentCard = paymentCardRepository
                .findPaymentCardById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment card was not found"));
        return paymentCardMapper.toPaymentCardResponse(paymentCard);
    }

    @Override
    public PaymentCardResponse createPaymentCard(CreatePaymentCardDto createPaymentCardDto) {
        paymentCardRepository.findPaymentCardByNumber(createPaymentCardDto.number())
                .ifPresent(c -> {throw new EntityExistsException("PaymentCard already exists");});

        long countCardsByUser = paymentCardRepository.countByUserId(createPaymentCardDto.userId());
        if (countCardsByUser >= 5) throw new RuntimeException("User can not have more than 5 cards");

        PaymentCard paymentCard = paymentCardMapper.fromPaymentCardCreateDto(createPaymentCardDto);
        paymentCard.setUser(userRepository.findUserById(createPaymentCardDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User was not found")));

        return paymentCardMapper.toPaymentCardResponse(paymentCardRepository.save(paymentCard));
    }

    @Override
    public PaymentCardResponse updatePaymentCardById(Long id, UpdatePaymentCardDto updatePaymentCardDto) {
        return null;
    }

    @Override
    public void deletePaymentCardById(Long id) {

    }
}