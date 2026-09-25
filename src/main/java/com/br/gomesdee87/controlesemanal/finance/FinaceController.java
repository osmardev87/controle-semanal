package com.br.gomesdee87.controlesemanal.finance;

import com.br.gomesdee87.controlesemanal.finance.dto.FinaceRequestDTO;
import com.br.gomesdee87.controlesemanal.finance.dto.FinaceResponseDTO;
import com.br.gomesdee87.controlesemanal.security.ApiSessionInterceptor;
import com.br.gomesdee87.controlesemanal.user.User;
import com.br.gomesdee87.controlesemanal.user.UserService;
import com.br.gomesdee87.controlesemanal.finance.dto.LoginDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/finace")
public class FinaceController {
    private final FinaceService finaceService;
    private final UserService userService;

    public FinaceController(FinaceService finaceService, UserService userService) {
        this.finaceService = finaceService;
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<FinaceResponseDTO> create(
            @RequestBody FinaceRequestDTO requestDTO,
            @SessionAttribute(ApiSessionInterceptor.USER_ID_ATTRIBUTE) Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(finaceService.create(requestDTO, userId));
    }

    @GetMapping("/")
    public ResponseEntity<List<FinaceResponseDTO>> listarTodosDoAnoAtual(
            @SessionAttribute(ApiSessionInterceptor.USER_ID_ATTRIBUTE) Long userId) {
        int anoAtual = LocalDate.now().getYear();
        LocalDate inicioAno = LocalDate.of(anoAtual, 1, 1);
        LocalDate fimAno = LocalDate.of(anoAtual, 12, 31);
        return ResponseEntity.ok(finaceService.buscarEntreDatas(inicioAno, fimAno, userId));
    }

    @GetMapping("/semana/{date}")
    public ResponseEntity<List<FinaceResponseDTO>> listSemana(
            @PathVariable LocalDate date,
            @SessionAttribute(ApiSessionInterceptor.USER_ID_ATTRIBUTE) Long userId) {
        return ResponseEntity.ok(finaceService.listaGastosDasemana(date, userId));
    }

    @GetMapping("/mes/{anoMes}")
    public ResponseEntity<List<FinaceResponseDTO>> listarPorMes(
            @PathVariable CharSequence anoMes,
            @SessionAttribute(ApiSessionInterceptor.USER_ID_ATTRIBUTE) Long userId) {
        YearMonth mesAno = YearMonth.parse(anoMes);
        return ResponseEntity.ok(finaceService.listarPorMes(mesAno.atDay(1), mesAno.atEndOfMonth(), userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @SessionAttribute(ApiSessionInterceptor.USER_ID_ATTRIBUTE) Long userId) {
        if (!finaceService.deletar(id, userId)) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/")
    public ResponseEntity<String> limpaBanco(
            @SessionAttribute(ApiSessionInterceptor.USER_ID_ATTRIBUTE) Long userId) {
        return ResponseEntity.ok(finaceService.limpaBanco(userId));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<FinaceResponseDTO>> listAll(
            @SessionAttribute(ApiSessionInterceptor.USER_ID_ATTRIBUTE) Long userId) {
        return ResponseEntity.ok(finaceService.listFinace(userId));
    }

    @PostMapping("/login")
    public ResponseEntity<User> buscarSenha(
            @Valid @RequestBody LoginDTO request, HttpServletRequest httpRequest) {
        User user = userService.autenticar(request.telephone(), request.password());
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        HttpSession previous = httpRequest.getSession(false);
        if (previous != null) previous.invalidate();
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(ApiSessionInterceptor.USER_ID_ATTRIBUTE, user.getId());
        session.setAttribute(ApiSessionInterceptor.PASSWORD_CHANGE_REQUIRED_ATTRIBUTE,
                Boolean.TRUE.equals(user.getPasswordChangeRequired()));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/session")
    public ResponseEntity<Map<String, Boolean>> validateSession(HttpSession session) {
        return ResponseEntity.ok(Map.of(
                "passwordChangeRequired",
                Boolean.TRUE.equals(session.getAttribute(ApiSessionInterceptor.PASSWORD_CHANGE_REQUIRED_ATTRIBUTE))));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.noContent().build();
    }
}