package com.iurimedina.validacao_clientes.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import com.iurimedina.validacao_clientes.model.Cliente;
import com.iurimedina.validacao_clientes.repository.ClienteRepository;
import com.iurimedina.validacao_clientes.service.ClienteService;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class ClienteCrontollerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	
	
	@Test
	void acessarComAutenticacaoInvalidaTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clientes")
				.with(httpBasic("abcd", "12345")))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	void acessarSemAutenticacaoTest() throws Exception {
	    mockMvc.perform(MockMvcRequestBuilders.get("/clientes"))
	        .andExpect(status().isUnauthorized());
	}
	
	@Test
	void listarClientesTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clientes")
				.with(httpBasic("iurimedina", "validacao-cliente")))
			.andExpect(status().isOk());
	}
	
	@Test
	void buscarClienteTest() throws Exception {	
		clienteRepository.deleteAll();
		
		ClienteRequestDto clienteDto = new ClienteRequestDto("Cliente Teste", "68276522000154", true);
		Cliente clienteSalvo = clienteService.salvar(clienteDto);
		System.out.println(clienteSalvo);

		mockMvc.perform(MockMvcRequestBuilders.get("/clientes/" + clienteSalvo.getId())
				.with(httpBasic("iurimedina", "validacao-cliente")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nome").value("Cliente Teste"))
			.andExpect(jsonPath("$.cnpj").value("68276522000154"))
			.andExpect(jsonPath("$.ativo").value(true));	
	}
		
	@Test
	void adicionarClienteTest() throws Exception {
		clienteRepository.deleteAll();
		
		ClienteRequestDto clienteDto = new ClienteRequestDto("Cliente Teste", "68276522000154", true);

		String json = objectMapper.writeValueAsString(clienteDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
				.with(httpBasic("iurimedina", "validacao-cliente"))
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.nome").value("Cliente Teste"))
			.andExpect(jsonPath("$.cnpj").value("68276522000154"))
			.andExpect(jsonPath("$.chave").exists())       
		    .andExpect(jsonPath("$.chave").isNotEmpty());  
		
	}
	
	@Test
	void atualizarClienteTest() throws Exception {
		clienteRepository.deleteAll();
		
		ClienteRequestDto clienteDto = new ClienteRequestDto("Cliente Teste", "68276522000154", true);
		Cliente clienteSalvo = clienteService.salvar(clienteDto);
		
		ClienteRequestDto clienteAtualizaDto = new ClienteRequestDto("Cliente atualizado", "68276522000154", true);
		
		String json = objectMapper.writeValueAsString(clienteAtualizaDto);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/clientes/" + clienteSalvo.getId())
				.with(httpBasic("iurimedina", "validacao-cliente"))
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(clienteSalvo.getId()))
			.andExpect(jsonPath("$.nome").value("Cliente atualizado"))
			.andExpect(jsonPath("$.cnpj").value("68276522000154"))
			.andExpect(jsonPath("$.chave").exists())       
		    .andExpect(jsonPath("$.chave").isNotEmpty());  
	
	}
	
	@Test
	void removerClienteTest() throws Exception {
		clienteRepository.deleteAll();
		
		ClienteRequestDto clienteDto = new ClienteRequestDto("Cliente Teste", "68276522000154", true);
		Cliente clienteSalvo = clienteService.salvar(clienteDto);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/clientes/" + clienteSalvo.getId())
				.with(httpBasic("iurimedina", "validacao-cliente"))
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
	
	@Test
	void adicionarClienteCnpjInvalidoTest() throws Exception {
	    ClienteRequestDto clienteDto = new ClienteRequestDto("Cliente Teste", "123", true);
	    String json = objectMapper.writeValueAsString(clienteDto);

	    mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
	            .with(httpBasic("iurimedina", "validacao-cliente"))
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	        .andExpect(status().isBadRequest());
	}
	
	@Test
	void adicionarClienteCnpjDuplicadoTest() throws Exception {
	    clienteRepository.deleteAll();

	    ClienteRequestDto clienteDto = new ClienteRequestDto("Cliente Teste", "68276522000154", true);
	    clienteService.salvar(clienteDto); 

	    String json = objectMapper.writeValueAsString(clienteDto);

	    mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
	            .with(httpBasic("iurimedina", "validacao-cliente"))
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	        .andExpect(status().isConflict()); 
	}

	@Test
	void buscarClienteInexistenteTest() throws Exception {
	    mockMvc.perform(MockMvcRequestBuilders.get("/clientes/9999")
	            .with(httpBasic("iurimedina", "validacao-cliente")))
	        .andExpect(status().isNotFound());
	}

	@Test
	void atualizarClienteInexistenteTest() throws Exception {
	    ClienteRequestDto clienteDto = new ClienteRequestDto("Novo Nome", "68276522000154", true);
	    String json = objectMapper.writeValueAsString(clienteDto);

	    mockMvc.perform(MockMvcRequestBuilders.put("/clientes/9999")
	            .with(httpBasic("iurimedina", "validacao-cliente"))
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	        .andExpect(status().isNotFound());
	}

	@Test
	void removerClienteInexistenteTest() throws Exception {
	    mockMvc.perform(MockMvcRequestBuilders.delete("/clientes/9999")
	            .with(httpBasic("iurimedina", "validacao-cliente"))
	            .with(csrf()))
	        .andExpect(status().isNotFound());
	}		
}
