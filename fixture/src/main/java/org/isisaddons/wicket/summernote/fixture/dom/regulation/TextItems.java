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
@DomainServiceLayout(menuBar= DomainServiceLayout.MenuBar.TERTIARY, named="Item Details",menuOrder="40")
@DomainService(repositoryFor = TextItem.class)
public class TextItems {



    //region > newRegulationText (action)
 //   @MemberOrder(sequence = "5")
 //   @ActionLayout(named="Add New Section")
    public TextItem newTextItem(
            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=10, named="Item No") String itemNo,
            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=1000, multiLine=8,named="Regulation Text") String plainRegulationText
     )
    {
        return newTextItem(
                itemNo,
                plainRegulationText,
                currentUserName()
        );
    }
  //  public String default0NewRegulationText() {
   //     return "";
    //}
    //public String default1NewRegulationText() {
     //   return "";
   // }
//    public SolasChapter default2NewFreeText() {
 //       return this.solasChapter;
  //  }
    //public boolean default3NewRegulationText() {
    //    return false;
    //}


    //region > allFreeTextSection (action)
    @MemberOrder(sequence = "6")
    @PropertyLayout(named="List All Text Items")
    public List<TextItem> allTextItems() {
        final List<TextItem> items = container.allMatches(
                new QueryDefault<TextItem>(TextItem.class,
                        "findTextItems",
                        "ownedBy", currentUserName()));
        if(items.isEmpty()) {
            container.warnUser("No text items found.");
        }
        return items;
    }
    //endregion

    // MHAGA... OK???
    //region > autoComplete (programmatic)
     @Programmatic // not part of metamodel
    public List<TextItem> autoComplete(final String textItem) {
        return container.allMatches(
                new QueryDefault<TextItem>(TextItem.class,
                        "findTextItems",
                        "ownedBy", currentUserName()));
    }
    //endregion


    //region > helpers
    @Programmatic // for use by fixtures
    /*The @Programmatic annotation can be used to cause Apache Isis to complete ignore a class member. 
     * This means it won't appear in any viewer, its value will not be persisted, 
     * and it won't appear in any XML snapshots .*/
    public TextItem newTextItem(
            final String itemNo,
            final String plainRegulationText,
            final String userName
            )
    {
        final TextItem textItem= container.newTransientInstance(TextItem.class);
        textItem.setItemNo(itemNo);
        textItem.setPlainRegulationText(plainRegulationText);
        textItem.setDocumentURI(null);
        textItem.setOwnedBy(userName);
        container.persist(textItem);
        container.flush();
        return textItem;
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
