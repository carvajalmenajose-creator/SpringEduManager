package cl.bootcamp.springedumanager_2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.bootcamp.springedumanager_2.model.Rol;
import cl.bootcamp.springedumanager_2.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	Optional<Usuario> findByCorreoIgnoreCase(String correo);

	boolean existsByCorreoIgnoreCase(String correo);

	boolean existsByCorreoIgnoreCaseAndIdNot(String correo, int id);

	long countByRolAndActivoTrue(Rol rol);
}
