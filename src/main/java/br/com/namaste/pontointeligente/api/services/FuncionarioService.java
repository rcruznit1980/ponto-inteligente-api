package br.com.namaste.pontointeligente.api.services;

import java.util.Optional;

import br.com.namaste.pontointeligente.api.model.Funcionario;

public interface FuncionarioService {
	
	/** Persiste um funcionário na base de dadis
	 * 
	 *  @param funcionario
	 *  @return Funcionario
	 *  
	 */
	
	Funcionario persistir(Funcionario funcionario);
	
	/** Busca e rertorna um funcionario dado um CPF
	 * 
	 * @param cpf
	 * @return Funcionario
	 *  
	 *  */
	
	Optional<Funcionario> buscarPorCpf(String cpf);
	
	/** Busca e rertorna um funcionario dado um email
	 * 
	 * @param email
	 * @return Funcionario
	 *  
	 *  */
	
	Optional<Funcionario> buscaPorEmail(String email);

	
	/** Busca e rertorna um funcionario por id
	 * 
	 * @param id
	 * @return Funcionario
	 *  
	 *  */
	
	Optional<Funcionario> buscarPorId(Long id);
	
}
