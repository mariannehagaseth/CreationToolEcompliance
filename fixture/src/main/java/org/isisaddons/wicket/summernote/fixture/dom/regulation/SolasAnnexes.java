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

@DomainService(repositoryFor = Chapter.class)
@DomainServiceLayout(named="SOLAS Annex",menuOrder="20")
public class SolasAnnexes {


    //region > newSolasChapter (action)
    @MemberOrder( sequence = "50")
    @ActionLayout(named="NEW Annex")
    public Chapter newSolasAnnex(

            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Chapter No") String chapterNumber,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=50, named="Chapter Title") String chapterTitle
    )
    {

        return newChapter(
                Chapter.ChapterAnnex.ANNEX,
                chapterNumber,
                chapterTitle,
                currentUserName()
        );
    }

    //region > allRegulationTexts (action)
    @Action(semantics=SemanticsOf.SAFE,restrictTo=RestrictTo.PROTOTYPING)
    @MemberOrder(sequence = "60")
    @PropertyLayout(named="List Annexes")
    public  List<Chapter> allChapters() {
        final  List<Chapter> items = container.allMatches(
                new QueryDefault<Chapter>(Chapter.class,
                        "findChaptersAnnexes",
                        "chapterAnnexArticle", Chapter.ChapterAnnex.ANNEX));
        if(items.isEmpty()) {
            container.warnUser("No Solas ANNEXES found.");
        }
        return items;
    }
    //endregion


    //region > helpers
    @Programmatic
    /*The @Programmatic annotation can be used to cause Apache Isis to complete ignore a class member.
     * This means it won't appear in any viewer, its value will not be persisted,
     * and it won't appear in any XML snapshots .*/
    public Chapter newChapter(
            final Chapter.ChapterAnnex chapterAnnex,
            final String chapterNumber,
            final String chapterTitle,
            final String userName
    )
    {
        final  Chapter chapter = container.newTransientInstance(Chapter.class);
        chapter.setChapterAnnexArticle(chapterAnnex);
        // start with manually add the solas chapter number:
        chapter.setChapterNumber(chapterNumber);
        chapter.setChapterTitle(chapterTitle);
        chapter.setAmendmentDate(clockService.now());
        chapter.setOwnedBy(userName);
        container.persist(chapter);
        container.flush();
        // Generate id: solasChapter.setSolasChapterNumber(solasChapter.getIdString());
        return chapter;
    }

    private String currentUserName() {
        return container.getUser().getName();
    }
    //endregion


/*
    //region > newSolasChapter (action)
    @MemberOrder(sequence = "10")
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

                currentUserName()
        );
    }


    //region > allRegulationTexts (action)
    @Action(semantics=SemanticsOf.SAFE,restrictTo=RestrictTo.PROTOTYPING)
    @MemberOrder(sequence = "20")
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
    //*The @Programmatic annotation can be used to cause Apache Isis to complete ignore a class member.
    // * This means it won't appear in any viewer, its value will not be persisted,
     //* and it won't appear in any XML snapshots .
    public SolasChapter newSolasChapter(
            final ChapterAnnex chapterAnnex,
          final String solasChapterNumber,
            final String solasChapterTitle,
         final String solasPartNumber,
          final String solasPartTitle,
             String regulationChapter,
            final String solasRegulationNumber,
        final String solasRegulationTitle,

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

    //endregion

*/


    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private ClockService clockService;

    //@javax.inject.Inject
    //private SolasCode solasCode;


    //endregion
}
