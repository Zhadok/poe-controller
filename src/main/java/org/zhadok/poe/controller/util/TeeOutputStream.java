package org.zhadok.poe.controller.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * https://stackoverflow.com/questions/7987395/how-to-write-data-to-two-java-io-outputstream-objects-at-once
 */
public final class TeeOutputStream extends OutputStream {

	  private final OutputStream out;
	  private final OutputStream tee;

	  public TeeOutputStream(OutputStream out, OutputStream tee) {
	    if (out == null)
	      throw new NullPointerException();
	    else if (tee == null)
	      throw new NullPointerException();

	    this.out = out;
	    this.tee = tee;
	  }

	  @Override
	  public void write(int b) throws IOException {
	    out.write(b);
	    tee.write(b);
	  }

	  @Override
	  public void write(byte[] b) throws IOException {
	    out.write(b);
	    tee.write(b);
	  }

	  @Override
	  public void write(byte[] b, int off, int len) throws IOException {
	    out.write(b, off, len);
	    tee.write(b, off, len);
	  }

	  @Override
	  public void flush() throws IOException {
	    out.flush();
	    tee.flush();
	  }

	  @Override
	  public void close() throws IOException {
	    try {
	      out.close();
	    } finally {
	      tee.close();
	    }
	  }
	}
