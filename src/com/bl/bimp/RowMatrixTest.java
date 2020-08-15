package com.bl.bimp;

import static org.junit.jupiter.api.Assertions.*;

class RowMatrixTest {

    @org.junit.jupiter.api.Test
    void combine() {
        RowMatrix<Pixel> m1 = new RowMatrix<Pixel>();
        RowMatrix<Pixel> m2 = new RowMatrix<Pixel>();
        Row<Pixel> r1 = new Row<Pixel>();
        Row<Pixel> r2 = new Row<Pixel>();
        r1.addCell(new Pixel(0));
        r2.addCell(new Pixel(-16777216));
        m1.addRow(r1);
        m2.addRow(r2);
        RowMatrix<Pixel> m3 = m1.combine(m2);
        int result = m3.getRows().get(0).getCells().get(0).getBit();
        assertEquals(-16777216, result);
    }
}