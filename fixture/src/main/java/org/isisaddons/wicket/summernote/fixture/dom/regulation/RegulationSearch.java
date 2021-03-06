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
import org.apache.isis.applib.query.Query;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionInteractionEvent;
import org.apache.isis.applib.services.eventbus.EventBusService;
import org.apache.isis.applib.services.wrapper.WrapperFactory;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.FragmentSKOSConceptOccurrences;
import org.joda.time.LocalDate;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.VersionStrategy;
import javax.naming.directory.SearchResult;
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
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.RegulationSearch "
                        + "WHERE ownedBy == :ownedBy"),
        @javax.jdo.annotations.Query(
                name = "findAllSearches", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.RegulationSearch "
                        + "WHERE ownedBy == :ownedBy")
 ,        @javax.jdo.annotations.Query(
         name = "findRegByRegTitle", language = "JDOQL",
         value = "SELECT "
                 + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Regulation "
                 + "WHERE regulationTitle == :regulationTitle")
 })
@DomainObject(objectType="REGULATIONSEARCH",autoCompleteRepository=RegulationSearchs.class, autoCompleteAction="autoComplete")
 // default unless overridden by autoCompleteNXxx() method
@DomainObjectLayout(named="Semantified Search",bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
		columnSpans={6,0,0,12},
		left={"Regulation Search based on Tags","Search Result"},
		middle={},
        right={})

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



    // Region searchName
    private String searchName;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Regulation Search based on Tags",  sequence="5")
    @PropertyLayout(hidden = Where.ALL_EXCEPT_STANDALONE_TABLES , typicalLength=100, named = "Search Name")
    public String getSearchName() {
        return searchName;
    }
    public void setSearchName(final String searchName) {this.searchName = searchName;
    }
    //endregion


    // Region regulationTitle
    private String regulationTitle;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Regulation Search based on Tags",  sequence="10")
    @PropertyLayout(typicalLength=100,named = "Regulation Title")
    public String getRegulationTitle() {
        return regulationTitle;
    }
    public void setRegulationTitle(final String regulationTitle) {this.regulationTitle = regulationTitle;
    }
    //endregion

    // BEGIN fetch Regulations
    @Action()
    @MemberOrder(name = "Regulation Title", sequence = "10")
    @ActionLayout(named = "Search", position = ActionLayout.Position.RIGHT)
    public RegulationSearch searchRegText() {
        // clear the previous search
        setRegs(null);
        container.flush();
        final  List<Regulation> foundRegs= container.allMatches(
                new QueryDefault<Regulation>(Regulation.class,
                        "findRegByRegTitle",
                        "regulationTitle", regulationTitle));
        if(foundRegs.isEmpty()) {
            container.warnUser("No Regulations found.");
        }
        Iterator it =foundRegs.iterator();
        while (it.hasNext()) {
            Regulation thisReg = (Regulation) it.next();
            getRegs().add(thisReg);
        }
        container.flush();
        container.informUser("Regulation Search Completed" + container.titleOf(this));
        return this;
    }
