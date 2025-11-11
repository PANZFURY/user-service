package practical.task.userservice.service;

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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

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

}