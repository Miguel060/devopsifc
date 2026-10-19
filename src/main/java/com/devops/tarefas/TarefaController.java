package com.devops.tarefas;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private static final Logger log = LoggerFactory.getLogger(TarefaController.class);

    private final TarefaRepository repo;

    public TarefaController(TarefaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Tarefa> listar() {
        log.debug("Listando todas as tarefas");
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscar(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Tarefa {} nao encontrada", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tarefa criar(@Valid @RequestBody Tarefa tarefa) {
        tarefa.setId(null);
        Tarefa salva = repo.save(tarefa);
        log.info("Tarefa criada id={} titulo={}", salva.getId(), salva.getTitulo());
        return salva;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizar(@PathVariable Long id, @Valid @RequestBody Tarefa dados) {
        return repo.findById(id).map(t -> {
            t.setTitulo(dados.getTitulo());
            t.setConcluida(dados.isConcluida());
            log.info("Tarefa atualizada id={}", id);
            return ResponseEntity.ok(repo.save(t));
        }).orElseGet(() -> {
            log.warn("Tentativa de atualizar tarefa inexistente id={}", id);
            return ResponseEntity.notFound().build();
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            log.warn("Tentativa de remover tarefa inexistente id={}", id);
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        log.info("Tarefa removida id={}", id);
        return ResponseEntity.noContent().build();
    }
}
