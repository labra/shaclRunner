package es.weso.shacler;

import java.io.InputStream;
import java.net.URI;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.jena.atlas.logging.Log;
import org.apache.jena.riot.RDFDataMgr;
import org.topbraid.shacl.arq.SHACLFunctions;
import org.topbraid.shacl.constraints.ModelConstraintValidator;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.util.JenaUtil;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.compose.MultiUnion;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileUtils;

public class ShaclValidator {
	
	Logger log = Logger.getLogger(ShaclValidator.class.getName());

	public Model validate(Model dataModel, Model shapesModel) throws Exception {
		Model shaclModel = ModelFactory.createDefaultModel();
		InputStream is = getClass().getResourceAsStream("/etc/shacl.ttl");
		shaclModel.read(is, SH.BASE_URI, FileUtils.langTurtle);
		
		Model combined = ModelFactory.createDefaultModel();
		combined.add(dataModel);
		combined.add(shapesModel);
		combined.add(shaclModel);
		
		SHACLFunctions.registerFunctions(combined);
		
		URI shapesGraphURI = URI.create("urn:x-shacl-shapes-graph:" + UUID.randomUUID().toString());
		Dataset dataset = ARQFactory.get().getDataset(dataModel);
		dataset.addNamedModel(shapesGraphURI.toString(), combined);
        
		Model results = ModelConstraintValidator.get().validateModel(dataset, shapesGraphURI, null, false, null);
        results.setNsPrefixes(shaclModel);
		return results; 
	}

	public Model validate(String dataFile, String schemaFile) throws Exception {
		log.info("Reading data file " + dataFile);
		Model dataModel =  RDFDataMgr.loadModel(dataFile);
		log.info("Reading shapes file " + schemaFile);
		Model shapesModel = RDFDataMgr.loadModel(schemaFile);
		return validate(dataModel,shapesModel);
	}
}