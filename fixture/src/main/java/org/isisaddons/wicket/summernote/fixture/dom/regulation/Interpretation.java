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

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
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
    @MemberOrder(name="Interpretation", sequence="10")
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

    // Region termAnnotated
    private String termAnnotated;
    //@SummernoteEditor(height = 100, maxHeight = 300)
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    @MemberOrder(name="Interpretation", sequence="15")
   // @PropertyLayout(hidden=Where.EVERYWHERE)
   // @ActionLayout(hidden=Where.EVERYWHERE)
    public String getTermAnnotated() {
        return termAnnotated;
    }
    public void setTermAnnotated(final String termAnnotated) {
      this.termAnnotated = termAnnotated;
             }
    public void modifyTermAnnotated(final String termAnnotated) {
        setTermAnnotated(termAnnotated);
    }
    public void clearTermAnnotated() {
        setTermAnnotated(null);
    }
    //endregion


    private String annotatedText;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    @MemberOrder(name="Interpretation", sequence="20")
    @PropertyLayout(typicalLength=10000, multiLine=3, hidden=Where.ALL_TABLES)
    @Property(editing = Editing.DISABLED,editingDisabledReason = "Update using action that calls an API from the consolidation services")
//    @SummernoteEditor(height = 100, maxHeight = 300)
//  @SummernoteEditor()
    public String getAnnotatedText() {
        return annotatedText;
    }
    public void setAnnotatedText(final String annotatedText) {
         System.out.println("1termAnnotated = "+termAnnotated+"." );
        System.out.println("1this.termAnnotated = "+this.termAnnotated+"." );
        System.out.println("1annotatedText = "+annotatedText+"." );
        System.out.println("1this.annotatedText = "+this.annotatedText+"." );
        System.out.println("1plainRegulationText = "+plainRegulationText+"." );
        System.out.println("1this.plainRegulationText = "+this.plainRegulationText+"." );

//        FragmentSKOSConceptOccurrences fragment = restClient.GetSkos(plainRegulationText);
//        System.out.println("REGULATION: fragment OK");
//        this.annotatedText = creationController.CheckTerms(plainRegulationText, fragment).get(1);

//        this.annotatedText = annotatedText;
        this.annotatedText = "Every <span style=\"background-color: rgb(0, 255, 0);\">ship</span> must have a <span style=\"background-color: rgb(0, 255, 0);\">polar code</span> <span style=\"background-color: rgb(0, 255, 0);\">certificate</span>";
       /* MHAGA;
        if (this.termAnnotated == null) {
            this.annotatedText = annotatedText;
            System.out.println("THIS.termAnnotated=NULL= "+this.termAnnotated+"." );
            System.out.println("THIS.annotatedText = "+this.annotatedText+"." );
        }
        else {
            this.annotatedText = this.termAnnotated;
        }
        System.out.println("2termAnnotated = "+termAnnotated+"." );
        System.out.println("2this.1termAnnotated = "+this.termAnnotated+"." );
        System.out.println("2annotatedText = "+annotatedText+"." );
        System.out.println("2this.annotatedText = "+this.annotatedText+"." );
        System.out.println("2plainRegulationText = "+plainRegulationText+"." );
        System.out.println("2this.plainRegulationText = "+this.plainRegulationText+"." );
*/
    }
    public void modifyAnnotatedText(final String annotatedText) {
        setAnnotatedText(annotatedText);
    }
    public void clearAnnotatedText() {
        setAnnotatedText(null);
    }


    // Region requirement
    private String requirement;
    @javax.jdo.annotations.Column(allowsNull="true", length=3000)
    //@Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Interpretation", sequence="90")
    @PropertyLayout(typicalLength=3000, multiLine=3)
    @SummernoteEditor(height = 100, maxHeight = 300)
    public String getRequirement() {
        return requirement;
    }
    /*   public void setRequirement(final String requirement) {
           this.requirement = requirement;
       } */
    public void setRequirement(final String requirement) {
       /* this.requirement = "<span style=\"background-color: yellow;\">every\"\" ship</span> must have a polar code certificate.<br>"; */
       /*THIS WORKS: this.requirement = "<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif;" +
                "font-size: small; line-height: normal;\"><span style=\"background-color:" +
                "yellow;\">Passenger ships</span> have more than 12 passengers.</span>";
    */
        /*THIS WORKS OK AS WELL:*/
        //     this.requirement = "<span style=\"background-color: rgb(0, 255, 0);\">Every ship</span> must have a polar code certificate.<br>";
        this.requirement = setColour();
    }
    public void modifyRequirement(final String requirement) {
        setRequirement(requirement);
    }
    public void clearRequirement() {
        setRequirement(null);
    }
    //endregion

    @Programmatic
    public String setColour () {
        return "<span style=\"background-color: rgb(0, 255, 0);\">Every ship</span> must have a polar code certificate.";
    }

        private String skosTerms;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    @MemberOrder(name="Interpretation", sequence="30")
    @PropertyLayout(typicalLength=10000, multiLine=6, named = "Terms", hidden=Where.ALL_TABLES)
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

