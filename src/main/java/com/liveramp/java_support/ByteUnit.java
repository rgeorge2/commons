package com.liveramp.java_support;

public enum ByteUnit {

  BYTES {
    public long toBytes(long bytes) {
      return bytes;
    }

    public long toKilobytes(long bytes) {
      return bytes / 1000L;
    }

    public long toMegabytes(long bytes) {
      return bytes / 1000000L;
    }

    public long toGigabytes(long bytes) {
      return bytes / 1000000000L;
    }

    public long toTerabytes(long bytes) {
      return bytes / 1000000000000L;
    }

    public long toPetabytes(long bytes) {
      return bytes / 1000000000000000L;
    }

    public long toKibibytes(long bytes) {
      return bytes / (Double.valueOf(Math.pow(2, 10L)).longValue());
    }

    public long toMibibytes(long bytes) {
      return bytes / (Double.valueOf(Math.pow(2, 20L)).longValue());
    }

    public long toGibibytes(long bytes) {
      return bytes / (Double.valueOf(Math.pow(2, 30L)).longValue());
    }

    public long toTebibytes(long bytes) {
      return bytes / (Double.valueOf(Math.pow(2, 40L)).longValue());
    }

    public long toPebibytes(long bytes) {
      return bytes / (Double.valueOf(Math.pow(2, 50L)).longValue());
    }

    public long convert(long other, ByteUnit from) {
      return from.toBytes(other);
    }
  },
  KILOBYTES {
    public long toBytes(long bytes) {
      return bytes * 1000;
    }

    public long toKilobytes(long bytes) {
      return bytes;
    }

    public long toMegabytes(long bytes) {
      return bytes / 1000L;
    }

    public long toGigabytes(long bytes) {
      return bytes / 1000000L;
    }

    public long toTerabytes(long bytes) {
      return bytes / 1000000000L;
    }

    public long toPetabytes(long bytes) {
      return bytes / 1000000000000L;
    }

    public long convert(long other, ByteUnit from) {
      return from.toKilobytes(other);
    }
  },
  MEGABYTES {
    public long toBytes(long bytes) {
      return bytes * 1000000L;
    }

    public long toKilobytes(long bytes) {
      return bytes * 1000L;
    }

    public long toMegabytes(long bytes) {
      return bytes;
    }

    public long toGigabytes(long bytes) {
      return bytes / 1000L;
    }

    public long toTerabytes(long bytes) {
      return bytes / 1000000L;
    }

    public long toPetabytes(long bytes) {
      return bytes / 1000000000L;
    }

    public long toKibibytes(long bytes) {
      return BYTES.toKibibytes(this.toBytes(bytes));
    }

    public long toMibibytes(long bytes) {
      return BYTES.toMibibytes(this.toBytes(bytes));
    }

    public long toGibibytes(long bytes) {
      return BYTES.toGibibytes(this.toBytes(bytes));
    }

    public long toTebibytes(long bytes) {
      return BYTES.toTebibytes(this.toBytes(bytes));
    }

    public long toPebibytes(long bytes) {
      return BYTES.toPebibytes(this.toBytes(bytes));
    }

    public long convert(long other, ByteUnit from) {
      return from.toMegabytes(other);
    }
  },
  GIGABYTES {
    public long toBytes(long bytes) {
      return bytes * 1000000000L;
    }

    public long toKilobytes(long bytes) {
      return bytes * 1000000L;
    }

    public long toMegabytes(long bytes) {
      return bytes * 1000L;
    }

    public long toGigabytes(long bytes) {
      return bytes;
    }

    public long toTerabytes(long bytes) {
      return bytes / 1000L;
    }

    public long toPetabytes(long bytes) {
      return bytes / 1000000L;
    }

    public long toKibibytes(long bytes) {
      return BYTES.toKibibytes(this.toBytes(bytes));
    }

    public long toMibibytes(long bytes) {
      return BYTES.toMibibytes(this.toBytes(bytes));
    }

    public long toGibibytes(long bytes) {
      return BYTES.toGibibytes(this.toBytes(bytes));
    }

    public long toTebibytes(long bytes) {
      return BYTES.toTebibytes(this.toBytes(bytes));
    }

    public long toPebibytes(long bytes) {
      return BYTES.toPebibytes(this.toBytes(bytes));
    }

    public long convert(long other, ByteUnit from) {
      return from.toGigabytes(other);
    }
  },
  TERABYTES {
    public long toBytes(long bytes) {
      return bytes * 1000000000000L;
    }

    public long toKilobytes(long bytes) {
      return bytes * 1000000000L;
    }

    public long toMegabytes(long bytes) {
      return bytes * 1000000L;
    }

    public long toGigabytes(long bytes) {
      return bytes * 1000L;
    }

    public long toTerabytes(long bytes) {
      return bytes;
    }

    public long toPetabytes(long bytes) {
      return bytes / 1000L;
    }

    public long toKibibytes(long bytes) {
      return BYTES.toKibibytes(this.toBytes(bytes));
    }

    public long toMibibytes(long bytes) {
      return BYTES.toMibibytes(this.toBytes(bytes));
    }

    public long toGibibytes(long bytes) {
      return BYTES.toGibibytes(this.toBytes(bytes));
    }

    public long toTebibytes(long bytes) {
      return BYTES.toTebibytes(this.toBytes(bytes));
    }

    public long toPebibytes(long bytes) {
      return BYTES.toPebibytes(this.toBytes(bytes));
    }

    public long convert(long other, ByteUnit from) {
      return from.toTerabytes(other);
    }
  },
  PETABYTES {
    public long toBytes(long bytes) {
      return bytes * 1000000000000000L;
    }

    public long toKilobytes(long bytes) {
      return bytes * 1000000000000L;
    }

    public long toMegabytes(long bytes) {
      return bytes * 1000000000L;
    }

    public long toGigabytes(long bytes) {
      return bytes * 1000000L;
    }

    public long toTerabytes(long bytes) {
      return bytes * 1000L;
    }

    public long toPetabytes(long bytes) {
      return bytes;
    }

    public long toKibibytes(long bytes) {
      return BYTES.toKibibytes(this.toBytes(bytes));
    }

    public long toMibibytes(long bytes) {
      return BYTES.toMibibytes(this.toBytes(bytes));
    }

    public long toGibibytes(long bytes) {
      return BYTES.toGibibytes(this.toBytes(bytes));
    }

    public long toTebibytes(long bytes) {
      return BYTES.toTebibytes(this.toBytes(bytes));
    }

    public long toPebibytes(long bytes) {
      return BYTES.toPebibytes(this.toBytes(bytes));
    }

    public long convert(long other, ByteUnit from) {
      return from.toPetabytes(other);
    }
  },
  KIBIBYTES {
    public long toBytes(long bytes) {
      return bytes * (Double.valueOf(Math.pow(2, 10L)).longValue());
    }

    public long toKilobytes(long bytes) {
      return BYTES.toKilobytes(this.toBytes(bytes));
    }

    public long toMegabytes(long bytes) {
      return BYTES.toMegabytes(this.toBytes(bytes));
    }

    public long toGigabytes(long bytes) {
      return BYTES.toGigabytes(this.toBytes(bytes));
    }

    public long toTerabytes(long bytes) {
      return BYTES.toTerabytes(this.toBytes(bytes));
    }

    public long toPetabytes(long bytes) {
      return BYTES.toPetabytes(this.toBytes(bytes));
    }

    public long toKibibytes(long bytes) {
      return bytes;
    }

    public long toMibibytes(long bytes) {
      return BYTES.toMibibytes(this.toBytes(bytes));
    }

    public long toGibibytes(long bytes) {
      return BYTES.toGibibytes(this.toBytes(bytes));
    }

    public long toTebibytes(long bytes) {
      return BYTES.toTebibytes(this.toBytes(bytes));
    }

    public long toPebibytes(long bytes) {
      return BYTES.toPebibytes(this.toBytes(bytes));
    }

    public long convert(long other, ByteUnit from) {
      return from.toPetabytes(other);
    }
  },
  MIBIBYTES {
    public long toBytes(long bytes) {
      return bytes * (Double.valueOf(Math.pow(2, 20L)).longValue());
    }

    public long toKilobytes(long bytes) {
      return BYTES.toKilobytes(this.toBytes(bytes));
    }

    public long toMegabytes(long bytes) {
      return BYTES.toMegabytes(this.toBytes(bytes));
    }

    public long toGigabytes(long bytes) {
      return BYTES.toGigabytes(this.toBytes(bytes));
    }

    public long toTerabytes(long bytes) {
      return BYTES.toTerabytes(this.toBytes(bytes));
    }

    public long toPetabytes(long bytes) {
      return BYTES.toPetabytes(this.toBytes(bytes));
    }

    public long toKibibytes(long bytes) {
      return BYTES.toKibibytes(this.toBytes(bytes));
    }

    public long toMibibytes(long bytes) {
      return BYTES.toMibibytes(this.toBytes(bytes));
    }

    public long toGibibytes(long bytes) {
      return BYTES.toGibibytes(this.toBytes(bytes));
    }

    public long toTebibytes(long bytes) {
      return BYTES.toTebibytes(this.toBytes(bytes));
    }

    public long toPebibytes(long bytes) {
      return BYTES.toPebibytes(this.toBytes(bytes));
    }

    public long convert(long other, ByteUnit from) {
      return from.toPetabytes(other);
    }
  },
  GIBIBYTES {
    public long toBytes(long bytes) {
      return bytes * (Double.valueOf(Math.pow(2, 30L)).longValue());
    }

    public long toKilobytes(long bytes) {
      return BYTES.toKilobytes(this.toBytes(bytes));
    }

    public long toMegabytes(long bytes) {
      return BYTES.toMegabytes(this.toBytes(bytes));
    }

    public long toGigabytes(long bytes) {
      return BYTES.toGigabytes(this.toBytes(bytes));
    }

    public long toTerabytes(long bytes) {
      return BYTES.toTerabytes(this.toBytes(bytes));
    }

    public long toPetabytes(long bytes) {
      return BYTES.toPetabytes(this.toBytes(bytes));
    }

    public long toKibibytes(long bytes) {
      return BYTES.toKibibytes(this.toBytes(bytes));
    }

    public long toMibibytes(long bytes) {
      return BYTES.toMibibytes(this.toBytes(bytes));
    }

    public long toGibibytes(long bytes) {
      return BYTES.toGibibytes(this.toBytes(bytes));
    }

    public long toTebibytes(long bytes) {
      return BYTES.toTebibytes(this.toBytes(bytes));
    }

    public long toPebibytes(long bytes) {
      return BYTES.toPebibytes(this.toBytes(bytes));
    }

    public long convert(long other, ByteUnit from) {
      return from.toPetabytes(other);
    }
  },
  PEBIBYTES {
    public long toBytes(long bytes) {
      return bytes * (Double.valueOf(Math.pow(2, 40L)).longValue());
    }

    public long toKilobytes(long bytes) {
      return BYTES.toKilobytes(this.toBytes(bytes));
    }

    public long toMegabytes(long bytes) {
      return BYTES.toMegabytes(this.toBytes(bytes));
    }

    public long toGigabytes(long bytes) {
      return BYTES.toGigabytes(this.toBytes(bytes));
    }

    public long toTerabytes(long bytes) {
      return BYTES.toTerabytes(this.toBytes(bytes));
    }

    public long toPetabytes(long bytes) {
      return BYTES.toPetabytes(this.toBytes(bytes));
    }

    public long toKibibytes(long bytes) {
      return BYTES.toKibibytes(this.toBytes(bytes));
    }

    public long toMibibytes(long bytes) {
      return BYTES.toMibibytes(this.toBytes(bytes));
    }

    public long toGibibytes(long bytes) {
      return BYTES.toGibibytes(this.toBytes(bytes));
    }

    public long toTebibytes(long bytes) {
      return BYTES.toTebibytes(this.toBytes(bytes));
    }

    public long toPebibytes(long bytes) {
      return BYTES.toPebibytes(this.toBytes(bytes));
    }

    public long convert(long other, ByteUnit from) {
      return from.toPetabytes(other);
    }
  },
  TEBIBYTES {
    public long toBytes(long bytes) {
      return bytes * (Double.valueOf(Math.pow(2, 50L)).longValue());
    }

    public long toKilobytes(long bytes) {
      return BYTES.toKilobytes(this.toBytes(bytes));
    }

    public long toMegabytes(long bytes) {
      return BYTES.toMegabytes(this.toBytes(bytes));
    }

    public long toGigabytes(long bytes) {
      return BYTES.toGigabytes(this.toBytes(bytes));
    }

    public long toTerabytes(long bytes) {
      return BYTES.toTerabytes(this.toBytes(bytes));
    }

    public long toPetabytes(long bytes) {
      return BYTES.toPetabytes(this.toBytes(bytes));
    }

    public long toKibibytes(long bytes) {
      return BYTES.toKibibytes(this.toBytes(bytes));
    }

    public long toMibibytes(long bytes) {
      return BYTES.toMibibytes(this.toBytes(bytes));
    }

    public long toGibibytes(long bytes) {
      return BYTES.toGibibytes(this.toBytes(bytes));
    }

    public long toTebibytes(long bytes) {
      return BYTES.toTebibytes(this.toBytes(bytes));
    }

    public long toPebibytes(long bytes) {
      return BYTES.toPebibytes(this.toBytes(bytes));
    }

    public long convert(long other, ByteUnit from) {
      return from.toPetabytes(other);
    }
  };

  public long toBytes(long bytes) {
    throw new AbstractMethodError();
  }

  public long toKilobytes(long bytes) {
    throw new AbstractMethodError();
  }

  public long toMegabytes(long bytes) {
    throw new AbstractMethodError();
  }

  public long toGigabytes(long bytes) {
    throw new AbstractMethodError();
  }

  public long toTerabytes(long bytes) {
    throw new AbstractMethodError();
  }

  public long toPetabytes(long bytes) {
    throw new AbstractMethodError();
  }

  public long convert(long other, ByteUnit from) {
    throw new AbstractMethodError();
  }

  public long toKibibytes(long bytes) {
    throw new AbstractMethodError();
  }

  public long toMibibytes(long bytes) {
    throw new AbstractMethodError();
  }

  public long toGibibytes(long bytes) {
    throw new AbstractMethodError();
  }

  public long toTebibytes(long bytes) {
    throw new AbstractMethodError();
  }

  public long toPebibytes(long bytes) {
    throw new AbstractMethodError();
  }

  public static void main(String[] args) {
    System.out.println(GIBIBYTES.toMibibytes(1));
  }
}