// END FETCH Regulations



   // @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private CreationController.RegulationType regulationType;
  //  @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    @javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(name="Regulation Search based on Tags", sequence="20")
    @PropertyLayout(named = "Regulation Type" ,typicalLength=100)
    public CreationController.RegulationType getRegulationType() {
        return regulationType;
    }
    public void setRegulationType(final CreationController.RegulationType regulationType) {
        this.regulationType = regulationType;
    }
    //endregion

    // BEGIN fetch Regulations
    @Action()
    @MemberOrder(name = "Regulation Type", sequence = "10")
    @ActionLayout(named = "Search", position = ActionLayout.Position.RIGHT)
    public RegulationSearch searchRegType() {
        // clear the previous search
        setRegs(null);
        container.flush();
        final  List<Regulation> foundRegs= container.allMatches(
                new QueryDefault<Regulation>(Regulation.class,
                        "findRegByRegulationType",
                        "regulationType", regulationType));
        if(foundRegs.isEmpty()) {
            container.warnUser("No Regulations found.");
        }
        else {
            Iterator it = foundRegs.iterator();
            while (it.hasNext()) {
                Regulation thisReg = (Regulation) it.next();
                getRegs().add(thisReg);
            }
        container.informUser("Regulation Search Completed" + container.titleOf(this));
        }
        container.flush();
        return this;
    }
    // END FETCH Regulations

  @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private CreationController.KPI kpi;
     @javax.jdo.annotations.Column(allowsNull="true")
    //   @Property(editing= Editing.DISABLED,editingDisabledReason="Use action to update kpi")
    /*The @Disabled annotation means that the member cannot be used in any instance of the class. When applied to the property it means that the user may not modify the value of that property (though it may still be modified programmatically). When applied to an action method, it means that the user cannot invoke that method.*/
    @MemberOrder(name="Regulation Search based on Tags", sequence="21")
    @PropertyLayout(hidden=Where.ALL_TABLES,named = "KPI", typicalLength=100)
    public CreationController.KPI getKpi() {
        return kpi;
    }
    public void setKpi(final CreationController.KPI kpi) {
        this.kpi = kpi;
    }
    //endregion

    // BEGIN fetch Regulations
    @Action()
    @MemberOrder(name = "KPI", sequence = "10")
    @ActionLayout(named = "Search", position = ActionLayout.Position.RIGHT)
    public RegulationSearch searchRegKPI() {
        // clear the previous search
        setRegs(null);
        container.flush();
        final  List<Regulation> foundRegs= container.allMatches(
                new QueryDefault<Regulation>(Regulation.class,
                        "findRegByKpi",
                        "kpi", kpi));
        if(foundRegs.isEmpty()) {
            container.warnUser("No Regulations found.");
        }
        else {
            Iterator it = foundRegs.iterator();
            while (it.hasNext()) {
                Regulation thisReg = (Regulation) it.next();
                getRegs().add(thisReg);
            }
            container.informUser("Regulation Search Completed" + container.titleOf(this));
        }
        container.flush();
        return this;
    }
    // END FETCH Regulations


    // Start region applicabilityDate
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private LocalDate applicabilityDate;
    @javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(name="Regulation Search based on Tags", sequence="30")
    @PropertyLayout(named = "Applicability Date")
    public LocalDate getApplicabilityDate() {
        return applicabilityDate;
    }
    public void setApplicabilityDate(final LocalDate applicabilityDate) {
        this.applicabilityDate = applicabilityDate;
    }
    // endregion


    // BEGIN fetch Regulations
    @Action()
    @MemberOrder(name = "Applicability Date", sequence = "10")
    @ActionLayout(named = "Search", position = ActionLayout.Position.RIGHT)
    public RegulationSearch searchApplicabilityDate() {
        // clear the previous search
        setRegs(null);
        container.flush();
        final  List<Regulation> foundRegs= container.allMatches(
                new QueryDefault<Regulation>(Regulation.class,
                        "findRegByApplicabilityDate",
                        "applicabilityDate", applicabilityDate));
        if(foundRegs.isEmpty()) {
            container.warnUser("No Regulations found.");
        }
        else {
            Iterator it = foundRegs.iterator();
            while (it.hasNext()) {
                Regulation thisReg = (Regulation) it.next();
                getRegs().add(thisReg);
            }
            container.informUser("Regulation Search Completed" + container.titleOf(this));
        }
        container.flush();
        return this;
    }
    // END FETCH Regulations


    //    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private CreationController.ApplicableInType applicableIn;
    @javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(name="Regulation Search based on Tags", sequence="40")
    @PropertyLayout(named = "Applicable In")
    public CreationController.ApplicableInType getApplicableIn() {
        return applicableIn;
    }
    public void setApplicableIn(final CreationController.ApplicableInType applicableIn) {
        this.applicableIn = applicableIn;
    }
    //endregion
    // BEGIN fetch Regulations
    @Action()
    @MemberOrder(name = "Applicable In", sequence = "10")
    @ActionLayout(named = "Search", position = ActionLayout.Position.RIGHT)
    public RegulationSearch searchApplicableIn() {
        // clear the previous search
        setRegs(null);
        container.flush();
        final  List<Regulation> foundRegs= container.allMatches(
                new QueryDefault<Regulation>(Regulation.class,
                        "findRegByApplicableIn",
                        "applicableIn", applicableIn));
        if(foundRegs.isEmpty()) {
            container.warnUser("No Regulations found.");
        }
        else {
            Iterator it = foundRegs.iterator();
            while (it.hasNext()) {
                Regulation thisReg = (Regulation) it.next();
                getRegs().add(thisReg);
            }
            container.informUser("Regulation Search Completed" + container.titleOf(this));
        }
        container.flush();
        return this;
    }
    // END FETCH Regulations




    // BEGIN REGION LIST OF REGULATIONS returned from SEARCH
    private SortedSet<Regulation> regs= new TreeSet<Regulation>();
    @SuppressWarnings("deprecation")
    @MemberOrder(name = "Search Result", sequence = "10")
    @CollectionLayout(named = "Search Result", sortedBy = RegulationComparator.class, render = RenderType.EAGERLY)
    public SortedSet<Regulation> getRegs() {
        return regs;
    }
    public void setRegs(SortedSet<Regulation> regs) {
        this.regs = regs;
    }

    // / overrides the natural ordering
    public static class RegulationComparator implements Comparator<Regulation> {
        @Override
        public int compare(Regulation p, Regulation q) {
            Ordering<Regulation> byReg= new Ordering<Regulation>() {
                public int compare(final Regulation p, final Regulation q) {
                    return Ordering.natural().nullsFirst().compare(p.getRegulationNumber(), q.getRegulationNumber());
                }
            };
            return byReg
                    .compound(Ordering.<Regulation>natural())
                    .compare(p, q);
        }
    }
// END LIST OF REGULATIONS returned from SEARCH

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
