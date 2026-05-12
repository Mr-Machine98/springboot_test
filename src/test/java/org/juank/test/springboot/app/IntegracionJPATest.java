package org.juank.test.springboot.app;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.juank.test.springboot.app.models.Cuenta;
import org.juank.test.springboot.app.repositories.ICuentaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * La anotación @DataJpaTest se utiliza para configurar un entorno de prueba específico para pruebas de
 * JPA (Java Persistence API) en Spring Boot. Esta anotación se encarga de configurar automáticamente
 * una base de datos en memoria, escanear las entidades JPA y configurar los repositorios de Spring Data JPA 
 * para que puedan ser utilizados en las pruebas.
 * Al usar @DataJpaTest, se pueden realizar pruebas de integración que involucren la interacción con 
 * la base de datos, como la creación, lectura, actualización y eliminación de entidades.
 * Además, esta anotación también proporciona soporte para transacciones, lo que permite 
 * que cada prueba se ejecute dentro de una transacción que se revertirá al finalizar la prueba, asegurando
 *  así un estado limpio para cada prueba.
 */
@DataJpaTest
public class IntegracionJPATest {

	@Autowired
	ICuentaRepository cuentaRepository;
	
	@Test
	@DisplayName(">>> Test para buscar una cuenta por su ID utilizando el repositorio de cuentas")
	void testFindById() {
		Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
		assertTrue(cuenta.isPresent());
		assertEquals("Juan", cuenta.orElseThrow().getPersona());
	}
	
	@Test
	@DisplayName(">>> Test para buscar una cuenta por el nombre de la persona asociada a la cuenta utilizando el método personalizado findByPersona del repositorio de cuentas")
	void testFindByPersona() {
		Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Juan");
		assertTrue(cuenta.isPresent());
		assertEquals("Juan", cuenta.orElseThrow().getPersona());
		assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
	}
	
	@Test
	@DisplayName(">>> Test para buscar una cuenta por el nombre de la persona asociada a la cuenta utilizando el método personalizado findByPersona del repositorio de cuentas, esperando que se lance una excepción NoSuchElementException si no se encuentra la cuenta")
	void testFindByPersonaThrowException() {
		Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Rod");
		assertThrows(NoSuchElementException.class, () -> {
			cuenta.orElseThrow().getPersona();
		});
		assertTrue(cuenta.isPresent(),
				() -> "La cuenta con la persona 'Rod' no se encontró en la base de datos");
	}
	
	@Test
	@DisplayName(">>> Test para guardar una cuenta utilizando el repositorio de cuentas y luego verificar que se ha guardado correctamente")
	void testSave() {
		
		//Given
		Cuenta cuenta = new Cuenta(null, "pepe", new BigDecimal("3000"));

		//When
		Cuenta cuentaBd = cuentaRepository.save(cuenta);
		
		//Then
		assertTrue(cuentaBd.getId() != null);
		assertEquals("pepe", cuentaBd.getPersona());
		assertEquals("3000", cuentaBd.getSaldo().toPlainString());
		
		
	}
	
	@Test
	@DisplayName(">>> Test para actualizar el saldo de una cuenta utilizando el repositorio de cuentas y luego verificar que se ha actualizado correctamente")
	void testUpdate() {
		
		//Given
		Cuenta cuenta = new Cuenta(null, "pepe", new BigDecimal("3000"));

		//When
		Cuenta cuentaBd = cuentaRepository.save(cuenta);
		
		//Then
		assertTrue(cuentaBd.getId() != null);
		assertEquals("pepe", cuentaBd.getPersona());
		assertEquals("3000", cuentaBd.getSaldo().toPlainString());
		
		cuentaBd.setSaldo(new BigDecimal("3800"));
		Cuenta cuentaUpdate = cuentaRepository.save(cuentaBd);
		
		assertEquals("3800", cuentaUpdate.getSaldo().toPlainString());
		
		
	}
	
	@Test
	@DisplayName(">>> Test para eliminar una cuenta utilizando el repositorio de cuentas y luego verificar que se ha eliminado correctamente")
	void testDelete() {
		Cuenta cuenta = cuentaRepository.findByPersona("Andres").orElseThrow();
		assertEquals("Andres", cuenta.getPersona());
		cuentaRepository.delete(cuenta);
		Optional<Cuenta> cuentaOptional = cuentaRepository.findByPersona("Andres");
		assertFalse(cuentaOptional.isPresent());
		assertEquals(1, cuentaRepository.findAll().size());
	}
	
}
