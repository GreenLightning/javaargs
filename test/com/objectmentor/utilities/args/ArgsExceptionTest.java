package com.objectmentor.utilities.args;

import static com.objectmentor.utilities.args.ArgsException.ErrorCode.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ArgsExceptionTest {

	@Test
	public void unexpectedArgument() {
		ArgsException e = new ArgsException(UNEXPECTED_ARGUMENT, 'x');
		assertThat(e.getMessage(), is(equalTo("Argument -x unexpected.")));
	}

	@Test
	public void missingString() {
		ArgsException e = new ArgsException(MISSING_STRING, 'x');
		assertThat(e.getMessage(), is(equalTo("Could not find string parameter for -x.")));
	}

	@Test
	public void invalidInteger() {
		ArgsException e = new ArgsException(INVALID_INTEGER, 'x', "Forty two");
		assertThat(e.getMessage(),
			is(equalTo("Argument -x expects an integer but was 'Forty two'.")));
	}

	@Test
	public void missingInteger() {
		ArgsException e = new ArgsException(MISSING_INTEGER, 'x');
		assertThat(e.getMessage(), is(equalTo("Could not find integer parameter for -x.")));
	}

	@Test
	public void invalidDouble() {
		ArgsException e = new ArgsException(INVALID_DOUBLE, 'x', "Forty two");
		assertThat(e.getMessage(),
			is(equalTo("Argument -x expects a double but was 'Forty two'.")));
	}

	@Test
	public void missingDouble() {
		ArgsException e = new ArgsException(MISSING_DOUBLE, 'x');
		assertThat(e.getMessage(), is(equalTo("Could not find double parameter for -x.")));
	}

	@Test
	public void invalidArgumentName() {
		ArgsException e = new ArgsException(INVALID_ARGUMENT_NAME, '#');
		assertThat(e.getMessage(), is(equalTo("'#' is not a valid argument name.")));
	}

	@Test
	public void invalidFormat() {
		ArgsException e = new ArgsException(INVALID_ARGUMENT_FORMAT, 'x', "$");
		assertThat(e.getMessage(), is(equalTo("'$' is not a valid argument format.")));
	}

	@Test
	public void unknownArgumentName() {
		ArgsException e = new ArgsException(UNKNOWN_ARGUMENT_NAME, 'y');
		assertThat(e.getMessage(), is(equalTo("Argument 'y' did not appear in schema.")));
	}

}