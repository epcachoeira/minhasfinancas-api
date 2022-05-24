package com.epc.minhasfinancas.model.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.epc.minhasfinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)

public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//Cenário
		Usuario usuario = new Usuario("Usuario", "usuario@email.com", "12345678");
	  	entityManager.persist(usuario);
		// Ação/Execução
		boolean result = repository.existsByEmail("usuario@email.com");
		// Verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveDarFalsoSeEmailNaoExiste() {
		
		// Ação/Execução
		boolean result = repository.existsByEmail("usuario@email.com");
		// Verificação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void devePersistirUmUsuarioNaBase() {
		Usuario usuario = new Usuario("Usuario", "usuario@email.com", "12345678");
		
		Usuario usuarioSalvo = repository.save(usuario);
		
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUsuarioPorEmail() {
		Usuario usuario = new Usuario("Usuario", "usuario@email.com", "12345678");
		
		entityManager.persist(usuario);
		
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();
	}

	@Test
	public void deveDarErroAoBuscarUsuarioPorEmail() {
//		Usuario usuario = new Usuario("Usuario", "usuario@email.com", "12345678");
		
//		entityManager.persist(usuario);
		
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
	}
}
