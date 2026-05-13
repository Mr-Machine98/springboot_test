package org.juank.test.springboot.app.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.juank.test.springboot.app.Datos;
import org.juank.test.springboot.app.services.ICuentaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * La clase CuentaControllerIntegrationTest es una clase de prueba que se utiliza
 * para realizar pruebas de integración en el controlador CuentaController.
 * Esta clase está anotada con @WebMvcTest, lo que indica que se trata de una
 * prueba de integración enfocada en el controlador web.
 * En esta clase se pueden definir métodos de prueba para verificar el
 * comportamiento del controlador CuentaController, como la respuesta a solicitudes HTTP,
 * la interacción con los servicios y la validación de datos.
 */
@WebMvcTest(CuentaController.class)
class CuentaControllerIntegrationTest {

	/**
	 * El campo mockMvc es una instancia de MockMvc que se utiliza para simular
	 * solicitudes HTTP y verificar las respuestas del controlador durante las pruebas.
	 * La anotación @Autowired se utiliza para inyectar automáticamente la instancia
	 * de MockMvc en la clase de prueba, lo que permite realizar pruebas de integración
	 * sin necesidad de configurar manualmente el entorno de pruebas.
	 */
	@Autowired
	private MockMvc mockMvc;
	
	
	@MockitoBean
	private ICuentaService cuentaService;

	@Test
	@DisplayName(">>> Test de integración del método detalle")
	void detalle() throws Exception {
		// given
		
		/** En esta sección se establece el comportamiento simulado del servicio ICuentaService utilizando Mockito.
		 * Se utiliza el método when() para definir que cuando se llame al método findCuentaById(1L) del servicio,
		 * se devolverá un objeto Cuenta específico obtenido de la clase Datos. Esto permite simular la respuesta
		 * del servicio sin necesidad de acceder a una base de datos real, lo que facilita las pruebas de integración
		 * del controlador.
		 */
		when(cuentaService.findCuentaById(1L)).thenReturn(Datos.getCuenta001().orElseThrow());
		
		// When and then
		
		/**
		 * En esta sección se realiza la prueba de integración utilizando MockMvc para 
		 * simular una solicitud HTTP GET al endpoint "/api/cuentas/1". Se verifica que
		 * la respuesta tenga un estado HTTP 200 (OK), que el contenido
		 * de la respuesta sea de tipo JSON, y que los campos "persona" y "saldo" en el
		 * JSON devuelto tengan los valores esperados ("Juan" y "1000", respectivamente).
		 * Esto permite validar que el controlador CuentaController está funcionando correctamente
		 *  y que está devolviendo la información esperada en la respuesta HTTP.
		 */
		this.mockMvc.perform(get("/api/cuentas/1")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.persona").value("Juan"))
			.andExpect(jsonPath("$.saldo").value("1000"));
	}
	
}