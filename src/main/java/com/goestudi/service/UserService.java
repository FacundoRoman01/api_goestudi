package com.goestudi.service;

import com.goestudi.dto.UserDTO;

import com.goestudi.model.User;

import com.goestudi.model.UserRole;

import com.goestudi.repository.UserRepository;

import com.goestudi.securiry.CustomUserDetails;

import com.goestudi.securiry.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;

@Service

public class UserService {

	@Autowired

	private UserRepository userRepository;

	@Autowired

	private PasswordEncoder passwordEncoder;

	@Autowired

	private JwtUtil jwtUtil;

// Registro de usuario

	public User register(UserDTO userDTO) {

		if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {

			throw new RuntimeException("El email ya está registrado");

		}

		User user = new User(

				userDTO.getUsername(),

				userDTO.getEmail(),

				passwordEncoder.encode(userDTO.getPassword()), // 🔒 Encriptamos la contraseña

				userDTO.getRole() != null ? userDTO.getRole() : UserRole.USER

		);

		return userRepository.save(user);

	}

// Login y generación de JWT

	public String login(String email, String password) {

		Optional<User> userOpt = userRepository.findByEmail(email);

		if (userOpt.isPresent()) {

			User user = userOpt.get();

			if (passwordEncoder.matches(password, user.getPassword())) {

				return jwtUtil.generateToken(new CustomUserDetails(user));

			}

		}

		throw new RuntimeException("Credenciales inválidas");

	}

// Obtener todos los usuarios

	public List<User> getAllUsers() {

		return userRepository.findAll();

	}

// Obtener usuario por ID

	public Optional<User> getUserById(Long id) {

		return userRepository.findById(id);

	}

// Actualizar usuario

	public User updateUser(Long id, UserDTO userDTO) {

		return userRepository.findById(id).map(user -> {

			user.setUsername(userDTO.getUsername());

			user.setEmail(userDTO.getEmail());

			if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {

				user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

			}

			user.setRole(userDTO.getRole());

			return userRepository.save(user);

		}).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

	}

// Eliminar usuario

	public void deleteUser(Long id) {

		if (!userRepository.existsById(id)) {

			throw new RuntimeException("Usuario no encontrado");

		}

		userRepository.deleteById(id);

	}

}