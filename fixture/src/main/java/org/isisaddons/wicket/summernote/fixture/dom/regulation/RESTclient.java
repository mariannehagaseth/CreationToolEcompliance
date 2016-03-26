package org.isisaddons.wicket.summernote.fixture.dom.regulation;

import org.apache.isis.applib.AbstractService;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.FragmentSKOSConceptOccurrences;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.MySKOSConcept;
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
	@Action
	public ShipClass GetRule(String regulationText) {
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

	System.out.println("-------------------\nhttp://192.168.33.10:9000/api/semantic/target");
// Doesnt work		System.out.println("-------------------\nhttp://192.168.33.10:9000/api/semantic/applicability");
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

	//192.168.33.10:9000/api/semantic/applicability is fetching the target!!
	@Action
	public String GetTarget() {
// Copied from SKOSfreeTextAsync
//		class SKOSFreetextCallback implements InvocationCallback<Response> {
		class SemanticTargetCallback implements InvocationCallback<Response> {

			@Override
			public void completed(Response response) {
				//Called when the invocation was successfully completed. Note that this does not necessarily mean the response has bean fully read, which depends on the parameterized invocation callback response type.
				//Once this invocation callback method returns, the underlying Response instance will be automatically closed by the runtime.
				//Parameters:
				//response - response data.
				System.out.println("Response status code =" + response.getStatus());
	//MHAGA: Target Class will repace MYSKOSConcept
				final MySKOSConcept skosConceptOccurrence = response.readEntity(MySKOSConcept.class);
				System.out.print("skosConceptOccurrence.toString()= ");
				System.out.println(skosConceptOccurrence.toString());
				int skosBegin=skosConceptOccurrence.getBegin();
				System.out.println("skosBegin = "+skosBegin);
				String skosText=skosConceptOccurrence.getText();
				System.out.println("skosText = "+skosText);
			}
			@Override
			public void failed(Throwable throwable) {
				System.out.println("Invocation failed.");
				throwable.printStackTrace();
			}
		}
		System.out.println("-------------------\nFreetext async POST Request Test");
		Client client = ClientBuilder.newClient();
//		WebTarget webTarget = client.target("http://192.168.33.10:9000/api/semantic/skos");
		WebTarget webTarget = client.target("http://192.168.33.10:9000/api/semantic/target");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
		invocationBuilder.header("Content-type", "text/plain");
		// AsyncInvoker async_invoker = invocationBuilder.async().get(sftc);
		AsyncInvoker async_invoker = invocationBuilder.async();
		InvocationCallback<Response> sftc = new SemanticTargetCallback();
		Future<Response> response = async_invoker.post(Entity.entity("A string entity to be POSTed", MediaType.TEXT_PLAIN), sftc);
		String responseGot="";
		try {
			responseGot=response.get().toString();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("response.get().toString()= "+ responseGot);
		return responseGot;
	}


	public FragmentSKOSConceptOccurrences  GetSkos(String regulationText) {

		FragmentSKOSConceptOccurrences fragment=null;

		class SkosCallback implements InvocationCallback<Response> {

			@Override
			public void completed(Response response) {
/*
				//Called when the invocation was successfully completed. Note that this does not necessarily mean the response has bean fully read, which depends on the parameterized invocation callback response type.
				//Once this invocation callback method returns, the underlying Response instance will be automatically closed by the runtime.
				//Parameters:
				//response - response data.
				System.out.println("Response status code in completed =" + response.getStatus());
//				SKOSConceptOccurrence skosConceptOccurrence = response.readEntity(SKOSConceptOccurrence.class);
				FragmentSKOSConceptOccurrences fragmentSKOSConceptOccurrences = response.readEntity(FragmentSKOSConceptOccurrences.class);
				System.out.print("fragmentSKOSConceptOccurrences.toString()= ");
				System.out.println(fragmentSKOSConceptOccurrences.toString());
				String fragmentUri=fragmentSKOSConceptOccurrences.getFragmentUri();
				System.out.println("FragmentUri = "+fragmentUri);


				List<SKOSConceptOccurrence> skosList = fragmentSKOSConceptOccurrences.getSkosConceptOccurrence();
				int lastSkosConcept = fragmentSKOSConceptOccurrences.getSkosConceptOccurrence().size();
				for (int i=0;i< lastSkosConcept; i++) {
 					int skosBegin = skosList.get(i).getBegin();
					System.out.println("skosBegin = "+"i="+i+" " + skosBegin);

 					int skosEnd = skosList.get(i).getEnd();
					System.out.println("skosEnd = "+"i="+i+" " + skosEnd);

					String skosConceptPropertyLabel = skosList.get(i).getSkosConceptProperty().value();
					System.out.println("skosConceptPropertyLabel = "+"i="+i+" " + skosConceptPropertyLabel);

					String skosConceptUri = skosList.get(i).getUri();
					String skosConceptProperyValue = skosConceptUri.substring(skosConceptUri.indexOf("#") + 1);
					System.out.println("skosConceptProperyValue = "+"i="+i+" " + skosConceptProperyValue);

					String usedTerm = regulationText.substring(skosBegin, skosEnd);
					System.out.println("usedTerm = "+"i="+i+" " + usedTerm);
					}
*/
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
 	//		String fragmentUri =response.get().readEntity(FragmentSKOSConceptOccurrences.class).getFragmentUri(); FAILS!!
	//		System.out.println("fragmentUri fetched ="+fragmentUri);
			//System.out.println("The entity got in GetSkos() = "+response.get().getEntity().toString());
//			FragmentSKOSConceptOccurrences skosConcept= responseEntity.readEntity(FragmentSKOSConceptOccurrences.class);
//			System.out.println("skosConcept = "+skosConcept.getSkosConceptOccurrence().get(0).getSkosConceptProperty());
			// FragmentSKOSConceptOccurrences fragmentSKOSConceptOccurrences = responseEntity.readEntity(FragmentSKOSConceptOccurrences.class);
			System.out.println("fragmentSKOS set: ");
			fragment = fragmentSKOS;
		} catch (InterruptedException e) {
			System.out.println("Interrupted Exception");
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.out.println("Execution Exception");
			e.printStackTrace();
		}
		//finally {
	//		System.out.println("Close");
//			responseEntity.close();
//		}
		System.out.println("Return fragment");

		return fragment;
		//return responseEntity.getText();
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


	
	@Action
	public String restClient() {
		/**
		  Conceptually, the steps required to submit a request are the following:
    1)obtain an Client instance
    2)create a WebTarget pointing at a Web resource
    3)build a request
    4)submit a request to directly retrieve a response or get a prepared Invocation for later submission
		 **/
		try {
			System.out.println("Hello Jersey Test!");
			System.out.println("-------------------\nFreetext POST Request Test");
	// Create a client instance:
	Client client = ClientBuilder.newClient();
	// Create a WebTarget:
	String resourceString = "http://192.168.33.10:9000/skos/freetext";
	WebTarget target = client.target(resourceString);
	 // Build a request:
	//Invocation.Builder invocationBuilder =	target.request(MediaType.APPLICATION_XML_TYPE);
	Invocation.Builder invocationBuilder =	target.request(MediaType.APPLICATION_XML);
	invocationBuilder.header("Content-type", "text/plain");
    Response response = invocationBuilder.post(Entity.entity("A string entity to be POSTed", MediaType.TEXT_PLAIN));
		System.out.println("response.getStatus() = " + response.getStatus());
		final MySKOSConcept skosConceptOccurrence;
		skosConceptOccurrence = response.readEntity(MySKOSConcept.class);
		System.out.println("skosConceptOccurrence: " + skosConceptOccurrence.toString());
    if (response.getStatus() != 200) {
			throw new RuntimeException("" + response.getStatus());
		}
     String value = response.readEntity(String.class);
     response.close();  // You should close connections!
     System.out.println("value= "+value);
     return value;
     	} catch (Exception e) {
			System.out.println("status:" + e.getMessage());
			if (e.getMessage().equals("404")) {
				System.out.println("Resource not found.");
			}
			if (e.getMessage().equals("500")) {
				System.out.println("An unknown error occured.");
			}
			return "Exception Catched in RESTclientTest";
		}
	}

//Region findSimilarRegulations
	@Programmatic
	public Set<RegulationText> findSimilarRegulations(RegulationText regulation){
		//search regulations similar to regulation:
		// Dummy fetch all regulations except the one given:
		List<RegulationText> regList= (List<RegulationText>) allMatches(new QueryDefault(RegulationText.class, "findRegulations"));
		if(regList.isEmpty()) {
				warnUser("No regulations found.");
			}else {regList.remove(regulation);}
		Set<RegulationText> regSet = null;
		try {
			regSet = new HashSet<RegulationText>(regList);
		} catch (Exception e) {
			System.out.print("No similar regulations found!!");
			e.printStackTrace();
		}
		return regSet;
		}
		//endregion

	//Region findSimilar
	@Programmatic
	public Set<RegulationText> findSimilars(){
		//search regulations similar to regulation:
		// Dummy fetch all regulations except the one given:
		List<RegulationText> regList= (List<RegulationText>) allMatches(new QueryDefault(RegulationText.class, "findRegulations"));
		if(regList.isEmpty()) {
			warnUser("No regulations found.");
		}else {}

		Set<RegulationText> regSet = null;
		try {
			regSet = new HashSet<RegulationText>(regList);
		} catch (Exception e) {
			System.out.print("No similar regulations found!!");
			e.printStackTrace();
		}
		return regSet;
	}
	//endregion



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
}
