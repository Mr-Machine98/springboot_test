package org.juank.test.springboot.app.models;

import java.math.BigDecimal;

import org.juank.test.springboot.app.exceptions.DineroInsuficienteException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cuenta {
	
	private Long id;	
	private String persona;
	private BigDecimal saldo;
	
	public void debito(BigDecimal monto) {
		BigDecimal newSaldo = this.saldo.subtract(monto);
		if (newSaldo.compareTo(BigDecimal.ZERO) < 0) {
			throw new DineroInsuficienteException("Dinero insuficiente en la cuenta");
		}
		this.saldo = newSaldo;
	}
	
	public void credito(BigDecimal monto) {
		this.saldo = this.saldo.add(monto);
	}
}
