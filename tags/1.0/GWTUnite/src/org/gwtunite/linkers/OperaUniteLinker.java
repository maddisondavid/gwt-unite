package org.gwtunite.linkers;

import com.google.gwt.core.ext.LinkerContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.AbstractLinker;
import com.google.gwt.core.ext.linker.ArtifactSet;
import com.google.gwt.core.ext.linker.CompilationResult;
import com.google.gwt.core.ext.linker.EmittedArtifact;
import com.google.gwt.core.ext.linker.LinkerOrder;
import com.google.gwt.core.ext.linker.LinkerOrder.Order;
import com.google.gwt.core.ext.linker.impl.StandardScriptReference;
import com.google.gwt.dev.About;
import com.google.gwt.dev.util.DefaultTextOutput;

@LinkerOrder(Order.PRIMARY)
public class OperaUniteLinker extends AbstractLinker {

	@Override
	public String getDescription() {
		return "Opera Unite Service Linker";
	}

	@Override
	public ArtifactSet link(TreeLogger logger, LinkerContext context, ArtifactSet artifacts) throws UnableToCompleteException {
        ArtifactSet toReturn = new ArtifactSet(artifacts);

        for (CompilationResult compilationResult : toReturn.find(CompilationResult.class)) {
        	toReturn.add(doEmitCompilation(logger, context, compilationResult));
        }
        toReturn.add(doEmitBootStrapper(logger, context, artifacts));

        return toReturn;
	}

	private EmittedArtifact doEmitBootStrapper(TreeLogger logger, LinkerContext context, ArtifactSet artifacts) throws UnableToCompleteException {
		DefaultTextOutput out = new DefaultTextOutput(context.isOutputCompact());
		out.print("<script>");
	    out.newlineOpt();
	    out.print("var $gwt_version = \"" + About.GWT_VERSION_NUM + "\";");
	    out.newlineOpt();
	    out.print("var $wnd = parent;");
	    out.newlineOpt();
	    out.print("var $doc = $wnd.document;");
	    out.newlineOpt();
	    out.print("var $moduleName, $moduleBase;");
	    out.newlineOpt();
	    out.print("var $stats = $wnd.__gwtStatsEvent ? function(a) {return $wnd.__gwtStatsEvent(a);} : null;");
	    out.newlineOpt();
	    out.print("$stats && $stats({moduleName:'" + context.getModuleName()
	        + "',subSystem:'startup',evtGroup:'moduleStartup'"
	        + ",millis:(new Date()).getTime(),type:'moduleEvalStart'});");
	    out.newlineOpt();
	    out.print("</script>");
		StringBuilder bootStrap = new StringBuilder();
		
		for (StandardScriptReference script : artifacts.find(StandardScriptReference.class)) {
			out.newlineOpt();
			out.print("<script src=\""+script.getSrc()+"\"></script>");
		}
		
		out.newlineOpt();
		out.print("<script src=\"scripts/script.js\"></script>");
		
		return emitString(logger, out.toString(),"index.html");
	}

	private EmittedArtifact doEmitCompilation(TreeLogger logger, LinkerContext context, CompilationResult compilationResult) throws UnableToCompleteException {
		StringBuffer code = new StringBuffer(((CompilationResult)compilationResult).getJavaScript());
		
		code.append("\n");
		code.append("var $stats=null;\n");
		code.append("gwtOnLoad(null,\""+ context.getModuleName()+"\",\"\");");
        return emitString(logger, code.toString(), "scripts/script.js");
	}

}
