package pro.sky.socksmarket.model;

public enum TransactionType {
    A("Приемка"),
    B("Выдача"),
    C("Списание");
    private String typeOfTransaction;

    TransactionType(String type) {
        typeOfTransaction = type;
    }

    public String getName() {
        return typeOfTransaction;
    }
}