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
import org.apache.isis.applib.services.eventbus.ActionInteractionEvent;
import org.apache.isis.applib.services.eventbus.EventBusService;
import org.apache.isis.applib.services.wrapper.WrapperFactory;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;

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
//@javax.jdo.annotations.Uniques({
//    @javax.jdo.annotations.Unique(
//            name="RootNode_must_be_unique",
//            members={"nodeLabel","rootURI"})
//})
@javax.jdo.annotations.Queries( {
        @javax.jdo.annotations.Query(
                name = "findSKOS", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.SKOS " )
})
@DomainObject(objectType="SKOS",autoCompleteRepository=SKOSs.class, autoCompleteAction="autoComplete", bounded = true)
 // default unless overridden by autoCompleteNXxx() method
@DomainObjectLayout(bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
        columnSpans={4,4,4,12},
        left={"SKOS"},
        middle={},
        right={} )
public class SKOS implements Categorized, Comparable<SKOS> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(SKOS.class);
      //endregion

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();
        buf.append(getUri());
         return buf.toString();
    }
    //endregion



    private String uri;
    @javax.jdo.annotations.Column(allowsNull="false")
    @Property(editing = Editing.DISABLED)
     public String getUri() {
        return uri;
    }
    public void setUri(final String uri) {
        this.uri= uri;
       }

    // This is the label of the chapter level: CHAPTER or ANNEX or DIRECTIVE.
    private String prefTerm;
    @javax.jdo.annotations.Column(allowsNull="false")
     @Property(editing = Editing.DISABLED)
    public String getPrefTerm() {
        return prefTerm;
    }
    public void setPrefTerm(final String prefTerm) {
        this.prefTerm =prefTerm;
     }

    // This is the label of the chapter level: CHAPTER or ANNEX or DIRECTIVE.
    private String usedTerm;
    @javax.jdo.annotations.Column(allowsNull="false")
     @Property(editing = Editing.DISABLED)
    public String getUsedTerm() {
        return usedTerm;
    }
    public void setUsedTerm(final String usedTerm) {
        this.usedTerm =usedTerm;
    }


    // This is the label of the chapter level: CHAPTER or ANNEX or DIRECTIVE.
    private String skosConceptProperty;
    @javax.jdo.annotations.Column(allowsNull="false")
     @Property(editing = Editing.DISABLED)
    public String getSkosConceptProperty() {
        return skosConceptProperty;
    }
    public void setSkosConceptProperty(final String skosConceptProperty) {
        this.skosConceptProperty =skosConceptProperty;
    }

    // BEGIN LINK FROM SKOS TO FREETEXT
    // mapping is done to this property:
    // Mapping back from SKOS to FreeText
    //Link to Section
    @javax.jdo.annotations.Column(allowsNull="true")
    @Property(editing= Editing.DISABLED,editingDisabledReason="Cannot be updated from here")
    @PropertyLayout(hidden=Where.REFERENCES_PARENT, named = "Link")
    private FreeText skosLink;
    @javax.jdo.annotations.Column(allowsNull="true")
    public FreeText  getSkosLink() { return skosLink; }
    @javax.jdo.annotations.Column(allowsNull="true")
    public void setSkosLink(FreeText skosLink) { this.skosLink= skosLink; }
    // END LINK FROM SKOS TO FREETEXT

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
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<SKOS> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final SKOS source,
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
                final SKOS source,
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
        return ObjectContracts.toString(this, "uri");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final SKOS other) {
        return ObjectContracts.compare(this, other, "uri");
    }
    //endregion

    //region > injected services


    @javax.inject.Inject
    private DomainObjectContainer container;




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

