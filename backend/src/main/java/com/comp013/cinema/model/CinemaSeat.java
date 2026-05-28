package com.comp013.cinema.model;

public class CinemaSeat {
    private final String seatNo;
    private final String row;
    private final int col;

    public CinemaSeat(String seatNo, String row, int col) {
        this.seatNo = seatNo;
        this.row = row;
        this.col = col;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public String getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
