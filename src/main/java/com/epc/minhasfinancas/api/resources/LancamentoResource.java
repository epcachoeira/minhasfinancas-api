package com.epc.minhasfinancas.api.resources;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epc.minhasfinancas.api.dto.AtualizaStatusDTO;
import com.epc.minhasfinancas.api.dto.LancamentoDTO;
import com.epc.minhasfinancas.exceptions.RegraNegocioException;
import com.epc.minhasfinancas.model.entity.Lancamento;
import com.epc.minhasfinancas.model.entity.Usuario;
import com.epc.minhasfinancas.model.enums.StatusLancamento;
import com.epc.minhasfinancas.model.enums.TipoLancamento;
import com.epc.minhasfinancas.services.LancamentoService;
import com.epc.minhasfinancas.services.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {

	private LancamentoService service;
	private UsuarioService usuarioService;

	public LancamentoResource(LancamentoService service, UsuarioService usuarioService) {
		this.service = service;
		this.usuarioService = usuarioService;
	}
	
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam(value = "tipo", required = false) TipoLancamento tipo,
			@RequestParam("usuario") Long idUsuario
			) {
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		lancamentoFiltro.setTipo(tipo);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Usuário não encontrado para o ID informado");
		} else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		
		return ResponseEntity.ok(lancamentos);
	}
	
	@GetMapping("{id}")
	public Optional<Object> obterLancamento(@PathVariable("id") Long id) {
		return Optional.ofNullable(service.obterPorId(id)
				.map(lancamento -> new ResponseEntity(converterParaDTO(lancamento), HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND)));
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento entidade = converter(dto);
			entidade = service.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
		return service.obterPorId(id).map( entity -> {
			try {
				Lancamento lancto = converter(dto);
				lancto.setId(entity.getId());
				service.atualizar(lancto);
				return ResponseEntity.ok(lancto);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(
			() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@PutMapping("{id}/atualliza-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id,
			@RequestBody AtualizaStatusDTO dto) {
		return service.obterPorId(id).map( entity -> {
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
			if(statusSelecionado == null) {
				return ResponseEntity.badRequest().body("Não foi possível atualizar. Status inválido");
			}
			try {
				entity.setStatus(statusSelecionado);
				service.atualizar(entity);
				return ResponseEntity.ok(entity);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(
			() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		
		service.obterPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(
			() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
		
		return null;
	}
	
	private LancamentoDTO converterParaDTO(Lancamento lancamento) {
		LancamentoDTO lancto = new LancamentoDTO();
		lancto.setId(lancamento.getId());
		lancto.setDescricao(lancamento.getDescricao());
		lancto.setMes(lancamento.getMes());
		lancto.setAno(lancamento.getAno());
		lancto.setValor(lancamento.getValor());
		lancto.setTipo(lancamento.getTipo().name());
		lancto.setStatus(lancamento.getStatus().name());
		lancto.setUsuario(lancamento.getUsuario().getId());
		
		return lancto;
	}
	
	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancto = new Lancamento();
		
		lancto.setId(dto.getId());
		lancto.setDescricao(dto.getDescricao());
		lancto.setMes(dto.getMes());
		lancto.setAno(dto.getAno());
		lancto.setValor(dto.getValor());
		
		Usuario usuario = usuarioService
		   .obterPorId(dto.getUsuario())
		   .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o ID informado"));
		lancto.setUsuario(usuario);
		
		if(dto.getTipo() != null) {
			lancto.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}
		
		if(dto.getStatus() != null) {
			lancto.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		} 
		
		return lancto;
	}
}
