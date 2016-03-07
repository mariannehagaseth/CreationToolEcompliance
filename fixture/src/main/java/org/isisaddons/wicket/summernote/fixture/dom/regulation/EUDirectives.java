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
import org.isisaddons.wicket.summernote.fixture.dom.regulation.SolasChapter.ChapterAnnex;

import java.util.List;

//import java.math.BigDecimal;

//@DomainServiceLayout(named="Regulation Hierarchy",menuOrder="10")
@DomainService(repositoryFor = SolasChapter.class)
@DomainServiceLayout(named="EU Directives",menuOrder="10.1")
public class EUDirectives {

       //region > newSolasChapter (action)
    @MemberOrder( sequence = "10")
    @ActionLayout(named="NEW EU Directive")
    public SolasChapter newSolasChapter(

   //         final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="SOLAS") ChapterAnnex chapterAnnex,
            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Chapter No") String solasChapterNumber,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=50, named="Chapter Title") String solasChapterTitle,
         final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Part No") String solasPartNumber,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=50, named="Part Title") String solasPartTitle,
           // final @Parameter( optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="SOLAS") String regulationChapter,
            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Regulation No") String solasRegulationNumber
            ,final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=50, named="Regulation Title") String solasRegulationTitle
         //   ,final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=1000, multiLine=5, named="SOLAS Regulation Intro Text") String solasRegulationIntroText
            //,
            //final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(named="Amendment Date") LocalDate amendmentDate,
            //final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(named="Finalized") boolean finalized
    )
    {

               return newSolasChapter(
                       ChapterAnnex.CHAPTER,
                       solasChapterNumber,
               solasChapterTitle,
                       solasPartNumber,
               solasPartTitle,
                       "REGULATION",
                        solasRegulationNumber,
                solasRegulationTitle,
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
    //public String default3NewSolasChapter() {
    //    if chapterAnnex = ChapterAnnex.CHAPTER) return ;
    //}

    //region > allRegulationTexts (action)
    @Action(semantics=SemanticsOf.SAFE,restrictTo=RestrictTo.PROTOTYPING)
    @MemberOrder(sequence = "30")
    @PropertyLayout(named="List Chapters")
    public  List<SolasChapter> allSolasChapters() {
        final  List<SolasChapter> items = container.allMatches(
                new QueryDefault<SolasChapter>(SolasChapter.class,
                        "findChaptersAnnexes",
                        "chapterAnnex", ChapterAnnex.CHAPTER));
        if(items.isEmpty()) {
            container.warnUser("No Solas Chapters found.");
        }
        return items;
    }
    //endregion



    //region > newSolasChapter (action)
    @MemberOrder(sequence = "20")
    @ActionLayout(named="NEW Annex")
    public SolasChapter newSolasAnnex(

            //         final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="SOLAS") ChapterAnnex chapterAnnex,
            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Annex No") String solasChapterNumber,
             final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=50, named="Annex Title") String solasChapterTitle,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Part No") String solasPartNumber,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=50, named="Part Title") String solasPartTitle,
            // final @Parameter( optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="SOLAS") String regulationChapter,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Chapter No") String solasRegulationNumber
            ,final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=50, named="Chapter Title") String solasRegulationTitle
            //   ,final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=1000, multiLine=5, named="SOLAS Regulation Intro Text") String solasRegulationIntroText
            //,
            //final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(named="Amendment Date") LocalDate amendmentDate,
            //final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(named="Finalized") boolean finalized
    )
    {

        return newSolasChapter(
                ChapterAnnex.ANNEX,
                solasChapterNumber,
                solasChapterTitle,
                solasPartNumber,
                solasPartTitle,
                "CHAPTER", //regulationChapter
                solasRegulationNumber,
                 solasRegulationTitle,
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
    //public String default3NewSolasChapter() {
    //    if chapterAnnex = ChapterAnnex.CHAPTER) return ;
    //}

    //region > allRegulationTexts (action)
    @Action(semantics=SemanticsOf.SAFE,restrictTo=RestrictTo.PROTOTYPING)
    @MemberOrder(sequence = "40")
    @PropertyLayout(named="List Annexes")
    public  List<SolasChapter> allSolasAnnexes() {
        final  List<SolasChapter> items = container.allMatches(
                new QueryDefault<SolasChapter>(SolasChapter.class,
                        "findChaptersAnnexes",
                        "chapterAnnex", ChapterAnnex.ANNEX));
        if(items.isEmpty()) {
            container.warnUser("No Solas Annexes found.");
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
            final ChapterAnnex chapterAnnex,
          final String solasChapterNumber,
            final String solasChapterTitle,
         final String solasPartNumber,
          final String solasPartTitle,
             String regulationChapter,
            final String solasRegulationNumber,
        final String solasRegulationTitle,
          //  final String solasRegulationIntroText,
             //       final LocalDate amendmentDate,
   //         final boolean finalized,
            final String userName
            )
    {
        final SolasChapter solasChapter = container.newTransientInstance(SolasChapter.class);
        solasChapter.setChapterAnnex(chapterAnnex);
        // start with manually add the solas chapter number:
        solasChapter.setSolasChapterNumber(solasChapterNumber);
     solasChapter.setSolasChapterTitle(solasChapterTitle);
        solasChapter.setSolasPartNumber(solasPartNumber);
     solasChapter.setSolasPartTitle(solasPartTitle);
        solasChapter.setRegulationChapter(regulationChapter);
        solasChapter.setSolasRegulationNumber( solasRegulationNumber);
     solasChapter.setSolasRegulationTitle(solasRegulationTitle);
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
