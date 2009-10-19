package org.gwtunite.client.rpc;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import org.gwtunite.client.commons.Logging;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.impl.AbstractSerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.Serializer;

/**
 * For internal use only. Used for server call serialization. This class is
 * carefully matched with the client-side version.
 */
public final class GwtUniteSerializationStreamWriter extends AbstractSerializationStreamWriter {

	
  /**
   * Builds a string that evaluates into an array containing the given elements.
   * This class exists to work around a bug in IE6/7 that limits the size of
   * array literals.
   */
  public static class LengthConstrainedArray {
    public static final int MAXIMUM_ARRAY_LENGTH = 1 << 15;
    private static final String POSTLUDE = "])";
    private static final String PRELUDE = "].concat([";

    private final StringBuffer buffer;
    private int count = 0;
    private boolean needsComma = false;
    private int total = 0;

    public LengthConstrainedArray() {
      buffer = new StringBuffer();
    }

    public LengthConstrainedArray(int capacityGuess) {
      buffer = new StringBuffer(capacityGuess);
    }

    public void addToken(CharSequence token) {
      total++;
      if (count++ == MAXIMUM_ARRAY_LENGTH) {
        if (total == MAXIMUM_ARRAY_LENGTH + 1) {
          buffer.append(PRELUDE);
        } else {
          buffer.append("],[");
        }
        count = 0;
        needsComma = false;
      }

      if (needsComma) {
        buffer.append(",");
      } else {
        needsComma = true;
      }

      buffer.append(token);
    }

    public void addToken(int i) {
      addToken(String.valueOf(i));
    }

    @Override
    public String toString() {
      if (total > MAXIMUM_ARRAY_LENGTH) {
        return "[" + buffer.toString() + POSTLUDE;
      } else {
        return "[" + buffer.toString() + "]";
      }
    }
  }

  /**
   * Enumeration used to provided typed instance writers.
   */
  private enum ValueWriter {
    BOOLEAN {
      @Override
      void write(GwtUniteSerializationStreamWriter stream, Object instance) {
        stream.writeBoolean(((Boolean) instance).booleanValue());
      }
    },
    BYTE {
      @Override
      void write(GwtUniteSerializationStreamWriter stream, Object instance) {
        stream.writeByte(((Byte) instance).byteValue());
      }
    },
    CHAR {
      @Override
      void write(GwtUniteSerializationStreamWriter stream, Object instance) {
        stream.writeChar(((Character) instance).charValue());
      }
    },
    DOUBLE {
      @Override
      void write(GwtUniteSerializationStreamWriter stream, Object instance) {
        stream.writeDouble(((Double) instance).doubleValue());
      }
    },
    FLOAT {
      @Override
      void write(GwtUniteSerializationStreamWriter stream, Object instance) {
        stream.writeFloat(((Float) instance).floatValue());
      }
    },
    INT {
      @Override
      void write(GwtUniteSerializationStreamWriter stream, Object instance) {
        stream.writeInt(((Integer) instance).intValue());
      }
    },
    LONG {
      @Override
      void write(GwtUniteSerializationStreamWriter stream, Object instance) {
        stream.writeLong(((Long) instance).longValue());
      }
    },
    OBJECT {
      @Override
      void write(GwtUniteSerializationStreamWriter stream, Object instance)
          throws SerializationException {
        stream.writeObject(instance);
      }
    },
    SHORT {
      @Override
      void write(GwtUniteSerializationStreamWriter stream, Object instance) {
        stream.writeShort(((Short) instance).shortValue());
      }
    },
    STRING {
      @Override
      void write(GwtUniteSerializationStreamWriter stream, Object instance) {
        stream.writeString((String) instance);
      }
    };

    abstract void write(GwtUniteSerializationStreamWriter stream, Object instance)
        throws SerializationException;
  }

