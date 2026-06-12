package com.bank.transferapi.repository;

import com.bank.transferapi.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository
        extends JpaRepository<TransactionLog, UUID> {
}