package practical.task.userservice.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practical.task.userservice.dto.request.paymentCardDto.CreatePaymentCardDto;
import practical.task.userservice.dto.request.paymentCardDto.UpdatePaymentCardDto;
import practical.task.userservice.dto.response.PaymentCardResponse;
import practical.task.userservice.exception.ExceedLimitOfCards;
import practical.task.userservice.mapper.PaymentCardMapper;
import practical.task.userservice.model.PaymentCard;
import practical.task.userservice.model.User;
import practical.task.userservice.repository.PaymentCardRepository;
import practical.task.userservice.repository.UserRepository;
import practical.task.userservice.service.PaymentCardService;
import practical.task.userservice.util.CreateEntityHelper;

@Service
public class PaymentCardServiceImpl implements PaymentCardService {

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
    public Page<PaymentCardResponse> getAll(Pageable pageable) {
        return paymentCardRepository
                .findAll(pageable)
                .map(paymentCardMapper::toPaymentCardResponse);
    }

    @Override
    public PaymentCardResponse getOneById(Long id) {
        PaymentCard paymentCard = paymentCardRepository
                .findPaymentCardById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment card was not found"));
        return paymentCardMapper.toPaymentCardResponse(paymentCard);
    }

    @Transactional
    @Override
    public PaymentCardResponse createPaymentCard(CreatePaymentCardDto createPaymentCardDto) {
        paymentCardRepository.findPaymentCardByNumber(createPaymentCardDto.number())
                .ifPresent(c -> {throw new EntityExistsException("PaymentCard already exists");});

        long countCardsByUser = paymentCardRepository.countByUserId(createPaymentCardDto.userId());
        if (countCardsByUser >= 5) throw new ExceedLimitOfCards("User can not have more than 5 cards");

        PaymentCard paymentCard = paymentCardMapper.fromPaymentCardCreateDto(createPaymentCardDto);

        User user = userRepository.findUserById(createPaymentCardDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        paymentCard.setUser(user);
        paymentCard.setHolder(user.getSurname() + " " + user.getName());

        return paymentCardMapper.toPaymentCardResponse(paymentCardRepository.save(paymentCard));
    }

    @Transactional
    @Override
    public PaymentCardResponse updatePaymentCardById(Long id, UpdatePaymentCardDto updatePaymentCardDto) {
        PaymentCard paymentCard = paymentCardRepository.findPaymentCardById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment card was not found"));

        if (updatePaymentCardDto.number() != null) {
            paymentCardRepository.findPaymentCardByNumber(updatePaymentCardDto.number())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(c -> {throw new EntityExistsException("Number is already used");});
        }

        if (updatePaymentCardDto.userId() != null) {
            User user = userRepository.findUserById(updatePaymentCardDto.userId())
                    .orElseThrow(() -> new EntityNotFoundException("User with id: " + updatePaymentCardDto.userId() + " was not found"));
            paymentCard.setUser(user);
            paymentCard.setHolder(user.getSurname() + " " + user.getName());
        }

        paymentCard.setNumber(CreateEntityHelper.resolveIfNotNull(updatePaymentCardDto.number(), paymentCard.getNumber()));
        paymentCard.setActive(CreateEntityHelper.resolveIfNotNull(updatePaymentCardDto.active(), paymentCard.isActive()));
        paymentCard.setExpirationDate(CreateEntityHelper.resolveIfNotNull(updatePaymentCardDto.expirationDate(), paymentCard.getExpirationDate()));

        paymentCardRepository.save(paymentCard);

        return paymentCardMapper.toPaymentCardResponse(paymentCard);
    }


    @Transactional
    @Override
    public void deletePaymentCardById(Long id) {
        PaymentCard paymentCard = paymentCardRepository.findPaymentCardById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment card was not found"));

        paymentCard.setActive(false);
        paymentCardRepository.save(paymentCard);

    }
}