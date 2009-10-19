package org.gwtunite.client.rpc;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.gwtunite.client.commons.Logging;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.impl.AbstractSerializationStreamReader;
import com.google.gwt.user.client.rpc.impl.Serializer;

public class GwtUniteSerializationStreamReader extends AbstractSerializationStreamReader {
	private final Serializer serializer;
	private final List<String> tokenList = new ArrayList<String>();	
	private String[] stringTable;
	private int tokenListIndex;
	
	 /**
	   * Map of {@link Class} objects to {@link ValueReader}s.
	   */
	  private static final Map<String, ValueReader> CLASS_TO_VALUE_READER = new IdentityHashMap<String, ValueReader>();

	  {
	    CLASS_TO_VALUE_READER.put(boolean.class.getName(), ValueReader.BOOLEAN);
	    CLASS_TO_VALUE_READER.put(byte.class.getName(), ValueReader.BYTE);
	    CLASS_TO_VALUE_READER.put(char.class.getName(), ValueReader.CHAR);
	    CLASS_TO_VALUE_READER.put(double.class.getName(), ValueReader.DOUBLE);
	    CLASS_TO_VALUE_READER.put(float.class.getName(), ValueReader.FLOAT);
	    CLASS_TO_VALUE_READER.put(int.class.getName(), ValueReader.INT);
	    CLASS_TO_VALUE_READER.put(long.class.getName(), ValueReader.LONG);
	    CLASS_TO_VALUE_READER.put(Object.class.getName(), ValueReader.OBJECT);
	    CLASS_TO_VALUE_READER.put(short.class.getName(), ValueReader.SHORT);
	    CLASS_TO_VALUE_READER.put(String.class.getName(), ValueReader.STRING);
	  }
	  
	
	public GwtUniteSerializationStreamReader(Serializer serializer) {
		this.serializer = serializer;
	}

	@Override
	public void prepareToRead(String encodedTokens) throws SerializationException {
	    tokenList.clear();
	    tokenListIndex = 0;
	    stringTable = null;

	    int idx = 0, nextIdx;
	    while (-1 != (nextIdx = encodedTokens.indexOf(RPC_SEPARATOR_CHAR, idx))) {
	      String current = encodedTokens.substring(idx, nextIdx);
	      tokenList.add(current);
	      idx = nextIdx + 1;
	    }
	    if (idx == 0) {
	      // Didn't find any separator, assume an older version with different
	      // separators and get the version as the sequence of digits at the
	      // beginning of the encoded string.
	      while (idx < encodedTokens.length()
	          && Character.isDigit(encodedTokens.charAt(idx))) {
	        ++idx;
	      }
	      if (idx == 0) {
	        throw new IncompatibleRemoteServiceException(
	            "Malformed or old RPC message received - expecting version "
	            + SERIALIZATION_STREAM_VERSION);
	      } else {
	        int version = Integer.valueOf(encodedTokens.substring(0, idx));
	        throw new IncompatibleRemoteServiceException("Expecting version "
	            + SERIALIZATION_STREAM_VERSION + " from client, got " + version
	            + ".");
	      }
	    }

	    super.prepareToRead(encodedTokens);

	    // Check the RPC version number sent by the client
	    if (getVersion() != SERIALIZATION_STREAM_VERSION) {
	      throw new IncompatibleRemoteServiceException("Expecting version "
	          + SERIALIZATION_STREAM_VERSION + " from client, got " + getVersion()
	          + ".");
	    }

	    // Read the type name table
	    //
	    deserializeStringTable();

	    // We don't need these, but we need them out of the stream
	    String moduleBaseURL = readString();
	    String strongName = readString();
	}
	
	@Override
	protected Object deserialize(String typeSignature) throws SerializationException {
		Logging.log("Deserializing : "+typeSignature);
		
	    int id = reserveDecodedObjectIndex();
	    Object instance = serializer.instantiate(this, typeSignature);
	    Logging.log("Instantiated " + instance);
	    rememberDecodedObject(id, instance);
	    serializer.deserialize(this, instance, typeSignature);
	    return instance;
	}

