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
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;

import java.util.List;

//import java.math.BigDecimal;

//@DomainServiceLayout(named="Regulation Hierarchy",menuOrder="10")
//@DomainServiceLayout(menuBar= DomainServiceLayout.MenuBar.SECONDARY, named="Sections",menuOrder="60")
@DomainServiceLayout(menuBar= DomainServiceLayout.MenuBar.PRIMARY, named="Sections",menuOrder="60")
@DomainService(repositoryFor = FreeTextTest.class)
public class FreeTextTests {



    //region > newRegulationText (action)
  @MemberOrder(sequence = "5")
   @ActionLayout(named="Add New Section TEST")
     public FreeTextTest newFreeTextTest(
            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=10, named="Section NO") String sectionNo,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=1000, multiLine=8,named="Regulation Text") String plainRegulationText
            //, final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(named="SOLAS Chapter") SolasChapter solasChapter
            //final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(named="Finalized") boolean finalized
    )
    {
        return newFreeTextTest(
                sectionNo,
                plainRegulationText,
                "",
                currentUserName()
        );
    }


    //region > allFreeTextSection (action)
    @MemberOrder(sequence = "6")
    @PropertyLayout(named="List All Sections TEST")
    public List<FreeTextTest> allFreeTexts() {
        final List<FreeTextTest> items = container.allMatches(
                new QueryDefault<FreeTextTest>(FreeTextTest.class,
                        "findByOwnedBy", 
                        "ownedBy", currentUserName()));
        if(items.isEmpty()) {
            container.warnUser("No free texts found.");
        }
        return items;
    }
    //endregion

    // MHAGA... OK???
    //region > autoComplete (programmatic)
     @Programmatic // not part of metamodel
    public List<SolasChapter> autoComplete(final String solasChapter) {
        return container.allMatches(
                new QueryDefault<SolasChapter>(SolasChapter.class,
                        "findByOwnedBy",
                        "ownedBy", currentUserName()));
    }
    //endregion


    //region > helpers
    @Programmatic // for use by fixtures
    /*The @Programmatic annotation can be used to cause Apache Isis to complete ignore a class member. 
     * This means it won't appear in any viewer, its value will not be persisted, 
     * and it won't appear in any XML snapshots .*/
    public FreeTextTest newFreeTextTest(
            final String sectionNo,
            final String plainRegulationText,
            final String annotatedText,
            final String userName
            )
    {
        final FreeTextTest freeText= container.newTransientInstance(FreeTextTest.class);
        freeText.setSectionNo(sectionNo);
        freeText.setPlainRegulationText(plainRegulationText);
        freeText.setAnnotatedText(annotatedText);
        freeText.setOwnedBy(userName);
        container.persist(freeText);
        container.flush();
        return freeText;
    }
    private String currentUserName() {
        return container.getUser().getName();
    }
    //endregion


    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private ClockService clockService;
    //endregion
}
