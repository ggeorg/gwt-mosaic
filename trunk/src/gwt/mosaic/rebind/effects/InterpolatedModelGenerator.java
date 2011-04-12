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

import gwt.mosaic.client.effects.GrinFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.uibinder.rebind.MortalLogger;

public class InterpolatedModelGenerator extends Generator {

	private static final String GRINFILE_SUFFIX = ".txt";

	/**
	 * Given a GrinCommands interface, return the path to its .txt file,
	 * suitable for any classloader to find it as a resource.
	 */
	private static String deduceGrinFile(MortalLogger logger,
			JClassType interfaceType) throws UnableToCompleteException {
		String grinFile = null;
		GrinFile annotation = interfaceType.getAnnotation(GrinFile.class);
		if (annotation == null) {
			// if the interface is defined as a nested class, use the same name
			// as the enclosing type
			if (interfaceType.getEnclosingType() != null) {
				interfaceType = interfaceType.getEnclosingType();
			}
			return slashify(interfaceType.getQualifiedSourceName())
					+ GRINFILE_SUFFIX;
		} else {
			grinFile = annotation.value();
			if (!grinFile.endsWith(GRINFILE_SUFFIX)) {
				logger.die("Grin file name must end with " + GRINFILE_SUFFIX);
			}

			/*
			 * If the grin file name (minus suffix) has no dots, make it
			 * relative to the binder's package, otherwise slashify the dots
			 */
			String unsuffixed = grinFile.substring(0,
					grinFile.lastIndexOf(GRINFILE_SUFFIX));
			if (!unsuffixed.contains(".")) {
				grinFile = slashify(interfaceType.getPackage().getName()) + "/"
						+ grinFile;
			} else {
				grinFile = slashify(unsuffixed) + GRINFILE_SUFFIX;
			}
		}
		return grinFile;
	}

	private static String slashify(String s) {
		return s.replace(".", "/");
	}

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		TypeOracle oracle = context.getTypeOracle();
		JClassType interfaceType;
		try {
			interfaceType = oracle.getType(typeName);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}

		String implName = interfaceType.getName().replace('.', '_') + "Impl";
		String packageName = interfaceType.getPackage().getName();
		PrintWriterManager writers = new PrintWriterManager(context, logger,
				packageName);
		PrintWriter printWriter = writers.tryToMakePrintWriterFor(implName);

		if (printWriter != null) {
			generateOnce(interfaceType, implName, printWriter, logger, oracle,
					writers);
		}

		return packageName + "." + implName;
	}

	private void generateOnce(JClassType interfaceType, String implName,
			PrintWriter printWriter, TreeLogger treeLogger, TypeOracle oracle,
			PrintWriterManager writerManager) throws UnableToCompleteException {
		MortalLogger logger = new MortalLogger(treeLogger);
		String grinFile = deduceGrinFile(logger, interfaceType);
		logger.getTreeLogger().log(Type.INFO, grinFile);

		GrinWriter grinWriter = new GrinWriter(interfaceType, implName,
				grinFile, oracle, logger);

		Reader reader = getReader(logger, grinFile);

		grinWriter.parse(reader, printWriter);

		writerManager.commit();
	}

	private Reader getReader(MortalLogger logger, String grinFile)
			throws UnableToCompleteException {
		URL url = InterpolatedModelGenerator.class.getClassLoader()
				.getResource(grinFile);
		if (url == null) {
			logger.die("Unable to find resource: " + grinFile);
		}
		try {
			return new BufferedReader(new InputStreamReader(url.openStream(),
					"UTF-8"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
