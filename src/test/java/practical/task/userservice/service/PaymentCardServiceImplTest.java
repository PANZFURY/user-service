package practical.task.userservice.service;

import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practical.task.userservice.dto.request.paymentCardDto.CreatePaymentCardDto;
import practical.task.userservice.dto.request.paymentCardDto.UpdatePaymentCardDto;
import practical.task.userservice.dto.response.PaymentCardResponse;
import practical.task.userservice.mapper.PaymentCardMapper;
import practical.task.userservice.model.PaymentCard;
import practical.task.userservice.model.User;
import practical.task.userservice.repository.PaymentCardRepository;
import practical.task.userservice.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;


import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentCardServiceImplTest {

    @Mock
    private PaymentCardRepository paymentCardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentCardMapper paymentCardMapper;

    @InjectMocks
    private PaymentCardServiceImpl paymentCardService;

    private User user;
    private PaymentCard card;
    private PaymentCardResponse cardResponse;
    private CreatePaymentCardDto createDto;
    private UpdatePaymentCardDto updateDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Britney");
        user.setSurname("Spears");

        card = new PaymentCard();
        card.setId(1L);
        card.setNumber("1234-5678-9012-3456");
        card.setUser(user);

        cardResponse = new PaymentCardResponse(
                1L,
                "Britney Spears",
                "1234-5678-9012-3456",
                LocalDate.of(2030, 12, 31),
                1L
        );

        createDto = new CreatePaymentCardDto(
                1L,
                "1234-5678-9012-3456",
                LocalDate.of(2030, 12, 31)
        );

        updateDto = new UpdatePaymentCardDto(
                1L,
                "1111-2222-3333-4444",
                LocalDate.of(2031, 12, 12),
                true
        );
    }

    @Test
    void testCreatePaymentCard_success() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(paymentCardMapper.fromPaymentCardCreateDto(createDto)).thenReturn(card);
        when(paymentCardRepository.save(card)).thenReturn(card);
        doReturn(cardResponse).when(paymentCardMapper).toPaymentCardResponse(card);

        PaymentCardResponse response = paymentCardService.createPaymentCard(createDto);

        verify(userRepository).findUserById(1L);
        verify(paymentCardRepository).save(argThat(c ->
                c.getNumber().equals(createDto.number()) && c.getUser().equals(user)
        ));

        assertEquals("1234-5678-9012-3456", response.number());
        assertEquals(1L, response.userId());
    }

    @Test
    void testCreatePaymentCard_numberAlreadyExists() {
        when(paymentCardRepository.findPaymentCardByNumber("1234-5678-9012-3456"))
                .thenReturn(Optional.of(card));

        assertThrows(EntityExistsException.class, () ->
                paymentCardService.createPaymentCard(createDto)
        );

        verify(paymentCardRepository, never()).save(any());
    }

}

