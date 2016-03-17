package org.isisaddons.wicket.summernote.fixture.dom.regulation;

import org.apache.isis.applib.AbstractService;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.MySKOSConcept;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.SKOSConceptProperty;

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
public class RESTclientTest extends AbstractService   {

	// From https://docs.jboss.org/resteasy/docs/3.0-beta-3/userguide/html/RESTEasy_Client_Framework.html

 	@Action
	public String SkosFreetext() {
	        Client client = ClientBuilder.newClient();
	        WebTarget webTarget = client.target("http://192.168.33.10:9000/skos/freetext");
	        Invocation.Builder invocationBuilder =
	                webTarget.request(MediaType.APPLICATION_XML_TYPE);
	        invocationBuilder.header("Content-type", "text/plain");
	        Response response = invocationBuilder.post(Entity.entity("A string entity to be POSTed", MediaType.TEXT_PLAIN));
	        System.out.println(response.getStatus());
	        String returnValue = response.readEntity(String.class);
	        System.out.println(returnValue);
	        return  returnValue;
	    }



//		er kopiert fra: https://bitbucket.org/droythorne/jersey/src/8f6d940ff0dd2e5b5bed0dc52c5a558b511a1c3e/src/main/java/Main.java?at=master 

	@Action
	public String SkosFreetextAsync() {

	class SKOSFreetextCallback implements InvocationCallback<Response> {

	@Override
	public void completed(Response response) {
		//Called when the invocation was successfully completed. Note that this does not necessarily mean the response has bean fully read, which depends on the parameterized invocation callback response type.
		//Once this invocation callback method returns, the underlying Response instance will be automatically closed by the runtime.
		//Parameters:
		//response - response data.
		System.out.println("Response status code " + response.getStatus() + " received.");
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




	System.out.println("-------------------\nFreetext async POST Request Test");	Client client = ClientBuilder.newClient();
	//WebTarget webTarget = client.target("http://192.168.33.10:9000/skos/freetext");
//	WebTarget webTarget = client.target("http://192.168.33.10:9000/skos/textgenerator"); THIS WAS THE PREIOUS ONE
		WebTarget webTarget = client.target("http://192.168.33.10:9000/api/semantic/skos");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
	invocationBuilder.header("Content-type", "text/plain");
	// AsyncInvoker async_invoker = invocationBuilder.async().get(sftc);
	AsyncInvoker async_invoker = invocationBuilder.async();
	InvocationCallback<Response> sftc = new SKOSFreetextCallback();
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
	@Programmatic
// Lager denne for å teste henting av Ship  Class
public MySKOSConcept fetchSKOSconcept(){



	class SKOSFreetextCallback implements InvocationCallback<Response> {

		MySKOSConcept skosConceptOccurrence;

		@Override
		public void completed(Response response) {
			System.out.println("Response status code "
					+ response.getStatus() + " received.");
			skosConceptOccurrence = response.readEntity(MySKOSConcept.class);
			System.out.print("skosConceptOccurrence.toString()= ");
			System.out.println(skosConceptOccurrence.toString());
			int skosBegin=skosConceptOccurrence.getBegin();
			System.out.println("skosBegin = "+skosBegin);
			String skosText=skosConceptOccurrence.getText();
			System.out.println("skosText = "+skosText);
			SKOSConceptProperty skosConceptProperty=skosConceptOccurrence.getSkosConceptProperty();
			System.out.println("skosConceptProperty = "+skosConceptProperty.value());
		}

		@Override
		public void failed(Throwable throwable) {
			System.out.println("Invocation failed.");
			throwable.printStackTrace();
		}
	}



	System.out.println("-------------------\nFreetext async POST Request Test");	Client client = ClientBuilder.newClient();
	//WebTarget webTarget = client.target("http://192.168.33.10:9000/skos/freetext");
	WebTarget webTarget = client.target("http://192.168.33.10:9000/skos/textgenerator");
	Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
	invocationBuilder.header("Content-type", "text/plain");
	// AsyncInvoker async_invoker = invocationBuilder.async().get(sftc);
	AsyncInvoker async_invoker = invocationBuilder.async();
	InvocationCallback<Response> sftc = new SKOSFreetextCallback();
	Future<Response> response = async_invoker.post(Entity.entity("A string entity to be POSTed", MediaType.TEXT_PLAIN), sftc);
	String responseGot="";
	System.out.println("responseGot is blank");

	try {
		System.out.println("responseGot=response.get().toString();");
		responseGot=response.get().toString();
	} catch (InterruptedException e) {
		e.printStackTrace();
	} catch (ExecutionException e) {
		e.printStackTrace();
	}

	System.out.println("response.get().toString()= " + responseGot);
	System.out.println("return skosConceptOccurrence");
	return null;
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

	//Region 	findSolasCodes
/*
	@Programmatic
	public Set<SolasCode> findSolasCodes (SolasChapter thisSolasChapter){
		//search regulations similar to regulation:
		// Dummy fetch all regulations except the one given:
		List<SolasCode> codeList= (List<SolasCode>) allMatches(new QueryDefault(SolasCode.class, "findCodes", "solasChapter", thisSolasChapter));
		if(codeList.isEmpty()) {
			warnUser("No Codes found.");
		}else {}

		Set<SolasCode> codeSet = null;
		try {
			codeSet = new HashSet<SolasCode>(codeList);
		} catch (Exception e) {
			System.out.print("No similar regulations found!!");
			e.printStackTrace();
		}
		return codeSet;
	}
	//endregion
*/


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
