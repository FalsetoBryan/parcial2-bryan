package com.parcial.dos.parcialdos.account.service;

import com.parcial.dos.parcialdos.account.dto.AccountOwnerBalanceDTO;
import com.parcial.dos.parcialdos.account.dto.AccountRequestDTO;
import com.parcial.dos.parcialdos.account.dto.AccountResponseDTO;

import java.util.List;
import java.util.Optional;

public interface IAccountService {

    AccountResponseDTO createAccount(AccountRequestDTO dto);

    List<AccountResponseDTO> getAll();

    Optional<AccountResponseDTO> getById(Long id);

    String updateBalance(Long id, AccountRequestDTO dto);

    void delete(Long id);

    Optional<AccountOwnerBalanceDTO> findByAccountNumber(String numeroCuenta);
}
