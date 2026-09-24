package com.br.gomesdee87.controlesemanal.finance;

import com.br.gomesdee87.controlesemanal.finance.dto.FinaceRequestDTO;
import com.br.gomesdee87.controlesemanal.finance.dto.FinaceResponseDTO;
import com.br.gomesdee87.controlesemanal.finance.dto.LoginDTO;
import com.br.gomesdee87.controlesemanal.user.UserService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
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
    public ResponseEntity<FinaceResponseDTO> create(@RequestBody FinaceRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(finaceService.create(requestDTO));
    }

    // ✅ TODOS = ano atual
    @GetMapping("/")
    public ResponseEntity<List<FinaceResponseDTO>> listarTodosDoAnoAtual() {
        int anoAtual = LocalDate.now().getYear();
        LocalDate inicioAno = LocalDate.of(anoAtual, 1, 1);
        LocalDate fimAno = LocalDate.of(anoAtual, 12, 31);

        return ResponseEntity.status(HttpStatus.OK)
                .body(finaceService.buscarEntreDatas(inicioAno, fimAno));
    }

    @GetMapping("/semana/{date}")
    public ResponseEntity<List<FinaceResponseDTO>> listSemana(@PathVariable("date") LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(finaceService.listaGastosDasemana(date));
    }

    @GetMapping("/mes/{anoMes}")
    public ResponseEntity<List<FinaceResponseDTO>> listarPorMes(@PathVariable CharSequence anoMes) {
        YearMonth mesAno = YearMonth.parse(anoMes);
        LocalDate inicioDoMes = mesAno.atDay(1);
        LocalDate fimDoMes = mesAno.atEndOfMonth();

        return ResponseEntity.status(HttpStatus.OK)
                .body(finaceService.listarPorMes(inicioDoMes, fimDoMes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        finaceService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/")
    public ResponseEntity<String> limpaBanco() {
        return ResponseEntity.status(HttpStatus.OK).body(finaceService.limpaBanco());
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<FinaceResponseDTO>> listAll() {
        return ResponseEntity.status(HttpStatus.OK).body(finaceService.listFinace());
    }

    // ✅ Endpoint que retorna a senha correta
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>>  buscarSenha(@RequestBody LoginDTO request) {

        Map<String, String> resp = new HashMap<>();
        // Pode vir de banco de dados, variável de ambiente, etc
        resp.put("telefone", "false");

        if (userService.buscarPorTelephone(request.telephone()) != null) {
            resp.put("telefone", "true");
        }

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

}
