package pro.sky.socksmarket.model;

import jdk.jfr.Percentage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Socks {
    Color color;
    Size size;
    int percentageOfCotton;
   // int quantity;
}
