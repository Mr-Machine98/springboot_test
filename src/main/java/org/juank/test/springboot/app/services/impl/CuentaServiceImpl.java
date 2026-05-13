package org.juank.test.springboot.app.services.impl;

import java.math.BigDecimal;

import org.juank.test.springboot.app.models.Banco;
import org.juank.test.springboot.app.models.Cuenta;
import org.juank.test.springboot.app.repositories.IBancoRepository;
import org.juank.test.springboot.app.repositories.ICuentaRepository;
import org.juank.test.springboot.app.services.ICuentaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CuentaServiceImpl implements ICuentaService {

	private ICuentaRepository cuentaRepository;
	private IBancoRepository bancoRepository;
	
	/**
	 * El método findCuentaById(Long id) se encarga de buscar una cuenta por su ID utilizando el repositorio de cuentas.
	 * @param id El ID de la cuenta a buscar.
	 * @return La cuenta encontrada o null si no se encuentra.
	 */
	@Override
	@Transactional(readOnly = true)
	public Cuenta findCuentaById(Long id) {
		return cuentaRepository.findById(id).orElseThrow();
	}

	
	/**
	 * El método revisarTotalTransferencias(Long id) se encarga de revisar el total de transferencias realizadas por un banco específico utilizando el repositorio de bancos.
	 * @param id El ID del banco para el cual se desea revisar el total de transferencias.
	 * @return El total de transferencias realizadas por el banco.
	 */
	@Override
	@Transactional(readOnly = true)
	public int revisarTotalTransferencias(Long id) {
		Banco banco = bancoRepository.findById(id).orElseThrow();
		return banco.getTotalTransferencias();
	}

	
	/**
	 * El método revisarSaldo(Long id) se encarga de revisar el saldo de una cuenta específica utilizando el repositorio de cuentas.
	 * @param id El ID de la cuenta para la cual se desea revisar el saldo.
	 * @return El saldo actual de la cuenta.
	 */
	@Override
	@Transactional(readOnly = true)
	public BigDecimal revisarSaldo(Long id) {
		Cuenta cuenta = cuentaRepository.findById(id).orElseThrow();
		return cuenta.getSaldo();
	}

	/**
	 * El método transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto) se encarga de realizar una transferencia de dinero entre dos cuentas específicas utilizando los repositorios de cuentas y bancos.
	 * @param numCuentaOrigen El número de cuenta de origen desde donde se realizará la transferencia.
	 * @param numCuentaDestino El número de cuenta de destino hacia donde se realizará la transferencia.
	 * @param monto El monto de dinero que se desea transferir.
	 * @param bancoId El ID del banco al que pertenecen las cuentas involucradas en la transferencia.
	 * @return void
	 */
	@Override
	@Transactional
	public void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long bancoId) {
		Banco banco = bancoRepository.findById(bancoId).orElseThrow();
		int totalTransferencias = banco.getTotalTransferencias();
		banco.setTotalTransferencias(++totalTransferencias);
		bancoRepository.save(banco);
		
		Cuenta cuentaOrigen = cuentaRepository.findById(numCuentaOrigen).orElseThrow();
		cuentaOrigen.debito(monto);
		cuentaRepository.save(cuentaOrigen);
		
		Cuenta cuentaDestino = cuentaRepository.findById(numCuentaDestino).orElseThrow();
		cuentaDestino.credito(monto);
		cuentaRepository.save(cuentaDestino);
		
	}

}
