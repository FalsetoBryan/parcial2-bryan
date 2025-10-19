package com.parcial.dos.parcialdos.account.service;

import com.parcial.dos.parcialdos.account.dto.AccountOwnerBalanceDTO;
import com.parcial.dos.parcialdos.account.dto.AccountRequestDTO;
import com.parcial.dos.parcialdos.account.dto.AccountResponseDTO;
import com.parcial.dos.parcialdos.account.entity.Account;
import com.parcial.dos.parcialdos.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService implements IAccountService {

    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO dto) {
        Account acc = new Account();
        acc.setAccountNumber(dto.getNumeroCuenta());
        acc.setOwnerName(dto.getDueno());
        acc.setBalance(dto.getBalanceActual() != null ? dto.getBalanceActual() : BigDecimal.ZERO);
        acc.setActive(true);
        Account saved = repo.save(acc);
        return toResponse(saved);
    }

    @Override
    public List<AccountResponseDTO> getAll() {
        return repo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public Optional<AccountResponseDTO> getById(Long id) {
        return repo.findById(id).map(this::toResponse);
    }

    @Override
    public String updateBalance(Long id, AccountRequestDTO dto) {
        Optional<Account> opt = repo.findById(id);
        if (opt.isEmpty()) {
            return "Cuenta no encontrada";
        }
        Account acc = opt.get();
        BigDecimal before = acc.getBalance() != null ? acc.getBalance() : BigDecimal.ZERO;
        BigDecimal after = dto.getBalanceActual() != null ? dto.getBalanceActual() : before;
        acc.setBalance(after);
        repo.save(acc);
        return String.format("La cuenta %s fue actualizada: balanceAnterior=%s, balanceActual=%s",
                acc.getAccountNumber(),
                before.setScale(2, RoundingMode.HALF_EVEN).toPlainString(),
                after.setScale(2, RoundingMode.HALF_EVEN).toPlainString()
        );
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<AccountOwnerBalanceDTO> findByAccountNumber(String numeroCuenta) {
        return repo.findByAccountNumber(numeroCuenta)
                .map(a -> new AccountOwnerBalanceDTO(a.getOwnerName(), a.getBalance()));
    }

    // Mapeo helper
    private AccountResponseDTO toResponse(Account a) {
        return new AccountResponseDTO(
                a.getId(),
                a.getAccountNumber(),
                a.getOwnerName(),
                a.getBalance(),
                a.getActive()
        );
    }
}