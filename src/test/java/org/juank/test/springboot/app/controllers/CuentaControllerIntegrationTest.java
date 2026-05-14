package org.juank.test.springboot.app.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.juank.test.springboot.app.Datos;
import org.juank.test.springboot.app.models.Cuenta;
import org.juank.test.springboot.app.models.TransaccionDTO;
import org.juank.test.springboot.app.services.ICuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
	
	ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}

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
	
	@Test
	@DisplayName(">>> Test de integración del método transferir")
	void testTransferir() throws JsonProcessingException, Exception {
		//given
		TransaccionDTO dto = new TransaccionDTO();
		dto.setCuentaOrigenId(1L);
		dto.setCuentaDestinoId(2L);
		dto.setMonto(new BigDecimal("100"));
		dto.setBancoId(1L);
		
		// Expected response
		Map<String, Object> response = Map.of(
			"date", LocalDate.now().toString(),
			"status", "OK",
			"message", "Transferencia realizada con éxito",
			"transaccion", dto
		);
		
		//when -> then
		mockMvc.perform(post("/api/cuentas/transferir") // Realiza una solicitud HTTP POST al endpoint "/api/cuentas/transferir" utilizando MockMvc.
			.contentType(MediaType.APPLICATION_JSON) // Establece el tipo de contenido de la solicitud como JSON.
			.content(objectMapper.writeValueAsString(dto))) // Convierte el objeto TransaccionDTO a una cadena JSON utilizando ObjectMapper y lo establece como el contenido de la solicitud.
			.andExpect(status().isOk()) // Verifica que el estado de la respuesta HTTP sea 200 (OK).
			.andExpect(jsonPath("$.date").value(LocalDate.now().toString())) // Verifica que el campo "date" en la respuesta JSON tenga el valor de la fecha actual.
			.andExpect(jsonPath("$.message").value("Transferencia realizada con éxito")) // Verifica que el campo "message" en la respuesta JSON tenga el valor "Transferencia realizada con éxito".;
			.andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(dto.getCuentaOrigenId())) // Verifica que el campo "transaccion.cuentaOrigenId" en la respuesta JSON tenga el mismo valor que el campo "cuentaOrigen
			.andExpect(content().json(objectMapper.writeValueAsString(response))); // Verifica que el contenido de la respuesta JSON sea igual al JSON esperado generado a partir del mapa "response" utilizando ObjectMapper.

	}
	
	@Test
	@DisplayName(">>> Test de integración del método listar")
	void testListar() throws Exception {
		// given
		List<Cuenta> cuentas = List.of(Datos.getCuenta001().orElseThrow(), Datos.getCuenta002().orElseThrow());
		when(cuentaService.findAll()).thenReturn(cuentas);
		
		// when and then
		mockMvc.perform(get("/api/cuentas")
			.contentType(MediaType.APPLICATION_JSON))
		    .andExpect(jsonPath("$[0].persona").value("Juan"))
		    .andExpect(jsonPath("$[0].saldo").value("1000"))
		    .andExpect(jsonPath("$[1].persona").value("Andres"))
		    .andExpect(jsonPath("$[1].saldo").value("2000"))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(content().json(objectMapper.writeValueAsString(cuentas)));
	}
	
	@Test
	@DisplayName(">>> Test de integación del método save")
	/**
	 * Prueba de integración para el endpoint POST /api/cuentas.
	 *
	 * Esta prueba verifica que:
	 * - Cuando se envía una cuenta nueva (sin id) al endpoint de creación,
	 *   el servicio `ICuentaService.save` asigna un id y devuelve la cuenta creada.
	 * - El controlador responde con estado 201 Created y el cuerpo JSON
	 *   contiene los valores esperados (id, persona, saldo).
	 * - Se invoca el método `save` del servicio exactamente una vez.
	 *
	 * Estructura de la prueba:
	 * - given: se construye una cuenta sin id y se simula el comportamiento del servicio
	 *          para asignar el id 3 al objeto guardado.
	 * - when:  se realiza una petición POST con el JSON de la cuenta.
	 * - then:  se verifican el status, el contenido y los campos JSON, y se comprueba
	 *          que el servicio haya sido invocado.
	 */
	void testGuardadr() throws Exception {

		// given: creamos una cuenta nueva (sin id) que será enviada en la petición
		Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal("3000"));

		// Simulamos que el servicio asigna el id 3 cuando se guarda la cuenta.
		when(this.cuentaService.save(any())).then(invocation -> {
			Cuenta c = invocation.getArgument(0);
			c.setId(3L);
			return c;
		});

		// when and then: realizamos la petición POST al endpoint de creación y
		// comprobamos la respuesta y el JSON devuelto.
		mockMvc
			.perform(post("/api/cuentas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cuenta)))
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(3))
			.andExpect(jsonPath("$.persona").value("Pepe"))
			.andExpect(jsonPath("$.saldo").value("3000"));

		// Verificamos que el servicio haya sido invocado para persistir la cuenta.
		verify(cuentaService).save(any());

	}
	
	
}