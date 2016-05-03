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

//import java.math.BigDecimal;

import com.google.common.collect.Ordering;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionInteractionEvent;
import org.apache.isis.applib.services.eventbus.EventBusService;
import org.apache.isis.applib.services.wrapper.WrapperFactory;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.FragmentSKOSConceptOccurrences;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.IRIList;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.IndividualShip;
import org.joda.time.LocalDate;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.VersionStrategy;
import java.util.*;
import java.util.stream.Collectors;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
@javax.jdo.annotations.Queries( {
        @javax.jdo.annotations.Query(
                name = "findByOwnedBy", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.ShipSearch "
                        + "WHERE ownedBy == :ownedBy"),
        @javax.jdo.annotations.Query(
                name = "findAllSearches", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.ShipSearch "
                        + "WHERE ownedBy == :ownedBy")
})
 @DomainObject(objectType="SHIPSEARCH",autoCompleteRepository=ShipSearchs.class, autoCompleteAction="autoComplete")
 // default unless overridden by autoCompleteNXxx() method
@DomainObjectLayout(named="Semantified Search",bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
		columnSpans={6,0,0,12},
		left={"Regulation Search based on Ship Particulars"},
		middle={},
        right={})

public class ShipSearch implements Categorized, Comparable<ShipSearch> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ShipSearch.class);
      //endregion

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();
         buf.append("Ship Search for "+getType());
        return buf.toString();
    }
    //endregion



    // Region type
    private String type;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Regulation Search based on Ship Particulars",  sequence="20")
    @PropertyLayout(typicalLength=100)
    public String getType() {
        return type;
    }
    public void setType(final String type) {this.type = type;
    }
    //endregion

    // Region shipTypeTonnage
    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="Regulation Search based on Ship Particulars",  sequence="30")
    @PropertyLayout(typicalLength=100)
    private double tonnage;
    public double getTonnage() {
        return tonnage;
    }
    public void setTonnage(final double tonnage) {this.tonnage = tonnage;
    }
    //endregion



    // Region shipTypeLength
    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="Regulation Search based on Ship Particulars",  sequence="40")
    @PropertyLayout(typicalLength=100)
    private double length;
    public double getLength() {
        return length;
    }
    public void setLength(final double length) {this.length = length;
    }
    //endregion



    // Region shipTypeLength
    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="Regulation Search based on Ship Particulars",  sequence="50")
    @PropertyLayout(typicalLength=100)
    private int passengerNumber;
    public int getPassengerNumber() {
        return passengerNumber;
    }
    public void setPassengerNumber(final int passengerNumber) {this.passengerNumber = passengerNumber;
    }
    //endregion


    // Region shipTypeDraft
    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="Regulation Search based on Ship Particulars",  sequence="60")
    @PropertyLayout(typicalLength=100)
    private double draft;
    public double getDraft() {
        return draft;
    }
    public void setDraft(final double draft) {this.draft = draft;
    }
    //endregion



    // Region shipTypeKeelLaidDate
    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="Regulation Search based on Ship Particulars",  sequence="80")
    @PropertyLayout(typicalLength=100)
    private int keelLaidDate;
    public int getKeelLaidDate() {
        return keelLaidDate;
    }
    public void setKeelLaidDate(final int keelLaidDate) {this.keelLaidDate = keelLaidDate;
    }
    //endregion



    //region > ownedBy (property)
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private String ownedBy;
    @PropertyLayout(hidden=Where.EVERYWHERE)
	@ActionLayout(hidden=Where.EVERYWHERE)
    @javax.jdo.annotations.Column(allowsNull="false")
    @Property(editing= Editing.DISABLED,editingDisabledReason="OwnedBy is static")
    public String getOwnedBy() {
        return ownedBy;
    }
	@ActionLayout(hidden=Where.EVERYWHERE)
    public void setOwnedBy(final String ownedBy) {
        this.ownedBy = ownedBy;
    }
    //endregion


    //region > version (derived property)
    @PropertyLayout(hidden=Where.EVERYWHERE)
    @ActionLayout(hidden=Where.EVERYWHERE)
    public Long getVersionSequence() {
        if(!(this instanceof javax.jdo.spi.PersistenceCapable)) {
            return null;
        } 
        javax.jdo.spi.PersistenceCapable persistenceCapable = (javax.jdo.spi.PersistenceCapable) this;
        final Long version = (Long) JDOHelper.getVersion(persistenceCapable);
        return version;}
    // hide property (imperatively, based on state of object)
    public boolean hideVersionSequence() {
        return !(this instanceof javax.jdo.spi.PersistenceCapable);
    }
    //endregion


    //region > id (derived property)
    @PropertyLayout(hidden=Where.EVERYWHERE)
    @ActionLayout(hidden=Where.EVERYWHERE)
    public Long getId() {
        if(!(this instanceof javax.jdo.spi.PersistenceCapable)) {
            return null;
        }
        javax.jdo.spi.PersistenceCapable persistenceCapable = (javax.jdo.spi.PersistenceCapable) this;
        final Long id = (Long) JDOHelper.getObjectId(persistenceCapable);
        return id;
    }
    // hide property (imperatively, based on state of object)
    public boolean hideId() {
        return !(this instanceof javax.jdo.spi.PersistenceCapable);
    }
    //endregion

    //region > id (derived property)
    @PropertyLayout(hidden=Where.EVERYWHERE)
    @ActionLayout(hidden=Where.EVERYWHERE)
    public String getIdString() {
        if(!(this instanceof javax.jdo.spi.PersistenceCapable)) {
            return null;
        }
        javax.jdo.spi.PersistenceCapable persistenceCapable = (javax.jdo.spi.PersistenceCapable) this;
        final String id = (String) JDOHelper.getObjectId(persistenceCapable).toString();
        final int indexEnd = id.indexOf("[");
        return id.substring(0,indexEnd);
    }



    // BEGIN REGION Link from ShipSearch to SearchResult.
     @javax.jdo.annotations.Join // Make a separate join table.
    private SortedSet<IRI> iris = new TreeSet<IRI>();
    @SuppressWarnings("deprecation")
  //  @MemberOrder(name = "SKOS Terms (Select)", sequence = "90")
    // @MemberOrder(sequence = "90")
    @CollectionLayout(named = "Regulations Applicable for Ship", sortedBy = IRISComparator.class, render = RenderType.EAGERLY)
    public SortedSet<IRI> getIris() {
        return iris;
    }
    public void setIris(SortedSet<IRI> iris) {
        this.iris = iris;
    }
    public void removeFromIris(final IRI iri) {
        if (iri == null || !getIris().contains(iri)) return;
        getIris().remove(iri);
    }
    // / overrides the natural ordering
    public static class IRISComparator implements Comparator<IRI> {
        @Override
        public int compare(IRI p, IRI q) {
            Ordering<IRI> byIri= new Ordering<IRI>() {
                public int compare(final IRI p, final IRI q) {
                    return Ordering.natural().nullsFirst().compare(p.getIri(), q.getIri());
                }
            };
            return byIri
                    .compound(Ordering.<IRI>natural())
                    .compare(p, q);
        }
    }
    // BEGIN fetch IRIs for this ship individual
    @Action()
    @MemberOrder(name = "Regulations Applicable for Ship", sequence = "10")
    @ActionLayout(named = "Search")
    public ShipSearch FetchIris() {


        IndividualShip indShip = new IndividualShip();
        indShip.setType(this.getType());
        indShip.setPass(this.getPassengerNumber());
        indShip.setTonnage(this.getTonnage());
        indShip.setLength(this.getLength());
        indShip.setDraft(this.getDraft());
        indShip.setKeellaid(this.getKeelLaidDate());

        List<String> regulationList = restClient.SearchRegulationByIndShip(indShip);
        System.out.println("ShipSearch regulationList OK");

        Iterator it = regulationList.iterator();
        while (it.hasNext()) {

            String iriFound = (String) it.next();

            Regulation regulationLink;
            String regText;

            // Have to find the regulation text and the link to this IRI:
            // Search on chapter level
            final  List<Regulation> foundRegs= container.allMatches(
                    new QueryDefault<Regulation>(Regulation.class,
                            "findRegulationByURI",
                            "documentURI", iriFound));
            if(foundRegs.isEmpty()) {
                regulationLink = null;
                regText = null;
             }
            else {
                // URI is unique, so only one chapter is found:
                regulationLink = foundRegs.get(0);
                regText = regulationLink.getPlainRegulationText();

            }
            IRI thisReg = iriS.newIRI(iriFound,regText,regulationLink,this);

            getIris().add(thisReg);
        }

        /*
        // BEGIN dummy test:
        IRI thisIRI= iriS.newIRI("Test URI", "This is the test regulation text", null, this);
        getIris().add(thisIRI);
        thisIRI= iriS.newIRI("Test URI no 2", "This is another test regulation text", null, this);
        getIris().add(thisIRI);
        // END dummy test:
        */
        container.flush();
        container.informUser("Regulations fetched for individual ship: " + container.titleOf(this));
        return this;
    }
