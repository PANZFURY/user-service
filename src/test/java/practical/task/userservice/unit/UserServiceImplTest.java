package practical.task.userservice.unit;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practical.task.userservice.dto.request.userDto.UserCreateDto;
import practical.task.userservice.dto.request.userDto.UserUpdateDto;
import practical.task.userservice.dto.response.UserResponse;
import practical.task.userservice.mapper.UserMapper;
import practical.task.userservice.model.User;
import practical.task.userservice.repository.PaymentCardRepository;
import practical.task.userservice.repository.UserRepository;
import practical.task.userservice.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PaymentCardRepository paymentCardRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponse userResponse;
    private UserCreateDto createDto;
    private UserUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Britney");
        user.setSurname("Spears");
        user.setBirthDate(LocalDate.of(1981, 12, 2));
        user.setEmail("britney@gmail.com");

        userResponse = new UserResponse(
                1L,
                "Britney",
                "Spears",
                LocalDate.of(1981, 12, 2),
                "britney@gmail.com",
                true
        );

        createDto = new UserCreateDto(
                "Britney",
                "Spears",
                LocalDate.of(1981, 12, 2),
                "britney@test.com"
        );

        updateDto = new UserUpdateDto(
                "Johny",
                "Depp",
                LocalDate.of(1990, 1, 1),
                "johnny@gmail.com"
        );
    }

    @Test
    void testCreateUser_success() {
        //given
        when(userRepository.findByEmail(createDto.email()))
                .thenReturn(Optional.empty());
        when(userMapper.fromUserCreateDto(createDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        //when
        UserResponse response = userService.createUser(createDto);

        //then
        assertEquals("Britney", response.name());
        verify(userRepository).findByEmail(createDto.email());
        verify(userRepository).save(user);
        verify(userMapper).fromUserCreateDto(createDto);
        verify(userMapper).toUserResponse(user);
    }

    @Test
    void testCreateUser_emailExists_throws() {
        //given
        when(userRepository.findByEmail(createDto.email()))
                .thenReturn(Optional.of(user));

        //when/then
        assertThrows(EntityExistsException.class,
                () -> userService.createUser(createDto));

        verify(userRepository).findByEmail(createDto.email());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).fromUserCreateDto(any());
    }

    @Test
    void testGetUserById_success() {
        //given
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        //when
        UserResponse response = userService.getOneById(1L);

        //then
        assertEquals("Britney", response.name());
        verify(userRepository).findUserById(1L);
    }

    @Test
    void testGetUserById_notFound() {
        //given
        when(userRepository.findUserById(99L)).thenReturn(Optional.empty());

        //when/then
        assertThrows(EntityNotFoundException.class, () -> userService.getOneById(99L));
    }

    @Test
    void testUpdateUser_success() {
        //given
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(updateDto.email())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        //when
        UserResponse response = userService.updateUserById(1L, updateDto);

        //then
        assertNotNull(response);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUser_emailAlreadyExists() {
        //given
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setEmail(updateDto.email());

        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(updateDto.email())).thenReturn(Optional.of(anotherUser));

        //when/then
        assertThrows(EntityExistsException.class, () -> userService.updateUserById(1L, updateDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteUserById_success() {
        //given
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(paymentCardRepository.findAllByUser(user)).thenReturn(List.of());

        //when
        userService.deleteUserById(1L);

        //then
        verify(userRepository).findUserById(1L);
        verify(paymentCardRepository).findAllByUser(user);
        verify(userRepository).save(argThat(u -> !u.isActive() && u.getId().equals(1L)));
    }

    @Test
    void testDeleteUserById_notFound() {
        //given
        when(userRepository.findUserById(1L)).thenReturn(Optional.empty());

        //when/then
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUserById(1L));
        verify(userRepository, never()).delete(any(User.class));
    }


}