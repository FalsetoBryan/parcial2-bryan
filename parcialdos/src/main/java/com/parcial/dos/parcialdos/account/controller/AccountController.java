package com.parcial.dos.parcialdos.account.controller;

import com.parcial.dos.parcialdos.account.dto.AccountOwnerBalanceDTO;
import com.parcial.dos.parcialdos.account.dto.AccountRequestDTO;
import com.parcial.dos.parcialdos.account.dto.AccountResponseDTO;
import com.parcial.dos.parcialdos.account.service.IAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final IAccountService service;

    public AccountController(IAccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> create(@RequestBody AccountRequestDTO dto) {
        AccountResponseDTO created = service.createAccount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Actualiza únicamente el balanceActual según README.
     * Retorna un String con el mensaje (o "Cuenta no encontrada").
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBalance(@PathVariable Long id, @RequestBody AccountRequestDTO dto) {
        String resp = service.updateBalance(id, dto);
        if ("Cuenta no encontrada".equals(resp)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-number/{numeroCuenta}")
    public ResponseEntity<AccountOwnerBalanceDTO> findByAccountNumber(@PathVariable String numeroCuenta) {
        return service.findByAccountNumber(numeroCuenta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
