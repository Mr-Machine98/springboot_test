package org.juank.test.springboot.app.repositories;

import java.util.List;

import org.juank.test.springboot.app.models.Cuenta;

public interface ICuentaRepository {
	
	/**
	 * El método findAll() se encarga de obtener una lista de todas las cuentas disponibles en el sistema utilizando el repositorio de cuentas.
	 * @return Una lista de todas las cuentas disponibles en el sistema.
	 */
	List<Cuenta> findAll();
	
	/**
	 * El método findById(Long id) se encarga de buscar una cuenta por su ID utilizando el repositorio de cuentas.
	 * @param id El ID de la cuenta a buscar.
	 * @return La cuenta encontrada o null si no se encuentra.
	 */
	Cuenta findById(Long id);
	
	/**
	 * El método save(Cuenta cuenta) se encarga de guardar una cuenta en el sistema utilizando el repositorio de cuentas.
	 * @param cuenta La cuenta que se desea guardar en el sistema.
	 * @return void
	 */
	void update(Cuenta cuenta);
}
