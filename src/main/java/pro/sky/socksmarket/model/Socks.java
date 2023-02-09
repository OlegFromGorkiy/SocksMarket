package pro.sky.socksmarket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sky.socksmarket.model.enums.Color;
import pro.sky.socksmarket.model.enums.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Socks {
    Color color;
    Size size;
    int percentageOfCotton;
}
