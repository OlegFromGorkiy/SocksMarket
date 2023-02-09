package pro.sky.socksmarket.services.impl;

import org.springframework.stereotype.Service;
import pro.sky.socksmarket.model.Socks;
import pro.sky.socksmarket.model.Transaction;
import pro.sky.socksmarket.model.TransactionType;
import pro.sky.socksmarket.services.TransactionService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    List<Transaction> transactions;

    @Override
    public boolean addTransaction(Socks socks, int quantity, TransactionType type) {
        Transaction result = new Transaction(socks, quantity, LocalDateTime.now(), type);
        return transactions.add(result);
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
