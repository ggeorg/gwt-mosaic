/*
 * Copyright 2010 ArkaSoft LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gwt.mosaic.rebind.effects;

import gwt.mosaic.client.effects.InterpolatedModel;
import gwt.mosaic.client.effects.InterpolatedModelParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.uibinder.rebind.IndentedWriter;
import com.google.gwt.uibinder.rebind.MortalLogger;

/**
 * Writer for GWT Grin generated classes.
 * 
 * @author ggeorg
 */
public class GrinWriter {

	/**
	 * The type we have been asked to generated, e.g. MyGrinCommands
	 */
	private final JClassType baseClass;

	/**
	 * The name of the class we're creating, e.g. MyGrinCommandsImpl
	 */
	private final String implClassName;

	private final String grinFile;
	// private final TypeOracle oracle;
	private final MortalLogger logger;

	public GrinWriter(JClassType baseClass, String implClassName,
			String grinFile, TypeOracle oracle, MortalLogger logger)
			throws UnableToCompleteException {
		this.baseClass = baseClass;
		this.implClassName = implClassName;
		// this.oracle = oracle;
		this.logger = logger;
		this.grinFile = grinFile;
		// this.fieldManager = fieldManager;
		// this.messages = messagesWriter;
	}

	/**
	 * Entry point for the code generation logic.
	 * 
	 * @param reader
	 * @param printWriter
	 */
	public void parse(Reader reader, PrintWriter printWriter)
			throws UnableToCompleteException {
		ShowParser showParser = new ShowParser(reader, grinFile, logger);

		AbstractInterpolatedModel model = null;
		try {
			model = showParser.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}

		StringWriter stringWriter = new StringWriter();
		IndentedWriter niceWriter = new IndentedWriter(new PrintWriter(
				stringWriter));
		writeShowBuilder(niceWriter, model);

		printWriter.print(stringWriter.toString());
	}

	/**
	 * Post an error message and halt processing. This method always throws an
	 * {@link UnableToCompleteException}
	 */
	public void die(String message) throws UnableToCompleteException {
		logger.die(message);
	}

	private void writeShowBuilder(IndentedWriter w,
			AbstractInterpolatedModel model) throws UnableToCompleteException {
		writePackage(w);

		writeImports(w);
		w.newline();

		writeClassOpen(w);

		w.write("public InterpolatedModel createModel() {");
		w.indent();
		w.write("return new InterpolatedModel(");
		w.write(makeIntArray(model.frames));
		w.write(",");
		w.write(makeIntArray(model.currValues));
		w.write(",");
		w.write(makeIntArray(model.values));
		w.write(", " + model.repeatFrame + ", " + model.loopCount + ");");
		w.outdent();
		w.write("}");

		// close class
		w.outdent();
		w.write("}");
	}

	protected String makeIntArray(int[][] values) {
		StringBuilder sb = new StringBuilder();
		sb.append("new int[][] {");
		boolean first = true;
		for (int[] v : values) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			if (v != null) {
				sb.append(makeIntArray(v));
			} else {
				sb.append("null");
			}
		}
		sb.append("}");
		return sb.toString();
	}

	protected String makeIntArray(int[] v) {
		StringBuilder sb = new StringBuilder();
		sb.append("new int[] {");
		boolean first = true;
		for (int i : v) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(i);
		}
		sb.append("}");
		return sb.toString();
	}

	private void writePackage(IndentedWriter w) {
		String packageName = baseClass.getPackage().getName();
		if (packageName.length() > 0) {
			w.write("package %1$s;", packageName);
			w.newline();
		}
	}

	private void writeImports(IndentedWriter w) {
		w.write("import %s;", InterpolatedModel.class.getName());
		w.write("import %s;", InterpolatedModelParser.class.getName());
	}

	private void writeClassOpen(IndentedWriter w) {
		String s = "public class %s implements %s {";
		w.write(s, implClassName,
				baseClass.getParameterizedQualifiedSourceName());
		w.indent();
	}

}
