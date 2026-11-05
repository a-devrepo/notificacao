package com.nca.notificacao.business;

import com.nca.notificacao.business.dto.TarefaDTO;
import com.nca.notificacao.infrastructure.exceptions.EmailException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${envio.mail.remetente}")
    private String remetente;

    @Value("${envio.mail.nomeRemetente}")
    private String nomeRemetente;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void enviarEmail(TarefaDTO dto) {

        try {
            var mensagem = mailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(mensagem, true, StandardCharsets.UTF_8.name());

            messageHelper.setFrom(new InternetAddress(remetente, nomeRemetente));
            messageHelper.setTo(InternetAddress.parse(dto.getEmailUsuario()));
            messageHelper.setSubject("Notificação de Tarefa");

            var context = new Context();
            context.setVariable("nomeTarefa", dto.getNomeTarefa());
            context.setVariable("dataEvento", dto.getDataEvento());
            context.setVariable("descricao", dto.getDescricao());

            var template = templateEngine.process("notificacao", context);
            messageHelper.setText(template, true);
            mailSender.send(mensagem);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException("Erro ao enviar email", e.getCause());
        }
    }
}
