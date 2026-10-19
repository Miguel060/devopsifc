package com.devops.tarefas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoint de demonstracao: gera erro 500 e log de ERROR para popular dashboards e Graylog. */
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @GetMapping("/erro")
    public String erro() {
        log.error("Erro simulado para demonstracao de observabilidade");
        throw new IllegalStateException("Erro simulado (HTTP 500)");
    }
}
