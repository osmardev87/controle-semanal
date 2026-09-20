package com.br.gomesdee87.controlesemanal.finance;

import com.br.gomesdee87.controlesemanal.finance.dto.FinaceRequestDTO;
import com.br.gomesdee87.controlesemanal.finance.dto.FinaceResponseDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FinaceService {
    private final FinaceRepository finaceRepository;

    public FinaceService(FinaceRepository finaceRepository) {
        this.finaceRepository = finaceRepository;
    }

    public FinaceResponseDTO create(FinaceRequestDTO requestDTO) {

        Finace openFinace = new Finace();
        openFinace.setDescription(requestDTO.description());
        openFinace.setPrice(requestDTO.price());
        openFinace.setDate(requestDTO.date());
        openFinace.setType(requestDTO.type());
        openFinace.setCategory(requestDTO.category());
        openFinace.setPayment(requestDTO.payment());

        Finace finace = finaceRepository.save(openFinace);

        return new FinaceResponseDTO(
                finace.getId(),
                finace.getDescription(),
                finace.getPrice(),
                finace.getDate(),
                finace.getType(),
                finace.getCategory(),
                finace.getPayment()
        );
    }

    public List<FinaceResponseDTO> listFinace() {
        return finaceRepository.findAll()
                .stream()
                .map(finace -> new FinaceResponseDTO(
                        finace.getId(),
                        finace.getDescription(),
                        finace.getPrice(),
                        finace.getDate(),
                        finace.getType(),
                        finace.getCategory(),
                        finace.getPayment()))
                .toList();
    }

    public List<FinaceResponseDTO> listaGastosDasemana(LocalDate date) {
        LocalDate inicioSemana = date.with( java.time.DayOfWeek.MONDAY );
        LocalDate fimSemana = inicioSemana.plusDays(6);
        return finaceRepository.findByDateBetween(inicioSemana, fimSemana)
                .stream()
                .map(finace -> new FinaceResponseDTO(
                        finace.getId(),
                        finace.getDescription(),
                        finace.getPrice(),
                        finace.getDate(),
                        finace.getType(),
                        finace.getCategory(),
                        finace.getPayment() ))
                .toList();
    }


    public String limpaBanco() {
        finaceRepository.deleteAll();
        return  "Banco limpo";
    }

    public List<FinaceResponseDTO> listarPorMes(LocalDate inicio, LocalDate fim) {
        return finaceRepository.findByDateBetween(inicio, fim)
                .stream()
                .map(finace -> new FinaceResponseDTO(
                        finace.getId(),
                        finace.getDescription(),
                        finace.getPrice(),
                        finace.getDate(),
                        finace.getType(),
                        finace.getCategory(),
                        finace.getPayment() ))
                .toList();
    }   

    public List<FinaceResponseDTO> buscarEntreDatas(LocalDate inicio, LocalDate fim) {
        return finaceRepository.findByDateBetween(inicio, fim)
                .stream()
                .map(finace -> new FinaceResponseDTO(
                        finace.getId(),
                        finace.getDescription(),
                        finace.getPrice(),
                        finace.getDate(),
                        finace.getType(),
                        finace.getCategory(),
                        finace.getPayment() ))
                .toList();
    }

    public void deletar(Long id) {
        finaceRepository.deleteById(id);
    }

}
