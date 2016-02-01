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

import java.util.SortedSet;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.eventbus.ActionInteractionEvent;
import org.apache.isis.applib.services.eventbus.EventBusService;
import org.apache.isis.applib.services.wrapper.WrapperFactory;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;

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
            members={"ownedBy","shipTypeName"})
})
@javax.jdo.annotations.Queries( {
        @javax.jdo.annotations.Query(
                name = "findByOwnedBy", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.ShipType "
                        + "WHERE ownedBy == :ownedBy"),
        @javax.jdo.annotations.Query(
        name = "findShipTypes", language = "JDOQL",
        value = "SELECT "
                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.ShipType ")
})
@DomainObject(bounded=true,editing=Editing.DISABLED,
            objectType="SHIPTYPE",
            autoCompleteRepository=ShipType.class, autoCompleteAction="autoComplete")
@DomainObjectLayout(bookmarking= BookmarkPolicy.AS_ROOT)
//@MemberGroupLayout (
//		columnSpans={4,4,4,12},
//		left={"Regulation Text (Edit)","Semantified Text","Regulation Tags (Edit)"},
//		middle={"Rule (Update)","Definition (Update)"},
//        right={"Search Term (Interpretation)","Select Ship Class (Interpretation)"})
public class ShipType implements Categorized, Comparable<ShipType> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ShipType.class);
      //endregion

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();
        buf.append(getShipTypeName());
        return buf.toString();
    }
    //endregion


    // Region shipTypeName
    private String shipTypeName;
    @javax.jdo.annotations.Column(allowsNull="false", length=100)
    @MemberOrder(  sequence="10")
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
    @MemberOrder(  sequence="20")
    @PropertyLayout(typicalLength=100)
    public String getShipTypeTonnage() {
        return shipTypeTonnage;
    }
    public void setShipTypeTonnage(final String shipTypeTonnage) {this.shipTypeTonnage = shipTypeTonnage;
    }
    //endregion



    // Region shipTypeLength
    private String shipTypeLength;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(  sequence="30")
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
    @MemberOrder(  sequence="40")
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
    @MemberOrder(  sequence="50")
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
    @MemberOrder(  sequence="60")
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
    @MemberOrder(  sequence="70")
    public LocalDate getShipTypeKeelLaidDate() {
        return shipTypeKeelLaidDate;
    }
    public void setShipTypeKeelLaidDate(final LocalDate shipTypeKeelLaidDate) {this.shipTypeKeelLaidDate = shipTypeKeelLaidDate;
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
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<ShipType> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final ShipType source,
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
                final ShipType source,
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
        return ObjectContracts.toString(this, "shipTypeName, ownedBy");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final ShipType other) {
        return ObjectContracts.compare(this, other, "shipTypeName, ownedBy");
    }
    //endregion

    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;


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
