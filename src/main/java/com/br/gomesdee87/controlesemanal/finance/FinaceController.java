package com.br.gomesdee87.controlesemanal.finance;

import com.br.gomesdee87.controlesemanal.finance.dto.FinaceRequestDTO;
import com.br.gomesdee87.controlesemanal.finance.dto.FinaceResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/finace")
public class FinaceController {
    private final FinaceService finaceService;

    public FinaceController(FinaceService finaceService) {
        this.finaceService = finaceService;
    }

    @PostMapping("/")
    public ResponseEntity<FinaceResponseDTO> create(@RequestBody FinaceRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(finaceService.create(requestDTO));
    }

    @GetMapping("/")
    public ResponseEntity<List<FinaceResponseDTO>> listAll() {
        return ResponseEntity.status(HttpStatus.OK).body(finaceService.listFinace());
    }

    @GetMapping("/semana/{date}")
    public ResponseEntity<List<FinaceResponseDTO>> listSemana(@PathVariable("date") LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(finaceService.listaGastosDasemana(date));
    }

    @GetMapping("/mes/{anoMes}")
    public ResponseEntity<List<FinaceResponseDTO>> listarPorMes(@PathVariable LocalDate anoMes) {
        // formato: "2026-09"
        return ResponseEntity.status(HttpStatus.OK).body(finaceService.listarPorMes(anoMes));
    }

    @DeleteMapping("/")
    public ResponseEntity<String> limpaBanco() {
        return ResponseEntity.status(HttpStatus.OK).body(finaceService.limpaBanco());
    }

}
