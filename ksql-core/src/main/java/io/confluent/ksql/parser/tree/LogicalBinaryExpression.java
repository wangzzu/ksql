/**
 * Copyright 2017 Confluent Inc.
 **/
package io.confluent.ksql.parser.tree;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class LogicalBinaryExpression
    extends Expression {

  public enum Type {
    AND, OR;

    public Type flip() {
      switch (this) {
        case AND:
          return LogicalBinaryExpression.Type.OR;
        case OR:
          return LogicalBinaryExpression.Type.AND;
        default:
          throw new IllegalArgumentException("Unsupported logical expression type: " + this);
      }
    }
  }

  private final Type type;
  private final Expression left;
  private final Expression right;

  public LogicalBinaryExpression(Type type, Expression left, Expression right) {
    this(Optional.empty(), type, left, right);
  }

  public LogicalBinaryExpression(NodeLocation location, Type type, Expression left,
                                 Expression right) {
    this(Optional.of(location), type, left, right);
  }

  private LogicalBinaryExpression(Optional<NodeLocation> location, Type type, Expression left,
                                  Expression right) {
    super(location);
    requireNonNull(type, "type is null");
    requireNonNull(left, "left is null");
    requireNonNull(right, "right is null");

    this.type = type;
    this.left = left;
    this.right = right;
  }

  public Type getType() {
    return type;
  }

  public Expression getLeft() {
    return left;
  }

  public Expression getRight() {
    return right;
  }

  @Override
  public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
    return visitor.visitLogicalBinaryExpression(this, context);
  }

  public static LogicalBinaryExpression and(Expression left, Expression right) {
    return new LogicalBinaryExpression(Optional.empty(), Type.AND, left, right);
  }

  public static LogicalBinaryExpression or(Expression left, Expression right) {
    return new LogicalBinaryExpression(Optional.empty(), Type.OR, left, right);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LogicalBinaryExpression that = (LogicalBinaryExpression) o;
    return type == that.type &&
           Objects.equals(left, that.left) &&
           Objects.equals(right, that.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, left, right);
  }
}