// END FETCH applicability


    //END REGION Link ShipSearch to IRIs list


    //END


    //region > lifecycle callbacks

    public void created() {
        LOG.debug("lifecycle callback: created: " + this.toString());
    }
    public void loaded() {
        LOG.debug("lifecycle callback: loaded: " + this.toString());
    }
    public void persisting() {
        LOG.debug("lifecycle callback: persisting: " + this.toString());
    }
    public void persisted() {
        LOG.debug("lifecycle callback: persisted: " + this.toString());
    }
    public void updating() {
        LOG.debug("lifecycle callback: updating: " + this.toString());
    }
    public void updated() {
        LOG.debug("lifecycle callback: updated: " + this.toString());
    }
    public void removing() {
        LOG.debug("lifecycle callback: removing: " + this.toString());
    }
    public void removed() {
        LOG.debug("lifecycle callback: removed: " + this.toString());
    }
    //endregion

    //region > object-level validation

    /**
     * In a real app, if this were actually a rule, then we'd expect that
     * invoking the {@link #completed() done} action would clear the {@link #getDueBy() dueBy}
     * property (rather than require the user to have to clear manually).
     */
    //endregion



    //region > events
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<ShipSearch> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final ShipSearch source,
                final Identifier identifier,
                final Object... arguments) {
            super(source, identifier, arguments);
            this.description = description;
        }
        public String getEventDescription() {
            return description;
        }
    }

    public static class DeletedEvent extends AbstractActionInteractionEvent {
        private static final long serialVersionUID = 1L;
        public DeletedEvent(
                final ShipSearch source,
                final Identifier identifier,
                final Object... arguments) {
            super("deleted", source, identifier, arguments);
        }
    }

    //endregion


    //region > toString, compareTo
    @Override
    public String toString() {
//        return ObjectContracts.toString(this, "description,complete,dueBy,ownedBy");
        return ObjectContracts.toString(this, "type");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final ShipSearch other) {
        return ObjectContracts.compare(this, other, "type");
    }
    //endregion

    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private RESTclient restClient;

    @javax.inject.Inject
    private IRIS iriS;


    @SuppressWarnings("deprecation")
	Bulk.InteractionContext bulkInteractionContext;
    public void injectBulkInteractionContext(@SuppressWarnings("deprecation") Bulk.InteractionContext bulkInteractionContext) {
        this.bulkInteractionContext = bulkInteractionContext;
    }

    @javax.inject.Inject
//    private Scratchpad scratchpad;

    EventBusService eventBusService;
    public void injectEventBusService(EventBusService eventBusService) {
        this.eventBusService = eventBusService;
    }

    @javax.inject.Inject
    private WrapperFactory wrapperFactory;

    @javax.inject.Inject
    private FreeTexts newFreeTextCall;

    @javax.inject.Inject
    private Chapters chapters;


    //endregion

}
