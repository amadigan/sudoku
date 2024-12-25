package com.andrewmadigan.sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sudoku implements Iterable<Cell> {
  private Cell[][] board;
  private long version;

  public Sudoku(byte[][] board) {
    this();
    setInitial(board);
  }

  public Sudoku(String board) {
    this();
    byte[] values = board.getBytes();

    byte[][] boardValues = new byte[9][9];

    for (int i = 0; i < 81; i++) {
      if (values[i] != '_') {
        boardValues[i / 9][i % 9] = (byte) (values[i] - '0');
      }
    }

    setInitial(boardValues);
  }

  public Sudoku() {
    this.board = new Cell[9][9];

    for (int row = 0; row < 9; row++) {
      for (int col = 0; col < 9; col++) {
        board[row][col] = new Cell(row, col, cell -> updated(cell.getRow(), cell.getCol(), cell.getValue()));
      }
    }
  }

  public void setInitial(byte[][] board) {
    for (int row = 0; row < 9; row++) {
      for (int col = 0; col < 9; col++) {
        if (board[row][col] != 0) {
          this.board[row][col].setValue(board[row][col]);
        }
      }
    }

    for (int i = 0; i < 9; i++) {
      updateNotes(getRow(i));
      updateNotes(getCol(i));
      updateNotes(getSquare(i));
    }

    version = 1;
  }

  private static void updateNotes(Cell[] cells) {
    for (Cell cell : cells) {
      byte value = cell.getValue();
      if (value != 0) {
        for (Cell c : cells) {
          c.remove(value);
        }
      }
    }
  }

  private void updated(int row, int col, byte value) {
    if (version == 0) {
      return;
    }
    version++;

    for (Cell c : getRow(row)) {
      c.remove(value);
    }

    for (Cell c : getCol(col)) {
      c.remove(value);
    }

    for (Cell c : getSquare((row / 3) * 3 + (col / 3))) {
      c.remove(value);
    }
  }

  public Cell[] getRow(int row) {
    return board[row];
  }

  public Cell[] getCol(int col) {
    Cell[] cells = new Cell[9];
    for (int i = 0; i < 9; i++) {
      cells[i] = board[i][col];
    }
    return cells;
  }

  public Cell[] getSquare(int square) {
    Cell[] cells = new Cell[9];
    int index = 0;

    int row = (square / 3) * 3;
    int col = (square % 3) * 3;

    for (int i = row; i < row + 3; i++) {
      for (int j = col; j < col + 3; j++) {
        cells[index++] = board[i][j];
      }
    }

    return cells;
  }

  public Cell getCell(int row, int col) {
    return board[row][col];
  }

  public long getVersion() {
    return version;
  }

  public boolean isSolved() {
    for (int row = 0; row < 9; row++) {
      for (int col = 0; col < 9; col++) {
        if (board[row][col].getValue() == 0) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Validate the current state of the board. If there are any conflicts, a list
   * containing the conflicting cells will be returned.
   */
  public List<Cell> validate() {
    List<Cell> conflicts = new ArrayList<>();

    for (int i = 0; i < 9; i++) {
      conflicts.addAll(validate(getRow(i)));
      conflicts.addAll(validate(getCol(i)));
      conflicts.addAll(validate(getSquare(i)));
    }

    return conflicts;
  }

  private List<Cell> validate(Cell[] cells) {
    List<Cell> conflicts = new ArrayList<>();
    Map<Byte, Cell> values = new HashMap<>();

    for (Cell cell : cells) {
      if (cell.getValue() == 0) {
        if (cell.size() == 0) {
          conflicts.add(cell);
        }
      } else if (values.containsKey(cell.getValue())) {
        conflicts.add(cell);

        Cell original = values.get(cell.getValue());

        if (original != null) {
          conflicts.add(original);
          values.put(cell.getValue(), null);
        }
      } else {
        values.put(cell.getValue(), cell);
      }
    }

    return conflicts;
  }

  public String toPrintable() {
    StringBuilder sb = new StringBuilder();

    for (int row = 0; row < 9; row++) {

      if (row == 3 || row == 6) {
        sb.append("----------+-----------+----------\n");
      } else if (row > 0) {
        sb.append("          |           |          \n");
      }

      for (int col = 0; col < 9; col++) {

        if (col == 3 || col == 6) {
          sb.append(" | ");
        }

        sb.append(" ");

        byte value = board[row][col].getValue();

        if (value == 0) {
          sb.append(" ");
        } else {
          sb.append(value);
        }

        if (col < 8) {
          sb.append(" ");
        }
      }
      sb.append("\n");
    }

    return sb.toString();
  }

  @Override
  public java.util.Iterator<Cell> iterator() {
    return new java.util.Iterator<Cell>() {
      private int row = 0;
      private int col = 0;

      @Override
      public boolean hasNext() {
        return row < 9 && col < 9;
      }

      @Override
      public Cell next() {
        Cell cell = board[row][col];

        col++;
        if (col == 9) {
          col = 0;
          row++;
        }

        return cell;
      }
    };
  }

  public String toString() {
    char[] chars = new char[81];

    int index = 0;

    for (Cell cell : this) {
      if (cell.getValue() == 0) {
        chars[index++] = '_';
      } else {
        chars[index++] = (char) ('0' + cell.getValue());
      }
    }

    return new String(chars);
  }

  public boolean equals(Object obj) {
    if (obj instanceof Sudoku) {
      Sudoku other = (Sudoku) obj;

      for (int row = 0; row < 9; row++) {
        for (int col = 0; col < 9; col++) {
          if (board[row][col].getValue() != other.board[row][col].getValue()) {
            return false;
          }
        }
      }

      return true;
    }

    return false;
  }

  public int hashCode() {
    int hash = 0;

    for (Cell cell : this) {
      hash = hash * 31 + cell.getValue();
    }

    return hash;
  }

}
