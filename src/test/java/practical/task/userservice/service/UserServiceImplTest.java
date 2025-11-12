package practical.task.userservice.service;

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
                "britney@gmail.com"
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
        when(userMapper.fromUserCreateDto(createDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        doReturn(userResponse).when(userMapper).toUserResponse(user);

        UserResponse response = userService.createUser(createDto);

        assertEquals("Britney", response.name());
        verify(userRepository).save(user);
        verify(userMapper).fromUserCreateDto(createDto);
        verify(userMapper).toUserResponse(user);
    }

    @Test
    void testGetUserById_success() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.getOneById(1L);

        assertEquals("Britney", response.name());
        verify(userRepository).findUserById(1L);
    }

    @Test
    void testGetUserById_notFound() {
        when(userRepository.findUserById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getOneById(99L));
    }

    @Test
    void testUpdateUser_success() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(updateDto.email())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.updateUserById(1L, updateDto);

        assertNotNull(response);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUser_emailAlreadyExists() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setEmail(updateDto.email());

        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(updateDto.email())).thenReturn(Optional.of(anotherUser));

        assertThrows(EntityExistsException.class, () -> userService.updateUserById(1L, updateDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteUserById_success() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(paymentCardRepository.findAllByUser(user)).thenReturn(List.of());

        userService.deleteUserById(1L);

        verify(userRepository).findUserById(1L);
        verify(paymentCardRepository).findAllByUser(user);
        verify(userRepository).save(argThat(u -> !u.isActive() && u.getId().equals(1L)));
    }

    @Test
    void testDeleteUserById_notFound() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUserById(1L));
        verify(userRepository, never()).delete(any(User.class));
    }

}