  /**
   * Enumeration used to provided typed vector writers.
   */
//  private enum VectorWriter {
//    BOOLEAN_VECTOR {
//      @Override
//      void write(ServerSerializationStreamWriter stream, Object instance) {
//        boolean[] vector = (boolean[]) instance;
//        stream.writeInt(vector.length);
//        for (int i = 0, n = vector.length; i < n; ++i) {
//          stream.writeBoolean(vector[i]);
//        }
//      }
//    },
//    BYTE_VECTOR {
//      @Override
//      void write(ServerSerializationStreamWriter stream, Object instance) {
//        byte[] vector = (byte[]) instance;
//        stream.writeInt(vector.length);
//        for (int i = 0, n = vector.length; i < n; ++i) {
//          stream.writeByte(vector[i]);
//        }
//      }
//    },
//    CHAR_VECTOR {
//      @Override
//      void write(ServerSerializationStreamWriter stream, Object instance) {
//        char[] vector = (char[]) instance;
//        stream.writeInt(vector.length);
//        for (int i = 0, n = vector.length; i < n; ++i) {
//          stream.writeChar(vector[i]);
//        }
//      }
//    },
//    DOUBLE_VECTOR {
//      @Override
//      void write(ServerSerializationStreamWriter stream, Object instance) {
//        double[] vector = (double[]) instance;
//        stream.writeInt(vector.length);
//        for (int i = 0, n = vector.length; i < n; ++i) {
//          stream.writeDouble(vector[i]);
//        }
//      }
//    },
//    FLOAT_VECTOR {
//      @Override
//      void write(ServerSerializationStreamWriter stream, Object instance) {
//        float[] vector = (float[]) instance;
//        stream.writeInt(vector.length);
//        for (int i = 0, n = vector.length; i < n; ++i) {
//          stream.writeFloat(vector[i]);
//        }
//      }
//    },
//    INT_VECTOR {
//      @Override
//      void write(ServerSerializationStreamWriter stream, Object instance) {
//        int[] vector = (int[]) instance;
//        stream.writeInt(vector.length);
//        for (int i = 0, n = vector.length; i < n; ++i) {
//          stream.writeInt(vector[i]);
//        }
//      }
//    },
//    LONG_VECTOR {
//      @Override
//      void write(ServerSerializationStreamWriter stream, Object instance) {
//        long[] vector = (long[]) instance;
//        stream.writeInt(vector.length);
//        for (int i = 0, n = vector.length; i < n; ++i) {
//          stream.writeLong(vector[i]);
//        }
//      }
//    },
//    OBJECT_VECTOR {
//      @Override
//      void write(ServerSerializationStreamWriter stream, Object instance)
//          throws SerializationException {
//        Object[] vector = (Object[]) instance;
//        stream.writeInt(vector.length);
//        for (int i = 0, n = vector.length; i < n; ++i) {
//          stream.writeObject(vector[i]);
//        }
//      }
//    },
//    SHORT_VECTOR {
//      @Override
//      void write(ServerSerializationStreamWriter stream, Object instance) {
//        short[] vector = (short[]) instance;
//        stream.writeInt(vector.length);
//        for (int i = 0, n = vector.length; i < n; ++i) {
//          stream.writeShort(vector[i]);
//        }
//      }
//    },
//    STRING_VECTOR {
//      @Override
//      void write(ServerSerializationStreamWriter stream, Object instance) {
//        String[] vector = (String[]) instance;
//        stream.writeInt(vector.length);
//        for (int i = 0, n = vector.length; i < n; ++i) {
//          stream.writeString(vector[i]);
//        }
//      }
//    };
//
//    abstract void write(ServerSerializationStreamWriter stream, Object instance)
//        throws SerializationException;
//  }

  /**
   * Map of {@link Class} objects to {@link ValueWriter}s.
   */
  private static final Map<Class<?>, ValueWriter> CLASS_TO_VALUE_WRITER = new IdentityHashMap<Class<?>, ValueWriter>();

