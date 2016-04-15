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

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.actinvoc.ActionInvocationContext;
import org.apache.isis.applib.services.eventbus.ActionInteractionEvent;
import org.apache.isis.applib.services.eventbus.EventBusService;
import org.apache.isis.applib.services.wrapper.WrapperFactory;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;
import org.isisaddons.wicket.summernote.cpt.applib.SummernoteEditor;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.FragmentSKOSConceptOccurrences;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.ShipClass;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;


@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
@DomainObject(objectType="INTERPRETATION",autoCompleteRepository=Interpretations.class, autoCompleteAction="autoComplete", bounded = true)
@DomainObjectLayout(bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
		columnSpans={6,0,0,6},
		left={"Interpretation"},
		middle={},
        right={})
public class Interpretation implements Categorized, Comparable<Interpretation> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Interpretation.class);
      //endregion

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();
        buf.append("Interpretation");
        return buf.toString();
    }
    //endregion


    // Region plainRegulationText
    private String plainRegulationText;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Interpretation", sequence="5")
    @PropertyLayout(typicalLength=10000, multiLine=4, named = "Text")
    public String getPlainRegulationText() {
        return plainRegulationText;
    }
    public void setPlainRegulationText(final String plainRegulationText) {
        this.plainRegulationText = plainRegulationText;
    }
    public void modifyPlainRegulationText(final String plainRegulationText) {
        setPlainRegulationText(plainRegulationText);
    }
    public void clearPlainRegulationText() {
        setPlainRegulationText(null);
    }
    //endregion

    // BEGIN REGION ANNOTATED TEXT
    private String annotatedText;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    @MemberOrder(name="Interpretation", sequence="10")
    @PropertyLayout(typicalLength=10000, multiLine=3, hidden=Where.ALL_TABLES)
    @Property(editing = Editing.DISABLED,editingDisabledReason = "Update using action that calls an API from the consolidation services")
  @SummernoteEditor(height = 100, maxHeight = 300)
    public String getAnnotatedText() {
        return annotatedText;
    }
    public void setAnnotatedText(final String annotatedText) {
        this.annotatedText = annotatedText;
    }
    public void modifyAnnotatedText(final String annotatedText) {
        setAnnotatedText(annotatedText);
    }
    public void clearAnnotatedText() {
        setAnnotatedText(null);
    }
// END REGION ANNOTATED TEXT

    private String skosTerms;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    @MemberOrder(name="Interpretation", sequence="20")
    @PropertyLayout(typicalLength=10000, multiLine=3, named = "Terms", hidden=Where.ALL_TABLES)
    //@Property(editing = Editing.DISABLED,editingDisabledReason = "Update using action that calls an API from the consolidation services")
    public String getSkosTerms() {
        return skosTerms;
    }
    public void setSkosTerms(final String skosTerms) {
        this.skosTerms = skosTerms;
    }
    public void modifySkosTerms(final String skosTerms) {
        setSkosTerms(skosTerms);
    }

    public void clearSkosTerms() {
        setSkosTerms(null);
    }

    @Action()
    @ActionLayout(position = ActionLayout.Position.PANEL)
    @MemberOrder(name="Interpretation", sequence="20")
    public Interpretation CheckTerms() {
        FragmentSKOSConceptOccurrences fragment = restClient.GetSkos(plainRegulationText);
        System.out.println("  fragment OK");
        List<String> annotation = new ArrayList<String>();
        setSkosTerms(creationController.ShowTerms(plainRegulationText, fragment).get(0));
        setAnnotatedText(creationController.ShowTerms(plainRegulationText, fragment).get(1));
         System.out.println("  skosTerms="+skosTerms);
        System.out.println(" annotatedText="+annotatedText);
        container.flush();
        container.informUser("Fetched SKOS terms completed for " + container.titleOf(this));
        return this;
    }
      //endregion


    //BEGIN show  Region target
    private String target;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    @MemberOrder(name="Interpretation", sequence="30")
    @PropertyLayout(typicalLength=10000, multiLine=3, named = "Target", hidden=Where.ALL_TABLES)
    //@Property(editing = Editing.DISABLED,editingDisabledReason = "Update using action that calls an API from the consolidation services")
    public String getTarget() {
        return target;
    }
    public void setTarget(final String target) {
        this.target= target;
    }
    public void modifyTarget(final String target) {setTarget(target);}

    public void clearTarget() {
        setTarget(null);
    }

    //@Action(semantics = SemanticsOf.IDEMPOTENT)
    @Action()
    //  @ActionLayout(named = "Check Terms", position = ActionLayout.Position.PANEL)
    @ActionLayout(position = ActionLayout.Position.PANEL)
    @MemberOrder(name="Interpretation", sequence="20")
    public Interpretation ShowTarget() {
        ShipClass shipClassFound = null;
        // CALLS THE TARGET API
        shipClassFound = restClient.GetTarget(plainRegulationText);
        // shipClassFound = restClient.GetApplicability(plainRegulationText);
        System.out.println(" :shipclassfound OK");
        setTarget(creationController.ShowShipClass(plainRegulationText,shipClassFound));
        System.out.println(" : target="+target);
        container.flush();
        container.informUser("Fetched Target completed for " + container.titleOf(this));
        return this;
    }

