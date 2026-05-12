package org.juank.test.springboot.app.repositories;

import java.util.List;

import org.juank.test.springboot.app.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBancoRepository extends JpaRepository<Banco, Long> {
}
