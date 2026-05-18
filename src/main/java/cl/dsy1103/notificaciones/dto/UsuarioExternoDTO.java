package cl.dsy1103.notificaciones.dto;

import lombok.Data;

@Data
public class UsuarioExternoDTO {
    private Long idUsuario;
    private String nombreCompleto;
    private String email; // ¡Esto es lo que realmente nos importa!
}