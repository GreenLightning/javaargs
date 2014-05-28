package com.objectmentor.utilities.args;

import static com.objectmentor.utilities.args.ArgsException.ErrorCode.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ArgsTest {

	@Test
	public void noSchemaOrArguments() throws ArgsException {
		Args args = new Args("", new String[0]);
		assertThat(args.nextArgument(), is(0));
	}

	@Test
	public void noSchemaButOneArgument() {
		try {
			new Args("", new String[] { "-x" });
			fail("Args constructor should have thrown exception.");
		} catch (ArgsException e) {
			assertThat(e.getErrorCode(), is(UNEXPECTED_ARGUMENT));
			assertThat(e.getErrorArgumentId(), is('x'));
		}
	}

	@Test
	public void noSchemaButMultipleArguments() {
		try {
			new Args("", new String[] { "-x", "-y" });
			fail("Args constructor should have thrown exception.");
		} catch (ArgsException e) {
			assertThat(e.getErrorCode(), is(UNEXPECTED_ARGUMENT));
			assertThat(e.getErrorArgumentId(), is('x'));
		}
	}

	@Test
	public void nonLetterSchema() {
		try {
			new Args("*", new String[] {});
			fail("Args constructor should have thrown exception.");
		} catch (ArgsException e) {
			assertThat(e.getErrorCode(), is(INVALID_ARGUMENT_NAME));
			assertThat(e.getErrorArgumentId(), is('*'));
		}
	}

	@Test
	public void invalidArgumentFormat() {
		try {
			new Args("f~", new String[] {});
			fail("Args constructor should have thrown exception.");
		} catch (ArgsException e) {
			assertThat(e.getErrorCode(), is(INVALID_ARGUMENT_FORMAT));
			assertThat(e.getErrorArgumentId(), is('f'));
		}
	}

	@Test
	public void spacesInFormat() throws ArgsException {
		Args args = new Args("x, y", new String[] { "-xy" });
		assertThat(args.has('x'), is(true));
		assertThat(args.has('y'), is(true));
		assertThat(args.nextArgument(), is(1));
	}

	@Test
	public void emptySlotsInFormat() throws ArgsException {
		Args args = new Args("x, ,y", new String[] { "-xy" });
		assertThat(args.has('x'), is(true));
		assertThat(args.has('y'), is(true));
		assertThat(args.nextArgument(), is(1));
	}
	@Test
	public void simpleBooleanPresent() throws ArgsException {
		Args args = new Args("x", new String[] { "-x" });
		assertThat(args.getBoolean('x'), is(true));
		assertThat(args.nextArgument(), is(1));
	}

	@Test
	public void simpleIntPresent() throws ArgsException {
		Args args = new Args("x#", new String[] { "-x", "42" });
		assertThat(args.has('x'), is(true));
		assertThat(args.getInt('x'), is(42));
		assertThat(args.nextArgument(), is(2));
	}

	@Test
	public void invalidInteger() {
		try {
			new Args("x#", new String[] { "-x", "Forty two" });
			fail("Args constructor should have thrown exception.");
		} catch (ArgsException e) {
			assertThat(e.getErrorCode(), is(INVALID_INTEGER));
			assertThat(e.getErrorArgumentId(), is('x'));
			assertThat(e.getErrorParameter(), is(equalTo("Forty two")));
		}
	}

	@Test
	public void missingInteger() {
		try {
			new Args("x#", new String[] { "-x" });
			fail("Args constructor should have thrown exception.");
		} catch (ArgsException e) {
			assertThat(e.getErrorCode(), is(MISSING_INTEGER));
			assertThat(e.getErrorArgumentId(), is('x'));
		}
	}

	@Test
	public void simpleDoublePresent() throws ArgsException {
		Args args = new Args("x##", new String[] { "-x", "42.3" });
		assertThat(args.has('x'), is(true));
		assertThat(args.getDouble('x'), is(42.3));
	}

	@Test
	public void invalidDouble() {
		try {
			new Args("x##", new String[] { "-x", "Forty two" });
			fail("Args constructor should have thrown exception.");
		} catch (ArgsException e) {
			assertThat(e.getErrorCode(), is(INVALID_DOUBLE));
			assertThat(e.getErrorArgumentId(), is('x'));
			assertThat(e.getErrorParameter(), is(equalTo("Forty two")));
		}
	}

	@Test
	public void missingDouble() {
		try {
			new Args("x##", new String[] { "-x" });
			fail("Args constructor should have thrown exception.");
		} catch (ArgsException e) {
			assertThat(e.getErrorCode(), is(MISSING_DOUBLE));
			assertThat(e.getErrorArgumentId(), is('x'));
		}
	}

	@Test
	public void simpleStringPresent() throws ArgsException {
		Args args = new Args("x*", new String[] { "-x", "param" });
		assertThat(args.has('x'), is(true));
		assertThat(args.getString('x'), is(equalTo("param")));
		assertThat(args.nextArgument(), is(2));
	}

	@Test
	public void missingString() {
		try {
			new Args("x*", new String[] { "-x" });
			fail("Args constructor should have thrown exception.");
		} catch (ArgsException e) {
			assertThat(e.getErrorCode(), is(MISSING_STRING));
			assertThat(e.getErrorArgumentId(), is('x'));
		}
	}

	@Test
	public void simpleStringArrayPresent() throws ArgsException {
		Args args = new Args("x[*]", new String[] { "-x", "alpha" });
		assertThat(args.has('x'), is(true));
		String[] result = args.getStringArray('x');
		assertThat(result.length, is(1));
		assertThat(result[0], is(equalTo("alpha")));
	}

	@Test
	public void missingStringArrayElement() {
		try {
			new Args("x[*]", new String[] { "-x" });
			fail("Args constructor should have thrown exception.");
		} catch (ArgsException e) {
			assertThat(e.getErrorCode(), is(MISSING_STRING));
			assertThat(e.getErrorArgumentId(), is('x'));
		}
	}

	@Test
	public void extraArguments() throws ArgsException {
		Args args = new Args("x,y*", new String[] { "-x", "-y", "alpha", "beta" });
		assertThat(args.getBoolean('x'), is(true));
		assertThat(args.getString('y'), is(equalTo("alpha")));
		assertThat(args.nextArgument(), is(3));
	}

	@Test
	public void extraArgumentsThatLookLikeFlags() throws ArgsException {
		Args args = new Args("x,y", new String[] { "-x", "alpha", "-y", "beta" });
		assertThat(args.has('x'), is(true));
		assertThat(args.has('y'), is(false));
		assertThat(args.getBoolean('x'), is(true));
		assertThat(args.getBoolean('y'), is(false));
		assertThat(args.nextArgument(), is(1));
	}

}