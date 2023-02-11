package pro.sky.socksmarket.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.socksmarket.exception.*;
import pro.sky.socksmarket.model.enums.Color;
import pro.sky.socksmarket.model.enums.Size;
import pro.sky.socksmarket.model.Socks;
import pro.sky.socksmarket.services.FilesService;
import pro.sky.socksmarket.services.SocksService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class SocksServiceImpl implements SocksService {
    @Value("${name.stock.data}")
    private String fileName;
    private final FilesService filesService;
    private Map<Socks, Integer> socksStock;

    public SocksServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostConstruct
    private void init() {
        try {
            readFromFile();
        } catch (Exception e) {
            socksStock = new HashMap<>();
        }
    }

    @Override
    public int add(Socks socks, int quantity) {
        //adding socks to stock, returns the quantity after adding
        if (quantity <= 0) throw new BadQuantityException();
        int current = getQuantity(socks);
        socksStock.put(socks, current + quantity);
        saveToFile();
        return current + quantity;
    }
    @Override
    public int take(Socks socks, int quantity) {
        //taking some socks from stock, returns quantity of taking socks
        if (quantity <= 0) {
            throw new BadQuantityException();
        }
        int current = getQuantity(socks);
        if (quantity > current) {
            throw new NoSocksException();
        }
        socksStock.put(socks, current - quantity);
        saveToFile();
        return quantity;
    }

    @Override
    public int getInfo(Color color, Size size, int cottonMin, int cottonMax) {
        //returns the number of socks of the given color, size and percentage of cotton
        return socksStock.keySet().stream()
                .filter(s -> s.getColor() == color && s.getSize() == size)
                .filter(s -> s.getPercentageOfCotton() <= cottonMax && s.getPercentageOfCotton() >= cottonMin)
                .mapToInt(s -> socksStock.get(s)).sum();
    }

    @Override
    public int getQuantity(Socks socks) {
        //returns the number of this socks
        return socksStock.getOrDefault(socks, 0);
    }
    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socksStock);
            filesService.saveToFile(fileName, json);
        } catch (JsonProcessingException e) {
            throw new SaveSocksException();
        }
    }
    private void readFromFile() {
        try {
            String json = filesService.readFromFile(fileName);
            socksStock = new ObjectMapper().readValue(json, new TypeReference<Map<Socks, Integer>>() {
            });
        } catch (JsonProcessingException e) {
            throw new ReadSocksException();
        }
    }
}
