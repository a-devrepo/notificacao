package com.nca.notificacao.controllers;

import com.nca.notificacao.business.EmailService;
import com.nca.notificacao.business.dto.TarefaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<Void> enviarEmail(@RequestBody TarefaDTO tarefaDTO) {
        emailService.enviarEmail(tarefaDTO);
        return ResponseEntity.ok().build();
    }
}
