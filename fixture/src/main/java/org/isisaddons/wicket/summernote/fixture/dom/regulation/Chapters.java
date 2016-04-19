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
import org.isisaddons.wicket.summernote.fixture.dom.regulation.Chapter.ChapterAnnex;

import java.util.List;

//import java.math.BigDecimal;

//@DomainServiceLayout(named="Regulation Hierarchy",menuOrder="10")
@DomainService(repositoryFor = Chapter.class)
@DomainServiceLayout(named="SOLAS Chapter",menuOrder="10")
public class Chapters {

       //region > newSolasChapter (action)
    @MemberOrder( sequence = "50")
    @ActionLayout(named="NEW Chapter")
    public Chapter newChapter(

            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Number") String chapterNumber,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=50, named="Title") String chapterTitle
    )
    {

               return newChapter(
                       ChapterAnnex.CHAPTER,
                       chapterNumber,
                       chapterTitle,
                       currentUserName()
               );
    }

    //region > allRegulationTexts (action)
    @Action(semantics=SemanticsOf.SAFE,restrictTo=RestrictTo.PROTOTYPING)
    @MemberOrder(sequence = "60")
    @PropertyLayout(named="List Chapters")
    public  List<Chapter> allChapters() {
        final  List<Chapter> items = container.allMatches(
                new QueryDefault<Chapter>(Chapter.class,
                        "findChaptersAnnexes",
                        "chapterAnnexArticle", ChapterAnnex.CHAPTER));
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
     public Chapter newChapter(
            final ChapterAnnex chapterAnnex,
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
        chapter.setDocumentURI(null);
        chapter.setRootURI(null);
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


    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private ClockService clockService;



    //endregion
}
