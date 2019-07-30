package br.com.namaste.pontointeligente.api.services;

import java.util.Optional;

import br.com.namaste.pontointeligente.api.model.Empresa;

public interface EmpresaService {

	
	//Retorna uma empresa a partir de um CNPJ 
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	//Cadastra uma nova empresa
	Empresa persistir (Empresa empresa);
}
