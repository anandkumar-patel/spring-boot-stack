package anand.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import anand.entity.User;
import anand.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Cacheable(value = "users", key = "#userId")
	public User getUserById(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}

	@CachePut(value = "users", key = "#user.id")
	public User updateUser(Long id, User user) {
		Optional<User> found = userRepository.findById(id);
		if (found.isPresent()) {
			User existingUser = found.get();
			existingUser.setFirstName(user.getFirstName());
			existingUser.setLastName(user.getLastName());
			existingUser.setEmail(user.getEmail());
			return userRepository.save(existingUser);
		}
		return null;
	}

	@CacheEvict(value = "users", key = "#id")
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}
