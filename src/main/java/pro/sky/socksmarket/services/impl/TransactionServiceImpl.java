package pro.sky.socksmarket.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.socksmarket.exception.ReadTransactionException;
import pro.sky.socksmarket.exception.SaveTransactionException;
import pro.sky.socksmarket.model.Socks;
import pro.sky.socksmarket.model.Transaction;
import pro.sky.socksmarket.model.enums.TransactionType;
import pro.sky.socksmarket.services.FilesService;
import pro.sky.socksmarket.services.TransactionService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Value("${name.transaction.data}")
    private String fileName;
    private final FilesService filesService;
    private List<Transaction> transactions;

    public TransactionServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }
    @PostConstruct
    private void init() {
        try {
            readFromFile();
        } catch (Exception e) {
            transactions = new LinkedList<>();
        }
    }

    @Override
    public boolean addTransaction(Socks socks, int quantity, TransactionType type) {
        try {
            transactions.add(new Transaction(socks, quantity, LocalDateTime.now(), type));
            saveToFile();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }

    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(transactions);
            filesService.saveToFile(fileName, json);
        } catch (JsonProcessingException e) {
            throw new SaveTransactionException();
        }
    }

    private void readFromFile() {
        try {
            String json = filesService.readFromFile(fileName);
            transactions = new ObjectMapper().readValue(json, new TypeReference<LinkedList<Transaction>>() {
            });
        } catch (JsonProcessingException e) {
            throw new ReadTransactionException();
        }
    }
}
