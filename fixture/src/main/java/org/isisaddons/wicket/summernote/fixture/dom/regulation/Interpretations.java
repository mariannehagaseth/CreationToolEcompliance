/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.isisaddons.wicket.summernote.fixture.dom.regulation;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.FragmentSKOSConceptOccurrences;

import java.util.ArrayList;
import java.util.List;

//import java.math.BigDecimal;

@DomainService(repositoryFor = Interpretation.class)
@DomainServiceLayout(named="Interpretation Tool",menuOrder="50")
public class Interpretations {


    //region > CheckTerms (action)
    @MemberOrder( sequence = "10")
    @ActionLayout(named="Check Terms")
    @Action(semantics=SemanticsOf.SAFE,restrictTo=RestrictTo.PROTOTYPING)
    public  String checkOneTerm(final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Term: ")  String term) {

        FragmentSKOSConceptOccurrences fragment = restClient.GetSkos(term);
        System.out.println("Interpret: fragment OK");
        List<String> annotation = new ArrayList<String>();
        String skosTerms =creationController.ShowTerms(term, fragment).get(0);
        String annotatedText =creationController.ShowTerms(term, fragment).get(1);
        System.out.println("Interpret: skosTerms="+skosTerms);
        System.out.println("Interpret: annotatedText="+annotatedText);
        container.flush();
        container.informUser("Fetched SKOS terms completed for " + container.titleOf(this));
        if (skosTerms.length()==0){skosTerms = "Could not find the term: "+term;}
        return skosTerms;
    }
    //endregion

    //region > Show Annotations (action)
    @MemberOrder( sequence = "20")
    @ActionLayout(named="Check Annotations")
    @Action(semantics=SemanticsOf.SAFE,restrictTo=RestrictTo.PROTOTYPING)
    public Interpretation showAnnotations(final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, multiLine=5,named="Regulation Text for Annotation:") String term) {
        FragmentSKOSConceptOccurrences fragment = restClient.GetSkos(term);
        System.out.println("Interpret: fragment OK");
        List<String> annotation = new ArrayList<String>();
        String skosTerms =creationController.ShowTerms(term, fragment).get(0);
        String annotatedText =creationController.ShowTerms(term, fragment).get(1);
        System.out.println("Interpret: skosTerms="+skosTerms);
        System.out.println("Interpret: annotatedText="+annotatedText);
        container.flush();
        return newInterpretation(
            term,
            currentUserName()
            );
    }
    //endregion

    //region > helpers
    @Programmatic
    public Interpretation newInterpretation(
            final String plainRegulationText,
            final String userName
    )
    {
        final Interpretation interpretation= container.newTransientInstance(Interpretation.class);
        interpretation.setPlainRegulationText(plainRegulationText);
        interpretation.setOwnedBy(userName);
        container.persist(interpretation);
        container.flush();
        // Generate id: solasChapter.setSolasChapterNumber(solasChapter.getIdString());
        return interpretation;
    }


@Programmatic
    private String currentUserName() {
        return container.getUser().getName();
    }
    //endregion

    //region > injected services

    @javax.inject.Inject
    private CreationController creationController;

    @javax.inject.Inject
    private RESTclient restClient;

    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private ClockService clockService;

     //endregion
}
