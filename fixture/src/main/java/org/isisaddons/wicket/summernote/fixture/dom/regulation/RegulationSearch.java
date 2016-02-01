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

import org.isisaddons.wicket.summernote.fixture.dom.regulation.RESTclientTest;
import org.isisaddons.wicket.summernote.fixture.dom.regulation.RegulationSearchs;
import org.isisaddons.wicket.summernote.fixture.dom.regulation.ShipType;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.eventbus.ActionInteractionEvent;
import org.apache.isis.applib.services.eventbus.EventBusService;
import org.apache.isis.applib.services.wrapper.WrapperFactory;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;
import org.joda.time.LocalDate;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
@javax.jdo.annotations.Uniques({
    @javax.jdo.annotations.Unique(
            name="Regulation_description_must_be_unique", 
            members={"ownedBy","regulationTitle"})
})
@javax.jdo.annotations.Queries( {
        @javax.jdo.annotations.Query(
                name = "findByOwnedBy", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.RegulationSearch "
                        + "WHERE ownedBy == :ownedBy"),
        @javax.jdo.annotations.Query(
                name = "findAllSearches", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.RegulationSearch "
                        + "WHERE ownedBy == :ownedBy")
 })
@DomainObject(objectType="REGULATIONSEARCH",autoCompleteRepository=RegulationSearchs.class, autoCompleteAction="autoComplete")
 // default unless overridden by autoCompleteNXxx() method
@DomainObjectLayout(named="Semantified Search",bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
		columnSpans={5,2,5,12},
		left={"Ship Type Search"},
		middle={"AndOr"},
        right={"Regulation Search"})

