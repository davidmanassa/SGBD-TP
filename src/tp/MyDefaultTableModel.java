package tp;

import javax.swing.table.DefaultTableModel;

class MyDefaultTableModel extends DefaultTableModel {
    private boolean[][] editable_cells; // 2d array to represent rows and columns
    int rows, cols;

    public MyDefaultTableModel(int rows, int cols) { // constructor
        super(rows, cols);
        this.editable_cells = new boolean[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public boolean isCellEditable(int row, int column) { // custom isCellEditable function
        return this.editable_cells[row][column];
    }

    public void setCellEditable(int row, int col, boolean value) {
        this.editable_cells[row][col] = value; // set cell true/false
        this.fireTableCellUpdated(row, col);
    }

    public void addRow(Object[] rowData) {
        super.addRow(rowData);
        boolean[][] newEdtCells = new boolean[rows + 1][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                newEdtCells[i][j] = editable_cells[i][j];
        this.editable_cells = newEdtCells;
        this.rows += 1;
    }
}