	  public Object deserializeValue(String type) throws SerializationException {
		    ValueReader valueReader = CLASS_TO_VALUE_READER.get(type);
		    if (valueReader != null) {
		      return valueReader.readValue(this);
		    } else {
		      // Arrays of primitive or reference types need to go through readObject.
		      return ValueReader.OBJECT.readValue(this);
		    }
	  }

	
  private void deserializeStringTable() throws SerializationException {
	    int typeNameCount = readInt();
	    List<String> buffer = new ArrayList<String>();
	    for (int typeNameIndex = 0; typeNameIndex < typeNameCount; ++typeNameIndex) {
	      String str = extract();
	      // Change quoted characters back.
	      int idx = str.indexOf('\\');
	      if (idx >= 0) {
	        StringBuilder buf = new StringBuilder();
	        int pos = 0;
	        while (idx >= 0) {
	          buf.append(str.substring(pos, idx));
	          if (++idx == str.length()) {
	            throw new SerializationException("Unmatched backslash: \""
	                + str + "\"");
	          }
	          char ch = str.charAt(idx);
	          pos = idx + 1;
	          switch (ch) {
	            case '0':
	              buf.append('\u0000');
	              break;
	            case '!':
	              buf.append(RPC_SEPARATOR_CHAR);
	              break;
	            case '\\':
	              buf.append(ch);
	              break;
	            case 'u':
	              try {
	                ch = (char) Integer.parseInt(str.substring(idx + 1, idx + 5), 16);
	              } catch (NumberFormatException e) {
	                throw new SerializationException(
	                    "Invalid Unicode escape sequence in \"" + str + "\"");
	              }
	              buf.append(ch);
	              pos += 4;
	              break;
	            default:
	              throw new SerializationException("Unexpected escape character "
	                  + ch + " after backslash: \"" + str + "\"");
	          }
	          idx = str.indexOf('\\', pos);
	        }
	        buf.append(str.substring(pos));
	        str = buf.toString();
	      }
	      buffer.add(str);
	    }

	    if (buffer.size() != typeNameCount) {
	      throw new SerializationException("Expected " + typeNameCount
	          + " string table elements; received " + buffer.size());
	    }

	    stringTable = buffer.toArray(new String[buffer.size()]);
  	}
	
	@Override
	protected String getString(int index) {
		if (index == 0) {
		  return null;
		}
		
		return stringTable[index - 1];
	}

	@Override
	public boolean readBoolean() throws SerializationException {
		return Boolean.parseBoolean(extract());
	}

	@Override
	public byte readByte() throws SerializationException {
		return Byte.parseByte(extract());
	}

	@Override
	public char readChar() throws SerializationException {
		return (char) Integer.parseInt(extract());
	}

	@Override
	public double readDouble() throws SerializationException {
		return Double.parseDouble(extract());
	}

	@Override
	public float readFloat() throws SerializationException {
		return Float.parseFloat(extract());
	}

	@Override
	public int readInt() throws SerializationException {
		return Integer.parseInt(extract());
	}

	@Override
	public long readLong() throws SerializationException {
		return Long.parseLong(extract());
	}

	@Override
	public short readShort() throws SerializationException {
		return Short.parseShort(extract());
	}

	@Override
	public String readString() throws SerializationException {
		return getString(readInt());
	}
	
	private String extract() throws SerializationException {
		try {
		  return tokenList.get(tokenListIndex++);
		} catch (IndexOutOfBoundsException e) {
		  throw new SerializationException("Too few tokens in RPC request", e);
		}
	}
	
	  /**
	   * Enumeration used to provided typed instance readers.
	   */
	  private enum ValueReader {
	    BOOLEAN {
	      @Override
	      Object readValue(GwtUniteSerializationStreamReader stream)
	          throws SerializationException {
	        return stream.readBoolean();
	      }
	    },
	    BYTE {
	      @Override
	      Object readValue(GwtUniteSerializationStreamReader stream)
	          throws SerializationException {
	        return stream.readByte();
	      }
	    },
	    CHAR {
	      @Override
	      Object readValue(GwtUniteSerializationStreamReader stream)
	          throws SerializationException {
	        return stream.readChar();
	      }
	    },
	    DOUBLE {
	      @Override
	      Object readValue(GwtUniteSerializationStreamReader stream)
	          throws SerializationException {
	        return stream.readDouble();
	      }
	    },
	    FLOAT {
	      @Override
	      Object readValue(GwtUniteSerializationStreamReader stream)
	          throws SerializationException {
	        return stream.readFloat();
	      }
	    },
	    INT {
	      @Override
	      Object readValue(GwtUniteSerializationStreamReader stream)
	          throws SerializationException {
	        return stream.readInt();
	      }
	    },
	    LONG {
	      @Override
	      Object readValue(GwtUniteSerializationStreamReader stream)
	          throws SerializationException {
	        return stream.readLong();
	      }
	    },
	    OBJECT {
	      @Override
	      Object readValue(GwtUniteSerializationStreamReader stream)
	          throws SerializationException {
	        return stream.readObject();
	      }
	    },
	    SHORT {
	      @Override
	      Object readValue(GwtUniteSerializationStreamReader stream)
	          throws SerializationException {
	        return stream.readShort();
	      }
	    },
	    STRING {
	      @Override
	      Object readValue(GwtUniteSerializationStreamReader stream)
	          throws SerializationException {
	        return stream.readString();
	      }
	    };

	    abstract Object readValue(GwtUniteSerializationStreamReader stream)
	        throws SerializationException;
	  }
}
