package pro.sky.socksmarket.services;

import pro.sky.socksmarket.model.Socks;
import pro.sky.socksmarket.model.Transaction;
import pro.sky.socksmarket.model.TransactionType;

import java.util.List;

public interface TransactionService {
    boolean addTransaction(Socks socks, int quantity, TransactionType type);

    List<Transaction> getTransactions();
}
