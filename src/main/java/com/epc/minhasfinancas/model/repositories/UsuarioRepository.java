package com.epc.minhasfinancas.model.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epc.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Boolean existsByEmail(String email);
	
	Optional<Usuario> findByEmail(String email);
	
	Optional<Usuario> findById(Long id);
	
}

