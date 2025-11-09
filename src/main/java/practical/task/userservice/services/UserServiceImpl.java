package practical.task.userservice.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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

    @Override
    public UserResponse createUser(UserCreateDto userCreateDto) {
        return null;
    }

    @Override
    public UserResponse updateUserById(Long id, UserUpdateDto userUpdateDto) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {

    }
}
