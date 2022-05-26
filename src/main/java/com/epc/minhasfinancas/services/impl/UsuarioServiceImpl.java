package com.epc.minhasfinancas.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epc.minhasfinancas.exceptions.ErroAutenticacao;
import com.epc.minhasfinancas.exceptions.RegraNegocioException;
import com.epc.minhasfinancas.model.entity.Usuario;
import com.epc.minhasfinancas.model.repositories.UsuarioRepository;
import com.epc.minhasfinancas.services.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;
	
	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		
		System.out.println(email + "  -  " + senha);
		
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não cadastrado");
		}
		else {
			if(!usuario.get().getSenha().equals(senha)) {
				throw new ErroAutenticacao("Senha inválida");
			}
		}
		
		return usuario.get();

	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este e-mail");
		}
		
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		return repository.findById(id);
	}

}