  /**
   * Number of escaped JS Chars.
   */
  private static final int NUMBER_OF_JS_ESCAPED_CHARS = 128;

  /**
   * A list of any characters that need escaping when printing a JavaScript
   * string literal. Contains a 0 if the character does not need escaping,
   * otherwise contains the character to escape with.
   */
  private static final char[] JS_CHARS_ESCAPED = new char[NUMBER_OF_JS_ESCAPED_CHARS];

  /**
   * This defines the character used by JavaScript to mark the start of an
   * escape sequence.
   */
  private static final char JS_ESCAPE_CHAR = '\\';

  /**
   * This defines the character used to enclose JavaScript strings.
   */
  private static final char JS_QUOTE_CHAR = '\"';

  /**
   * Index into this array using a nibble, 4 bits, to get the corresponding
   * hexa-decimal character representation.
   */
  private static final char NIBBLE_TO_HEX_CHAR[] = {
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
      'E', 'F'};

  private static final char NON_BREAKING_HYPHEN = '\u2011';

  static {
    /*
     * NOTE: The JS VM in IE6 & IE7 do not interpret \v correctly. They convert
     * JavaScript Vertical Tab character '\v' into 'v'. As such, we do not use
     * the short form of the unicode escape here.
     */
    JS_CHARS_ESCAPED['\u0000'] = '0';
    JS_CHARS_ESCAPED['\b'] = 'b';
    JS_CHARS_ESCAPED['\t'] = 't';
    JS_CHARS_ESCAPED['\n'] = 'n';
    JS_CHARS_ESCAPED['\f'] = 'f';
    JS_CHARS_ESCAPED['\r'] = 'r';
    JS_CHARS_ESCAPED[JS_ESCAPE_CHAR] = JS_ESCAPE_CHAR;
    JS_CHARS_ESCAPED[JS_QUOTE_CHAR] = JS_QUOTE_CHAR;

    CLASS_TO_VALUE_WRITER.put(boolean.class, ValueWriter.BOOLEAN);
    CLASS_TO_VALUE_WRITER.put(byte.class, ValueWriter.BYTE);
    CLASS_TO_VALUE_WRITER.put(char.class, ValueWriter.CHAR);
    CLASS_TO_VALUE_WRITER.put(double.class, ValueWriter.DOUBLE);
    CLASS_TO_VALUE_WRITER.put(float.class, ValueWriter.FLOAT);
    CLASS_TO_VALUE_WRITER.put(int.class, ValueWriter.INT);
    CLASS_TO_VALUE_WRITER.put(long.class, ValueWriter.LONG);
    CLASS_TO_VALUE_WRITER.put(Object.class, ValueWriter.OBJECT);
    CLASS_TO_VALUE_WRITER.put(short.class, ValueWriter.SHORT);
    CLASS_TO_VALUE_WRITER.put(String.class, ValueWriter.STRING);
  }

  /**
   * This method takes a string and outputs a JavaScript string literal. The
   * data is surrounded with quotes, and any contained characters that need to
   * be escaped are mapped onto their escape sequence.
   * 
   * Assumptions: We are targeting a version of JavaScript that that is later
   * than 1.3 that supports unicode strings.
   */
  private static String escapeString(String toEscape) {
    // make output big enough to escape every character (plus the quotes)
    char[] input = toEscape.toCharArray();
    CharVector charVector = new CharVector(input.length * 2 + 2, input.length);

    charVector.add(JS_QUOTE_CHAR);

    for (int i = 0, n = input.length; i < n; ++i) {
      char c = input[i];
      if (needsUnicodeEscape(c)) {
        unicodeEscape(c, charVector);
      } else {
        charVector.add(c);
      }
    }

    charVector.add(JS_QUOTE_CHAR);
    return String.valueOf(charVector.asArray(), 0, charVector.getSize());
  }

