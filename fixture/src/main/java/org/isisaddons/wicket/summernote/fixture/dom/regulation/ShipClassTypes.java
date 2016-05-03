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

// @DomainServiceLayout(menuBar= DomainServiceLayout.MenuBar.PRIMARY,named="Regulation Search",menuOrder="60")
@DomainServiceLayout(menuBar= DomainServiceLayout.MenuBar.TERTIARY,named="Ship Classes",menuOrder="69")
@DomainService(repositoryFor = ShipClassType.class)
public class ShipClassTypes {



    //region > newShipType (action)
    @MemberOrder(sequence = "60")
    @ActionLayout(named="Add New Ship Class Type")
    public ShipClassType newShipClassType(
            final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength=100, named="Name") String type,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Tonnage >") double minTonnageIn,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Tonnage >=") double minTonnageEx,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Tonnage <") double maxTonnageIn,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Tonnage <= ") double maxTonnageEx,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Length >") double minLengthIn,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Length >=") double minLengthEx,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Length <") double maxLengthIn,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Length <=") double maxLengthEx,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Draught >") double minDraughtIn,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Draught >=") double minDraughtEx,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Draught <") double maxDraughtIn,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Draught <=") double maxDraughtEx,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="No of Passengers >") int minPassengersIn,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="No of Passengers >=") int minPassengersEx,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="No of Passengers <") int maxPassengerIn,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="No of Passengers <=") int maxPassengersEx,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Keel Laid Date >") int minKeelLaidIn,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Keel Laid Date >=") int minKeelLaidEx,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Keel Laid Date <") int maxKeelLaidIn,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Keel Laid Date <=") int maxKeelLaidEx,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Length Unit") String lengthUnit,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Tonnage Unit") String tonnageUnit,
            final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100, named="Draught Unit") String draughtUnit
            )
    {
        return newShipClassType(
         type,
        minTonnageIn,
        minTonnageEx,
        maxTonnageIn,
        maxTonnageEx,
        minLengthEx,
        maxLengthEx,
        minLengthIn,
        maxLengthIn,
        minDraughtEx,
        maxDraughtEx,
        minDraughtIn,
        maxDraughtIn,
        minPassengersEx,
        maxPassengersEx,
        minPassengersIn,
        maxPassengerIn,
        minKeelLaidEx,
        maxKeelLaidEx,
        minKeelLaidIn,
        maxKeelLaidIn,
        lengthUnit,
        tonnageUnit,
        draughtUnit,
        currentUserName());
    }
    public double default1NewShipClassType() {return 0;}
    public double default2NewShipClassType() {return 0;}
    public double default3NewShipClassType() {return 0;}
    public double default4NewShipClassType() {return 0;}
    public double default5NewShipClassType() {return 0;}
    public double default6NewShipClassType() {return 0;}
    public double default7NewShipClassType() {return 0;}
    public double default8NewShipClassType() {return 0;}
    public double default9NewShipClassType() {return 0;}
    public double default10NewShipClassType() {return 0;}
    public double default11NewShipClassType() {return 0;}
    public double default12NewShipClassType() {return 0;}
    public int default13NewShipClassType() {return 0;}
    public int default14NewShipClassType() {return 0;}
    public int default15NewShipClassType() {return 0;}
    public int default16NewShipClassType() {return 0;}
    public int default17NewShipClassType() {return 0;}
    public int default18NewShipClassType() {return 0;}
    public int default19NewShipClassType() {return 0;}
    public int default20NewShipClassType() {return 0;}
    public String default21NewShipClassType() {return "M";}
    public String default22NewShipClassType() {return "GT";}
    public String default23NewShipClassType() {return "M";}

    //region > all Ship Types (action)
    @Action(semantics=SemanticsOf.SAFE,restrictTo=RestrictTo.PROTOTYPING)
    @MemberOrder(sequence = "61")
    // Add this: OK???
    @PropertyLayout(named="List all ShipClassTypes")
    public List<ShipClassType> allShipClassTypes() {
        final List<ShipClassType> items = container.allMatches(
                new QueryDefault<ShipClassType>(ShipClassType.class,
                        "findShipClassTypes" ));
        //     if(items.isEmpty()) {
        //         container.warnUser("No ship types found.");
        //    }
        return items;
    }
    //endregion


    //region > helpers
    @Programmatic // for use by fixtures
    /*The @Programmatic annotation can be used to cause Apache Isis to complete ignore a class member.
     * This means it won't appear in any viewer, its value will not be persisted,
     * and it won't appear in any XML snapshots .*/
    public ShipClassType newShipClassType(
            final String type,
            final double minTonnageIn,
            final double minTonnageEx,
            final double maxTonnageIn,
            final double maxTonnageEx,
            final double minLengthEx,
            final double maxLengthEx,
            final double minLengthIn,
            final double maxLengthIn,
            final double minDraughtEx,
            final double maxDraughtEx,
            final double minDraughtIn,
            final double maxDraughtIn,
            final int minPassengersEx,
            final int maxPassengersEx,
            final int minPassengersIn,
            final int maxPassengerIn,
            final int minKeelLaidEx,
            final int maxKeelLaidEx,
            final int minKeelLaidIn,
            final int maxKeelLaidIn,
            final String lengthUnit,
            final String tonnageUnit,
            final String draughtUnit,
            final String username
    )
    {
        final ShipClassType shipClassType = container.newTransientInstance(ShipClassType.class);
        shipClassType.setType(type);
        shipClassType.setMinTonnageEx(minTonnageEx);
        shipClassType.setMaxTonnageEx(maxTonnageEx);
        shipClassType.setMinTonnageIn(minTonnageIn);
                shipClassType.setMaxTonnageIn(maxTonnageIn);
                shipClassType.setMinLengthEx(minLengthEx);
                shipClassType.setMaxLengthEx(maxLengthEx);
                shipClassType.setMinLengthIn(minLengthIn);
                shipClassType.setMaxLengthIn(maxLengthIn);
                shipClassType.setMinDraughtEx(minDraughtEx);
                shipClassType.setMaxDraughtEx(maxDraughtEx);
                shipClassType.setMinDraughtIn(minDraughtIn);
                shipClassType.setMaxDraughtIn(maxDraughtIn);
                shipClassType.setMinPassengersEx(minPassengersEx);
                shipClassType.setMaxPassengersEx(maxPassengersEx);
                shipClassType.setMinPassengersIn(minPassengersIn);
                shipClassType.setMaxPassengerIn(maxPassengerIn);
                shipClassType.setMinKeelLaidEx(minKeelLaidEx);
                shipClassType.setMaxKeelLaidEx(maxKeelLaidEx);
                shipClassType.setMinKeelLaidIn(minKeelLaidIn);
                shipClassType.setMaxKeelLaidIn(maxKeelLaidIn);
                shipClassType.setLengthUnit(lengthUnit);
                shipClassType.setTonnageUnit(tonnageUnit);
                shipClassType.setDraughtUnit(draughtUnit);
         container.persist(shipClassType);
        container.flush();
        return shipClassType;
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
