package org.juank.test.springboot.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

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
		verify(cuentaRepository, times(2)).update(any(Cuenta.class)); // Verificamos que el método update del repositorio de cuentas haya sido llamado 2 veces con cualquier objeto de tipo Cuenta 
	
		verify(bancoRepository, times(2)).findById(1L); // Verificamos que el método findById del repositorio de bancos haya sido llamado 2 vez con el ID 1L
		verify(bancoRepository, times(1)).update(any()); // Verificamos que el método update del repositorio de bancos haya sido llamado 1 vez con cualquier objeto
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
		verify(cuentaRepository, times(2)).findById(1l);
	}


}
