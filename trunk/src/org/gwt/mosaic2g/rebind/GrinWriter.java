package org.gwt.mosaic2g.rebind;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;

import org.gwt.mosaic2g.client.scene.InterpolatedModel;
import org.gwt.mosaic2g.client.scene.InterpolatedModelParser;

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
	private final TypeOracle oracle;
	private final MortalLogger logger;

	public GrinWriter(JClassType baseClass, String implClassName,
			String grinFile, TypeOracle oracle, MortalLogger logger)
			throws UnableToCompleteException {
		this.baseClass = baseClass;
		this.implClassName = implClassName;
		this.oracle = oracle;
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

		w.write("public InterpolatedModel getModel() {");
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
