package practical.task.userservice.services;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practical.task.userservice.dtos.UserResponse;
import practical.task.userservice.dtos.requests.UserCreateDto;
import practical.task.userservice.dtos.requests.UserUpdateDto;
import practical.task.userservice.mappers.UserMapper;
import practical.task.userservice.models.User;
import practical.task.userservice.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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

    @Override
    public UserResponse updateUserById(Long id, UserUpdateDto userUpdateDto) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {

    }
}
