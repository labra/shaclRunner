package es.weso.shacler;

import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

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

	public Model validate(String dataFile, String schemaFile) throws InterruptedException {
		
		// Load the main data model
		Model dataModel = ModelFactory.createDefaultModel();
		return dataModel;
/*		dataModel.read(dataFile, "urn:dummy", FileUtils.langTurtle);
		
		Model schemaModel = ModelFactory.createDefaultModel();
		schemaModel.read(schemaFile, "urn:dummy", FileUtils.langTurtle);
		
		// Load the shapes Model (here, includes the dataModel because that has templates in it)
		Model shaclModel = ModelFactory.createDefaultModel();
		InputStream is = getClass().getResourceAsStream("/etc/shacl.ttl");
		shaclModel.read(is, SH.BASE_URI, FileUtils.langTurtle);
		MultiUnion unionGraph = new MultiUnion(new Graph[] {
			shaclModel.getGraph(),
			dataModel.getGraph(),
			schemaModel.getGraph()
		});
		Model shapesModel = ModelFactory.createModelForGraph(unionGraph);
		
		SHACLFunctions.registerFunctions(shapesModel);
		
		// Create Dataset that contains both the main query model and the shapes model
		// (here, using a temporary URI for the shapes graph)
		URI shapesGraphURI = URI.create("urn:x-shacl-shapes-graph:" + UUID.randomUUID().toString());
		Dataset dataset = ARQFactory.get().getDataset(dataModel);
		dataset.addNamedModel(shapesGraphURI.toString(), shapesModel);
		
		// Run the validator and print results
		Model results = ModelConstraintValidator.get().validateModel(dataset, shapesGraphURI, null, false, null);
		return results; */
	}
}