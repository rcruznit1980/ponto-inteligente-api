package br.com.namaste.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.namaste.pontointeligente.api.model.Lancamento;
import br.com.namaste.pontointeligente.api.repositories.LancamentoRepository;
import br.com.namaste.pontointeligente.api.services.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Override
	public Page<Lancamento> buscaPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
		log.info("Buscando lancamento para o funcionario ID {}", funcionarioId);
		return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
	}

	@Cacheable("lancamentoPorId")
	public Optional<Lancamento> buscarPorId(Long id) {
		log.info("Buscando lancamento pelo ID {}", id);
		return this.lancamentoRepository.findById(id);
	}

	@CachePut("lancamentoPorId")
	public Lancamento persistir(Lancamento lancamento) {
		log.info("Persistindo o lancamento {}", lancamento );
		return this.lancamentoRepository.save(lancamento);
	}

	public void remover(Long id) {
		log.info("Removendo o lancamento id {}", id );
		this.lancamentoRepository.deleteById(id);
	}

}
