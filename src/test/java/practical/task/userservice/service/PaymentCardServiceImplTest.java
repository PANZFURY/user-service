package practical.task.userservice.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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


import static org.junit.Assert.assertNotNull;
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

    @Test
    void testUpdatePaymentCardById_success() {
        Long id = 1L;

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Britney");
        existingUser.setSurname("Spears");

        User newUser = new User();
        newUser.setId(2L);
        newUser.setName("Taylor");
        newUser.setSurname("Swift");

        PaymentCard existingCard = new PaymentCard();
        existingCard.setId(id);
        existingCard.setUser(existingUser);
        existingCard.setNumber("5555-6666-7777-8888");
        existingCard.setHolder("Spears Britney");
        existingCard.setExpirationDate(LocalDate.of(2030, 1, 1));
        existingCard.setActive(true);

        UpdatePaymentCardDto updateDto = new UpdatePaymentCardDto(
                2L,
                "1111-2222-3333-4444",
                LocalDate.of(2031, 12, 12),
                false
        );

        when(paymentCardRepository.findPaymentCardById(id)).thenReturn(Optional.of(existingCard));
        when(paymentCardRepository.findPaymentCardByNumber(updateDto.number())).thenReturn(Optional.empty());
        when(userRepository.findUserById(updateDto.userId())).thenReturn(Optional.of(newUser));

        when(paymentCardRepository.save(any(PaymentCard.class))).thenAnswer(inv -> inv.getArgument(0));

        PaymentCardResponse expectedResponse = new PaymentCardResponse(
                id,
                "Swift Taylor",
                updateDto.number(),
                updateDto.expirationDate(),
                newUser.getId()
        );

        when(paymentCardMapper.toPaymentCardResponse(any(PaymentCard.class))).thenReturn(expectedResponse);

        PaymentCardResponse result = paymentCardService.updatePaymentCardById(id, updateDto);

        assertNotNull(result);
        assertEquals("Swift Taylor", result.holder());
        assertEquals(updateDto.number(), result.number());
        assertEquals(updateDto.expirationDate(), result.expirationDate());
        assertEquals(updateDto.userId(), result.userId());

        verify(paymentCardRepository).findPaymentCardById(id);
        verify(paymentCardRepository).findPaymentCardByNumber(updateDto.number());
        verify(userRepository).findUserById(updateDto.userId());
        verify(paymentCardRepository).save(argThat(saved ->
                saved.getId().equals(id)
                        && saved.getUser().equals(newUser)
                        && saved.getNumber().equals(updateDto.number())
                        && saved.getHolder().equals("Swift Taylor")
                        && saved.getExpirationDate().equals(updateDto.expirationDate())
                        && !saved.isActive()
        ));
        verify(paymentCardMapper).toPaymentCardResponse(any(PaymentCard.class));
        verifyNoMoreInteractions(paymentCardRepository);
    }

    @Test
    void testUpdatePaymentCardById_numberAlreadyExists() {
        Long id = 1L;
        PaymentCard existingCard = new PaymentCard();
        existingCard.setId(id);

        PaymentCard otherCard = new PaymentCard();
        otherCard.setId(2L);
        otherCard.setNumber("1111-2222-3333-4444");

        when(paymentCardRepository.findPaymentCardById(id)).thenReturn(Optional.of(existingCard));
        when(paymentCardRepository.findPaymentCardByNumber("1111-2222-3333-4444"))
                .thenReturn(Optional.of(otherCard));

        UpdatePaymentCardDto updateDto = new UpdatePaymentCardDto(
                1L,
                "1111-2222-3333-4444",
                LocalDate.of(2031, 12, 12),
                true
        );

        assertThrows(EntityExistsException.class,
                () -> paymentCardService.updatePaymentCardById(id, updateDto));

        verify(paymentCardRepository).findPaymentCardById(id);
        verify(paymentCardRepository).findPaymentCardByNumber("1111-2222-3333-4444");
        verify(paymentCardRepository, never()).save(any());
    }

    @Test
    void testDeletePaymentCardById_success() {
        Long cardId = 1L;

        PaymentCard card = new PaymentCard();
        card.setId(cardId);
        card.setActive(true);

        when(paymentCardRepository.findPaymentCardById(cardId)).thenReturn(Optional.of(card));
        when(paymentCardRepository.save(any(PaymentCard.class))).thenAnswer(inv -> inv.getArgument(0));

        paymentCardService.deletePaymentCardById(cardId);

        verify(paymentCardRepository).findPaymentCardById(cardId);
        verify(paymentCardRepository).save(argThat(saved ->
                saved.getId().equals(cardId) && !saved.isActive()
        ));
    }

    @Test
    void testDeletePaymentCardById_notFound() {
        Long cardId = 1L;

        when(paymentCardRepository.findPaymentCardById(cardId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> paymentCardService.deletePaymentCardById(cardId));

        verify(paymentCardRepository).findPaymentCardById(cardId);
        verify(paymentCardRepository, never()).save(any());
    }

}


