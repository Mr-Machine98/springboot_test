package org.juank.test.springboot.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.juank.test.springboot.app.exceptions.DineroInsuficienteException;
import org.juank.test.springboot.app.models.Cuenta;
import org.juank.test.springboot.app.repositories.IBancoRepository;
import org.juank.test.springboot.app.repositories.ICuentaRepository;
import org.juank.test.springboot.app.services.ICuentaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class SpringbootTestApplicationTests {
	
	@MockitoBean
	ICuentaRepository cuentaRepository;
	
	@MockitoBean
	IBancoRepository bancoRepository;
	
	@Autowired
	ICuentaService cuentaService;

	/* Comentamos este bloque ya que podemos usar anotaciones para inicializar los mocks 
	@BeforeEach
	void setUp() {
		cuentaRepository = mock(ICuentaRepository.class);
		bancoRepository = mock(IBancoRepository.class);
		cuentaService = new CuentaServiceImpl(cuentaRepository, bancoRepository);
	}
	*/
	
	@Test
	@DisplayName(">>> Test para revisar el saldo de las cuentas antes y después de una transferencia")
	void revisarSaldo() {
		// Given
		when(cuentaRepository.findById(1L)).thenReturn(Datos.getCuenta001()); // Configuramos el mock para que devuelva una cuenta específica cuando se llame al método findById con el ID 1L
		when(cuentaRepository.findById(2L)).thenReturn(Datos.getCuenta002()); // Configuramos el mock para que devuelva otra cuenta específica cuando se llame al método findById con el ID 2L
		when(bancoRepository.findById(1L)).thenReturn(Datos.getBanco()); // Configuramos el mock para que devuelva un banco específico cuando se llame al método findById con el ID 1L
		
		BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L); // Llamamos al método revisarSaldo del servicio de cuentas para obtener el saldo de la cuenta con ID 1L
		BigDecimal saldoDestino = cuentaService.revisarSaldo(2L); // Llamamos al método revisarSaldo del servicio de cuentas para obtener el saldo de la cuenta con ID 2L
		
		assertEquals("1000", saldoOrigen.toPlainString()); // Verificamos que el saldo de la cuenta de origen sea igual a 1000
		assertEquals("2000", saldoDestino.toPlainString()); // Verificamos que el saldo de la cuenta de destino sea igual a 2000
	
		cuentaService.transferir(1L, 2L, new BigDecimal("100"), 1L); // Llamamos al método transferir del servicio de cuentas para transferir 100 unidades de la cuenta con ID 1L a la cuenta con ID 2L, utilizando el banco con ID 1L
	
		
		saldoOrigen = cuentaService.revisarSaldo(1L); // Llamamos al método revisarSaldo del servicio de cuentas para obtener el saldo de la cuenta con ID 1L
		saldoDestino = cuentaService.revisarSaldo(2L); // Llamamos al método revisarSaldo del servicio de cuentas para obtener el saldo de la cuenta con ID 2L
	
		assertEquals("900", saldoOrigen.toPlainString()); // Verificamos que el saldo de la cuenta de origen sea igual a 900 después de la transferencia
		assertEquals("2100", saldoDestino.toPlainString()); // Verificamos que el saldo de la cuenta de destino sea igual a 2100 después de la transferencia
		
		int total = cuentaService.revisarTotalTransferencias(1L);
		assertEquals(1, total); // Verificamos que el total de transferencias realizadas por el banco con ID 1L sea igual a 1 después de la transferencia
		
		verify(cuentaRepository, times(3)).findById(1L); // Verificamos que el método findById del repositorio de cuentas haya sido llamado 3 veces con el ID 1L
		verify(cuentaRepository, times(3)).findById(2L); // Verificamos que el método findById del repositorio de cuentas haya sido llamado 3 veces con el ID 2L
		verify(cuentaRepository, times(2)).save(any(Cuenta.class)); // Verificamos que el método update del repositorio de cuentas haya sido llamado 2 veces con cualquier objeto de tipo Cuenta 
	
		verify(bancoRepository, times(2)).findById(1L); // Verificamos que el método findById del repositorio de bancos haya sido llamado 2 vez con el ID 1L
		verify(bancoRepository, times(1)).save(any()); // Verificamos que el método update del repositorio de bancos haya sido llamado 1 vez con cualquier objeto
	}
	
	@Test
	@DisplayName(">>> Test para revisar el saldo de las cuentas antes y después de una transferencia con dinero insuficiente")
	void revisarSaldoDineroInsuficiente() {
		// Given
		when(cuentaRepository.findById(1L)).thenReturn(Datos.getCuenta001()); // Configuramos el mock para que devuelva una cuenta específica cuando se llame al método findById con el ID 1L
		when(cuentaRepository.findById(2L)).thenReturn(Datos.getCuenta002()); // Configuramos el mock para que devuelva otra cuenta específica cuando se llame al método findById con el ID 2L
		when(bancoRepository.findById(1L)).thenReturn(Datos.getBanco()); // Configuramos el mock para que devuelva un banco específico cuando se llame al método findById con el ID 1L
		
		BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L); // Llamamos al método revisarSaldo del servicio de cuentas para obtener el saldo de la cuenta con ID 1L
		BigDecimal saldoDestino = cuentaService.revisarSaldo(2L); // Llamamos al método revisarSaldo del servicio de cuentas para obtener el saldo de la cuenta con ID 2L
		
		assertEquals("1000", saldoOrigen.toPlainString()); // Verificamos que el saldo de la cuenta de origen sea igual a 1000
		assertEquals("2000", saldoDestino.toPlainString()); // Verificamos que el saldo de la cuenta de destino sea igual a 2000
	
		
		assertThrows(DineroInsuficienteException.class, () -> {
			cuentaService.transferir(1L, 2L, new BigDecimal("1200"), 1L); // Intentamos transferir un monto mayor al saldo disponible en la cuenta de origen para verificar que se lance la excepción DineroInsuficienteException
		});
		
		verify(cuentaRepository, times(2)).findById(1L); // Verificamos que el método findById del repositorio de cuentas haya sido llamado 2 veces con el ID 1L
		verify(cuentaRepository, never()).findAll();
	}
	
	@Test
	@DisplayName(">>> Test para verificar que el método findCuentaById devuelve la misma instancia de cuenta para el mismo ID")
	void test() {
		when(cuentaRepository.findById(1L)).thenReturn(Datos.getCuenta001()); // Configuramos el mock para que devuelva una cuenta específica cuando se llame al método findById con el ID 1L
		Cuenta cuenta1 = cuentaService.findCuentaById(1L); // Llamamos al método findCuentaById del servicio de cuentas para obtener la cuenta con ID 1L
		Cuenta cuenta2 = cuentaService.findCuentaById(1L); // Llamamos nuevamente al método findCuentaById del servicio de cuentas para obtener la cuenta con ID 1L y verificar que se devuelve la misma instancia
		assertSame(cuenta1, cuenta2); // Verificamos que las dos variables cuenta1 y cuenta2 hacen referencia a la misma instancia de cuenta
		assertTrue(cuenta1 == cuenta2); // Verificamos que las dos variables cuenta1 y cuenta2 son exactamente la misma instancia de cuenta utilizando el operador de igualdad de referencia (==)
		assertEquals("Juan", cuenta1.getPersona()); // Verificamos que el nombre de la persona asociada a la cuenta sea igual a "Juan"
		assertEquals("Juan", cuenta2.getPersona()); // Verificamos que el nombre de la persona asociada a la cuenta sea igual a "Juan"
		verify(cuentaRepository, times(2)).findById(1l); // Verificamos que el findbyId se llame 2 veces
	}

	
	@Test
	@DisplayName(">>> Test para verificar que el método findAll devuelve todos los datos para comparar")
	void testfindAll() {
		// Given
		List<Cuenta> cuentas = Arrays.asList( // Crea una lista de cuentas para usar como datos de prueba.
				Datos.getCuenta001().orElseThrow(), // Obtiene la primera cuenta de prueba; lanza excepción si no existe.
				Datos.getCuenta002().orElseThrow()  // Obtiene la segunda cuenta de prueba; lanza excepción si no existe.
		);
		when(this.cuentaRepository.findAll()).thenReturn(cuentas); 
		// Simula el comportamiento del repositorio:
		// cuando se invoque findAll(), devolverá la lista "cuentas".

		// When
		List<Cuenta> cuentasExpected = this.cuentaService.findAll(); 
		// Ejecuta el método real del servicio que internamente llama al repositorio.

		// Then
		assertFalse(cuentasExpected.isEmpty()); 
		// Verifica que la lista retornada no esté vacía.

		assertEquals(2, cuentasExpected.size()); 
		// Verifica que la lista tenga exactamente 2 elementos.

		assertTrue(cuentasExpected.contains(Datos.getCuenta002().orElseThrow())); 
		// Verifica que la lista contenga la segunda cuenta esperada.

		verify(this.cuentaRepository).findAll();
		// Verifica que el método findAll() del repositorio haya sido llamado una vez.

	}
	
	@Test
	@DisplayName(">>> Test para probar el métod save de cuentaRepository")
	void testSave() {
		Cuenta newCuenta = new Cuenta(null, "Pepe", new BigDecimal("3000")); 
		// Crea una nueva cuenta sin ID, simulando una entidad nueva antes de guardarse en la base de datos.

		when(cuentaRepository.save(any())).then( inv -> {
			// Simula el comportamiento del método save() del repositorio.
			// any() indica que acepta cualquier objeto Cuenta como parámetro.

			Cuenta c = inv.getArgument(0); 
			// Obtiene el objeto Cuenta enviado al método save().

			c.setId(3L); 
			// Simula que la base de datos genera automáticamente el ID 3 al guardar la cuenta.

			return c;
			// Retorna la cuenta ya con el ID asignado.
		});

		Cuenta cuentaDb = this.cuentaService.save(newCuenta); 
		// Ejecuta el método save() del servicio que internamente usa el repositorio.

		assertEquals("Pepe", cuentaDb.getPersona()); 
		// Verifica que el nombre de la persona guardada sea "Pepe".

		assertEquals(3, cuentaDb.getId()); 
		// Verifica que el ID asignado a la cuenta sea 3.

		assertEquals("3000", cuentaDb.getSaldo().toPlainString()); 
		// Verifica que el saldo guardado sea exactamente "3000".

		verify(cuentaRepository).save(any());
		// Verifica que el método save() del repositorio haya sido ejecutado.

	}

}
