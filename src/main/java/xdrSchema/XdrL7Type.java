/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package xdrSchema;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class XdrL7Type extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 8001601619798392354L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"XdrL7Type\",\"namespace\":\"xdrSchema\",\"fields\":[{\"name\":\"AppStatus\",\"type\":\"int\",\"default\":0},{\"name\":\"ClassID\",\"type\":\"int\",\"default\":0},{\"name\":\"Protocol\",\"type\":\"int\",\"default\":0}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<XdrL7Type> ENCODER =
      new BinaryMessageEncoder<XdrL7Type>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<XdrL7Type> DECODER =
      new BinaryMessageDecoder<XdrL7Type>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<XdrL7Type> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<XdrL7Type> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<XdrL7Type>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this XdrL7Type to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a XdrL7Type from a ByteBuffer. */
  public static XdrL7Type fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public int AppStatus;
  @Deprecated public int ClassID;
  @Deprecated public int Protocol;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public XdrL7Type() {}

  /**
   * All-args constructor.
   * @param AppStatus The new value for AppStatus
   * @param ClassID The new value for ClassID
   * @param Protocol The new value for Protocol
   */
  public XdrL7Type(java.lang.Integer AppStatus, java.lang.Integer ClassID, java.lang.Integer Protocol) {
    this.AppStatus = AppStatus;
    this.ClassID = ClassID;
    this.Protocol = Protocol;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return AppStatus;
    case 1: return ClassID;
    case 2: return Protocol;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: AppStatus = (java.lang.Integer)value$; break;
    case 1: ClassID = (java.lang.Integer)value$; break;
    case 2: Protocol = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'AppStatus' field.
   * @return The value of the 'AppStatus' field.
   */
  public java.lang.Integer getAppStatus() {
    return AppStatus;
  }

  /**
   * Sets the value of the 'AppStatus' field.
   * @param value the value to set.
   */
  public void setAppStatus(java.lang.Integer value) {
    this.AppStatus = value;
  }

  /**
   * Gets the value of the 'ClassID' field.
   * @return The value of the 'ClassID' field.
   */
  public java.lang.Integer getClassID() {
    return ClassID;
  }

  /**
   * Sets the value of the 'ClassID' field.
   * @param value the value to set.
   */
  public void setClassID(java.lang.Integer value) {
    this.ClassID = value;
  }

  /**
   * Gets the value of the 'Protocol' field.
   * @return The value of the 'Protocol' field.
   */
  public java.lang.Integer getProtocol() {
    return Protocol;
  }

  /**
   * Sets the value of the 'Protocol' field.
   * @param value the value to set.
   */
  public void setProtocol(java.lang.Integer value) {
    this.Protocol = value;
  }

  /**
   * Creates a new XdrL7Type RecordBuilder.
   * @return A new XdrL7Type RecordBuilder
   */
  public static xdrSchema.XdrL7Type.Builder newBuilder() {
    return new xdrSchema.XdrL7Type.Builder();
  }

  /**
   * Creates a new XdrL7Type RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new XdrL7Type RecordBuilder
   */
  public static xdrSchema.XdrL7Type.Builder newBuilder(xdrSchema.XdrL7Type.Builder other) {
    return new xdrSchema.XdrL7Type.Builder(other);
  }

  /**
   * Creates a new XdrL7Type RecordBuilder by copying an existing XdrL7Type instance.
   * @param other The existing instance to copy.
   * @return A new XdrL7Type RecordBuilder
   */
  public static xdrSchema.XdrL7Type.Builder newBuilder(xdrSchema.XdrL7Type other) {
    return new xdrSchema.XdrL7Type.Builder(other);
  }

  /**
   * RecordBuilder for XdrL7Type instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<XdrL7Type>
    implements org.apache.avro.data.RecordBuilder<XdrL7Type> {

    private int AppStatus;
    private int ClassID;
    private int Protocol;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(xdrSchema.XdrL7Type.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.AppStatus)) {
        this.AppStatus = data().deepCopy(fields()[0].schema(), other.AppStatus);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.ClassID)) {
        this.ClassID = data().deepCopy(fields()[1].schema(), other.ClassID);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.Protocol)) {
        this.Protocol = data().deepCopy(fields()[2].schema(), other.Protocol);
        fieldSetFlags()[2] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing XdrL7Type instance
     * @param other The existing instance to copy.
     */
    private Builder(xdrSchema.XdrL7Type other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.AppStatus)) {
        this.AppStatus = data().deepCopy(fields()[0].schema(), other.AppStatus);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.ClassID)) {
        this.ClassID = data().deepCopy(fields()[1].schema(), other.ClassID);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.Protocol)) {
        this.Protocol = data().deepCopy(fields()[2].schema(), other.Protocol);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'AppStatus' field.
      * @return The value.
      */
    public java.lang.Integer getAppStatus() {
      return AppStatus;
    }

    /**
      * Sets the value of the 'AppStatus' field.
      * @param value The value of 'AppStatus'.
      * @return This builder.
      */
    public xdrSchema.XdrL7Type.Builder setAppStatus(int value) {
      validate(fields()[0], value);
      this.AppStatus = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'AppStatus' field has been set.
      * @return True if the 'AppStatus' field has been set, false otherwise.
      */
    public boolean hasAppStatus() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'AppStatus' field.
      * @return This builder.
      */
    public xdrSchema.XdrL7Type.Builder clearAppStatus() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'ClassID' field.
      * @return The value.
      */
    public java.lang.Integer getClassID() {
      return ClassID;
    }

    /**
      * Sets the value of the 'ClassID' field.
      * @param value The value of 'ClassID'.
      * @return This builder.
      */
    public xdrSchema.XdrL7Type.Builder setClassID(int value) {
      validate(fields()[1], value);
      this.ClassID = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'ClassID' field has been set.
      * @return True if the 'ClassID' field has been set, false otherwise.
      */
    public boolean hasClassID() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'ClassID' field.
      * @return This builder.
      */
    public xdrSchema.XdrL7Type.Builder clearClassID() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'Protocol' field.
      * @return The value.
      */
    public java.lang.Integer getProtocol() {
      return Protocol;
    }

    /**
      * Sets the value of the 'Protocol' field.
      * @param value The value of 'Protocol'.
      * @return This builder.
      */
    public xdrSchema.XdrL7Type.Builder setProtocol(int value) {
      validate(fields()[2], value);
      this.Protocol = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'Protocol' field has been set.
      * @return True if the 'Protocol' field has been set, false otherwise.
      */
    public boolean hasProtocol() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'Protocol' field.
      * @return This builder.
      */
    public xdrSchema.XdrL7Type.Builder clearProtocol() {
      fieldSetFlags()[2] = false;
      return this;
    }

    @SuppressWarnings("unchecked")
    public XdrL7Type build() {
      try {
        XdrL7Type record = new XdrL7Type();
        record.AppStatus = fieldSetFlags()[0] ? this.AppStatus : (java.lang.Integer) defaultValue(fields()[0]);
        record.ClassID = fieldSetFlags()[1] ? this.ClassID : (java.lang.Integer) defaultValue(fields()[1]);
        record.Protocol = fieldSetFlags()[2] ? this.Protocol : (java.lang.Integer) defaultValue(fields()[2]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<XdrL7Type>
    WRITER$ = (org.apache.avro.io.DatumWriter<XdrL7Type>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<XdrL7Type>
    READER$ = (org.apache.avro.io.DatumReader<XdrL7Type>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}