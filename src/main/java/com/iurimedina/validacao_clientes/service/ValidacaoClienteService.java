package com.iurimedina.validacao_clientes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iurimedina.validacao_clientes.repository.ClienteRepository;

@Service
public class ValidacaoClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	public boolean validarChaveAtiva(String chave, String cnpj) {
		return clienteRepository.findByChaveAndCnpjAndAtivoIsTrue(chave, cnpj).isPresent();

	}
}
