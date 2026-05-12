package org.juank.test.springboot.app;

import java.util.Optional;

import org.juank.test.springboot.app.models.Banco;
import org.juank.test.springboot.app.models.Cuenta;

public class Datos {

	public static Optional<Cuenta> getCuenta001() {
		return Optional.of(new Cuenta(1L, "Juan",  new java.math.BigDecimal("1000")));
	}
	
	public static Optional<Cuenta> getCuenta002() {
		return Optional.of(new Cuenta(2L, "Andres",  new java.math.BigDecimal("2000")));
	}
	
	public static Optional<Banco> getBanco() {
		return Optional.of(new Banco(1L, "Banco BBVA", 0));
	}
}
