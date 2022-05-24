package com.epc.minhasfinancas.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.epc.minhasfinancas.model.entity.Usuario;
import com.epc.minhasfinancas.model.repositories.UsuarioRepository;
import com.epc.minhasfinancas.services.UsuarioService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@Autowired
	UsuarioService service;
	
//	@SpyBean
//	UsuarioServiceImpl service;
	
//	@MockBean
	@Autowired
	UsuarioRepository repository;
	
//	@Before
//	public void setIp() {
////		repository = Mockito.mock(UsuarioRepository.class);
//		service = Mockito.spy(UsuarioServiceImpl.class);
//		
////		service = new UsuarioServiceImpl(repository);
//	}
	
	@Test
	public void deveAutenticarUsuarioComSucesso() {
		// cenário
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = new Usuario("Usuario", email, senha);
		service.salvarUsuario(usuario);
		
		// ação
		Usuario result = service.autenticar(email, senha);
		
		// verificação
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveValidarEmail() {
		
		repository.deleteAll();
		
		service.validarEmail("usuario@email.com");
		
	}
	
	@Test
	public void deveLancarErroAoValidarEmailAoExistirEmailCadastrado() {
		
		String senha = "senha";
		Usuario usuario = new Usuario("Usuario", "usuario@email.com", senha);
		repository.save(usuario);
		
		service.validarEmail("usuario@email.com");
//		repository.existsByEmail("usuario@email.com");
		
		
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		String senha = "senha";
		Usuario usuario = new Usuario("Usuario", "usuario@email.com", senha);
		repository.save(usuario);
		
//		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		service.autenticar("usuario@email.com", "sen");
	}
}
