package com.iurimedina.validacao_clientes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iurimedina.validacao_clientes.dto.ValidacaoRequestDto;
import com.iurimedina.validacao_clientes.repository.ClienteRepository;
import com.iurimedina.validacao_clientes.service.ValidacaoClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/validacao-chave-cliente")
@Tag(name = "Validacao Chave", description = "Verifica se a chave de um cliente especifico é valida e se o CNPJ é o mesmo. ")
public class ValidacaoChaveController {
	
	@Autowired
	private ValidacaoClienteService validacaoClienteService;
	
	@Autowired
	ClienteRepository clienteRepository;

	@Operation(summary = "Valida a chave do cliente", description = "Verifica se a chave fornecida é válida e pertence ao CNPJ informado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Chave validada com sucesso.", 
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Chave do cliente inválida ou inexistente.", 
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", 
                     content = @Content(mediaType = "application/json"))
    })
	@PostMapping
	public ResponseEntity<?> validarChaveCliente(@RequestBody ValidacaoRequestDto request) {
		
		boolean isChaveAtiva = validacaoClienteService.validarChaveAtiva(request.getChave(), request.getCnpj());
		
		if(isChaveAtiva) {
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chave do cliente invalida ou inexistente.");
	}
}
