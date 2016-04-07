package org.isisaddons.wicket.summernote.fixture.dom.regulation;

import org.apache.isis.applib.AbstractService;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.FragmentSKOSConceptOccurrences;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.SKOSConceptOccurrence;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.ShipClass;

import java.util.ArrayList;
import java.util.List;

@DomainService
@DomainServiceLayout(menuBar= DomainServiceLayout.MenuBar.TERTIARY)
public class CreationController extends AbstractService   {


	public List<String> CheckTerms(final String plainRegulationText, final FragmentSKOSConceptOccurrences fragment) {

		// Call API to fetch SKOS terms
		String fragmentUri = "";
		List<SKOSConceptOccurrence> skosList = null;
		List <String> skosListRead =null;
		// VAriable used to build the SKOS term list to show to the user.
		String builtSkosTerms = "";
		// Variable used to put colours on the annotated text for the user.
		// this.requirement = "<span style=\"background-color: rgb(255, 255, 0);\">Every ship</span> must have a polar code certificate.<br>";
		final String startYellow = "<span style=\"background-color: rgb(255, 255, 0);\">";
		final String startGreen = "<span style=\"background-color: rgb(0, 255, 0);\">";
		final String endColour = "</span>";
		final String endString = "<br>";
		String colourText = "";
		System.out.println("plainRegulationText = " + plainRegulationText);
		//FragmentSKOSConceptOccurrences fragment = restClient.GetSkos(plainRegulationText);

		if (fragment == null){
			System.out.println("fragment is null");
		}
		else
		{
			fragmentUri=fragment.getFragmentUri();
			System.out.println("FragmentUri = "+fragmentUri);

			int nextChar = 0;
			int skosBegin = 0;
			int skosEnd = 0;
			int skosBeginPrevious = -1;
			int skosEndPrevious = -1;

			skosList = fragment.getSkosConceptOccurrence();

			int lastSkosConcept = fragment.getSkosConceptOccurrence().size();
			if (lastSkosConcept >0) {
				for (int i = 0; i < lastSkosConcept; i++) {
					skosBegin = skosList.get(i).getBegin();
					System.out.println("skosBegin = " + "i=" + i + " " + skosBegin);

					skosEnd = skosList.get(i).getEnd();
					System.out.println("skosEnd = " + "i=" + i + " " + skosEnd);

					String skosConceptPropertyLabel = skosList.get(i).getSkosConceptProperty().value();
					System.out.println("skosConceptPropertyLabel = " + "i=" + i + " " + skosConceptPropertyLabel);

					String skosConceptUri = skosList.get(i).getUri();
					String skosConceptProperyValue = skosConceptUri.substring(skosConceptUri.indexOf("#") + 1);
					skosConceptProperyValue = skosConceptProperyValue.replace("_"," ");
					System.out.println("skosConceptProperyValue = " + "i=" + i + " " + skosConceptProperyValue);

					String usedTerm = plainRegulationText.substring(skosBegin, skosEnd);
					System.out.println("usedTerm = " + "i=" + i + " " + usedTerm);
					System.out.println("nextChar=" + nextChar + " skosBegin="+skosBegin+" skosEnd="+skosEnd);
					if ((skosBegin != skosBeginPrevious)&& (skosEnd != skosEndPrevious)) {
						// only copy the term to the Text once.
						if (nextChar < skosBegin) {
							colourText = colourText + plainRegulationText.substring(nextChar, skosBegin);
						}
					}
					if (skosConceptPropertyLabel.equals("altLabel")) {
						System.out.println("usedTerm.substring(usedTerm.length()).toUpperCase()="+usedTerm.substring(usedTerm.length()-1).toUpperCase());
						System.out.println("(skosConceptProperyValue.toUpperCase().....= "+ skosConceptProperyValue.toUpperCase());
						System.out.println("(usedTerm.toUpperCase().....= "+ usedTerm.substring(0,usedTerm.length()-1).toUpperCase());

						if (((skosConceptProperyValue.toUpperCase().equals(usedTerm.substring(0,usedTerm.length()-1).toUpperCase())) && (usedTerm.substring(usedTerm.length()-1).toUpperCase()).equals("S"))) {
							// The alternative label is used. however, it is just pluralis of an existing term:
							// Set gr-een colour of the term.
							if ((skosBegin != skosBeginPrevious)&& (skosEnd != skosEndPrevious)) {
								colourText = colourText+startGreen; }
							System.out.println("altLabel with plural S");
							builtSkosTerms = builtSkosTerms+"This term is OK: \""+ usedTerm+"\"\n";
							System.out.println("builtSkosTerms="+ builtSkosTerms);
							System.out.println("skosConceptProperyValue-4="+skosConceptProperyValue);
							System.out.println("usedTerm-4="+usedTerm);

						}
						else {
							// The alternative label is used. Show the preferred label.
							if (((skosConceptProperyValue.toUpperCase().replace(" ","").equals(usedTerm.substring(0,usedTerm.length()-2).replace(" ","").toUpperCase())) && (usedTerm.substring(usedTerm.length()-1).toUpperCase()).equals("S"))) {
								System.out.println("skosConceptProperyValue-2="+skosConceptProperyValue);
								System.out.println("usedTerm-2="+usedTerm);
								builtSkosTerms = builtSkosTerms + "\"" + skosConceptProperyValue+"s" + "\"" + " should be used instead of " + "\"" + usedTerm + "\"." + "\n";
								System.out.println("extra s added");
							}
							else {
								System.out.println("skosConceptProperyValue.toUpperCase().replace()ELSE="+skosConceptProperyValue.toUpperCase().replace("",""));
								System.out.println("usedTerm.substring(0, usedTerm.length() - 1).replace().toUpperCase())ELSE=" + usedTerm.substring(0, usedTerm.length() - 1).replace(" ", "").toUpperCase());
								System.out.println("usedTerm.substring(usedTerm.length()-1).toUpperCase()ELSE="+usedTerm.substring(usedTerm.length()-1).toUpperCase());
								if (((skosConceptProperyValue.toUpperCase().replace(" ","").equals(usedTerm.substring(0,usedTerm.length()-1).replace(" ","").toUpperCase())) && (usedTerm.substring(usedTerm.length()-1).toUpperCase()).equals("S"))) {
									System.out.println("skosConceptProperyValue-7=" + skosConceptProperyValue);
									System.out.println("usedTerm-7=" + usedTerm);

									builtSkosTerms = builtSkosTerms + "\"" + skosConceptProperyValue+"s" + "\"" + " should be used instead of " + "\"" + usedTerm + "\"." + "\n";
								}
								else{
									System.out.println("skosConceptProperyValue-3=" + skosConceptProperyValue);
									System.out.println("usedTerm-3=" + usedTerm);

									builtSkosTerms = builtSkosTerms + "\"" + skosConceptProperyValue + "\"" + " should be used instead of " + "\"" + usedTerm + "\"." + "\n";
								}
							}
							// set yellow colour of the term
							if ((skosBegin != skosBeginPrevious)&& (skosEnd != skosEndPrevious)) {
								colourText = colourText + startYellow;}
							System.out.println("altLabel");
							System.out.println("builtSkosTerms="+ builtSkosTerms);
						}
					}
					if (skosConceptPropertyLabel.equals("prefLabel")) {
						// Set gr-een colour of the term.
						if ((skosBegin != skosBeginPrevious)&& (skosEnd != skosEndPrevious)) {
							colourText = colourText+startGreen;}
						System.out.println("skosConceptProperyValue-6="+skosConceptProperyValue);
						System.out.println("usedTerm-6="+usedTerm);
						System.out.println("This term is OK: "+ usedTerm);
						builtSkosTerms = builtSkosTerms+"This term is OK: \""+ usedTerm+"\"\n";
					}
					if ((skosBegin != skosBeginPrevious)&& (skosEnd != skosEndPrevious)) {
						colourText = colourText + usedTerm;
						colourText = colourText + endColour;
					}
					nextChar = skosEnd;

					// needed to avoid repeating the output-text if two preflabels in different concept schemas are found.
					skosBeginPrevious = skosBegin;
					skosEndPrevious = skosEnd;
				}//for
				// Copy the rest of the text, after the last SKOS term is found:
				System.out.println("nextChar when the last SKOS term is found="+ nextChar);
				System.out.println("plainRegulationText.length()-1 ="+ String.valueOf(plainRegulationText.length()-1));
				if (nextChar < plainRegulationText.length()-1){
					//Must copy the rest of the text, after the last SKOS term:
					colourText = colourText+plainRegulationText.substring(nextChar);
				}
			}//if
		}//else
		//setSkosTerms(builtSkosTerms);

		List<String> annotation = new ArrayList<String>();

		annotation.add(0,builtSkosTerms);// This is String skosTerms!! skosTerms = builtSkosTerms;
		annotation.add(1, colourText); // This is annotatedText!! annotatedText = colourText;

	 	System.out.println("colourText = " + colourText);
		//setAnnotatedText(colourText);

		//container.flush();
		//container.informUser("Fetched SKOS terms completed for " + container.titleOf(this));
		//System.out.println("Fetching SKOS Terms here using GetSkos!!");
		//System.out.println("builtSkosTerms= "+builtSkosTerms);
		return annotation;
	}

