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

//import java.math.BigDecimal;

//@DomainServiceLayout(named="Regulation Hierarchy",menuOrder="10")
@DomainService(repositoryFor = IRI.class)
@DomainServiceLayout(menuBar= DomainServiceLayout.MenuBar.TERTIARY,named="IRI",menuOrder="51")
public class IRIS {



    //region > helpers
    @Programmatic
    /*The @Programmatic annotation can be used to cause Apache Isis to complete ignore a class member. 
     * This means it won't appear in any viewer, its value will not be persisted, 
     * and it won't appear in any XML snapshots .*/
    public IRI newIRI(
            final String iri,
            final String plainRegulationText,
            final Regulation regulationLink,
            final ShipSearch searchLink
            )
    {
        final IRI iriNew= container.newTransientInstance(IRI.class);

        iriNew.setIri(iri);
        iriNew.setPlainRegulationText(plainRegulationText);
        iriNew.setRegulationLink(regulationLink);
        iriNew.setSearchLink(searchLink);

         container.persist(iriNew);
        container.flush();
         return iriNew;
    }


    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private ClockService clockService;

    //@javax.inject.Inject
    //private SolasCode solasCode;


    //endregion
}
