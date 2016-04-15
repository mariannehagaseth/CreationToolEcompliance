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
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.clock.ClockService;
import org.isisaddons.wicket.summernote.fixture.dom.regulation.Chapter.ChapterAnnex;

//import java.math.BigDecimal;

//@DomainServiceLayout(named="Regulation Hierarchy",menuOrder="10")
@DomainService(repositoryFor = Part.class)
@DomainServiceLayout(named="SOLAS Chapter",menuOrder="50")
public class RootNodes {

    /*
       //region > newPart (action)
    @MemberOrder( sequence = "10")
    @ActionLayout(named="NEW Part")
    public Part newPart(
          final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Part No") String partNumber,
          final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=50, named="Part Title") String partTitle
    )
    {
        return newPart(
                ChapterAnnex.CHAPTER,
                partNumber,
                partTitle,
                currentUserName()
        );
    }
*/
/*
    //region > allRegulationTexts (action)
    @Action(semantics=SemanticsOf.SAFE,restrictTo=RestrictTo.PROTOTYPING)
    @MemberOrder(sequence = "30")
    @PropertyLayout(named="List Parts")
    public  List<Part> allParts() {
        final  List<Part> items = container.allMatches(
                new QueryDefault<Part>(Part.class,
                        "findParts",
                        "chapterAnnexArticle", ChapterAnnex.CHAPTER));
        if(items.isEmpty()) {
            container.warnUser("No Solas Chapters found.");
        }
        return items;
    }
    //endregion
*/

    //region > helpers
    @Programmatic
    /*The @Programmatic annotation can be used to cause Apache Isis to complete ignore a class member. 
     * This means it won't appear in any viewer, its value will not be persisted, 
     * and it won't appear in any XML snapshots .*/
    public Part newPart(
            final ChapterAnnex chapterAnnexArticle,
            final String chapterNumber,
            final String partNumber,
            final String partTitle,
            final String userName
            )
    {
        final Part part = container.newTransientInstance(Part.class);
        part.setChapterAnnexArticle(chapterAnnexArticle);
        // start with manually add the solas chapter number:
        part.setChapterNumber(chapterNumber);
        part.setPartNumber(partNumber);
        part.setPartTitle(partTitle);
        part.setAmendmentDate(clockService.now());
        part.setFinalized(false);
        part.setOwnedBy(userName);
        container.persist(part);
        container.flush();
       // Generate id: solasChapter.setSolasChapterNumber(solasChapter.getIdString());
        return part;
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
