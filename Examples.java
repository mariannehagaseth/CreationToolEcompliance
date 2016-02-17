package com.temis.luxid.webservice.samples;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class Examples {

	
	
	
	public static void main(String[] args) {

		
		
		//	***********************************************************
		//	* EXAMPLE 1 - CALL WEBSERVICE TO EXTRACT A 'RULE' FROM TEXT
		//	***********************************************************
		
		//The text to be annotated:
		String stringInput = "Oil tankers longer than 100 metres but smaller than 50000 GT must have at least 4 life rafts.";
		//A passenger ship shall be subject to the surveys specified below
		//Main Example: "Oil tankers longer than 100 metres but smaller than 50000 GT must have at least 4 life rafts."
		//and shorter than 100m 
		//larger than 350 GT but smaller than 700 GT
		//All oil tankers bigger than 500 GT and built before 1 January 1980 must have at least 4 life rafts.
		//"Cargo ships of 500 gross tonnage and upwards and constructed on or after 1 July 2002 must have at least 3 lifeboats."
		
		
		//The RESTCalls class is used to call the TMS webservice
				
		//This is getting the target ship class of the rule
		ShipClass s1 = RESTCalls.getTargetClass(stringInput);
		
		//Print the results
		System.out.println("Ship type: " + s1.getType());
		System.out.println("MinTonnageEX: " + s1.getMinTonnageEX() + " " + s1.getTonnageUnit());
		System.out.println("MaxTonnageEX: " + s1.getMaxTonnageEX() + " " + s1.getTonnageUnit());
		System.out.println("MinTonnageIN: " + s1.getMinTonnageIN() + " " + s1.getTonnageUnit());
		System.out.println("MaxTonnageIN: " + s1.getMaxTonnageIN() + " " + s1.getTonnageUnit());
		System.out.println("MinLengthEX: " + s1.getMinLengthEX() + " " + s1.getLengthUnit());
		System.out.println("MaxLengthEX: " + s1.getMaxLengthEX() + " " + s1.getLengthUnit());
		System.out.println("MinLengthIN: " + s1.getMinLengthIN() + " " + s1.getLengthUnit());
		System.out.println("MaxLengthIN: " + s1.getMaxLengthIN() + " " + s1.getLengthUnit());
		System.out.println("MinDraft: " + s1.getMinDraftEX() + " " + s1.getLengthUnit());
		System.out.println("MaxDraft: " + s1.getMaxDraftEX() + " " + s1.getLengthUnit());
		System.out.println("MinPassEX: " + s1.getMinPassEX());
		System.out.println("MaxPassEX: " + s1.getMaxPassEX());
		System.out.println("MinPassIN: " + s1.getMinPassIN());
		System.out.println("MaxPassIN: " + s1.getMaxPassIN());
		System.out.println("MinKeelLaid: " + s1.getMinKeelLaidEX());
		System.out.println("MaxKeelLaid: " + s1.getMaxKeelLaidEX());
		
		
		
		
		//	*********************************************************************
		//	* EXAMPLE 2 - CALL WEBSERVICE TO EXTRACT AN 'APPLICABILITY' FROM TEXT
		//	*********************************************************************
		
		//Even if the text does not contain a full 'rule' (or the system does not manage to extract it), we can still look for applicability ship classes.
		//This is particularly relevant for headline texts (see example below) from which we extract 'seed' classes.
		//So in practice, call this function on the same text if getTargetClass does not return anything!
		
		stringInput = "Applicable to all bulk carriers no larger than 50000 GT";
		ShipClass s2 = RESTCalls.getApplicabilityClass(stringInput);
		//Print the results
		System.out.println("Ship type: " + s2.getType());
		System.out.println("MinTonnageEX: " + s2.getMinTonnageEX() + " " + s2.getTonnageUnit());
		System.out.println("MaxTonnageEX: " + s2.getMaxTonnageEX() + " " + s2.getTonnageUnit());
		System.out.println("MinTonnageIN: " + s2.getMinTonnageIN() + " " + s2.getTonnageUnit());
		System.out.println("MaxTonnageIN: " + s2.getMaxTonnageIN() + " " + s2.getTonnageUnit());
		System.out.println("MinLengthEX: " + s2.getMinLengthEX() + " " + s2.getLengthUnit());
		System.out.println("MaxLengthEX: " + s2.getMaxLengthEX() + " " + s2.getLengthUnit());
		System.out.println("MinLengthIN: " + s2.getMinLengthIN() + " " + s2.getLengthUnit());
		System.out.println("MaxLengthIN: " + s2.getMaxLengthIN() + " " + s2.getLengthUnit());
		System.out.println("MinDraft: " + s2.getMinDraftEX() + " " + s2.getLengthUnit());
		System.out.println("MaxDraft: " + s2.getMaxDraftEX() + " " + s2.getLengthUnit());
		System.out.println("MinPassEX: " + s2.getMinPassEX());
		System.out.println("MaxPassEX: " + s2.getMaxPassEX());
		System.out.println("MinPassIN: " + s2.getMinPassIN());
		System.out.println("MaxPassIN: " + s2.getMaxPassIN());
		System.out.println("MinKeelLaid: " + s2.getMinKeelLaidEX());
		System.out.println("MaxKeelLaid: " + s2.getMaxKeelLaidEX());
		
		
		
		
		
		//	**********************************************************
		//	* EXAMPLE 3 - CREATE NEW TARGET SHIP CLASS IN THE ONTOLOGY
		//	**********************************************************
		
		//Now let's shove the ship class created in Example 1 into the ontology.
		
		//Create an instance of the interface class
		OntologyInterface oi = new OntologyInterface();
		
		//The create function takes lists of ships as input - in this case, there is only one ship
		Set<ShipClass> shipset = new HashSet<ShipClass>();
		shipset.add(s1);

		try {
			oi.createTargetShipClassIntersection("MyNewShipClass", shipset, null, false);
			//The ships in the second set (here set to null) are complemented. I.e. when you pass in the two sets (A, B) and (C, D), the resulting target ship class will be (A INTERSECTION B INTERSECTION NOT C INTERSECTION NOT D).
			//The second set is meant for exceptions (e.g. "... this does not apply to oil tankers bigger than 500 tons").
			//The boolean includeSeeds (last input variable, here set to false) determines whether 'seed' classes (restrictions coming from headlines, scope chapters etc.) are included.
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//This should create a ship class with the properties of 's' in the ontology output file
		//Using the input string "Oil tankers longer than 100 metres but smaller than 50000 GT must have at least 4 life rafts.", this give the following:
		//Ship
		//and shipLength some double[> 100.0]
		//and shipTonnage some double[< 50000.0]
		//and shipType value "crude oil tanker"
		
		System.out.println("");
		System.out.println("");
		
		
		
		
		
		
		//	*****************************************************************************************
		//	* EXAMPLE 4 - CREATE NEW TARGET SHIP CLASS FROM MORE THAN ONE SHIP, INCLUDING COMPLEMENTS
		//	*****************************************************************************************
		
		//Another example with a couple more ships (created 'by hand' rather than returned by the TMS WS)
		ShipClass firstship = new ShipClass();
		firstship.setMinLengthEX(280);
		
		ShipClass secondship = new ShipClass();
		secondship.setMinPassEX(18);
		secondship.setMinKeelLaidEX(20010130);
		
		//The next two ships constitute 'exceptions' - i.e. the resulting target class will intersect with the COMPLEMENTS of these ones.
		ShipClass complship1 = new ShipClass();
		complship1.setMaxDraftEX(5);
		complship1.setMinDraftEX(95);
		
		ShipClass complship2 = new ShipClass();
		complship2.setMinDraftEX(95);
		
		Set<ShipClass> a = new HashSet<ShipClass>();
		a.add(firstship);
		a.add(secondship);

		Set<ShipClass> b = new HashSet<ShipClass>();
		b.add(complship1);
		b.add(complship2);
		
		try {
			oi.createTargetShipClassIntersection("ExceptionShipClass", a, b, false);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//The resulting class in the ontology is the intersection of the first two ships and the complements of the other two.
		//In particular, you should see (note that the draft condition is tautological in this example):
		//Ship
		//and not (shipDraft some double[> 95.0])
		//and (not (shipDraft some double[> 95.0]))
		//     or (not (shipDraft some double[< 5.0]))
		//and shipKeelLaid some integer[> "20010130"^^integer]
		//and shipLength some double[> 280.0]
		//and shipPassengerNumber some integer[> "18"^^integer]
		
		
		
		
		
		//	*************************************************************************************
		//	* EXAMPLE 5 - CREATE NEW TARGET SHIP CLASS FROM MORE THAN ONE SHIP, INCLUDING 'SEEDS'
		//	*************************************************************************************
		
		//Now let's include 'seed' classes (i.e. collect restrictions from headlines and other parts of the document that are higher up in the RDF tree). This is hardcoded at the moment, will need to complete.
		ShipClass thirdship = new ShipClass();
		thirdship.setMinDraftEX(5);
		thirdship.setType("crude oil tanker");
		ShipClass fourthship = new ShipClass();
		fourthship.setMaxLengthEX(350);
		
		Set<ShipClass> set = new HashSet<ShipClass>();
		set.add(thirdship);
		set.add(fourthship);
		
		try {
			oi.createTargetShipClassIntersection("ShipClassWithSeed", set, null, true);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//The resulting class in the ontology will be the intersection of thirdship, fourthship and SEED_Headline2 (which is currently hardcoded to be the seed class to be used - easy to change).
		//In particular:
		//Ship
		//and SEED_Headline2
		//and Ship
		//and shipDraft some double[> 5.0]
		//and shipLength some double[< 350.0]
		//and shipType value "crude oil tanker"
		
		System.out.println("");
		
		
		
		
		//	******************************************
		//	* EXAMPLE 6 - CREATE SHIP CLASS AS A UNION
		//	******************************************

		//Let's create a ship class as a union of two ship classes - use the MARPOL example
		ShipClass mytanker = new ShipClass();
		mytanker.setType("crude oil tanker");
		mytanker.setMinTonnageEX(150);
		
		ShipClass mygenericship = new ShipClass();
		mygenericship.setMinTonnageEX(400);
		
		Set<ShipClass> myset = new HashSet<ShipClass>();
		myset.add(mytanker);
		myset.add(mygenericship);
		try {
			oi.createTargetShipClassUnion("UnionShipClass", myset, null, false);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//This should produce the following ship class in the ontology:
		//Ship
		//and (shipTonnage some double[> 150.0]
		//and shipType value "crude oil tanker")
		//or (shipTonnage some double[> 400.0])
		System.out.println("");
		
		
		
		
		//	********************************************************************
		//	* EXAMPLE 7 - RETRIEVE REGULATIONS FOR A CONCRETE INSTANCE OF A SHIP
		//	********************************************************************
		
		//Now let's create a concrete ship instance and retrieve the relevant regulations.
		IndividualShip myship = new IndividualShip();
		myship.setType("crude oil tanker");
		myship.setTonnage(50000);
		myship.setLength(290);
		myship.setPassengerNumber(20);
		myship.setDraft(8);
		myship.setKeelLaid(20021030);
		
		//Get the names of the (relevant) classes containing this individual as a set of strings.
		Set<String> regs = oi.getRegulationsForShipInstance(myship);

		for (String reg : regs){
			System.out.println(reg);
		}
		//The following should be returned (assuming the above examples have been used):
		//ShipClassWithSeed
		//UnionShipClass
		//OilTankers
		//BigShip
		//Ship
		//ExceptionShipClass
		
	}

}
