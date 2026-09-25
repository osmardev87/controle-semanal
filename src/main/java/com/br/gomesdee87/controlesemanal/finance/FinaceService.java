package com.br.gomesdee87.controlesemanal.finance;

import com.br.gomesdee87.controlesemanal.finance.dto.FinaceRequestDTO;
import com.br.gomesdee87.controlesemanal.finance.dto.FinaceResponseDTO;
import com.br.gomesdee87.controlesemanal.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FinaceService {
    private final FinaceRepository finaceRepository;
    private final UserRepository userRepository;

    public FinaceService(FinaceRepository finaceRepository, UserRepository userRepository) {
        this.finaceRepository = finaceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public FinaceResponseDTO create(FinaceRequestDTO requestDTO, Long userId) {
        if (requestDTO.clientId() != null && !requestDTO.clientId().isBlank()) {
            var existing = finaceRepository.findByUser_IdAndClientId(userId, requestDTO.clientId());
            if (existing.isPresent()) return toResponse(existing.get());
        }
        Finace finance = new Finace();
        finance.setDescription(requestDTO.description());
        finance.setPrice(requestDTO.price());
        finance.setDate(requestDTO.date());
        finance.setType(requestDTO.type());
        finance.setCategory(requestDTO.category());
        finance.setPayment(requestDTO.payment());
        finance.setClientId(requestDTO.clientId());
        finance.setUser(userRepository.getReferenceById(userId));
        return toResponse(finaceRepository.save(finance));
    }

    @Transactional
    public List<FinaceResponseDTO> listFinace(Long userId) {
        return finaceRepository.findByUser_IdOrderByDateDescIdDesc(userId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public List<FinaceResponseDTO> listaGastosDasemana(LocalDate date, Long userId) {
        LocalDate inicioSemana = date.with(java.time.DayOfWeek.MONDAY);
        LocalDate fimSemana = inicioSemana.plusDays(6);
        return buscarEntreDatas(inicioSemana, fimSemana, userId);
    }

    @Transactional
    public String limpaBanco(Long userId) {
        finaceRepository.deleteAllForUser(userId);
        return "Seus lançamentos foram apagados.";
    }

    @Transactional
    public List<FinaceResponseDTO> listarPorMes(LocalDate inicio, LocalDate fim, Long userId) {
        return buscarEntreDatas(inicio, fim, userId);
    }

    @Transactional
    public List<FinaceResponseDTO> buscarEntreDatas(LocalDate inicio, LocalDate fim, Long userId) {
        return finaceRepository.findByUser_IdAndDateBetweenOrderByDateDescIdDesc(userId, inicio, fim)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public boolean deletar(Long id, Long userId) {
        return finaceRepository.deleteByIdForUser(id, userId) > 0;
    }

    private FinaceResponseDTO toResponse(Finace finance) {
        return new FinaceResponseDTO(
                finance.getId(), finance.getDescription(), finance.getPrice(), finance.getDate(),
                finance.getType(), finance.getCategory(), finance.getPayment(), finance.getClientId());
    }
}