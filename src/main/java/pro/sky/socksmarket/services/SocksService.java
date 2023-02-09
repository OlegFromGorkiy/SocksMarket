package pro.sky.socksmarket.services;

import pro.sky.socksmarket.model.enums.Color;
import pro.sky.socksmarket.model.enums.Size;
import pro.sky.socksmarket.model.Socks;

public interface SocksService {
    int add(Socks socks, int quantity);

    int take(Socks socks, int quantity);

    int getInfo(Color color, Size size, int cottonMin, int cottonMax);

    int getQuantity(Socks socks);
}
