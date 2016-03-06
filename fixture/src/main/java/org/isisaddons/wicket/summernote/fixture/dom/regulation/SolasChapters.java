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
@DomainService(repositoryFor = SolasChapter.class)
@DomainServiceLayout(named="SOLAS",menuOrder="5.1")

public class SolasChapters {

       //region > newSolasChapter (action)
    @MemberOrder(sequence = "5")
    @ActionLayout(named="Add SOLAS Chapter")
    public SolasChapter newSolasChapter(

           final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="SOLAS Chapter No") String solasChapterNumber,
           // final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=50, named="SOLAS Chapter Title") String solasChapterTitle,
         final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="SOLAS Part No") String solasPartNumber,
           // final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=50, named="SOLAS Part Title") String solasPartTitle,
            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="SOLAS Regulation No") String solasRegulationNumber
            //,final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=50, named="SOLAS Regulation Title") String solasRegulationTitle
         //   ,final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=1000, multiLine=5, named="SOLAS Regulation Intro Text") String solasRegulationIntroText
            //,
            //final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(named="Amendment Date") LocalDate amendmentDate,
            //final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(named="Finalized") boolean finalized
    )
    {

               return newSolasChapter(
               solasChapterNumber,
              //  solasChapterTitle,
             solasPartNumber,
               // solasPartTitle,
                solasRegulationNumber,
              //  solasRegulationTitle,
               //        solasRegulationIntroText,
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
    @MemberOrder(sequence = "6")
    @PropertyLayout(named="List SOLAS Chapters")
    public  List<SolasChapter> allSolasChapters() {
        final  List<SolasChapter> items = container.allMatches(
                new QueryDefault<SolasChapter>(SolasChapter.class,
                        "findByOwnedBy",
                        "ownedBy", currentUserName()));
        if(items.isEmpty()) {
            container.warnUser("No Solas Chapters found.");
        }
        return items;
    }
    //endregion

    //region > helpers
    @Programmatic
    /*The @Programmatic annotation can be used to cause Apache Isis to complete ignore a class member. 
     * This means it won't appear in any viewer, its value will not be persisted, 
     * and it won't appear in any XML snapshots .*/
    public SolasChapter newSolasChapter(
          final String solasChapterNumber,
     //       final String solasChapterTitle,
         final String solasPartNumber,
       //     final String solasPartTitle,
            final String solasRegulationNumber,
         //   final String solasRegulationTitle,
          //  final String solasRegulationIntroText,
             //       final LocalDate amendmentDate,
   //         final boolean finalized,
            final String userName
            )
    {
        final SolasChapter solasChapter = container.newTransientInstance(SolasChapter.class);
        // start with manually add the solas chapter number:
        solasChapter.setSolasChapterNumber(solasChapterNumber);
        //solasChapter.setSolasChapterTitle(solasChapterTitle);
        solasChapter.setSolasPartNumber(solasPartNumber);
        //solasChapter.setSolasPartTitle(solasPartTitle);
        solasChapter.setSolasRegulationNumber( solasRegulationNumber);
        //solasChapter.setSolasRegulationTitle(solasRegulationTitle);
        //solasChapter.setSolasRegulationIntroText(solasRegulationIntroText);
        solasChapter.setAmendmentDate(clockService.now());
        solasChapter.setFinalized(false);
        solasChapter.setOwnedBy(userName);
        container.persist(solasChapter);
        container.flush();
       // Generate id: solasChapter.setSolasChapterNumber(solasChapter.getIdString());
        return solasChapter;
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

    //@javax.inject.Inject
    //private SolasCode solasCode;


    //endregion
}
