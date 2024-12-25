package com.andrewmadigan.sudoku;

import java.util.BitSet;
import java.util.function.Consumer;

public class Cell {
  private int row;
  private int col;
  private byte value;
  private BitSet notes;
  private Consumer<Cell> updateFn;

  public Cell(int row, int col, Consumer<Cell> updateFn) {
    this.row = row;
    this.col = col;
    notes = new BitSet();
    notes.set(0, 9);
    this.updateFn = updateFn;
  }

  public byte getValue() {
    return value;
  }

  public void setValue(byte value) {
    if (this.value == value) {
      return;
    }

    if (this.value != 0) {
      throw new IllegalStateException("Cannot change a cell that is already set");
    }

    this.value = value;
    notes.clear();
    updateFn.accept(this);
  }

  public void remove(byte value) {
    notes.clear(value - 1);

    if (this.value == 0 && notes.cardinality() == 0) {
      throw new IllegalStateException("No more notes left");
    }
  }

  public boolean contains(byte value) {
    return notes.get(value - 1);
  }

  public int size() {
    return notes.cardinality();
  }

  public byte getNote() {
    if (size() == 1) {
      return (byte) (notes.nextSetBit(0) + 1);
    }
    return 0;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public String toString() {
    return "(" + row + ", " + col + ") = " + value;
  }

}
