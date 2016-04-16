package org.isisaddons.wicket.summernote.fixture.dom.regulation;

import org.apache.isis.applib.AbstractService;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@DomainService
@DomainServiceLayout(menuBar= DomainServiceLayout.MenuBar.TERTIARY)
public class RESTclient extends AbstractService   {

// target is basically a full rule, and applicability is really the target
// 192.168.33.10:9000/api/semantic/target is fetching a full rule.
//		er kopiert fra: https://bitbucket.org/droythorne/jersey/src/8f6d940ff0dd2e5b5bed0dc52c5a558b511a1c3e/src/main/java/Main.java?at=master
	//Target is basically fetching the full rule.

// Calls the TARGET-API.
	@Action
	public ShipClass GetTarget(String regulationText) {

		ShipClass shipClassInRule = null;
  	class SemanticRuleCallback implements InvocationCallback<Response> {

			@Override
	public void completed(Response response) {
 	}

	@Override
	public void failed(Throwable throwable) {
	System.out.println("Invocation failed.");
	throwable.printStackTrace();
	}
	}

	System.out.println("GetTarget");
 		Client client = ClientBuilder.newClient();
 		WebTarget webTarget = client.target("http://192.168.33.10:9000/api/semantic/target");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
	invocationBuilder.header("Content-type", "text/plain");
 	AsyncInvoker async_invoker = invocationBuilder.async();
	InvocationCallback<Response> sftc = new SemanticRuleCallback();
	Future<Response> response = async_invoker.post(Entity.entity(regulationText, MediaType.TEXT_PLAIN), sftc);
		Response responseEntity = null;
			try {
				ShipClass shipClassFound;
 				responseEntity=response.get();
				System.out.println("ResponseEntity in GetTarget() = "+responseEntity.toString());
				if (responseEntity.getStatus() ==200) {
					// responseEntity is OK.
					shipClassFound = responseEntity.readEntity(ShipClass.class);
					System.out.println("shipClassFound set... ");
					shipClassInRule = shipClassFound;
				}
				else
				{// responseEntity is NOT OK.
					shipClassFound = null;
					System.out.println("shipClassFound set to null ");
					shipClassInRule = shipClassFound;

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		System.out.println("return shipClassInRule");
		return shipClassInRule;
	}

// CALLS the APPLICABILITY-API:
	public ShipClass GetApplicability(String regulationText) {
		ShipClass shipClassInRule = null;
		class SemanticRuleCallback implements InvocationCallback<Response> {

			@Override
			public void completed(Response response) {
			}

			@Override
			public void failed(Throwable throwable) {
				System.out.println("Invocation failed.");
				throwable.printStackTrace();
			}
		}

	 	System.out.println("GetApplicability");
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://192.168.33.10:9000/api/semantic/applicability");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
		invocationBuilder.header("Content-type", "text/plain");
		AsyncInvoker async_invoker = invocationBuilder.async();
		InvocationCallback<Response> sftc = new SemanticRuleCallback();
		Future<Response> response = async_invoker.post(Entity.entity(regulationText, MediaType.TEXT_PLAIN), sftc);
		Response responseEntity = null;
		try {
			ShipClass shipClassFound;
			responseEntity=response.get();
			System.out.println("ResponseEntity in GetApplicability() = "+responseEntity.toString());
			if (responseEntity.getStatus() ==200) {
				// call OK
				shipClassFound = responseEntity.readEntity(ShipClass.class);
				System.out.println("shipClassFound set... ");
				shipClassInRule = shipClassFound;
			}
			else
			{
				// call NOT OK
				shipClassFound = null;
				System.out.println("shipClassFound set to null... ");
				shipClassInRule = shipClassFound;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("return shipClassInRule");
		return shipClassInRule;
	}


	// CALLS the TARGET-API: MUST CHANGE THIS TO APPLICABILITY???
	public ShipClassType GetShipClass(String regulationText) {
		ShipClassType shipClassTypeInRule = null;
		class SemanticRuleCallback implements InvocationCallback<Response> {
			@Override
			public void completed(Response response) {
			}
			@Override
			public void failed(Throwable throwable) {
				System.out.println("Invocation failed.");
				throwable.printStackTrace();
			}
		}
 		System.out.println("-------------------\nhttp://192.168.33.10:9000/api/semantic/target");
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://192.168.33.10:9000/api/semantic/target");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
		invocationBuilder.header("Content-type", "text/plain");
		AsyncInvoker async_invoker = invocationBuilder.async();
		InvocationCallback<Response> sftc = new SemanticRuleCallback();
		Future<Response> response = async_invoker.post(Entity.entity(regulationText, MediaType.TEXT_PLAIN), sftc);
		Response responseEntity = null;
		try {
			ShipClass shipClassFound;
			responseEntity=response.get();
			System.out.println("ResponseEntity in GetShipClass() = "+responseEntity.toString());
			if (responseEntity.getStatus() ==200) {
				// call OK
				//ShipClassType shipClassTypeFound = responseEntity.readEntity(ShipClassType.class);
				shipClassFound = responseEntity.readEntity(ShipClass.class);
				System.out.println("shipClassFound set... ");
				shipClassTypeInRule = newShipClassTypeCall.newShipClassType(
						shipClassFound.getType(),
						shipClassFound.getMinTonnageIn(),
						shipClassFound.getMinTonnageEx(),
						shipClassFound.getMinTonnageIn(),
						shipClassFound.getMinTonnageEx(),
						shipClassFound.getMinLengthIn(),
						shipClassFound.getMinLengthEx(),
						shipClassFound.getMaxLengthIn(),
						shipClassFound.getMaxLengthEx(),
						shipClassFound.getMinDraughtIn(),
						shipClassFound.getMinDraughtEx(),
						shipClassFound.getMaxDraughtIn(),
						shipClassFound.getMaxDraughtEx(),
						shipClassFound.getMinPassengersIn(),
						shipClassFound.getMinPassengersEx(),
						shipClassFound.getMaxPassengerIn(),
						shipClassFound.getMaxPassengersEx(),
						shipClassFound.getMinKeelLaidIn(),
						shipClassFound.getMinKeelLaidEx(),
						shipClassFound.getMaxKeelLaidIn(),
						shipClassFound.getMaxKeelLaidEx(),
						shipClassFound.getLengthUnit(),
						shipClassFound.getTonnageUnit(),
						shipClassFound.getDraughtUnit(),
						currentUserName()
				);
			}
			else
			{
				// call NOT OK
				shipClassFound = null;

				System.out.println("shipClassFound set to NULL... ");
				shipClassTypeInRule = null;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("return shipClassTypeInRule");
		return shipClassTypeInRule;
	}



	public FragmentSKOSConceptOccurrences  GetSkos(String regulationText) {

		FragmentSKOSConceptOccurrences fragment=null;

		class SkosCallback implements InvocationCallback<Response> {

			@Override
			public void completed(Response response) {

  			}
			@Override
			public void failed(Throwable throwable) {
				System.out.println("Invocation failed.!!");
				throwable.printStackTrace();
			}
		}
		System.out.println("-------------------\nRESTClient http://192.168.33.10:9000/api/semantic/skos");
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://192.168.33.10:9000/api/semantic/skos");

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
		invocationBuilder.header("Content-type", "text/plain");

		AsyncInvoker async_invoker = invocationBuilder.async();
		InvocationCallback<Response> sftc = new SkosCallback();
		System.out.println("regulationText = " + regulationText);
		Future<Response> response = async_invoker.post(Entity.entity(regulationText, MediaType.TEXT_PLAIN), sftc);

		SKOSConceptOccurrence skosResponse = null;
		Response responseEntity = null;

		try {
			responseEntity=response.get();
			System.out.println("ResponseEntity in GetSkos1() = "+responseEntity.toString());
			FragmentSKOSConceptOccurrences fragmentSKOS = responseEntity.readEntity(FragmentSKOSConceptOccurrences.class);
			System.out.println("fragmentSKOS set: ");
			fragment = fragmentSKOS;
		} catch (InterruptedException e) {
			System.out.println("Interrupted Exception");
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.out.println("Execution Exception");
			e.printStackTrace();
		}

		System.out.println("Return fragment");

		return fragment;

	}


	public String CreateRdfRootNode(DocumentRoot rootNode) {

		String rootURI = "";

 		class SemanticRuleCallback implements InvocationCallback<Response> {

			@Override
			public void completed(Response response) {
			}

			@Override
			public void failed(Throwable throwable) {
				System.out.println("Invocation failed.");
				throwable.printStackTrace();
			}
		}

		System.out.println("CreateRdfRootNode");
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://192.168.33.10:9000/api/rdf/document/root");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.TEXT_PLAIN);
		invocationBuilder.header("Content-type", "text/plain");
		AsyncInvoker async_invoker = invocationBuilder.async();
		InvocationCallback<Response> sftc = new SemanticRuleCallback();
		System.out.println("rootNode.getTitle())="+rootNode.getTitle());
		System.out.println("rootNode.getShortTitle())="+rootNode.getShortTitle());
//		System.out.println("rootNode.getaddConcept())="+rootNode.get);
		System.out.println("rootNode.getVersion())="+rootNode.getVersion());
		System.out.println("rootNode.getRealises())="+rootNode.getRealises());
		System.out.println("rootNode.getText())="+rootNode.getText());

		Future<Response> response = async_invoker.post(Entity.entity(rootNode, MediaType.APPLICATION_XML), sftc);
		Response responseEntity = null;
		try {
			System.out.println("Try responseEntity=response.get() in CreateRdfRootNode:");
			responseEntity = response.get();
			if (responseEntity.getStatus() ==200) {
				// CALL OK
				System.out.println("ResponseEntity in CreateRdfRootNode() = " + responseEntity.toString());
				rootURI = responseEntity.readEntity(String.class);
				System.out.println("ROOTURI found... = " + rootURI);
			}
			else
			{
				//CALL NOT OK
				System.out.println("Call not OK in CreatedRdfRootNode");
				rootURI=null;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("return rootURI");
		return rootURI;
	}

	public IRIList CreateDocumentComponentNode(DocumentComponentList documentComponentList) {

		IRIList iriList = new IRIList();

		class SemanticRuleCallback implements InvocationCallback<Response> {

			@Override
			public void completed(Response response) {
			}

			@Override
			public void failed(Throwable throwable) {
				System.out.println("Invocation failed.");
				throwable.printStackTrace();
			}
		}

		System.out.println("CreateDocumentComponentNode");
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://192.168.33.10:9000/api/rdf/document/component");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.TEXT_PLAIN);
		invocationBuilder.header("Content-type", "text/plain");
		AsyncInvoker async_invoker = invocationBuilder.async();
		InvocationCallback<Response> sftc = new SemanticRuleCallback();
		Future<Response> response = async_invoker.post(Entity.entity(documentComponentList, MediaType.APPLICATION_XML), sftc);
		Response responseEntity = null;
		try {
			responseEntity=response.get();
			System.out.println("ResponseEntity in CreateDocumentComponentNode() = "+responseEntity.toString());
			if (responseEntity.getStatus() ==200) {
// CALL OK
				iriList = responseEntity.readEntity(IRIList.class);
				System.out.println("iriList found... = " + iriList.getIris().get(0));
			}
			else
			{
// CALL NOT OK
				iriList = null;
				System.out.println("iriList set=null");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("return rootURI");
		return iriList;
	}


	// Calls the SHIP API to create a target ship class in the ontoloy.
	// Will also need another API to link the new target ship class to the RDF graph.
	@Action
	public String CreateTargetShipClass(ShipClass shipClass) {

		String targetShipClassURI = null;

 		class SemanticRuleCallback implements InvocationCallback<Response> {

			@Override
			public void completed(Response response) {
			}
			@Override
			public void failed(Throwable throwable) {
				System.out.println("Invocation failed.");
				throwable.printStackTrace();
			}
		}

		System.out.println("CreateTargetShipClass");
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://192.168.33.10:9000/api/semantic/ship");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.TEXT_PLAIN);
		invocationBuilder.header("Content-type", "text/plain");
		AsyncInvoker async_invoker = invocationBuilder.async();
		InvocationCallback<Response> sftc = new SemanticRuleCallback();
		Future<Response> response = async_invoker.post(Entity.entity(shipClass, MediaType.APPLICATION_XML), sftc);
		Response responseEntity = null;
		try {
 			responseEntity=response.get();
			System.out.println("ResponseEntity in GetTarget() = "+responseEntity.toString());
			if (responseEntity.getStatus() ==200) {
				// responseEntity is OK.
				targetShipClassURI = responseEntity.readEntity(String.class);
				System.out.println("shipClass created with URI= "+targetShipClassURI);
 			}
			else
			{// responseEntity is NOT OK.
				targetShipClassURI = null;
				System.out.println("targetShipClassURI set to null ");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("return targetShipClassURI");
		return targetShipClassURI;
	}

	private String currentUserName() {
		return container.getUser().getName();
	}
	//endregion


	//region > injected services

	@javax.inject.Inject
	private DomainObjectContainer container;

	@javax.inject.Inject
	private ShipClassTypes newShipClassTypeCall;

}

