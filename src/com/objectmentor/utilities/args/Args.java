package com.objectmentor.utilities.args;

import static com.objectmentor.utilities.args.ArgsException.ErrorCode.*;

import java.util.*;

public class Args {

	private Map<Character, ArgumentMarshaler> marshalers;
	private Set<Character> argsFound;
	private ListIterator<String> argsIterator;

	public Args(String schema, String[] args) throws ArgsException {
		marshalers = new HashMap<Character, ArgumentMarshaler>();
		argsFound = new HashSet<Character>();

		parseSchema(schema);
		parseArguments(Arrays.asList(args));
	}

	private void parseSchema(String schema) throws ArgsException {
		for (String element : schema.split(",")) {
			element = element.trim();
			if (element.length() > 0) {
				parseSchemaElement(element);
			}
		}
	}

	private void parseSchemaElement(String element) throws ArgsException {
		char elementId = element.charAt(0);
		String elementTail = element.substring(1);
		validateSchemaElementId(elementId);
		if (elementTail.length() == 0) {
			marshalers.put(elementId, new BooleanArgumentMarshaler());
		} else if (elementTail.equals("*")) {
			marshalers.put(elementId, new StringArgumentMarshaler());
		} else if (elementTail.equals("#")) {
			marshalers.put(elementId, new IntegerArgumentMarshaler());
		} else if (elementTail.equals("##")) {
			marshalers.put(elementId, new DoubleArgumentMarshaler());
		} else if (elementTail.equals("[*]")) {
			marshalers.put(elementId, new StringArrayArgumentMarshaler());
		} else {
			throw new ArgsException(INVALID_ARGUMENT_FORMAT, elementId, elementTail);
		}
	}

	private void validateSchemaElementId(char elementId) throws ArgsException {
		if (!Character.isLetter(elementId)) {
			throw new ArgsException(INVALID_ARGUMENT_NAME, elementId);
		}
	}

	private void parseArguments(List<String> argsList) throws ArgsException {
		for (argsIterator = argsList.listIterator(); argsIterator.hasNext();) {
			String argString = argsIterator.next();
			if (argString.startsWith("-")) {
				parseArgumentCharacters(argString.substring(1));
			} else {
				argsIterator.previous();
				break;
			}
		}
	}

	private void parseArgumentCharacters(String argChars) throws ArgsException {
		for (int i = 0; i < argChars.length(); i++) {
			parseArgumentCharacter(argChars.charAt(i));
		}
	}

	private void parseArgumentCharacter(char argChar) throws ArgsException {
		ArgumentMarshaler m = marshalers.get(argChar);
		if (m == null) {
			throw new ArgsException(UNEXPECTED_ARGUMENT, argChar);
		} else {
			argsFound.add(argChar);
			try {
				m.set(argsIterator);
			} catch (ArgsException e) {
				e.setErrorArgumentId(argChar);
				throw e;
			}
		}
	}

	public boolean found(char arg) throws ArgsException {
		validateArgumentId(arg);
		return argsFound.contains(arg);
	}

	public int extraArgumentsIndex() {
		return argsIterator.nextIndex();
	}

	public boolean getBoolean(char arg) throws ArgsException {
		ArgumentMarshaler marshaler = getAndValidateMarshaler(arg, BooleanArgumentMarshaler.class);
		return BooleanArgumentMarshaler.getValue(marshaler);
	}

	public String getString(char arg) throws ArgsException {
		ArgumentMarshaler marshaler = getAndValidateMarshaler(arg, StringArgumentMarshaler.class);
		return StringArgumentMarshaler.getValue(marshaler);
	}

	public String getStringOrDefault(char arg, String defaultValue) throws ArgsException {
		ArgumentMarshaler marshaler = getAndValidateMarshaler(arg, StringArgumentMarshaler.class);
		if (found(arg)) {
			return StringArgumentMarshaler.getValue(marshaler);
		} else {
			return defaultValue;
		}
	}

	public int getInt(char arg) throws ArgsException {
		ArgumentMarshaler marshaler = getAndValidateMarshaler(arg, IntegerArgumentMarshaler.class);
		return IntegerArgumentMarshaler.getValue(marshaler);
	}

	public int getIntOrDefault(char arg, int defaultValue) throws ArgsException {
		ArgumentMarshaler marshaler = getAndValidateMarshaler(arg, IntegerArgumentMarshaler.class);
		if (found(arg)) {
			return IntegerArgumentMarshaler.getValue(marshaler);
		} else {
			return defaultValue;
		}
	}

	public double getDouble(char arg) throws ArgsException {
		ArgumentMarshaler marshaler = getAndValidateMarshaler(arg, DoubleArgumentMarshaler.class);
		return DoubleArgumentMarshaler.getValue(marshaler);
	}

	public double getDoubleOrDefault(char arg, double defaultValue) throws ArgsException {
		ArgumentMarshaler marshaler = getAndValidateMarshaler(arg, DoubleArgumentMarshaler.class);
		if (found(arg)) {
			return DoubleArgumentMarshaler.getValue(marshaler);
		} else {
			return defaultValue;
		}
	}

	public String[] getStringArray(char arg) throws ArgsException {
		ArgumentMarshaler marshaler = getAndValidateMarshaler(arg, StringArrayArgumentMarshaler.class);
		return StringArrayArgumentMarshaler.getValue(marshaler);
	}

	private void validateArgumentId(char arg) throws ArgsException {
		if (!marshalers.containsKey(arg)) {
			throw new ArgsException(UNKNOWN_ARGUMENT_NAME, arg);
		}
	}

	private ArgumentMarshaler getAndValidateMarshaler(char arg, Class<? extends ArgumentMarshaler> clazz)
		throws ArgsException {
		validateArgumentId(arg);
		ArgumentMarshaler marshaler = marshalers.get(arg);
		if (!clazz.isInstance(marshaler)) {
			throw new ArgsException(WRONG_ARGUMENT_TYPE, arg);
		}
		return marshaler;
	}

}