package pro.sky.socksmarket.services.impl;

import org.springframework.stereotype.Service;
import pro.sky.socksmarket.exception.BadQuantityException;
import pro.sky.socksmarket.exception.NoSocksException;
import pro.sky.socksmarket.model.Color;
import pro.sky.socksmarket.model.Size;
import pro.sky.socksmarket.model.Socks;
import pro.sky.socksmarket.services.SocksService;

import java.util.Map;

@Service
public class SocksServiceImpl implements SocksService {
    private Map<Socks, Integer> socksStock;

    @Override
    public int add(Socks socks, int quantity) {
        //adding socks to stock, returns the quantity after adding
        if (quantity <= 0) throw new BadQuantityException();
        int current = getQuantity(socks);
        socksStock.put(socks, current + quantity);
        return current + quantity;
    }

    @Override
    public int take(Socks socks, int quantity) {
        //taking some socks from stock, returns quantity of taking socks
        if (quantity <= 0) throw new BadQuantityException();
        int current = getQuantity(socks);
        if (quantity > current) {
            //socksStock.put(socks, 0);
            //return current;
            throw new NoSocksException();
        }
        socksStock.put(socks, current - quantity);
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
}