	public String ShowRule(final String plainRegulationText, final ShipClass shipClassFound) {
		// Call API to fetch Target, that is the Rule.

		String ruleFound = "";

		String shipType = null;
		String tonnageUnit = null;
		String minTonnageEX= null;
		String maxTonnageEX= null;
		String minTonnageIN = null;
		String maxTonnageIN = null;
		String minLengthEX = null;
		String maxLengthEX = null;
		String minLengthIN = null;
		String maxLengthIN = null;
		String minPassEX = null;
		String maxPassEX = null;
		String minPassIN = null;
		String maxPassIN = null;
		String minKeelLaidIN = null;
		String maxKeelLaidIN = null;
		String minKeelLaidEX = null;
		String maxKeelLaidEX = null;
		String minDraughtEx = null;
		String maxDraughtEx = null;
		String minDraughtIn = null;
		String maxDraughtIn = null;


		String lengthUnit = null;
		String draughtUnit = null;

	//	ShipClass shipClassFound = null;

//		shipClassFound = restClient.GetRule(plainRegulationText);

		if (shipClassFound == null) {
			System.out.println("No ship class found in rule");
		} else {
			shipType = shipClassFound.getType();
			System.out.println("shipClassName = " + shipType);
			if (shipType.length()>0) {
				ruleFound = ruleFound + "SHIP TYPE : ";
				ruleFound = ruleFound + shipType + "\n";
			}

			minTonnageIN = String.valueOf(shipClassFound.getMinTonnageIn());
			minTonnageEX= String.valueOf(shipClassFound.getMinTonnageEx());
			maxTonnageIN = String.valueOf(shipClassFound.getMaxTonnageIn());
			maxTonnageEX= String.valueOf(shipClassFound.getMaxTonnageEx());
			minLengthIN = String.valueOf(shipClassFound.getMinLengthIn());
			minLengthEX = String.valueOf(shipClassFound.getMinLengthEx());
			maxLengthIN = String.valueOf(shipClassFound.getMaxLengthIn());
			maxLengthEX = String.valueOf(shipClassFound.getMaxLengthEx());
			minPassIN = String.valueOf(shipClassFound.getMinPassengersIn());
			minPassEX = String.valueOf(shipClassFound.getMinPassengersEx());
			maxPassIN = String.valueOf(shipClassFound.getMaxPassengerIn());
			maxPassEX = String.valueOf(shipClassFound.getMaxPassengersEx());
			minKeelLaidIN = String.valueOf(shipClassFound.getMinKeelLaidIn());
			minKeelLaidEX = String.valueOf(shipClassFound.getMinKeelLaidEx());
			maxKeelLaidIN = String.valueOf(shipClassFound.getMaxKeelLaidIn());
			maxKeelLaidEX = String.valueOf(shipClassFound.getMaxKeelLaidEx());
			minDraughtEx = String.valueOf(shipClassFound.getMinDraughtEx());
			maxDraughtEx = String.valueOf(shipClassFound.getMaxDraughtEx());
			minDraughtIn = String.valueOf(shipClassFound.getMinDraughtIn());
			maxDraughtIn = String.valueOf(shipClassFound.getMaxDraughtIn());


			tonnageUnit = shipClassFound.getTonnageUnit();
			lengthUnit = shipClassFound.getLengthUnit();
			draughtUnit = shipClassFound.getDraughtUnit();

			if (!minTonnageIN.equals("0.0")) {ruleFound = ruleFound+ "TONNAGE >= " + minTonnageIN +" "+tonnageUnit+"\n";}
			if (!minTonnageEX.equals("0.0")) {ruleFound = ruleFound+ "TONNAGE > " + minTonnageEX +" "+tonnageUnit+"\n";}
			if (!maxTonnageIN.equals("0.0")) {ruleFound = ruleFound+ "TONNAGE <= " +maxTonnageIN+" "+tonnageUnit+"\n";}
			if (!maxTonnageEX.equals("0.0")) {ruleFound = ruleFound+ "TONNAGE < "+maxTonnageEX+" "+tonnageUnit+"\n";}
			if (!minLengthIN.equals("0.0")) {ruleFound = ruleFound+ "LENGTH >= "+minLengthIN+" "+lengthUnit+"\n";}
			if (!minLengthEX.equals("0.0")) {ruleFound = ruleFound+ "LENGTH > "+minLengthEX+" "+lengthUnit+"\n";}
			if (!maxLengthIN.equals("0.0")) {ruleFound = ruleFound+ "LENGTH =< "+maxLengthIN+" "+lengthUnit+"\n";}
			if (!maxLengthEX.equals("0.0")) {ruleFound = ruleFound+ "LENGTH < "+maxLengthEX+" "+lengthUnit+"\n";}
			if (!minPassIN.equals("0")) {ruleFound = ruleFound+ "No of PASSENGERS >= "+minPassIN+"\n";}
			if (!minPassEX.equals("0")) {ruleFound = ruleFound+ "No of PASSENGERS > "+minPassEX+"\n";}
			if (!maxPassIN.equals("0")) {ruleFound = ruleFound+ "No of PASSENGERS =< "+maxPassIN+"\n";}
			if (!maxPassEX.equals("0")) {ruleFound = ruleFound+ "No of PASSENGERS < "+maxPassEX+"\n";}
			if (!minKeelLaidIN.equals("0")) {ruleFound = ruleFound+ "KEEL LAID DATE >= "+minKeelLaidIN+"\n";}
			if (!minKeelLaidEX.equals("0")) {ruleFound = ruleFound+ "KEEL LAID DATE > "+minKeelLaidEX+"\n";}
			if (!maxKeelLaidIN.equals("0")) {ruleFound = ruleFound+ "KEEL LAID DATE =< "+maxKeelLaidIN+"\n";}
			if (!maxKeelLaidEX.equals("0")) {ruleFound = ruleFound+ "KEEL LAID DATE > "+maxKeelLaidEX+"\n";}
			if (!minDraughtIn.equals("0.0")) {ruleFound = ruleFound+"DRAUGHT >= "+minDraughtIn+" "+draughtUnit+"\n";}
			if (!minDraughtEx.equals("0.0")) {ruleFound = ruleFound+"DRAUGHT > "+minDraughtEx+" "+draughtUnit+"\n";}
			if (!maxDraughtIn.equals("0.0")) {ruleFound = ruleFound+"DRAUGHT =< "+maxDraughtIn+" "+draughtUnit+"\n";}
			if (!maxDraughtEx.equals("0.0")) {ruleFound = ruleFound+"DRAUGHT < "+maxDraughtEx+" "+draughtUnit+"\n";}
		}//ShipType is found
		System.out.println("ruleFound="+ruleFound+".");
		System.out.println("shipType="+shipType+".");
		System.out.println("shipType-length="+shipType.length()+".");

		if (shipType.length() == 0){ruleFound="Could not find a rule target from this sentence.";}
 		System.out.println("Fetched Rule in getRule!!="+ruleFound);
		return ruleFound;
	}

}
