package org.juank.test.springboot.app.services;

import java.math.BigDecimal;
import java.util.List;

import org.juank.test.springboot.app.models.Cuenta;

public interface ICuentaService {
	
	/**
	 * El método findAll() se encarga de obtener una lista de todas las cuentas utilizando el repositorio de cuentas.
	 * @return Una lista de todas las cuentas disponibles.
	 */
	List<Cuenta> findAll();
	
	/**
	 * El método save(Cuenta cuenta) se encarga de guardar una cuenta utilizando el repositorio de cuentas.
	 * @param cuenta La cuenta que se desea guardar.
	 * @return La cuenta guardada con su ID asignado.
	 */
	Cuenta save(Cuenta cuenta);
	
	/**
	 * El método findCuentaById(Long id) se encarga de buscar una cuenta por su ID utilizando el repositorio de cuentas.
	 * @param id El ID de la cuenta a buscar.
	 * @return La cuenta encontrada o null si no se encuentra.
	 */
	Cuenta findCuentaById(Long id);
	
	/** El método revisarTotalTransferencias(Long id) se encarga de revisar el total de transferencias realizadas por un banco específico utilizando el repositorio de bancos.
	 * @param id El ID del banco para el cual se desea revisar el total de transferencias.
	 * @return El total de transferencias realizadas por el banco.
	 */
	int revisarTotalTransferencias(Long id);
	
	/** El método revisarSaldo(Long id) se encarga de revisar el saldo de una cuenta específica utilizando el repositorio de cuentas.
	 * @param id El ID de la cuenta para la cual se desea revisar el saldo.
	 * @return El saldo actual de la cuenta.
	 */
	BigDecimal revisarSaldo(Long id);
	
	/** El método transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto) se encarga de realizar una transferencia de dinero entre dos cuentas específicas utilizando los repositorios de cuentas y bancos.
	 * @param numCuentaOrigen El número de cuenta de origen desde donde se realizará la transferencia.
	 * @param numCuentaDestino El número de cuenta de destino hacia donde se realizará la transferencia.
	 * @param monto El monto de dinero que se desea transferir.
	 * @param bancoId El ID del banco al que pertenecen las cuentas involucradas en la transferencia.
	 * @return void
	 */
	void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long bancoId);
}
