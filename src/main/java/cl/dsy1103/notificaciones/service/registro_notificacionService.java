package cl.dsy1103.notificaciones.service;

import cl.dsy1103.notificaciones.dto.registro_notificacionRequestDTO;
import cl.dsy1103.notificaciones.dto.registro_notificacionResponseDTO;
import cl.dsy1103.notificaciones.model.registro_notificacion;
import cl.dsy1103.notificaciones.repository.registro_notificacionRepository;

import cl.dsy1103.notificaciones.client.UsuarioClient;
import cl.dsy1103.notificaciones.dto.UsuarioExternoDTO;
import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class registro_notificacionService {

    private final registro_notificacionRepository repository;

    private final UsuarioClient usuarioClient;

    // ── MAPEO PRIVADO: Entidad → ResponseDTO ─────────
    private registro_notificacionResponseDTO mapToDTO(registro_notificacion entidad) {
        return new registro_notificacionResponseDTO(
                entidad.getIdNotificacion(),
                entidad.getIdUsuario(),
                entidad.getIdPedido(),
                entidad.getTipo(),
                entidad.getMensaje(),
                entidad.getFechaEnvio()
        );
    }

    // ── OBTENER TODOS ────────────────────────────────
    public List<registro_notificacionResponseDTO> obtenerTodas() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ── OBTENER POR ID ───────────────────────────────
    public Optional<registro_notificacionResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }

    // ── OBTENER POR USUARIO ───────────────────────────────
    public List<registro_notificacionResponseDTO> obtenerPorUsuario(Long idUsuario) {
        return repository.findByIdUsuario(idUsuario)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ── GUARDAR  ──────────────────────────────────────
    public registro_notificacionResponseDTO guardar(registro_notificacionRequestDTO dto) {

        if (dto.getIdUsuario() != null && dto.getIdUsuario() > 0) {
            try {
                log.info("Llamando a ms-usuarios para obtener el correo del usuario ID: {}", dto.getIdUsuario());
                UsuarioExternoDTO usuario = usuarioClient.obtenerUsuarioPorId(dto.getIdUsuario());

                log.info(">>> SIMULACIÓN DE ENVÍO DE EMAIL <<<");
                log.info("Para: {} ({})", usuario.getEmail(), usuario.getNombreCompleto());
                log.info("Asunto: {}", dto.getTipo());
                log.info("Cuerpo del mensaje: {}", dto.getMensaje());
                log.info("--------------------------------------");

            } catch (Exception e) {
                log.warn("TOLERANCIA A FALLOS: No se pudo obtener el correo de ms-usuarios. El mensaje se guardará en BD igual. Motivo: {}", e.getMessage());
            }
        }

        registro_notificacion nuevaNotificacion = new registro_notificacion(
                null,
                dto.getIdUsuario(),
                dto.getIdPedido(),
                dto.getTipo(),
                dto.getMensaje(),
                LocalDateTime.now()
        );

        registro_notificacion guardado = repository.save(nuevaNotificacion);
        log.info("Notificación registrada exitosamente en la base de datos local.");

        return mapToDTO(guardado);
    }

    // ── ACTUALIZAR   ──────────────────────────────────────
    public Optional<registro_notificacionResponseDTO> actualizar(Long id, registro_notificacionRequestDTO dto) {
        return repository.findById(id).map(existente -> {
            existente.setIdUsuario(dto.getIdUsuario());
            existente.setIdPedido(dto.getIdPedido());
            existente.setTipo(dto.getTipo());
            existente.setMensaje(dto.getMensaje());

            return mapToDTO(repository.save(existente));
        });
    }

    // ── ELIMINAR ─────────────────────────────────────
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}