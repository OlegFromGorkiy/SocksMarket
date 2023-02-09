package pro.sky.socksmarket.model.enums;

public enum TransactionType {
    BUY("Приемка"),
    SELL("Выдача"),
    WRITE_OFF("Списание");
    private String typeOfTransaction;

    TransactionType(String type) {
        typeOfTransaction = type;
    }

    public String getName() {
        return typeOfTransaction;
    }
}