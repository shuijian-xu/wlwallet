package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.bouncycastle.util.Arrays;

public class Script {

	public Stack cmds;

	public Script() {
		this(new Stack());
	}

	public Script(Stack cmds) {
		super();
		this.cmds = cmds;
	}

	public static Script parse(ByteArrayInputStream s) {

		BigInteger length = Utils.readVarint(s);

		BigInteger count = BigInteger.ZERO;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Stack cmds = new Stack();

		while (count.compareTo(length) < 0) {
			byte[] current_bin = new byte[1];
			try {
				s.read(current_bin);
				count = count.add(BigInteger.ONE);
				BigInteger current = new BigInteger(1, current_bin);
				int current_int = current.intValue();
				if (current_int >= 1 && current_int <= 75) {
					int n = current_int;
					byte[] data = new byte[n];
					s.read(data);
					cmds.push(data);
					count = count.add(BigInteger.valueOf(n));
				} else if (current_int == 76) {
					byte[] data_length_bin = new byte[1];
					BigInteger data_length = Utils
							.little_endian_to_biginteger(data_length_bin);
					byte[] data = new byte[data_length.intValue()];
					s.read(data);
					cmds.push(data);
					count = count.add(data_length).add(BigInteger.ONE);
				} else if (current_int == 77) {
					byte[] data_length_bin = new byte[2];
					BigInteger data_length = Utils
							.little_endian_to_biginteger(data_length_bin);
					byte[] data = new byte[data_length.intValue()];
					s.read(data);
					cmds.push(data);
					count = count.add(data_length).add(BigInteger.valueOf(2));
				} else {
					cmds.push(current_bin);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Script(cmds);
	}

	public static Script parse(DataInputStream s) {

		BigInteger length = Utils.readVarint(s);

		BigInteger count = BigInteger.ZERO;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Stack cmds = new Stack();

		while (count.compareTo(length) < 0) {
			byte[] current_bin = new byte[1];
			try {
				s.read(current_bin);
				count = count.add(BigInteger.ONE);
				BigInteger current = new BigInteger(1, current_bin);
				int current_int = current.intValue();
				if (current_int >= 1 && current_int <= 75) {
					int n = current_int;
					byte[] data = new byte[n];
					s.read(data);
					cmds.push(data);
					count = count.add(BigInteger.valueOf(n));
				} else if (current_int == 76) {
					byte[] data_length_bin = new byte[1];
					BigInteger data_length = Utils
							.little_endian_to_biginteger(data_length_bin);
					byte[] data = new byte[data_length.intValue()];
					s.read(data);
					cmds.push(data);
					count = count.add(data_length).add(BigInteger.ONE);
				} else if (current_int == 77) {
					byte[] data_length_bin = new byte[2];
					BigInteger data_length = Utils
							.little_endian_to_biginteger(data_length_bin);
					byte[] data = new byte[data_length.intValue()];
					s.read(data);
					cmds.push(data);
					count = count.add(data_length).add(BigInteger.valueOf(2));
				} else {
					cmds.push(current_bin);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Script(cmds);
	}

	public byte[] raw_serialize() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int cmdsSize = this.cmds.size();
		for (int i = 0; i < cmdsSize; i++) {
			byte[] cmd = (byte[]) cmds.get(i);
			if (cmd.length == 1) {
				byte[] b = Arrays.reverse(cmd);
				try {
					baos.write(b);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				byte[] cmd_bin = cmd;
				int length = cmd_bin.length;
				if (length < 75) {
					byte[] b = Utils.biginteger_to_little_endian(
							BigInteger.valueOf(length), 1);
					try {
						baos.write(b);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (length > 75 && length < 0x100) {
					try {
						baos.write(Utils.biginteger_to_little_endian(
								BigInteger.valueOf(76), 1));
						baos.write(Utils.biginteger_to_little_endian(
								BigInteger.valueOf(length), 1));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (length >= 0x100 && length <= 520) {
					try {
						baos.write(Utils.biginteger_to_little_endian(
								BigInteger.valueOf(77), 1));
						baos.write(Utils.biginteger_to_little_endian(
								BigInteger.valueOf(length), 2));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("too long an cmd");
					System.exit(1);
					;
				}
				try {
					baos.write(cmd_bin);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		byte[] result = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public byte[] serialize() {
		byte[] tmp = this.raw_serialize();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] result = null;
		try {
			baos.write(Utils.encodeVarint(BigInteger.valueOf(tmp.length)));
			baos.write(tmp);
			result = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Script add(Script other) {
		boolean b = this.cmds.addAll(other.cmds);
		return new Script(this.cmds);
	}

	public boolean evaluate(BigInteger z, ArrayList<byte[]> witness) {

		Stack cmds = new Stack();
		List list = java.util.Arrays.asList(this.cmds);
		cmds = (Stack) list.get(0);
		// cmds.addAll(list);

		Stack stack = new Stack();
		Stack alstack = new Stack();

		while (cmds.size() > 0) {
			// Object cmd = cmds.remove(0);
			byte[] cmd = (byte[]) cmds.remove(0);
			new OP();
			if (cmd.length == 1) {
				int v = new BigInteger(1, cmd).intValue();
				Class c = OP.class;
				String name = OP.OP_CODE_FUNCTIONS.get(v);
				Method operation = null;
				try {

				} catch (SecurityException e1) {
					e1.printStackTrace();
				}
				try {
					if (v == 99 || v == 100) {

						try {
							operation = c.getMethod(name, Stack.class);
						} catch (NoSuchMethodException e2) {
							e2.printStackTrace();
						} catch (SecurityException e2) {
							e2.printStackTrace();
						}

						if (!(boolean) operation.invoke(OP.class.newInstance(),
								stack, cmds)) {
							System.out.println("bap op! ");
							return false;
						}
					} else if (v == 107 || v == 108) {

						try {
							operation = c.getMethod(name, Stack.class);
						} catch (NoSuchMethodException e2) {
							e2.printStackTrace();
						} catch (SecurityException e2) {
							e2.printStackTrace();
						}

						if (!(boolean) operation.invoke(OP.class.newInstance(),
								stack, alstack)) {
							System.out.println("bap op! ");
							return false;
						}
					} else if (v == 172 || v == 173 || v == 174 || v == 175) {

						try {
							operation = c.getMethod(name, Stack.class,
									BigInteger.class);
						} catch (NoSuchMethodException e2) {
							e2.printStackTrace();
						} catch (SecurityException e2) {
							e2.printStackTrace();
						}

						if (!(boolean) operation.invoke(OP.class.newInstance(),
								stack, z)) {
							System.out.println("bap op! ");
							return false;
						}
					} else {

						try {
							operation = c.getMethod(name, Stack.class);

						} catch (NoSuchMethodException e2) {
							e2.printStackTrace();
						} catch (SecurityException e2) {
							e2.printStackTrace();
						}

						if (!(boolean) operation.invoke(OP.class.newInstance(),
								stack)) {
							System.out.println("bap op! ");
							return false;
						}
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}

			} else {
				stack.push(cmd);
				if (cmds.size() == 3
						&& ((byte[]) cmds.get(0))[0] == new byte[] { (byte) 0xa9 }[0]
						&& ((byte[]) cmds.get(1)).length == 20
						&& ((byte[]) cmds.get(2))[0] == new byte[] { (byte) 0x87 }[0]) {
					cmds.pop();
					byte[] h160 = (byte[]) cmds.pop();
					cmds.pop();
					OP op = new OP();
					if (!op.oPhash160(stack)) {
						return false;
					}
					stack.push(h160);
					if (!op.oPequal(stack)) {
						return false;
					}
					if (!op.oPverify(stack)) {
						System.out.println("bad p2sh h160");
						return false;
					}

					byte[] redeem_script = Arrays.concatenate(
							Utils.encodeVarint(BigInteger.valueOf(cmd.length)),
							cmd);

					ByteArrayInputStream bais = new ByteArrayInputStream(
							redeem_script);
					Stack extend = Script.parse(bais).cmds;
					cmds.addAll(extend);
				}
				if (cmds.size() == 2
						&& ((byte[]) cmds.get(0))[0] == (byte) 0x00
						&& ((byte[]) cmds.get(1)).length == 20) {
					byte[] h160 = (byte[]) cmds.pop();
					cmds.pop();
					cmds.addAll(witness);
					cmds.addAll(Script.p2pkhScript(h160).cmds);
				}
				if (cmds.size() == 2
						&& ((byte[]) cmds.get(0))[0] == (byte) 0x00
						&& ((byte[]) cmds.get(1)).length == 32) {
					byte[] s256 = (byte[]) cmds.pop();
					cmds.pop();
					int witnessLen = witness.size();
					cmds.addAll(witness.subList(0, witnessLen - 1));
					byte[] witnessScript = witness.get(witnessLen - 1);

					BigInteger s256BigInt = new BigInteger(1, s256);
					BigInteger witnessScriptBigInt = new BigInteger(1,
							Utils.sha256(witnessScript));
					if (!s256BigInt.equals(witnessScriptBigInt)) {
						System.out.println("bad sha256");
						return false;
					}
					byte[] witnessScriptCmdsBin = Arrays.concatenate(Utils
							.encodeVarint(BigInteger
									.valueOf(witnessScript.length)),
							witnessScript);
					ByteArrayInputStream bais = new ByteArrayInputStream(witnessScriptCmdsBin);
					Stack witnessScriptCmd = Script.parse(bais).cmds;
					cmds.addAll(witnessScriptCmd);
				}
			}
		}
		if (stack.size() == 0) {
			return false;
		}
		if (stack.pop().equals(BigInteger.ZERO.toByteArray())) {
			return false;
		}

		return true;
	}

	public boolean evaluate(BigInteger z) {

		Stack cmds = new Stack();
		List list = java.util.Arrays.asList(this.cmds);
		cmds = (Stack) list.get(0);
		// cmds.addAll(list);

		Stack stack = new Stack();
		Stack alstack = new Stack();

		while (cmds.size() > 0) {
			// Object cmd = cmds.remove(0);
			byte[] cmd = (byte[]) cmds.remove(0);
			new OP();
			if (cmd.length == 1) {
				int v = new BigInteger(1, cmd).intValue();
				Class c = OP.class;
				String name = OP.OP_CODE_FUNCTIONS.get(v);

				Method operation = null;

				try {

				} catch (SecurityException e1) {
					e1.printStackTrace();
				}

				try {
					if (v == 99 || v == 100) {

						try {
							operation = c.getMethod(name, Stack.class);
						} catch (NoSuchMethodException e2) {
							e2.printStackTrace();
						} catch (SecurityException e2) {
							e2.printStackTrace();
						}

						if (!(boolean) operation.invoke(OP.class.newInstance(),
								stack, cmds)) {
							System.out.println("bap op! ");
							return false;
						}
					} else if (v == 107 || v == 108) {

						try {
							operation = c.getMethod(name, Stack.class);
						} catch (NoSuchMethodException e2) {
							e2.printStackTrace();
						} catch (SecurityException e2) {
							e2.printStackTrace();
						}

						if (!(boolean) operation.invoke(OP.class.newInstance(),
								stack, alstack)) {
							System.out.println("bap op! ");
							return false;
						}
					} else if (v == 172 || v == 173 || v == 174 || v == 175) {

						try {
							operation = c.getMethod(name, Stack.class,
									BigInteger.class);
						} catch (NoSuchMethodException e2) {
							e2.printStackTrace();
						} catch (SecurityException e2) {
							e2.printStackTrace();
						}

						if (!(boolean) operation.invoke(OP.class.newInstance(),
								stack, z)) {
							System.out.println("bap op! ");
							return false;
						}
					} else {

						try {
							operation = c.getMethod(name, Stack.class);

						} catch (NoSuchMethodException e2) {
							e2.printStackTrace();
						} catch (SecurityException e2) {
							e2.printStackTrace();
						}

						if (!(boolean) operation.invoke(OP.class.newInstance(),
								stack)) {
							System.out.println("bap op! ");
							return false;
						}
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}

			} else {
				stack.push(cmd);
				if (cmds.size() == 3
						&& ((byte[]) cmds.get(0))[0] == new byte[] { (byte) 0xa9 }[0]
						&& ((byte[]) cmds.get(1)).length == 20
						&& ((byte[]) cmds.get(2))[0] == new byte[] { (byte) 0x87 }[0]) {
					cmds.pop();
					byte[] h160 = (byte[]) cmds.pop();
					cmds.pop();
					OP op = new OP();
					if (!op.oPhash160(stack)) {
						return false;
					}
					stack.push(h160);
					if (!op.oPequal(stack)) {
						return false;
					}
					if (!op.oPverify(stack)) {
						System.out.println("bad p2sh h160");
						return false;
					}

					byte[] redeem_script = Arrays.concatenate(
							Utils.encodeVarint(BigInteger.valueOf(cmd.length)),
							cmd);

					ByteArrayInputStream bais = new ByteArrayInputStream(
							redeem_script);
					Stack extend = Script.parse(bais).cmds;
					cmds.addAll(extend);
				}
			}
		}
		if (stack.size() == 0) {
			return false;
		}
		if (stack.pop().equals(BigInteger.ZERO.toByteArray())) {
			return false;
		}

		return true;
	}

	public boolean isP2pkhScriptPubkey() {
		boolean result = this.cmds.size() == 5
				&& ((byte[]) (this.cmds.get(0)))[0] == (byte) 0x76
				&& ((byte[]) (this.cmds.get(1)))[0] == (byte) 0xa9
				&& ((byte[]) this.cmds.get(2)).length == 20
				&& ((byte[]) (this.cmds.get(3)))[0] == (byte) 0x88
				&& ((byte[]) (this.cmds.get(4)))[0] == (byte) 0xac;
		return result;
	}

	public boolean isP2shScriptPubkey() {
		return (this.cmds.size() == 3
				&& ((byte[]) this.cmds.get(0))[0] == (new byte[] { (byte) 0xa9 })[0]
				&& ((byte[]) this.cmds.get(1)).length == 20 && ((byte[]) this.cmds
					.get(2))[0] == (new byte[] { (byte) 0x87 })[0]);
	}

	public boolean isP2wpkhScriptPubkey() {
		return this.cmds.size() == 2
				&& ((byte[]) this.cmds.get(0))[0] == (byte) 0x00
				&& ((byte[]) this.cmds.get(1)).length == 20;
	}

	public boolean isP2wshScriptPubkey() {
		return this.cmds.size() == 2
				&& ((byte[]) this.cmds.get(0))[0] == (byte) 0x00
				&& ((byte[]) this.cmds.get(1)).length == 32;
	}

	public String address(boolean testnet) {
		if (this.isP2pkhScriptPubkey()) {
			byte[] h160 = (byte[]) this.cmds.get(2);
			return Utils.h160_to_p2pkh_address(h160, testnet);
		} else if (this.isP2shScriptPubkey()) {
			byte[] h160 = (byte[]) this.cmds.get(1);
			return Utils.h160_to_p2sh_address(h160, testnet);
		} else {
			return null;
		}
	}

	@Override
	public String toString() {

		new OP();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cmds.size(); i++) {
			byte[] cmd = (byte[]) cmds.get(i);
			if (cmd.length == 1) {
				int v = new BigInteger(1, cmd).intValue();

				String name = "";
				if (OP.OP_CODE_NAME.containsKey(v)) {
					name = OP.OP_CODE_NAME.get(v) + " ";
				} else {
					name = "OP_" + v + " ";
				}
				sb.append(name);
			} else {
				BigInteger b = new BigInteger(1, cmd);

				int len = cmd.length * 2;
				sb.append(String.format("%0" + len + "x", b) + " ");
			}
		}
		return sb.toString();

	}

	public static Script p2pkhScript(byte[] h160) {
		Stack stack = new Stack();
		byte[] opdup = { (byte) 0x76 };
		byte[] ophash160 = { (byte) 0xa9 };
		byte[] opequalverify = { (byte) 0x88 };
		byte[] opchecksig = { (byte) 0xac };

		stack.push(opdup);
		stack.push(ophash160);
		stack.push(h160);
		stack.push(opequalverify);
		stack.push(opchecksig);

		return new Script(stack);
	}

	public static Script p2shScript(byte[] h160) {
		Stack stack = new Stack();
		byte[] ophash160 = { (byte) 0xa9 };
		byte[] opequal = { (byte) 0x87 };
		stack.push(ophash160);
		stack.push(h160);
		stack.push(opequal);
		return new Script(stack);
	}

	public static Script p2wpkhScript(byte[] h160) {
		Stack stack = new Stack();
		stack.push(new byte[] { (byte) 0x00 });
		stack.push(h160);
		return new Script(stack);
	}

	public static Script p2wshScript(byte[] h256) {
		Stack stack = new Stack();
		stack.push(new byte[] { (byte) 0x00 });
		stack.push(h256);
		return new Script(stack);
	}

}