  /**
   * Returns <code>true</code> if the character requires the \\uXXXX unicode
   * character escape sequence. This is necessary if the raw character could be
   * consumed and/or interpreted as a special character when the JSON encoded
   * response is evaluated. For example, 0x2028 and 0x2029 are alternate line
   * endings for JS per ECMA-232, which are respected by Firefox and Mozilla.
   * 
   * @param ch character to check
   * @return <code>true</code> if the character requires the \\uXXXX unicode
   *         character escape
   * 
   * Notes:
   * <ol>
   * <li> The following cases are a more conservative set of cases which are are
   * in the future proofing space as opposed to the required minimal set. We
   * could remove these and still pass our tests.
   * <ul>
   * <li>UNASSIGNED - 6359</li>
   * <li>NON_SPACING_MARK - 530</li>
   * <li>ENCLOSING_MARK - 10</li>
   * <li>COMBINING_SPACE_MARK - 131</li>
   * <li>SPACE_SEPARATOR - 19</li>
   * <li>CONTROL - 65</li>
   * <li>PRIVATE_USE - 6400</li>
   * <li>DASH_PUNCTUATION - 1</li>
   * <li>Total Characters Escaped: 13515</li>
   * </ul>
   * </li>
   * <li> The following cases are the minimal amount of escaping required to
   * prevent test failure.
   * <ul>
   * <li>LINE_SEPARATOR - 1</li>
   * <li>PARAGRAPH_SEPARATOR - 1</li>
   * <li>FORMAT - 32</li>
   * <li>SURROGATE - 2048</li>
   * <li>Total Characters Escaped: 2082</li>
   * </li>
   * </ul>
   * </li>
   * </ol>
   */
  private static boolean needsUnicodeEscape(char ch) {
    switch (ch) {
      case ' ':
        // ASCII space gets caught in SPACE_SEPARATOR below, but does not
        // need to be escaped
        return false;
      case JS_QUOTE_CHAR:
      case JS_ESCAPE_CHAR:
        // these must be quoted or they will break the protocol
        return true;
      case NON_BREAKING_HYPHEN:
          // This can be expanded into a break followed by a hyphen
          return true;
      default:
    	  /**
    	   * Note : Not sure what to do here since we GWT doesn't support Character#getType.
    	   * What's more, the implementation of Character.getType is complicated and dependent on the
    	   * character set being used.
    	   */
//        switch (Character.getType(ch)) {
//          // Conservative
//          case Character.COMBINING_SPACING_MARK:
//          case Character.ENCLOSING_MARK:
//          case Character.NON_SPACING_MARK:
//          case Character.UNASSIGNED:
//          case Character.PRIVATE_USE:
//          case Character.SPACE_SEPARATOR:
//          case Character.CONTROL:
//
//            // Minimal
//          case Character.LINE_SEPARATOR:
//          case Character.FORMAT:
//          case Character.PARAGRAPH_SEPARATOR:
//          case Character.SURROGATE:
//            return true;
//
//          default:
//            break;
//        }
//        break;
    }
    return false;
  }

  /**
   * Writes a safe escape sequence for a character.  Some characters have a
   * short form, such as \n for U+000D, while others are represented as \\xNN
   * or \\uNNNN.
   * 
   * @param ch character to unicode escape
   * @param charVector char vector to receive the unicode escaped representation
   */
  private static void unicodeEscape(char ch, CharVector charVector) {
    charVector.add(JS_ESCAPE_CHAR);
    if (ch < NUMBER_OF_JS_ESCAPED_CHARS && JS_CHARS_ESCAPED[ch] != 0) {
      charVector.add(JS_CHARS_ESCAPED[ch]);
    } else if (ch < 256) {
      charVector.add('x');
      charVector.add(NIBBLE_TO_HEX_CHAR[(ch >> 4) & 0x0F]);
      charVector.add(NIBBLE_TO_HEX_CHAR[ch & 0x0F]);
    } else {
      charVector.add('u');
      charVector.add(NIBBLE_TO_HEX_CHAR[(ch >> 12) & 0x0F]);
      charVector.add(NIBBLE_TO_HEX_CHAR[(ch >> 8) & 0x0F]);
      charVector.add(NIBBLE_TO_HEX_CHAR[(ch >> 4) & 0x0F]);
      charVector.add(NIBBLE_TO_HEX_CHAR[ch & 0x0F]);
    }
  }

