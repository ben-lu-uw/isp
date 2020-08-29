package com.bl.isp;

import java.util.ArrayList;

public class Row<T> {
    private ArrayList<T> cells;

    public ArrayList<T> getCells() {
        return cells;
    }

    public void setCells(ArrayList<T> cells) {
        this.cells = cells;
    }

    public void addCell(T p){
        cells.add(p);
    }

    public Row() {
        this.cells = new ArrayList<T>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("imageProcessor.Row{\n");
        sb.append("cells=\n");
        for (int i = 0; i < cells.size(); i++){
            sb.append(cells.get(i).toString());
        }
        sb.append("\n");
        return sb.toString();

    }
}
