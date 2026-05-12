package org.juank.test.springboot.app.repositories;

import java.util.List;

import org.juank.test.springboot.app.models.Banco;

public interface IBancoRepository {
	
	/** El método findAll() se encarga de obtener una lista de todos los bancos disponibles en el repositorio.
	 * @return Una lista de objetos Banco que representa todos los bancos disponibles.
	 */
	List<Banco> findAll();
	
	/**
	 * El método findById(Long id) se encarga de buscar un banco específico por su ID utilizando el repositorio de bancos.
	 * @param id El ID del banco a buscar.
	 * @return El banco encontrado o null si no se encuentra.
	 */
	Banco findById(Long id);
	
	/**
	 * El método update(Banco banco) se encarga de actualizar la información de un banco específico utilizando el repositorio de bancos.
	 * @param banco El objeto Banco que contiene la información actualizada del banco que se desea actualizar en el repositorio.
	 * @return void
	 */
	void update(Banco banco);
}
