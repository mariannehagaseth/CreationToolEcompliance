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
@DomainService(repositoryFor = Regulation.class)
@DomainServiceLayout(named="SOLAS Chapter",menuOrder="50")
public class Regulations {


    //region > helpers
    @Programmatic
    /*The @Programmatic annotation can be used to cause Apache Isis to complete ignore a class member. 
     * This means it won't appear in any viewer, its value will not be persisted, 
     * and it won't appear in any XML snapshots .*/
    public Regulation newRegulation(
            final ChapterAnnex chapterAnnexArticle,
            final String chapterNumber,
            final String partNumber,
            final String partTitle,
            final String regulationNumber,
            final String regulationTitle,
            final String userName
            )
    {
        final Regulation regulation = container.newTransientInstance(Regulation.class);
        regulation.setChapterAnnexArticle(chapterAnnexArticle);
        // start with manually add the solas chapter number:
        regulation.setChapterNumber(chapterNumber);
        regulation.setPartNumber(partNumber);
        regulation.setPartTitle(partTitle);
        regulation.setAmendmentDate(clockService.now());
        regulation.setFinalized(false);
        if (chapterAnnexArticle == ChapterAnnex.CHAPTER) {regulation.setRegulationLabel("REGULATION"); };
        if (chapterAnnexArticle == ChapterAnnex.ANNEX) {regulation.setRegulationLabel("CHAPTER"); };
        if (chapterAnnexArticle == ChapterAnnex.DIRECTIVE) {regulation.setRegulationLabel("ARTICLE") ;};
        regulation.setRegulationNumber(regulationNumber);
        regulation.setRegulationTitle(regulationTitle);
        regulation.setOwnedBy(userName);
        container.persist(regulation);
        container.flush();
       // Generate id: solasChapter.setSolasChapterNumber(solasChapter.getIdString());
        return regulation;
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
