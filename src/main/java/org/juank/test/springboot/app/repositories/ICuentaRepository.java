package org.juank.test.springboot.app.repositories;

import java.util.Optional;

import org.juank.test.springboot.app.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICuentaRepository extends JpaRepository<Cuenta, Long> {
	
	@Query("SELECT c FROM Cuenta c WHERE c.persona = :persona") // Consulta personalizada para buscar una cuenta por el nombre de la persona asociada a la cuenta
	Optional<Cuenta> findByPersona(String persona); // Método personalizado para buscar una cuenta por el nombre de la persona asociada a la cuenta
}
