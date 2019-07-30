package br.com.namaste.pontointeligente.api.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.namaste.pontointeligente.api.enums.PerfilEnum;
import br.com.namaste.pontointeligente.api.model.Funcionario;


public class JwtUserFactory {

	public JwtUserFactory() {

	}

	/**
	 * Converte e gera um JwtUser com base nos dados de um funcionário.
	 * 
	 * @param funcionario
	 * @return JwtUser
	 */
	public static JwtUser create(Funcionario funcionario) {
		return new JwtUser(funcionario.getId(), funcionario.getEmail(), funcionario.getSenha(),
				mapToGrantedAuthorities(funcionario.getPerfil()));

	}
	
	/**
	 * Converte o perfil do usuário para o formato utilizado pelo Spring Security.
	 * 
	 * @param perfil
	 * @return
	 */
	private static List<GrantedAuthority> mapToGrantedAuthorities(PerfilEnum perfil) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(perfil.toString()));

		return authorities;
	}

}