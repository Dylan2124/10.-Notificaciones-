package cl.dsy1103.notificaciones.service;

import cl.dsy1103.notificaciones.dto.registro_notificacionRequestDTO;
import cl.dsy1103.notificaciones.dto.registro_notificacionResponseDTO;
import cl.dsy1103.notificaciones.model.registro_notificacion;
import cl.dsy1103.notificaciones.repository.registro_notificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class registro_notificacionService {

    private final registro_notificacionRepository repository;

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

    // ── GUARDAR ──────────────────────────────────────
    public registro_notificacionResponseDTO guardar(registro_notificacionRequestDTO dto) {
        registro_notificacion nuevaNotificacion = new registro_notificacion(
                null,
                dto.getIdUsuario(),
                dto.getIdPedido(),
                dto.getTipo(),
                dto.getMensaje(),
                LocalDateTime.now()
        );

        return mapToDTO(repository.save(nuevaNotificacion));
    }

    // ── ACTUALIZAR ───────────────────────────────────
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