package com.iurimedina.validacao_clientes.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.iurimedina.validacao_clientes.dto.ClienteRequestDto;
import com.iurimedina.validacao_clientes.model.Cliente;
import com.iurimedina.validacao_clientes.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente salvar(ClienteRequestDto clienteDto) {
		
		String cnpjTratado = tratarCnpj(clienteDto.getCnpj());
		
		if(!validarCnpj(cnpjTratado)) {
			throw new IllegalArgumentException("O CNPJ informado não é valido.");
		}
		
		boolean clienteExistente = clienteRepository.existsByCnpj(cnpjTratado);
		
		if(clienteExistente) {
			throw new DataIntegrityViolationException("Já existe um cliente com o mesmo CNPJ: " + cnpjTratado);
		}
		
		Cliente cliente = new Cliente(clienteDto.getNome(), cnpjTratado, clienteDto.getAtivo());

		String chave = UUID.randomUUID().toString(); //gera a chave aleatoriamente para o cliente
		cliente.setChave(chave);

		return clienteRepository.save(cliente);
	}
	
	public Cliente atualizar(Cliente cliente) {
		Optional<Cliente> clienteAtual = clienteRepository.findById(cliente.getId());
		
		if (clienteAtual.isEmpty()) {
	        throw new EntityNotFoundException("Cliente não encontrado.");
	    }
		
		String novoCnpj = tratarCnpj(cliente.getCnpj());
		
		if(!validarCnpj(novoCnpj)) {
			throw new IllegalArgumentException("O CNPJ informado não é valido.");
		}
		
		if(!clienteAtual.get().getCnpj().equals(novoCnpj) && clienteRepository.existsByCnpj(novoCnpj)) {
			throw new DataIntegrityViolationException("CNPJ já cadastrado em outro cliente.");
		}
		
		BeanUtils.copyProperties(cliente, clienteAtual.get(), "id", "chave");
			
		Cliente clienteSalvo = clienteRepository.save(clienteAtual.get());
		return clienteSalvo;
		
	}
	
	public void excluir(Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		
		if(cliente.isEmpty()) {
			throw new EntityNotFoundException("Entidade de id "+ id + " nao encontrada");
		}
		
		clienteRepository.deleteById(id);
	}
	
	
	public String tratarCnpj(String cnpj) {
		cnpj = cnpj.replaceAll("[^0-9]", "");
		return cnpj;
	}

	
	public boolean validarCnpj(String cnpj) {
	    cnpj = cnpj.replaceAll("[^0-9]", "");
	    if (cnpj.length() != 14) return false;

	    int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
	    int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

	    try {
	        int soma = 0;
	        for (int i = 0; i < 12; i++) {
	            soma += (cnpj.charAt(i) - '0') * peso1[i];
	        }
	        int digito1 = (soma % 11 < 2) ? 0 : (11 - soma % 11);

	        soma = 0;
	        for (int i = 0; i < 13; i++) {
	            soma += (cnpj.charAt(i) - '0') * peso2[i];
	        }
	        int digito2 = (soma % 11 < 2) ? 0 : (11 - soma % 11);

	        return cnpj.endsWith("" + digito1 + digito2);
	    } catch (Exception e) {
	        return false;
	    }
	}
}
