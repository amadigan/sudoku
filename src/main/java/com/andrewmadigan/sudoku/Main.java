package com.andrewmadigan.sudoku;

public class Main {

  public static void main(String[] args) {
    Sudoku board = new Sudoku(new byte[][] {
        { 0, 0, 0, 9, 0, 0, 0, 8, 2 },
        { 0, 2, 7, 0, 4, 6, 0, 0, 9 },
        { 5, 1, 0, 8, 0, 3, 0, 0, 0 },
        { 3, 8, 0, 0, 0, 2, 9, 5, 1 },
        { 0, 5, 0, 0, 8, 0, 3, 7, 6 },
        { 1, 0, 0, 5, 3, 0, 0, 2, 0 },
        { 0, 0, 8, 0, 0, 0, 0, 0, 0 },
        { 7, 3, 1, 0, 0, 0, 8, 9, 0 },
        { 2, 0, 5, 0, 9, 0, 0, 1, 7 }
    });

    System.out.println(board);
    System.out.println(board.toPrintable());

    for (long v = board.getVersion(); !board.isSolved(); v = board.getVersion()) {
      for (int row = 0; row < 9; row++) {
        for (int col = 0; col < 9; col++) {
          Cell cell = board.getCell(row, col);
          if (cell.size() == 1) {
            cell.setValue(cell.getNote());
          }
        }
      }

      if (v == board.getVersion()) {
        System.out.println("No progress made");
        break;
      }

      System.out.println(board.toPrintable());

      if (!board.validate().isEmpty()) {
        System.out.println("Invalid board");
        break;
      }
    }
  }

}
