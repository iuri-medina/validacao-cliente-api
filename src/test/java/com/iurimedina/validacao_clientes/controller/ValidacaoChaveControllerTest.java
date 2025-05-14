package com.iurimedina.validacao_clientes.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iurimedina.validacao_clientes.dto.ClienteRequestDto;
import com.iurimedina.validacao_clientes.dto.ValidacaoRequestDto;
import com.iurimedina.validacao_clientes.model.Cliente;
import com.iurimedina.validacao_clientes.repository.ClienteRepository;
import com.iurimedina.validacao_clientes.service.ClienteService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ValidacaoChaveControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Test
	void validarChaveClienteTest() throws Exception {
		clienteRepository.deleteAll();
		
		ClienteRequestDto clienteDto = new ClienteRequestDto("Cliente Teste", "68276522000154", true);
		Cliente clienteSalvo = clienteService.salvar(clienteDto);
		
		ValidacaoRequestDto validacaoDto = new ValidacaoRequestDto(clienteSalvo.getChave(), "68276522000154");
		String json = objectMapper.writeValueAsString(validacaoDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/validacao-chave-cliente")
				.with(httpBasic("importador-oferta", "importador@oferta"))
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk());
	}
	
	@Test
	void chaveInvalidaTest() throws Exception {
		clienteRepository.deleteAll();
		
		ClienteRequestDto clienteDto = new ClienteRequestDto("Cliente Teste", "68276522000154", true);
		clienteService.salvar(clienteDto);
		
		ValidacaoRequestDto validacaoDto = new ValidacaoRequestDto("12345", "68276522000154");
		String json = objectMapper.writeValueAsString(validacaoDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/validacao-chave-cliente")
				.with(httpBasic("importador-oferta", "importador@oferta"))
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void chaveValidaClienteInativoTest() throws Exception {
	    clienteRepository.deleteAll();

	    ClienteRequestDto clienteDto = new ClienteRequestDto("Cliente Inativo", "68276522000154", false);
	    Cliente clienteSalvo = clienteService.salvar(clienteDto);

	    ValidacaoRequestDto validacaoDto = new ValidacaoRequestDto(clienteSalvo.getChave(), "68276522000154");
	    String json = objectMapper.writeValueAsString(validacaoDto);

	    mockMvc.perform(MockMvcRequestBuilders.post("/validacao-chave-cliente")
	            .with(httpBasic("importador-oferta", "importador@oferta"))
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	        .andExpect(status().isBadRequest());
	}
	
	@Test
	void cnpjValidoChaveErradaTest() throws Exception {
	    clienteRepository.deleteAll();

	    ClienteRequestDto clienteDto = new ClienteRequestDto("Cliente Teste", "68276522000154", true);
	    clienteService.salvar(clienteDto);

	    ValidacaoRequestDto validacaoDto = new ValidacaoRequestDto("chave-inexistente", "68276522000154");
	    String json = objectMapper.writeValueAsString(validacaoDto);

	    mockMvc.perform(MockMvcRequestBuilders.post("/validacao-chave-cliente")
	            .with(httpBasic("importador-oferta", "importador@oferta"))
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	void requisicaoComCamposNulosTest() throws Exception {
	    ValidacaoRequestDto validacaoDto = new ValidacaoRequestDto(null, null);
	    String json = objectMapper.writeValueAsString(validacaoDto);

	    mockMvc.perform(MockMvcRequestBuilders.post("/validacao-chave-cliente")
	            .with(httpBasic("importador-oferta", "importador@oferta"))
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	        .andExpect(status().isBadRequest());
	}

	@Test
	void requisicaoSemAutenticacaoTest() throws Exception {
		clienteRepository.deleteAll();
		
		ClienteRequestDto clienteDto = new ClienteRequestDto("Cliente Teste", "68276522000154", true);
		Cliente clienteSalvo = clienteService.salvar(clienteDto);
		
		ValidacaoRequestDto validacaoDto = new ValidacaoRequestDto(clienteSalvo.getChave(), "68276522000154");
		String json = objectMapper.writeValueAsString(validacaoDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/validacao-chave-cliente")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isUnauthorized());
	
	}
	
	@Test
	void requisicaoComAutenticacaoErradaTest() throws Exception {
clienteRepository.deleteAll();
		
		ClienteRequestDto clienteDto = new ClienteRequestDto("Cliente Teste", "68276522000154", true);
		Cliente clienteSalvo = clienteService.salvar(clienteDto);
		
		ValidacaoRequestDto validacaoDto = new ValidacaoRequestDto(clienteSalvo.getChave(), "68276522000154");
		String json = objectMapper.writeValueAsString(validacaoDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/validacao-chave-cliente")
				.with(httpBasic("abcd", "12345"))
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isUnauthorized());
	
	}
}
