package practical.task.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practical.task.userservice.dto.request.userDto.UserCreateDto;
import practical.task.userservice.mapper.UserMapper;
import practical.task.userservice.model.User;
import practical.task.userservice.repository.PaymentCardRepository;
import practical.task.userservice.repository.UserRepository;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PaymentCardRepository paymentCardRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserCreateDto userCreateDto;
    private User user;

    @BeforeEach
    void setUp() {
        userCreateDto = new UserCreateDto("Britney", "Spears", LocalDate.of(1981, 12, 2), "britney@gmail.com");
        user = new User();
        user.setName("Britney");
        user.setSurname("Spears");
        user.setBirthDate(LocalDate.of(1981, 12, 2));
        user.setEmail("britney@gmail.com");
    }


}