  private final Serializer serializer;

  private ArrayList<String> tokenList = new ArrayList<String>();

  private int tokenListCharCount;

  public GwtUniteSerializationStreamWriter(Serializer serializer) {
	  this.serializer = serializer;
  }

  @Override
  public void prepareToWrite() {
    super.prepareToWrite();
    tokenList.clear();
    tokenListCharCount = 0;
  }

  public void serializeValue(Object value, Class<?> type)
      throws SerializationException {
    ValueWriter valueWriter = CLASS_TO_VALUE_WRITER.get(type);
    Logging.log("Using value Writer ="+valueWriter);
    if (valueWriter != null) {
      valueWriter.write(this, value);
    } else {
      // Arrays of primitive or reference types need to go through writeObject.
      ValueWriter.OBJECT.write(this, value);
    }
  }

  /**
   * Build an array of JavaScript string literals that can be decoded by the
   * client via the eval function.
   * 
   * NOTE: We build the array in reverse so the client can simply use the pop
   * function to remove the next item from the list.
   */
  @Override
  public String toString() {
    // Build a JavaScript string (with escaping, of course).
    // We take a guess at how big to make to buffer to avoid numerous resizes.
    //
    int capacityGuess = 2 * tokenListCharCount + 2 * tokenList.size();
    LengthConstrainedArray stream = new LengthConstrainedArray(capacityGuess);
    writePayload(stream);
    writeStringTable(stream);
    writeHeader(stream);

    return stream.toString();
  }

  public void writeLong(long fieldValue) {
    /*
     * Client code represents longs internally as an array of two Numbers. In
     * order to make serialization of longs faster, we'll send the component
     * parts so that the value can be directly reconstituted on the client.
     */
    double[] parts = makeLongComponents((int) (fieldValue >> 32),
        (int) fieldValue);
    assert parts.length == 2;
    writeDouble(parts[0]);
    writeDouble(parts[1]);
  }

  @Override
  protected void append(String token) {
    tokenList.add(token);
    if (token != null) {
      tokenListCharCount += token.length();
    }
  }

  @Override
  protected String getObjectTypeSignature(Object o) {
    Class<?> clazz = o.getClass();

    if (o instanceof Enum) {
      Enum<?> e = (Enum<?>) o;
      clazz = e.getDeclaringClass();
    }

    String typeName = clazz.getName();

    String serializationSignature = serializer.getSerializationSignature(typeName);
    if (serializationSignature != null) {
      typeName += "/" + serializationSignature;
    }
    return typeName;
  }


  @Override
  protected void serialize(Object instance, String typeSignature) throws SerializationException {
    serializer.serialize(this, instance, typeSignature);
  }

  /**
   * Notice that the field are written in reverse order that the client can just
   * pop items out of the stream.
   */
  private void writeHeader(LengthConstrainedArray stream) {
    stream.addToken(getFlags());
    stream.addToken(getVersion());
  }

  private void writePayload(LengthConstrainedArray stream) {
    ListIterator<String> tokenIterator = tokenList.listIterator(tokenList.size());
    while (tokenIterator.hasPrevious()) {
      stream.addToken(tokenIterator.previous());
    }
  }

  private void writeStringTable(LengthConstrainedArray stream) {
    LengthConstrainedArray tableStream = new LengthConstrainedArray();
    for (String s : getStringTable()) {
      tableStream.addToken(escapeString(s));
    }
    stream.addToken(tableStream.toString());
  }
}
