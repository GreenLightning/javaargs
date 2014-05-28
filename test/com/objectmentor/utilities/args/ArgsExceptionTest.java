package com.objectmentor.utilities.args;

import static com.objectmentor.utilities.args.ArgsException.ErrorCode.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ArgsExceptionTest {

	@Test
	public void unexpectedArgument() {
		ArgsException e = new ArgsException(UNEXPECTED_ARGUMENT, 'x', null);
		assertThat(e.errorMessage(), is(equalTo("Argument -x unexpected.")));
	}

	@Test
	public void missingString() {
		ArgsException e = new ArgsException(MISSING_STRING, 'x', null);
		assertThat(e.errorMessage(), is(equalTo("Could not find string parameter for -x.")));
	}

	@Test
	public void invalidInteger() {
		ArgsException e = new ArgsException(INVALID_INTEGER, 'x', "Forty two");
		assertThat(e.errorMessage(),
			is(equalTo("Argument -x expects an integer but was 'Forty two'.")));
	}

	@Test
	public void missingInteger() {
		ArgsException e = new ArgsException(MISSING_INTEGER, 'x', null);
		assertThat(e.errorMessage(), is(equalTo("Could not find integer parameter for -x.")));
	}

	@Test
	public void invalidDouble() {
		ArgsException e = new ArgsException(INVALID_DOUBLE, 'x', "Forty two");
		assertThat(e.errorMessage(),
			is(equalTo("Argument -x expects a double but was 'Forty two'.")));
	}

	@Test
	public void missingDouble() {
		ArgsException e = new ArgsException(MISSING_DOUBLE, 'x', null);
		assertThat(e.errorMessage(), is(equalTo("Could not find double parameter for -x.")));
	}

	@Test
	public void invalidArgumentName() {
		ArgsException e = new ArgsException(INVALID_ARGUMENT_NAME, '#', null);
		assertThat(e.errorMessage(), is(equalTo("'#' is not a valid argument name.")));
	}

	@Test
	public void invalidFormat() {
		ArgsException e = new ArgsException(INVALID_ARGUMENT_FORMAT, 'x', "$");
		assertThat(e.errorMessage(), is(equalTo("'$' is not a valid argument format.")));
	}

}