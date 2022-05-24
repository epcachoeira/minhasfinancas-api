package com.epc.minhasfinancas.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epc.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
