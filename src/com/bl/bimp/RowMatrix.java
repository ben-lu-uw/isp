package com.bl.bimp;

import java.util.ArrayList;

public class RowMatrix<T extends Combinable<T>> {
    private ArrayList<Row<T>> rows;

    public ArrayList<Row<T>> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Row<T>> rows) {
        this.rows = rows;
    }

    public void addRow(Row<T> r){
        rows.add(r);
    }

    public RowMatrix<T> combine(RowMatrix<T> other){
        RowMatrix<T> combinedMatrix = new RowMatrix<T>();
        for(int y = 0; y < rows.size(); y++){
            Row<T> row = rows.get(y);
            Row<T> otherRow = other.rows.get(y);

            ArrayList<T> cells = row.getCells();
            ArrayList<T> otherCells = otherRow.getCells();

            Row<T> combinedRow = new Row<T>();
            for(int x = 0; x < cells.size(); x++){

                T cell = cells.get(x);
                T otherCell = otherCells.get(x);
                combinedRow.addCell(cell.combine(otherCell));
            }
            combinedMatrix.addRow(combinedRow);
        }
        return combinedMatrix;
    }

    public RowMatrix() {
        this.rows = new ArrayList<Row<T>>();
    }

    @Override
    public String toString() {
        return "imageProcessor.RowMatrix{" +
                "rows=" + rows +
                '}';
    }
}

