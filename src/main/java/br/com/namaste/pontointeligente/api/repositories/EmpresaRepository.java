package br.com.namaste.pontointeligente.api.repositories;

	

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.namaste.pontointeligente.api.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
	
	@Transactional(readOnly=true)
	Empresa findByCnpj(String cnpj);

}