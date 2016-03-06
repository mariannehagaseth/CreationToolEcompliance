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
import org.joda.time.LocalDate;

import java.util.List;

//import java.math.BigDecimal;

//@DomainServiceLayout(named="Regulation Hierarchy",menuOrder="10")
@DomainServiceLayout(menuBar= DomainServiceLayout.MenuBar.TERTIARY,named="RegulationSearch",menuOrder="20")
@DomainService(repositoryFor = RegulationSearch.class)
public class RegulationSearchs {


    //region > newRegulationSearch (action)
    @MemberOrder(sequence = "10")
    @ActionLayout(named="Start New Search")
    public RegulationSearch newRegulationSearch(
            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Search Name") String searchName
    )
    {
        return newRegulationSearch(
                searchName,
                currentUserName()
        );
    }


    @Programmatic // for use by fixtures
    /*The @Programmatic annotation can be used to cause Apache Isis to complete ignore a class member.
     * This means it won't appear in any viewer, its value will not be persisted,
     * and it won't appear in any XML snapshots .*/
    public RegulationSearch newRegulationSearch(
            final String searchName,
            final String userName
    )
    {
        final RegulationSearch regulationSearch = container.newTransientInstance(RegulationSearch.class);
        regulationSearch.setSearchName(searchName);
        regulationSearch.setOwnedBy(userName);
        regulationSearch.setAndOr(RegulationSearch.LogicType.AND);
        container.persist(regulationSearch);
        container.flush();
        return regulationSearch;
    }
     //endregion newRegulationSearch



    //region > allRegulationSearchs (action)
    @Action(semantics=SemanticsOf.SAFE,restrictTo=RestrictTo.PROTOTYPING)
    @MemberOrder(sequence = "20")
    // Add this: OK???
    @PropertyLayout(named="List all Regulation Searches")
    public List<RegulationSearch> allRegulationSearchs() {
        final List<RegulationSearch> items = container.allMatches(
                new QueryDefault<RegulationSearch>(RegulationSearch.class,
                        "findAllSearches",
                        "ownedBy", currentUserName()));
        if(items.isEmpty()) {
            container.warnUser("No Regulation Searches found.");
        }
        return items;
    }
    //endregion

    //region > newShipType (action)
    @MemberOrder(sequence = "30")
    @ActionLayout(named="Add New Ship Type")
    public ShipType newShipType(
            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Name") String shipTypeName,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Tonnage") String shipTypeTonnage,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Length") String shipTypeLength,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Passenger Number") String shipTypePassengerNumber,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Draft") String shipTypeDraft,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Crew Number") String shipTypeCrewNumber,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Keel Laid Date") LocalDate shipTypeKeelLaidDate
    )
    {
        return newShipType(
       shipTypeName,
        shipTypeTonnage,
        shipTypeLength,
        shipTypePassengerNumber,
         shipTypeDraft,
         shipTypeCrewNumber,
        shipTypeKeelLaidDate,
                currentUserName()
        );
    }


    //region > all Ship Types (action)
    @Action(semantics=SemanticsOf.SAFE,restrictTo=RestrictTo.PROTOTYPING)
    @MemberOrder(sequence = "40")
    // Add this: OK???
    @PropertyLayout(named="List all ShipTypes")
    public List<ShipType> allShipTypes() {
        final List<ShipType> items = container.allMatches(
                new QueryDefault<ShipType>(ShipType.class,
                        "findShipTypes" ));
   //     if(items.isEmpty()) {
   //         container.warnUser("No ship types found.");
    //    }
        return items;
    }
    //endregion

    //region > autoComplete (programmatic)
   /* TRENGER DENNE??
    @Programmatic // not part of metamodel
    public List<RegulationSearch> autoComplete(final String regulationTitle) {
        return container.allMatches(
                new QueryDefault<RegulationSearch>(RegulationSearch.class,
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
    public ShipType newShipType(
            final String shipTypeName,
            final String shipTypeTonnage,
            final String shipTypeLength,
            final String shipTypePassengerNumber,
            final String shipTypeDraft,
            final String shipTypeCrewNumber,
            final LocalDate shipTypeKeelLaidDate,
            final String userName
            )
    {
        final ShipType shipType = container.newTransientInstance(ShipType.class);
        shipType.setShipTypeName(shipTypeName);
        shipType.setShipTypeTonnage(shipTypeTonnage);
        shipType.setShipTypeLength(shipTypeLength);
        shipType.setShipTypePassengerNumber(shipTypePassengerNumber);
        shipType.setShipTypeDraft(shipTypeDraft);
        shipType.setShipTypeCrewNumber(shipTypeCrewNumber);
        shipType.setShipTypeKeelLaidDate(shipTypeKeelLaidDate);
        shipType.setOwnedBy(userName);
        container.persist(shipType);
        container.flush();
        return shipType;
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
