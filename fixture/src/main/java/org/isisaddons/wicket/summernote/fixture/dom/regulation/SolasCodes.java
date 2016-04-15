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
@DomainServiceLayout(menuBar= DomainServiceLayout.MenuBar.TERTIARY,named="SolasCode",menuOrder="1")
@DomainService(repositoryFor = SolasCode.class)
public class SolasCodes {

    //region > notYetFinalized (action)
   // @Bookmarkable
    @Action(semantics=SemanticsOf.SAFE)
    @MemberOrder(sequence = "6")
    public List<SolasCode> notYetFinalized() {
        final List<SolasCode> items = notYetFinalizedNoUi();
        if(items.isEmpty()) {
            container.informUser("All regulations have been finalized :-)");
        }
        return items;
    }

    @Programmatic
    public List<SolasCode> notYetFinalizedNoUi() {
        return container.allMatches(
                new QueryDefault<SolasCode>(SolasCode.class,
                        "findByOwnedByAndCompleteIsFalse", 
                        "ownedBy", currentUserName()));
    }
    //endregion

    
    //region > isFinalized (action)
    @Action(semantics=SemanticsOf.SAFE)
    @MemberOrder(sequence = "2")
    public List<SolasCode> isFinalized() {
        final List<SolasCode> items = isFinalizedNoUi();
        if(items.isEmpty()) {
            container.warnUser("No regulations are Finalized!");
        }
        return items;
    }

    @Programmatic
    public List<SolasCode> isFinalizedNoUi() {
        return container.allMatches(
            new QueryDefault<SolasCode>(SolasCode.class,
                    "findByOwnedByAndCompleteIsTrue", 
                    "ownedBy", currentUserName()));
    }
    //endregion


    //region > newRegulationText (action)
    @MemberOrder(sequence = "5")
    @ActionLayout(named="Add New SOLAS Code")
    public SolasCode newSolasCode(
            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, multiLine=5,named="Regulation Text") String plainRegulationText,
            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=50, multiLine=5,named="Regulation Title") String regulationTitle
            //,
            //final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(named="Amendment Date") LocalDate amendmentDate,
            //final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(named="Finalized") boolean finalized
    )
    {
        return newSolasCode(
                plainRegulationText,
                regulationTitle,
                // amendmentDate,
                // finalized,
                currentUserName()
        );
    }
  //  public String default0NewRegulationText() {
   //     return "";
    //}
    //public String default1NewRegulationText() {
     //   return "";
   // }
    //public LocalDate default2NewRegulationText() {
     //   return clockService.now();
   // }
    //public boolean default3NewRegulationText() {
    //    return false;
    //}

    //region > allRegulationTexts (action)
    @Action(semantics=SemanticsOf.SAFE,restrictTo=RestrictTo.PROTOTYPING)
    @MemberOrder(sequence = "1")
    // Add this: OK???
    @PropertyLayout(named="List all SOLAS Codes")
 //   public List<SolasCode> allRegulations() {
    public List<SolasCode> allSolasCodes() {
        final List<SolasCode> items = container.allMatches(
                new QueryDefault<SolasCode>(SolasCode.class,
                        "findByOwnedBy", 
                        "ownedBy", currentUserName()));
        if(items.isEmpty()) {
            container.warnUser("No regulations found.");
        }
        return items;
    }
    //endregion

    //region > autoComplete (programmatic)
   /* TRENGER DENNE??
    @Programmatic // not part of metamodel
    public List<RegulationText> autoComplete(final String regulationTitle) {
        return container.allMatches(
                new QueryDefault<RegulationText>(RegulationText.class,
                        "findByOwnedByAndRegulationTitleContains", 
                        "ownedBy", currentUserName(), 
                        "regulationTitle", regulationTitle));
    } */
    //endregion


    //region > helpers
    @Programmatic // for use by fixtures
    /*The @Programmatic annotation can be used to cause Apache Isis to complete ignore a class member. 
     * This means it won't appear in any viewer, its value will not be persisted, 
     * and it won't appear in any XML snapshots .*/
    public SolasCode newSolasCode(
            final String plainRegulationText,
            final String regulationTitle,
     //       final LocalDate amendmentDate,
   //         final boolean finalized,
            final String userName
            )
    {
        final SolasCode solasCode = container.newTransientInstance(SolasCode.class);
        solasCode.setPlainRegulationText(plainRegulationText);
        solasCode.setRegulationTitle(regulationTitle);
        //regulationText.setAmendmentDate(amendmentDate);
        //regulationText.setFinalized(finalized);
        solasCode.setAmendmentDate(clockService.now());
        solasCode.setFinalized(false);
        solasCode.setOwnedBy(userName);
//        Object regId=JDOHelper.getObjectId(regulationText);
        solasCode.setRegulationTitle(regulationTitle);
        container.persist(solasCode);
        container.flush();
        solasCode.setRegulationTitle(solasCode.getIdString()+": "+ regulationTitle);
        return solasCode;
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
