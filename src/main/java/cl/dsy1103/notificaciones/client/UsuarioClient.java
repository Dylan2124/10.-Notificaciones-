package cl.dsy1103.notificaciones.client;

import cl.dsy1103.notificaciones.dto.UsuarioExternoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-usuarios", url = "http://localhost:8081/api/usuarios")
public interface UsuarioClient {

    @GetMapping("/{id}")
    UsuarioExternoDTO obtenerUsuarioPorId(@PathVariable("id") Long id);
}