public class RegulationSearch implements Categorized, Comparable<RegulationSearch> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(RegulationSearch.class);
      //endregion

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();
         buf.append(getSearchName());
        return buf.toString();
    }
    //endregion


    //Region List and select ShipType:
    // Search ShipType (Interpretation)
    // Do not need to store the search result!!
    // Later
    private Set<ShipType> possibleShipTypes = new TreeSet<>();
    //@MemberOrder(sequence="10")
    @PropertyLayout(hidden = Where.EVERYWHERE , typicalLength=100)
    @Collection()
    @CollectionLayout(hidden = Where.EVERYWHERE, named = "Fetch Ship Type", render = RenderType.EAGERLY) // not compatible with neo4j (as of DN v3.2.3)
    public Set<ShipType> getPossibleShipTypes() {return restClientTest.findShipType();}
    @ActionLayout(hidden = Where.EVERYWHERE)
    public void setPossibleShipTypes() {this.possibleShipTypes=restClientTest.findShipType();}
    @MemberOrder(name="shipTypeName", sequence="10")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public RegulationSearch selectShipType (  @ParameterLayout(typicalLength = 20) final ShipType shipType){
        updateSelectedShipType(shipType);
        return this;
    }
    // provide a drop-down
    public java.util.Collection<ShipType> choices0SelectShipType() {return getPossibleShipTypes();}
    //endregion
    // end region List and select ShipType


    @Programmatic
    private void updateSelectedShipType(ShipType shipType){
        // Sets the values of the selected Ship Types.
        this.setShipTypeName(shipType.getShipTypeName());
        this.setShipTypeTonnage(shipType.getShipTypeTonnage());
        this.setShipTypeLength(shipType.getShipTypeLength());
        this.setShipTypePassengerNumber(shipType.getShipTypePassengerNumber());
        this.setShipTypeDraft(shipType.getShipTypeDraft());
        this.setShipTypeCrewNumber(shipType.getShipTypeCrewNumber());
        this.setShipTypeKeelLaidDate(shipType.getShipTypeKeelLaidDate());
    }

    // Region searchName
    private String searchName;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Ship Type Search",  sequence="20")
    @PropertyLayout(hidden = Where.ALL_EXCEPT_STANDALONE_TABLES , typicalLength=100)
    public String getSearchName() {
        return searchName;
    }
    public void setSearchName(final String searchName) {this.searchName = searchName;
    }
    //endregion






    // Region shipTypeName
    private String shipTypeName;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Ship Type Search",  sequence="30")
    @PropertyLayout(typicalLength=100)
    public String getShipTypeName() {
        return shipTypeName;
    }
    public void setShipTypeName(final String shipTypeName) {this.shipTypeName = shipTypeName;
    }
    //endregion



    // Region shipTypeTonnage
    private String shipTypeTonnage;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Ship Type Search",  sequence="32")
    @PropertyLayout(typicalLength=100)
    public String getShipTypeTonnage() {
        return shipTypeTonnage;
    }
    public void setShipTypeTonnage(final String shipTypeTonnage) {this.shipTypeTonnage = shipTypeTonnage;
    }
    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "><=", position = ActionLayout.Position.RIGHT)
    @MemberOrder(name="shipTypeTonnage",
            sequence="20")
    public RegulationSearch setTonnageComparator() {
        //
        container.flush();
        container.informUser("Semantify compeleted for " + container.titleOf(this));
        System.out.print("Calling Semantify here!!");
        return this;
    }
    //endregion



    // Region shipTypeLength
    private String shipTypeLength;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Ship Type Search",  sequence="34")
    @PropertyLayout(typicalLength=100)
    public String getShipTypeLength() {
        return shipTypeLength;
    }
    public void setShipTypeLength(final String shipTypeLength) {this.shipTypeLength = shipTypeLength;
    }
    //endregion


    // Region shipTypeLength
    private String shipTypePassengerNumber;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Ship Type Search",  sequence="36")
    @PropertyLayout(typicalLength=100)
    public String getShipTypePassengerNumber() {
        return shipTypePassengerNumber;
    }
    public void setShipTypePassengerNumber(final String shipTypePassengerNumber) {this.shipTypePassengerNumber = shipTypePassengerNumber;
    }
    //endregion

    // Region shipTypeDraft
    private String shipTypeDraft;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Ship Type Search",  sequence="38")
    @PropertyLayout(typicalLength=100)
    public String getShipTypeDraft() {
        return shipTypeDraft;
    }
    public void setShipTypeDraft(final String shipTypeDraft) {this.shipTypeDraft = shipTypeDraft;
    }
    //endregion



    // Region shipTypeCrewNumber
    private String shipTypeCrewNumber;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Ship Type Search",  sequence="40")
    @PropertyLayout(typicalLength=100)
    public String getShipTypeCrewNumber() {
        return shipTypeCrewNumber;
    }
    public void setShipTypeCrewNumber(final String shipTypeCrewNumber) {this.shipTypeCrewNumber = shipTypeCrewNumber;
    }
    //endregion




    // Region shipTypeKeelLaidDate
    private LocalDate shipTypeKeelLaidDate;
    @javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(name="Ship Type Search",  sequence="42")
    public LocalDate getShipTypeKeelLaidDate() {
        return shipTypeKeelLaidDate;
    }
    public void setShipTypeKeelLaidDate(final LocalDate shipTypeKeelLaidDate) {this.shipTypeKeelLaidDate = shipTypeKeelLaidDate;
    }
    //endregion

    // region LogicType
    public enum LogicType {
        AND,
        OR}
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true") // ok ???
    private LogicType andOr;
    @PropertyLayout(named="")
    @javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(name="AndOr", sequence="10")
    public LogicType getAndOr() {
        return andOr;
    }
    public void setAndOr(final LogicType andOr) {
        this.andOr = andOr;
    }
    //endregion


    public enum ComparatorType {
        Greater,
        Less,
        Equal
    }


    // Region regulationTitle
    private String regulationTitle;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Regulation Search",  sequence="10")
    @PropertyLayout(typicalLength=100)
    public String getRegulationTitle() {
        return regulationTitle;
    }
    public void setRegulationTitle(final String regulationTitle) {this.regulationTitle = regulationTitle;
    }
    //endregion


    //region > regulationType (property)
    public static enum RegulationType {
        Certificate,
        Procedure,
        Checklist,
        TechnicalSpecification,
        OperationalSpecification,
        FunctionalRequirement,
        GoalBasedRegulation,
        Guideline,
        ReportSpecification,
        Template,
        Other;
    }
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private RegulationType regulationType;
    @javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(name="Regulation Search", sequence="20")
    public RegulationType getRegulationType() {
        return regulationType;
    }
    public void setRegulationType(final RegulationType regulationType) {
        this.regulationType = regulationType;
    }
    //endregion


    // Start region applicabilityDate
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private LocalDate applicabilityDate;
    @javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(name="Regulation Search", sequence="30")
    public LocalDate getApplicabilityDate() {
        return applicabilityDate;
    }
    public void setApplicabilityDate(final LocalDate applicabilityDate) {
        this.applicabilityDate = applicabilityDate;
    }
    // endregion

    //region > applicableInType (property)
    public static enum ApplicableInType {
        EU,
        Denmark,
        Sweden,
                Cyprus,
        Finland,
                Belgium,
        Italy,
                France,
        Latvia,
        UnitedKingdom,
        Lithuania,
                Bulgaria,
        Netherlands,
                Malta,
        Poland,
                Hungary,
        CzechRepublic,
        Germany,
                Estonia,
        Portugal,
                Spain,
        Greece
    }
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private ApplicableInType applicableIn;
    @javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(name="Regulation Search", sequence="40")
    public ApplicableInType getApplicableIn() {
        return applicableIn;
    }
    public void setApplicableIn(final ApplicableInType applicableIn) {
        this.applicableIn = applicableIn;
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
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<RegulationSearch> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final RegulationSearch source,
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
                final RegulationSearch source,
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
        return ObjectContracts.toString(this, "searchName, ownedBy");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final RegulationSearch other) {
        return ObjectContracts.compare(this, other, "searchName, ownedBy");
    }
    //endregion

    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private RegulationSearchs regulationSearchs;

    @javax.inject.Inject
    private RESTclientTest restClientTest;

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

    //endregion
}
