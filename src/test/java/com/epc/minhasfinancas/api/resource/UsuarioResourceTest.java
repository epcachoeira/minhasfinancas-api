package com.epc.minhasfinancas.api.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.epc.minhasfinancas.api.dto.UsuarioDTO;
import com.epc.minhasfinancas.api.resources.UsuarioResource;
import com.epc.minhasfinancas.exceptions.ErroAutenticacao;
import com.epc.minhasfinancas.exceptions.RegraNegocioException;
import com.epc.minhasfinancas.model.entity.Usuario;
import com.epc.minhasfinancas.services.LancamentoService;
import com.epc.minhasfinancas.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc
public class UsuarioResourceTest {

	static final String API = "/api/usuarios";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UsuarioService service;
	
	@MockBean
	LancamentoService lancamentoService;
	
	@Test
	public void deveAutenticarUmUsuario() throws Exception {
		//cenario
		UsuarioDTO dto = new UsuarioDTO("Jorge", "jorge@gmail.com", "123456");
		Usuario usuario = new Usuario("Jorge", "jorge@gmail.com", "123456");
		
		Mockito.when(service.autenticar("jorge@gmail.com", "123456")).thenReturn(usuario);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execução e verificação
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API.concat("/autenticar"))
													.accept(MediaType.APPLICATION_JSON)
													.contentType(MediaType.APPLICATION_JSON)
													.content(json);

		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()))
			;
		
	}
	
	@Test
	public void deveFalharAoAutenticarUmUsuario() throws Exception {
		//cenario
		UsuarioDTO dto = new UsuarioDTO("Jorge", "jorge@gmail.com", "123456");
		
		Mockito.when(service.autenticar("jorge@gmail.com", "123456")).thenThrow(ErroAutenticacao.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execução e verificação
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API.concat("/autenticar"))
													.accept(MediaType.APPLICATION_JSON)
													.contentType(MediaType.APPLICATION_JSON)
													.content(json);

		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			;
		
	}
	
	@Test
	public void deveCriarUmNovoUsuario() throws Exception {
		//cenario
		UsuarioDTO dto = new UsuarioDTO("Jorge", "jorge@gmail.com", "123456");
		Usuario usuario = new Usuario("Jorge", "jorge@gmail.com", "123456");
		
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execução e verificação
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API)
													.accept(MediaType.APPLICATION_JSON)
													.contentType(MediaType.APPLICATION_JSON)
													.content(json);

		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()))
			;
		
	}
	
	@Test
	public void deveFalharAoCriarUmNovoUsuario() throws Exception {
		//cenario
		UsuarioDTO dto = new UsuarioDTO("Jorge", "jorge@gmail.com", "123456");
		
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execução e verificação
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API)
													.accept(MediaType.APPLICATION_JSON)
													.contentType(MediaType.APPLICATION_JSON)
													.content(json);

		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			;
		
	}
}