// END SHOW target


    //BEGIN show applicability
    private String applicability;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    @MemberOrder(name="Interpretation", sequence="35")
    @PropertyLayout(typicalLength=10000, multiLine=3, named = "Applicability", hidden=Where.ALL_TABLES)
    //@Property(editing = Editing.DISABLED,editingDisabledReason = "Update using action that calls an API from the consolidation services")
    public String getApplicability() {
        return applicability;
    }
    public void setApplicability(final String applicability) {
        this.applicability= applicability;
    }
    public void modifyApplicability(final String applicability) {
        setApplicability(applicability);}

    public void clearApplicability() {
        setApplicability(null);
    }

    //@Action(semantics = SemanticsOf.IDEMPOTENT)
    @Action()
    //  @ActionLayout(named = "Check Terms", position = ActionLayout.Position.PANEL)
    @ActionLayout(position = ActionLayout.Position.PANEL)
    @MemberOrder(name="Interpretation", sequence="30")
    public Interpretation ShowApplicability() {
        ShipClass shipClassFound = null;
        // CALLS THE APPLICABILITY API
        //    shipClassFound = restClient.GetRule(plainRegulationText);
        shipClassFound = restClient.GetApplicability(plainRegulationText);
        System.out.println(":applicability shipclassfound OK");
        setApplicability(creationController.ShowShipClass(plainRegulationText,shipClassFound));
        System.out.println(": applicability="+applicability);
        container.flush();
        container.informUser("Fetched Applicability completed for " + container.titleOf(this));
        return this;
    }
// END SHOW applicability


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


    //region > events
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<Interpretation> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final Interpretation source,
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
                final Interpretation source,
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
        return ObjectContracts.toString(this, "ownedBy");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final Interpretation other) {
        return ObjectContracts.compare(this, other, "ownedBy");
    }
    //endregion

    //region > injected services

     @javax.inject.Inject
    private Interpretations interpretations;

    /**
     * public only so can be injected from integ tests
     */
    @javax.inject.Inject
    public ActionInvocationContext actionInvocationContext;

    @javax.inject.Inject
    private DomainObjectContainer container;


    @SuppressWarnings("deprecation")
	Bulk.InteractionContext bulkInteractionContext;
    public void injectBulkInteractionContext(@SuppressWarnings("deprecation") Bulk.InteractionContext bulkInteractionContext) {
        this.bulkInteractionContext = bulkInteractionContext;
    }

    @javax.inject.Inject
    private RESTclient restClient;

    @javax.inject.Inject
    private CreationController creationController;

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

