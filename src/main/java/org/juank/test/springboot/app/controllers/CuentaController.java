package org.juank.test.springboot.app.controllers;

import java.time.LocalDate;
import java.util.Map;

import org.juank.test.springboot.app.models.Cuenta;
import org.juank.test.springboot.app.models.TransaccionDTO;
import org.juank.test.springboot.app.services.ICuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {
	
	@Autowired
	private ICuentaService cuentaService;
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Cuenta details(@PathVariable Long id) {
		return this.cuentaService.findCuentaById(id);
	}
	
	@PostMapping("/transferir")
	public ResponseEntity<?> postMethodName(@RequestBody TransaccionDTO dto) {
		this.cuentaService.transferir(
			dto.getCuentaOrigenId(),
			dto.getCuentaDestinoId(),
			dto.getMonto(),
			dto.getBancoId()
		);
		return ResponseEntity.ok(Map.of(
			"date", LocalDate.now(),
			"status", "OK",
			"message", "Transferencia realizada con éxito",
			"transaccion", dto
		));
	}
	

}
