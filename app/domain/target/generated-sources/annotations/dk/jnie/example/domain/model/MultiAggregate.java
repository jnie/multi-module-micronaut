package dk.jnie.example.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link MultiAggregateDef}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code MultiAggregate.builder()}.
 */
@Generated(from = "MultiAggregateDef", generator = "Immutables")
@SuppressWarnings({"all"})
@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
public final class MultiAggregate implements MultiAggregateDef {
  private final String answer;

  private MultiAggregate(String answer) {
    this.answer = answer;
  }

  /**
   * @return The value of the {@code answer} attribute
   */
  @Override
  public String getAnswer() {
    return answer;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link MultiAggregateDef#getAnswer() answer} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for answer
   * @return A modified copy or the {@code this} object
   */
  public final MultiAggregate withAnswer(String value) {
    String newValue = Objects.requireNonNull(value, "answer");
    if (this.answer.equals(newValue)) return this;
    return new MultiAggregate(newValue);
  }

  /**
   * This instance is equal to all instances of {@code MultiAggregate} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof MultiAggregate
        && equalsByValue((MultiAggregate) another);
  }

  private boolean equalsByValue(MultiAggregate another) {
    return answer.equals(another.answer);
  }

  /**
   * Computes a hash code from attributes: {@code answer}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + answer.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code MultiAggregate} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "MultiAggregate{"
        + "answer=" + answer
        + "}";
  }

  /**
   * Creates an immutable copy of a {@link MultiAggregateDef} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable MultiAggregate instance
   */
  public static MultiAggregate copyOf(MultiAggregateDef instance) {
    if (instance instanceof MultiAggregate) {
      return (MultiAggregate) instance;
    }
    return MultiAggregate.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link MultiAggregate MultiAggregate}.
   * <pre>
   * MultiAggregate.builder()
   *    .answer(String) // required {@link MultiAggregateDef#getAnswer() answer}
   *    .build();
   * </pre>
   * @return A new MultiAggregate builder
   */
  public static MultiAggregate.Builder builder() {
    return new MultiAggregate.Builder();
  }

  /**
   * Builds instances of type {@link MultiAggregate MultiAggregate}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "MultiAggregateDef", generator = "Immutables")
  public static final class Builder {
    private static final long INIT_BIT_ANSWER = 0x1L;
    private long initBits = 0x1L;

    private String answer;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code MultiAggregateDef} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(MultiAggregateDef instance) {
      Objects.requireNonNull(instance, "instance");
      this.answer(instance.getAnswer());
      return this;
    }

    /**
     * Initializes the value for the {@link MultiAggregateDef#getAnswer() answer} attribute.
     * @param answer The value for answer 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder answer(String answer) {
      this.answer = Objects.requireNonNull(answer, "answer");
      initBits &= ~INIT_BIT_ANSWER;
      return this;
    }

    /**
     * Builds a new {@link MultiAggregate MultiAggregate}.
     * @return An immutable instance of MultiAggregate
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public MultiAggregate build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new MultiAggregate(answer);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_ANSWER) != 0) attributes.add("answer");
      return "Cannot build MultiAggregate, some of required attributes are not set " + attributes;
    }
  }
}
