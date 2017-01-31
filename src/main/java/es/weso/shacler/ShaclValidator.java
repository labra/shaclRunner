package es.weso.shacler;

import java.io.InputStream;
import java.net.URI;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.jena.atlas.logging.Log;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.compose.MultiUnion;
import org.apache.jena.query.Dataset;
import org.topbraid.shacl.arq.SHACLFunctions;
import org.topbraid.shacl.constraints.ModelConstraintValidator;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.util.JenaUtil;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;


public class ShaclValidator {
	
	Logger log = Logger.getLogger(ShaclValidator.class.getName());

	public Model validate(Model dataModel, Model shapesModel) throws Exception {
		Model shaclModel = SHACLSystemModel.getSHACLModel();
		MultiUnion unionGraph = new MultiUnion(new Graph[] {
			shaclModel.getGraph(),
			dataModel.getGraph(),
			shapesModel.getGraph()
		});
		Model all = ModelFactory.createModelForGraph(unionGraph);

		// Make sure all sh:Functions are registered
		SHACLFunctions.registerFunctions(all);
		
		URI shapesGraphURI = URI.create("urn:x-shacl-shapes-graph:" + UUID.randomUUID().toString());
		Dataset dataset = ARQFactory.get().getDataset(dataModel);
		dataset.addNamedModel(shapesGraphURI.toString(), all);
        
		Model results = new ModelConstraintValidator().validateModel(dataset, shapesGraphURI, null, true, null, null).getModel();
        results.setNsPrefixes(all);
		return results; 
	}

	public Model validate(String dataFile, String schemaFile) throws Exception {
		log.info("Reading data file " + dataFile);
		Model dataModel =  RDFDataMgr.loadModel(dataFile);   // loadModel(dataFile);
		log.info("Reading shapes file " + schemaFile);
		Model shapesModel = RDFDataMgr.loadModel(schemaFile);
		return validate(dataModel,shapesModel);
	}
}