package br.com.namaste.pontointeligente.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import br.com.namaste.pontointeligente.api.model.Lancamento;

public interface LancamentoService {
	
	/** 
	 * Retorna uma lista paginada de lançamentos de um determinado funcionario
	 * 
	 * @param funcionarioId
	 * @param pageRequest
	 * @return Page<Lancamento>
	 */
	
	Page<Lancamento> buscaPorFuncionarioId(Long funcionarioId, PageRequest pageRequest);
	
	/**
	 * Retorna um lancamento por ID
	 * 
	 * @param id
	 * @return Optional<lancamento>
	 */
	
	Optional<Lancamento> buscarPorId(Long id);
	
	/**
	 * Persiste um lançamento na base de dados
	 * 
	 * @param lancamento
	 * @return lancamento
	 */
	
	Lancamento persistir (Lancamento lancamento);
	
	
	
	/**
	 * Remove um lançamento da base de dados
	 * 
	 * @param id
	 */
	
	void remover(Long id);
}
