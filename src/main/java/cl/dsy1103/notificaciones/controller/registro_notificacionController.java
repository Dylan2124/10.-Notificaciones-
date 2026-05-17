package cl.dsy1103.notificaciones.controller;

import cl.dsy1103.notificaciones.dto.registro_notificacionRequestDTO;
import cl.dsy1103.notificaciones.dto.registro_notificacionResponseDTO;
import cl.dsy1103.notificaciones.service.registro_notificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class registro_notificacionController {

    private final registro_notificacionService service;

    // ── GET  ────────────────────────────────────
    @GetMapping
    public ResponseEntity<?> obtenerTodas() {
        List<registro_notificacionResponseDTO> lista = service.obtenerTodas();
        if (lista.isEmpty()){
            return ResponseEntity.ok("No se encontraron notificaciones");
        }
        return ResponseEntity.ok(service.obtenerTodas());
    }

    // ── GET: OBTENER POR ID  ───────
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<registro_notificacionResponseDTO> opt = service.obtenerPorId(id);

        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró la notificación con el ID: " + id);
        }

        return ResponseEntity.ok(opt.get());
    }

    // ── GET: OBTENER NOTIFICACIONES POR USUARIO ───────
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long idUsuario) {
        List<registro_notificacionResponseDTO> lista = service.obtenerPorUsuario(idUsuario);
        if (lista.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontraron notificaciones para el usuario: " + idUsuario);
        }
        return ResponseEntity.ok(lista);
    }

    // ── POST: CREAR NOTIFICACIÓN ──────────────────────────────
    @PostMapping
    public ResponseEntity<registro_notificacionResponseDTO> guardar(
            @Valid @RequestBody registro_notificacionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(request));
    }

    // ── PUT: ACTUALIZAR ───────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody registro_notificacionRequestDTO request) {

        Optional<registro_notificacionResponseDTO> actualizado = service.actualizar(id, request);

        if (actualizado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error al actualizar: La notificación con ID " + id + " no existe.");
        }

        return ResponseEntity.ok(actualizado.get());
    }

    // ── DELETE: ELIMINAR ──────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (service.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se puede eliminar. No existe notificación con ID: " + id));
        }

        service.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Notificación con ID " + id + " eliminada con éxito."));
    }

}