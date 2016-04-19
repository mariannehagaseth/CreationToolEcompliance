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
@DomainServiceLayout(menuBar= DomainServiceLayout.MenuBar.PRIMARY,named="Search Tool",menuOrder="60")
@DomainService(repositoryFor = RegulationSearch.class)
public class ShipSearchs {


    //region > newRegulationSearch (action)
    @MemberOrder(sequence = "10")
    @ActionLayout(named="Search Regulations for Ship")
    public ShipSearch newShipSearch(
            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Ship Type") String type
           ,final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Tonnage") double tonnage
            ,final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Length") double length
            ,final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Passenger Number") int passengerNumber
            ,final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Draft") double draft
         , final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Keel Laid Date") int keelLaidDate
    )
    {
        return newShipSearch(
                type,
                tonnage,
                length,
                passengerNumber,
                draft,
                keelLaidDate,
                currentUserName()
        );
    }

    @Programmatic // for use by fixtures
    /*The @Programmatic annotation can be used to cause Apache Isis to complete ignore a class member.
     * This means it won't appear in any viewer, its value will not be persisted,
     * and it won't appear in any XML snapshots .*/
    public ShipSearch newShipSearch(
            final String type,
            final double tonnage,
            final double length,
            final int passengerNumber,
            final double draft,
            final int keelLaidDate,
            final String userName
    )
    {
        final ShipSearch shipSearch = container.newTransientInstance(ShipSearch.class);
        shipSearch.setType(type);
        shipSearch.setOwnedBy(userName);
        shipSearch.setTonnage(tonnage);
        shipSearch.setLength(length);
        shipSearch.setPassengerNumber(passengerNumber);
        shipSearch.setDraft(draft);
        shipSearch.setKeelLaidDate(keelLaidDate);
        container.persist(shipSearch);
        container.flush();
        return shipSearch;
    }

    //endregion newShipSearch

    //region > allShipSearchs (action)
    @Action(semantics=SemanticsOf.SAFE,restrictTo=RestrictTo.PROTOTYPING)
    @MemberOrder(sequence = "20")
    @PropertyLayout(named="List Search Regulations for Ship")
    public List<ShipSearch> allShipSearchs() {
        final List<ShipSearch> items = container.allMatches(
                new QueryDefault<ShipSearch>(ShipSearch.class,
                        "findAllSearches",
                        "ownedBy", currentUserName()));
        if(items.isEmpty()) {
            container.warnUser("No Searches found.");
        }
        return items;
    }
    //endregion


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
