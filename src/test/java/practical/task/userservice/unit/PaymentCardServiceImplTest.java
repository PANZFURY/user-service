package practical.task.userservice.unit;

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
import practical.task.userservice.service.impl.PaymentCardServiceImpl;

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
                1L,
                true
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
                false
        );
    }

    @Test
    void testCreatePaymentCard_success() {
        //given
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(paymentCardMapper.fromPaymentCardCreateDto(createDto)).thenReturn(card);
        when(paymentCardRepository.save(card)).thenReturn(card);
        doReturn(cardResponse).when(paymentCardMapper).toPaymentCardResponse(card);

        //when
        PaymentCardResponse response = paymentCardService.createPaymentCard(createDto);

        //then
        verify(userRepository).findUserById(1L);
        verify(paymentCardRepository).save(argThat(c ->
                c.getNumber().equals(createDto.number()) && c.getUser().equals(user)
        ));

        assertEquals("1234-5678-9012-3456", response.number());
        assertEquals(1L, response.userId());
    }

    @Test
    void testCreatePaymentCard_numberAlreadyExists() {
        //given
        when(paymentCardRepository.findPaymentCardByNumber(createDto.number()))
                .thenReturn(Optional.of(card));

        //when/then
        assertThrows(EntityExistsException.class,
                () -> paymentCardService.createPaymentCard(createDto));

        verify(paymentCardRepository, never()).save(any());
    }

    @Test
    void testUpdatePaymentCardById_success() {
        Long id = 1L;

        User newUser = new User();
        newUser.setId(2L);
        newUser.setName("Taylor");
        newUser.setSurname("Swift");

        PaymentCard existingCard = new PaymentCard();
        existingCard.setId(id);
        existingCard.setUser(user);
        existingCard.setNumber("5555-6666-7777-8888");
        existingCard.setActive(true);

        //given
        when(paymentCardRepository.findPaymentCardById(id)).thenReturn(Optional.of(existingCard));
        when(paymentCardRepository.findPaymentCardByNumber(updateDto.number()))
                .thenReturn(Optional.empty());
        when(userRepository.findUserById(updateDto.userId()))
                .thenReturn(Optional.of(newUser));
        when(paymentCardRepository.save(any(PaymentCard.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        PaymentCardResponse expected = new PaymentCardResponse(
                id,
                "Swift Taylor",
                updateDto.number(),
                updateDto.expirationDate(),
                2L,
                true
        );

        when(paymentCardMapper.toPaymentCardResponse(any(PaymentCard.class))).thenReturn(expected);

        //when
        PaymentCardResponse result = paymentCardService.updatePaymentCardById(id, updateDto);

        //then
        assertEquals(expected.number(), result.number());
        assertEquals(expected.holder(), result.holder());
        assertEquals(expected.expirationDate(), result.expirationDate());

        verify(paymentCardRepository).findPaymentCardById(id);
        verify(paymentCardRepository).findPaymentCardByNumber(updateDto.number());
        verify(userRepository).findUserById(updateDto.userId());
        verify(paymentCardRepository).save(argThat(saved ->
                saved.getUser().equals(newUser)
                        && saved.getNumber().equals(updateDto.number())
                        && !saved.isActive()
        ));
    }

    @Test
    void testUpdatePaymentCardById_numberAlreadyExists() {
        //given
        PaymentCard existingCard = new PaymentCard();
        existingCard.setId(1L);

        PaymentCard otherCard = new PaymentCard();
        otherCard.setId(2L);

        when(paymentCardRepository.findPaymentCardById(1L))
                .thenReturn(Optional.of(existingCard));
        when(paymentCardRepository.findPaymentCardByNumber(updateDto.number()))
                .thenReturn(Optional.of(otherCard));

        //when/then
        assertThrows(EntityExistsException.class,
                () -> paymentCardService.updatePaymentCardById(1L, updateDto));

        verify(paymentCardRepository).findPaymentCardById(1L);
        verify(paymentCardRepository).findPaymentCardByNumber(updateDto.number());
        verify(paymentCardRepository, never()).save(any());
    }

    @Test
    void testDeletePaymentCardById_success() {
        //given
        PaymentCard card = new PaymentCard();
        card.setId(1L);
        card.setActive(true);

        when(paymentCardRepository.findPaymentCardById(1L)).thenReturn(Optional.of(card));
        when(paymentCardRepository.save(any(PaymentCard.class))).thenAnswer(inv -> inv.getArgument(0));

        //when
        paymentCardService.deletePaymentCardById(1L);

        //then
        verify(paymentCardRepository).findPaymentCardById(1L);
        verify(paymentCardRepository).save(argThat(saved ->
                saved.getId().equals(1L) && !saved.isActive()
        ));
    }

    @Test
    void testDeletePaymentCardById_notFound() {
        //given
        when(paymentCardRepository.findPaymentCardById(1L)).thenReturn(Optional.empty());

        //when/then
        assertThrows(EntityNotFoundException.class,
                () -> paymentCardService.deletePaymentCardById(1L));

        verify(paymentCardRepository, never()).save(any());
    }

}


