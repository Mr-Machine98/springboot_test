package org.juank.test.springboot.app;

import org.juank.test.springboot.app.models.Banco;
import org.juank.test.springboot.app.models.Cuenta;

public class Datos {

	public static Cuenta getCuenta001() {
		return new Cuenta(1L, "Juan",  new java.math.BigDecimal("1000"));
	}
	
	public static Cuenta getCuenta002() {
		return new Cuenta(2L, "Andres",  new java.math.BigDecimal("2000"));
	}
	
	public static Banco getBanco() {
		return new Banco(1L, "Banco BBVA", 0);
	}
}
