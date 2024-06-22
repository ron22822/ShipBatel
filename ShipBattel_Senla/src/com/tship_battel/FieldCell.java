package com.tship_battel;

public enum FieldCell
{
    EMPTY(" "),
    SHIP("#"),
    HIT_SHIP("%%"),//не стоит забывать про спец символы <3
    MISS("*");

    String symbol;

    FieldCell(String symbol)
    {
        this.symbol = symbol;
    }

    public String getSymbol()
    {
        return symbol;
    }
}
