package dk.jnie.example.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link DomainRequestDef}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code DomainRequest.builder()}.
 */
@Generated(from = "DomainRequestDef", generator = "Immutables")
@SuppressWarnings({"all"})
@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
public final class DomainRequest implements DomainRequestDef {
  private final String question;

  private DomainRequest(String question) {
    this.question = question;
  }

  /**
   * @return The value of the {@code question} attribute
   */
  @Override
  public String getQuestion() {
    return question;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link DomainRequestDef#getQuestion() question} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for question
   * @return A modified copy or the {@code this} object
   */
  public final DomainRequest withQuestion(String value) {
    String newValue = Objects.requireNonNull(value, "question");
    if (this.question.equals(newValue)) return this;
    return new DomainRequest(newValue);
  }

  /**
   * This instance is equal to all instances of {@code DomainRequest} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof DomainRequest
        && equalsByValue((DomainRequest) another);
  }

  private boolean equalsByValue(DomainRequest another) {
    return question.equals(another.question);
  }

  /**
   * Computes a hash code from attributes: {@code question}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + question.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code DomainRequest} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "DomainRequest{"
        + "question=" + question
        + "}";
  }

  /**
   * Creates an immutable copy of a {@link DomainRequestDef} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable DomainRequest instance
   */
  public static DomainRequest copyOf(DomainRequestDef instance) {
    if (instance instanceof DomainRequest) {
      return (DomainRequest) instance;
    }
    return DomainRequest.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link DomainRequest DomainRequest}.
   * <pre>
   * DomainRequest.builder()
   *    .question(String) // required {@link DomainRequestDef#getQuestion() question}
   *    .build();
   * </pre>
   * @return A new DomainRequest builder
   */
  public static DomainRequest.Builder builder() {
    return new DomainRequest.Builder();
  }

  /**
   * Builds instances of type {@link DomainRequest DomainRequest}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "DomainRequestDef", generator = "Immutables")
  public static final class Builder {
    private static final long INIT_BIT_QUESTION = 0x1L;
    private long initBits = 0x1L;

    private String question;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code DomainRequestDef} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(DomainRequestDef instance) {
      Objects.requireNonNull(instance, "instance");
      this.question(instance.getQuestion());
      return this;
    }

    /**
     * Initializes the value for the {@link DomainRequestDef#getQuestion() question} attribute.
     * @param question The value for question 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder question(String question) {
      this.question = Objects.requireNonNull(question, "question");
      initBits &= ~INIT_BIT_QUESTION;
      return this;
    }

    /**
     * Builds a new {@link DomainRequest DomainRequest}.
     * @return An immutable instance of DomainRequest
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public DomainRequest build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new DomainRequest(question);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_QUESTION) != 0) attributes.add("question");
      return "Cannot build DomainRequest, some of required attributes are not set " + attributes;
    }
  }
}
