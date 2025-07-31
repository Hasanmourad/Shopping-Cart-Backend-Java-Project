package com.hasanmo.dreamshops.service.user;

import com.hasanmo.dreamshops.dto.UserDto;
import com.hasanmo.dreamshops.exceptions.AlreadyExistsException;
import com.hasanmo.dreamshops.exceptions.ResourceNotFoundExeption;
import com.hasanmo.dreamshops.model.User;
import com.hasanmo.dreamshops.repository.UserRepository;
import com.hasanmo.dreamshops.request.CreateUserRequest;
import com.hasanmo.dreamshops.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundExeption("user not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(user -> {
                    User userNew = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(request.getPassword());
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return  userRepository.save(userNew);
                }).orElseThrow(() -> new AlreadyExistsException("this email is in use by another user"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundExeption("User not found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).
                ifPresentOrElse(userRepository::delete, () -> {throw new ResourceNotFoundExeption("user not found");});

    }

    @Override
    public UserDto convertUserToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }
}
