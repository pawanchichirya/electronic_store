package com.pc.electronic.store.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pc.electronic.store.entities.User;

public interface UserRepository extends JpaRepository<User,String> {

	Optional<User> findByEmail(String email);
	Optional<List<User>> findByNameContaining(String keyword);
}
