package ij.process;

public enum Operation {
  INVERT(0), FILL(1), ADD(2), MULT(3), AND(4), OR(5),
  XOR(6), GAMMA(7), LOG(8), MINIMUM(9), MAXIMUM(10), SQR(11), SQRT(12), EXP(13), ABS(14), SET(15);
  private final int operation;

  Operation(int operation) {
    this.operation = operation;
  }

  public int getOperation() {
    return operation;
  }
}
