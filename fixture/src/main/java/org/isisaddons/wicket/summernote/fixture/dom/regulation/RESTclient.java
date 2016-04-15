package org.isisaddons.wicket.summernote.fixture.dom.regulation;

import org.apache.isis.applib.AbstractService;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.DocumentRoot;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.FragmentSKOSConceptOccurrences;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.SKOSConceptOccurrence;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.ShipClass;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
 				responseEntity=response.get();
				System.out.println("ResponseEntity in GetRule() = "+responseEntity.toString());
				ShipClass shipClassFound = responseEntity.readEntity(ShipClass.class);
 				System.out.println("shipClassFound set... ");
				shipClassInRule = shipClassFound;
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
			responseEntity=response.get();
			System.out.println("ResponseEntity in GetRule() = "+responseEntity.toString());
			ShipClass shipClassFound = responseEntity.readEntity(ShipClass.class);
			System.out.println("shipClassFound set... ");
			shipClassInRule = shipClassFound;
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
			responseEntity=response.get();
			System.out.println("ResponseEntity in GetRule() = "+responseEntity.toString());
			//ShipClassType shipClassTypeFound = responseEntity.readEntity(ShipClassType.class);
			ShipClass shipClassFound = responseEntity.readEntity(ShipClass.class);

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
	//	Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
		invocationBuilder.header("Content-type", "text/plain");

		AsyncInvoker async_invoker = invocationBuilder.async();
		InvocationCallback<Response> sftc = new SkosCallback();
//		Future<Response> response = async_invoker.post(Entity.entity("A string entity to be POSTed", MediaType.TEXT_PLAIN), sftc);
		System.out.println("regulationText = " + regulationText);
		Future<Response> response = async_invoker.post(Entity.entity(regulationText, MediaType.TEXT_PLAIN), sftc);
//		Future<Response> response = async_invoker.post(Entity.entity(regulationText, MediaType.APPLICATION_XML), sftc); reason=Unsupported Media Type}}

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
		Future<Response> response = async_invoker.post(Entity.entity(rootNode, MediaType.TEXT_PLAIN), sftc);
		Response responseEntity = null;
		try {
			responseEntity=response.get();
			System.out.println("ResponseEntity in CreateRdfRootNode() = "+responseEntity.toString());
			rootURI = responseEntity.readEntity(String.class);
			System.out.println("ROOTURI found... = "+ rootURI);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("return rootURI");
		return rootURI;
	}



	@Action
	 public void SkosUriRequest() {
        Client client = ClientBuilder.newClient();
       // WebTarget webTarget = client.target("http://192.168.33.10:9000/skos/uri_request");
		WebTarget webTarget = client.target("http://192.168.33.10:9000/skos/textgenerator");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.TEXT_PLAIN_TYPE);
        invocationBuilder.header("Content-type", "application/xml");
        Response response = invocationBuilder.post(Entity.entity("<URIRequest><iri>http:/e-compliance/test</iri></URIRequest>", MediaType.APPLICATION_XML));
        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
    }






// Region Find ShipType s
	@Programmatic
	public Set<ShipType> findShipType(){
		//Find all ship types stored in database
		// Update this to fetch from API??
		List<ShipType> shipTypeList= (List<ShipType>) allMatches(new QueryDefault(ShipType.class, "findShipTypes"));
		//if(shipTypeList.isEmpty()) {
		//	warnUser("No ship types found.");
//		}
		Set<ShipType> shipTypeSet = null;
		try {
			shipTypeSet = new HashSet<ShipType>(shipTypeList);
		} catch (Exception e) {
		//	System.out.print("No ship types found!!");
			e.printStackTrace();
		}
		return shipTypeSet;
	}
	//endregion

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

