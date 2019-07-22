package br.com.namaste.pontointeligente.api.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.namaste.pontointeligente.api.dtos.CadastroPJDto;
import br.com.namaste.pontointeligente.api.enums.PerfilEnum;
import br.com.namaste.pontointeligente.api.model.Empresa;
import br.com.namaste.pontointeligente.api.model.Funcionario;
import br.com.namaste.pontointeligente.api.response.Response;
import br.com.namaste.pontointeligente.api.service.impl.EmpresaServiceImpl;
import br.com.namaste.pontointeligente.api.services.EmpresaService;
import br.com.namaste.pontointeligente.api.services.FuncionarioService;
import br.com.namaste.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins="*")
public class CadastroPJController {
	
	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public CadastroPJController() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Cadastra uma PJ no sistema
	 * @param cadastroPJDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroPJDto>>
	 * @throws NoSuchAlgorithException
	 */
	
	@PostMapping
	public ResponseEntity<Response<CadastroPJDto>> cadastrar (@Valid @RequestBody CadastroPJDto cadastroPJDto,
			BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Cadastrando PJ: {}", cadastroPJDto.toString());
		Response<CadastroPJDto> response = new Response<CadastroPJDto>();
		
		validarDadosExistentes(cadastroPJDto, result);
		Empresa empresa = this.converterDtoParaEmpresa(cadastroPJDto);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPJDto, result);
		
		if (result.hasErrors()) {
			log.error("Erro validando dados do cadastro PJ: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterCadastroPJDto(funcionario));
		
		return ResponseEntity.ok(response);
		
	}
	
	/**
	 * Verifica se a empresa ou o funcionario já existem na base de dados
	 * @param cadastroPJDto
	 * @return result
	 */
	
	private void validarDadosExistentes(CadastroPJDto cadastroPJDto, BindingResult result) {
		this.empresaService.buscarPorCNPJ(cadastroPJDto.getCnpj())
				.ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente")));
		
		this.funcionarioService.buscarPorCpf(cadastroPJDto.getCpf())
		.ifPresent(func -> result.addError(new ObjectError("funcionario", "Funcionario já existente")));
		
		this.funcionarioService.buscaPorEmail(cadastroPJDto.getEmail())
		.ifPresent(func -> result.addError(new ObjectError("funcionario", "E-mail já existente")));
	}
	

	private Empresa converterDtoParaEmpresa(CadastroPJDto cadastroPJDto) {
		Empresa empresa = new Empresa();
		
		empresa.setCnpj(cadastroPJDto.getCnpj());
		empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());
		
		return empresa;
	}
	
	
	private Funcionario converterDtoParaFuncionario(CadastroPJDto cadastroPJDto, BindingResult result) throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		
		funcionario.setNome(cadastroPJDto.getNome());
		funcionario.setEmail(cadastroPJDto.getEmail());
		funcionario.setCpf(cadastroPJDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha()));
		
		return funcionario;
	}
	
	private CadastroPJDto converterCadastroPJDto(Funcionario funcionario)  {
		CadastroPJDto cadastroPJDto = new CadastroPJDto();
		
		cadastroPJDto.setId(funcionario.getId());
		cadastroPJDto.setNome(funcionario.getNome());
		cadastroPJDto.setEmail(funcionario.getEmail());
		cadastroPJDto.setCpf(funcionario.getCpf());
		cadastroPJDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		cadastroPJDto.setCnpj(funcionario.getEmpresa().getCnpj());
		
		return cadastroPJDto;
	}
	
}
