package practical.task.userservice.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practical.task.userservice.dto.response.UserResponse;
import practical.task.userservice.dto.request.userDto.UserCreateDto;
import practical.task.userservice.dto.request.userDto.UserUpdateDto;
import practical.task.userservice.mapper.UserMapper;
import practical.task.userservice.model.PaymentCard;
import practical.task.userservice.model.User;
import practical.task.userservice.repository.PaymentCardRepository;
import practical.task.userservice.repository.UserRepository;
import practical.task.userservice.util.CreateEntityHelper;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PaymentCardRepository paymentCardRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PaymentCardRepository paymentCardRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.paymentCardRepository = paymentCardRepository;
    }

    @Override
    public Page<UserResponse> getAll(Specification<User> spec, Pageable pageable) {
        return userRepository
                .findAll(spec, pageable)
                .map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse getOneById(Long id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toUserResponse(user);
    }

    @Transactional
    @Override
    public UserResponse createUser(UserCreateDto userCreateDto) {
        User user = userMapper.fromUserCreateDto(userCreateDto);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new EntityExistsException("User already exists");
        }

        return userMapper.toUserResponse(user);
    }

    @Transactional
    @Override
    public UserResponse updateUserById(Long id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        String newEmail = userUpdateDto.email();
        if (newEmail != null && !newEmail.equals(user.getEmail())) {
            userRepository.findByEmail(newEmail)
                    .ifPresent(uf -> {throw new EntityExistsException("Email has already been used");});
        }

        user.setName(CreateEntityHelper.resolveIfNotNull(userUpdateDto.name(), user.getName()));
        user.setSurname(CreateEntityHelper.resolveIfNotNull(userUpdateDto.surname(), user.getSurname()));
        user.setBirthDate(CreateEntityHelper.resolveIfNotNull(userUpdateDto.birthDate(), user.getBirthDate()));
        user.setEmail(CreateEntityHelper.resolveIfNotNull(userUpdateDto.email(), user.getEmail()));

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        user.setActive(false);
        List<PaymentCard> paymentCards = paymentCardRepository.findAllByUser(user);
        paymentCards.forEach(paymentCard -> {paymentCard.setActive(false);});

        userRepository.save(user);

        /*
        userRepository.delete(user);
         */
    }
}
