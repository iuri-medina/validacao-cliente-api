package com.iurimedina.validacao_clientes.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iurimedina.validacao_clientes.dto.ClienteRequestDto;
import com.iurimedina.validacao_clientes.model.Cliente;
import com.iurimedina.validacao_clientes.repository.ClienteRepository;
import com.iurimedina.validacao_clientes.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;


@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Gerenciamento de clientes")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	
	@Operation(summary = "Lista todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso.", 
	                 content = @Content(mediaType = "application/json", 
	                                    array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
	    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", 
	                 content = @Content(mediaType = "application/json"))
	})
	@GetMapping
	public List<Cliente> listarClientes() {
		return clienteRepository.findAll();
	}
	
	
	@Operation(summary = "Busca um cliente por ID", description = "Retorna os dados do cliente especificado pelo ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado.", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado.", 
                     content = @Content(mediaType = "application/json"))
    })
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarCliente(@PathVariable Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		
		if(cliente.isPresent()) {
			return ResponseEntity.ok(cliente);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	
	@Operation(summary = "Cadastra um novo cliente", description = "Cria um novo cliente e gera automaticamente uma chave única para ele.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso.", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "409", description = "Já existe um cliente com o mesmo CNPJ.", 
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "O CNPJ informado não é válido.", 
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", 
                     content = @Content(mediaType = "application/json"))
    })
	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody ClienteRequestDto clienteDto) {
		
		try {
			Cliente cliente = clienteService.salvar(clienteDto);
			return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
		} 
		catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
		
	
	@Operation(summary = "Atualiza um cliente", description = "Modifica os dados de um cliente existente pelo ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso.", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "409", description = "Já existe um cliente com o mesmo CNPJ.", 
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "O CNPJ informado não é válido.", 
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado.", 
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", 
                     content = @Content(mediaType = "application/json"))
    })
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ClienteRequestDto clienteDto) {
		try {
			Cliente cliente = new Cliente(clienteDto.getNome(), clienteDto.getCnpj(), clienteDto.isAtivo());
			cliente.setId(id);
			Cliente clienteSalvo = clienteService.atualizar(cliente);
			return ResponseEntity.ok(clienteSalvo);
		} 
		catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
	
	
	@Operation(summary = "Exclui um cliente", description = "Remove um cliente pelo ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso."),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado.", 
                     content = @Content(mediaType = "application/json"))
    })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		
		try {
			clienteService.excluir(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	
}
