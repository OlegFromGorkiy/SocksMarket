package pro.sky.socksmarket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Transaction {

    private Socks socks;
    private int quantity;
    private LocalDateTime time;
    private TransactionType type;


}
