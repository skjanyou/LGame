package loon.utils;

import java.io.IOException;

import loon.LRelease;

public class ArrayByteReader implements LRelease {

	private static final byte R = '\r';
	private static final byte N = '\n';

	private ArrayByte in;

	public ArrayByteReader(ArrayByte stream) {
		in = stream;
	}

	public void close() {
		in.close();
	}

	public void skip(long n) throws IOException {
		if (in == null) {
			return;
		}
		in.skip(n);
	}

	public int read() {
		int c = -1;
		if (in == null) {
			return c;
		}
		return c = in.readByte();
	}

	public int read(byte[] buf) {
		int c = -1;
		if (in == null) {
			return c;
		}
		return c = in.read(buf);
	}

	public int read(byte[] buf, int offset, int length) {
		int c = -1;
		if (in == null) {
			return c;
		}
		return c = in.read(buf, offset, length);
	}

	public String readLine() throws IOException {
		if (in == null) {
			return "";
		}
		if (in.available() <= 0) {
			return null;
		}
		StringBuilder sbr = new StringBuilder();
		int c = -1;
		boolean keepReading = true;
		do {
			c = in.readByte();
			switch (c) {
			case N:
				keepReading = false;
				break;
			case R:
				continue;
			case -1:
				return null;
			default:
				sbr.append((char) c);
			}
		} while (keepReading);
		return sbr.toString();
	}
}
