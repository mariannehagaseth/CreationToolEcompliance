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
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.FragmentSKOSConceptOccurrences;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.ShipClass;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import java.util.ArrayList;
import java.util.List;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
/* @javax.jdo.annotations.Uniques({
    @javax.jdo.annotations.Unique(
            name="SubSection_description_must_be_unique",
            members={"sectionNo","solasChapter"})
})
*/

@javax.jdo.annotations.Queries( {
        @javax.jdo.annotations.Query(
                name = "findTextItems", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.TextItem "
                        + "WHERE ownedBy == :ownedBy"),
        @javax.jdo.annotations.Query(
        name = "findTextItems", language = "JDOQL",
        value = "SELECT "
                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.TextItem "),
    @javax.jdo.annotations.Query(
            name = "findByOwnedByAndCompleteIsFalse", language = "JDOQL",
            value = "SELECT "
                    + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.TextItem "
                    + "WHERE ownedBy == :ownedBy "
                    + "   && finalized == false")
})
@DomainObject(objectType="TEXTITEM",autoCompleteRepository=TextItems.class, autoCompleteAction="autoComplete")
 // default unless overridden by autoCompleteNXxx() method
@DomainObjectLayout(bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
		columnSpans={4,0,0,8},
		left={"TextItem"},
		middle={},
        right={})
public class TextItem implements Categorized, Comparable<TextItem> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(TextItem.class);
      //endregion

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();

        if (getFreeText().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.CHAPTER)) {buf.append("SOLAS CHAPTER ");}
        if (getFreeText().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.ANNEX)) {buf.append("SOLAS ANNEX ");}
        if (getFreeText().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.DIRECTIVE))  {buf.append("EU DIRECTIVE ");}
        buf.append(getFreeText().getRegulationLink().getChapterNumber());
        if (!getFreeText().getRegulationLink().getPartNumber().equalsIgnoreCase("-")) {
            if ((getFreeText().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.CHAPTER)) || (getFreeText().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.ANNEX))) {
                buf.append(" PART ");
            }
            if ((getFreeText().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.DIRECTIVE))) {
                buf.append(" TITLE ");
            }
        }
        buf.append(getFreeText().getRegulationLink().getPartNumber());
        if (getFreeText().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.CHAPTER)) {buf.append(" REGULATION "); }
        if (getFreeText().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.ANNEX)) {buf.append(" CHAPTER ");}
        if (getFreeText().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.DIRECTIVE)) {buf.append(" ARTICLE ");}
        buf.append(getFreeText().getRegulationLink().getRegulationNumber());
        buf.append("SECTION ");
        buf.append(getFreeText().getSectionNo());

        buf.append("ITEM ");
        buf.append(getItemNo());
        return buf.toString();
    }
    //endregion

    // Region itemNo
    private String itemNo;
    @javax.jdo.annotations.Column(allowsNull="true", length=10)
    @MemberOrder(name="TextItem", sequence="8")
    @PropertyLayout(typicalLength=10)
    public String getItemNo() {
        return itemNo;
    }
    public void setItemNo(final String itemNo) {
        this.itemNo = itemNo;
    }
    public void modifyItemNo(final String itemNo) { this.itemNo = itemNo;}
    public void clearItemNo() {
        setItemNo(null);
    }
    //endregion


    // Region plainRegulationText
    private String plainRegulationText;
    @javax.jdo.annotations.Column(allowsNull="false", length=10000)
   // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="TextItem", sequence="10")
    @PropertyLayout(named = "Text", typicalLength=10000, multiLine=8)
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

    // mapping is done to this property:
    // Mapping back from TextItem to FreeText
    //Link to Section
    @javax.jdo.annotations.Column(allowsNull="true")
    @Property(editing= Editing.DISABLED,editingDisabledReason="Section cannot be updated from here")
    @PropertyLayout(hidden=Where.REFERENCES_PARENT, named = "Section")
    @MemberOrder(name="TextItem", sequence="50")
    private FreeText freeText;
    @javax.jdo.annotations.Column(allowsNull="true")
    public FreeText getFreeText() { return freeText; }
    @javax.jdo.annotations.Column(allowsNull="true")
    public void setFreeText(FreeText freeText) { this.freeText= freeText; }

    // End   TextITem to FreeTExt




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

    // BEGIN REGION ANNOTATED TEXT
    private String annotatedText;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 10000)
    @MemberOrder(name = "Annotation", sequence = "10")
    @PropertyLayout(typicalLength = 10000, multiLine = 3, hidden = Where.ALL_TABLES)
    @Property(editing = Editing.DISABLED, editingDisabledReason = "Update using action that calls an API from the consolidation services")
//   @SummernoteEditor(height = 100, maxHeight = 300)
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

    @javax.jdo.annotations.Column(allowsNull = "true", length = 10000)
    @MemberOrder(name = "Annotation", sequence = "20")
    @PropertyLayout(typicalLength = 10000, multiLine = 3, named = "Terms", hidden = Where.ALL_TABLES)
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
    @MemberOrder(name = "Terms", sequence = "20")
    public TextItem CheckTerms() {
        FragmentSKOSConceptOccurrences fragment = restClient.GetSkos(plainRegulationText);
        System.out.println("TEXTITEM: fragment OK");
        List<String> annotation = new ArrayList<String>();
        setSkosTerms(creationController.ShowTerms(plainRegulationText, fragment).get(0));
//  SummernoteEditor:  setAnnotatedText(creationController.ShowTerms(plainRegulationText, fragment).get(1));
        setAnnotatedText(plainRegulationText);
        System.out.println("TEXTITEM: skosTerms=" + skosTerms);
        System.out.println("TEXTITEM: annotatedText=" + annotatedText);
        container.flush();
        container.informUser("Fetched SKOS terms completed for " + container.titleOf(this));
        return this;
    }

    //endregion


    //BEGIN show  Region target
    private String target;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 10000)
    @MemberOrder(name = "Annotation", sequence = "30")
    @PropertyLayout(typicalLength = 10000, multiLine = 3, named = "Target", hidden = Where.ALL_TABLES)
    //@Property(editing = Editing.DISABLED,editingDisabledReason = "Update using action that calls an API from the consolidation services")
    public String getTarget() {
        return target;
    }

    public void setTarget(final String target) {
        this.target = target;
    }

    public void modifyTarget(final String target) {
        setTarget(target);
    }

    public void clearTarget() {
        setTarget(null);
    }

    //@Action(semantics = SemanticsOf.IDEMPOTENT)
    @Action()
    //  @ActionLayout(named = "Check Terms", position = ActionLayout.Position.PANEL)
    @ActionLayout(position = ActionLayout.Position.PANEL)
    @MemberOrder(name = "Terms", sequence = "20")
    public TextItem ShowTarget() {
        ShipClass shipClassFound = null;
        // CALLS THE TARGET API
        shipClassFound = restClient.GetTarget(plainRegulationText);
        // shipClassFound = restClient.GetApplicability(plainRegulationText);
        System.out.println("TEXTITEM:shipclassfound OK");
        setTarget(creationController.ShowShipClass(plainRegulationText, shipClassFound));
        System.out.println("TEXTITEM: target=" + target);
        container.flush();
        container.informUser("Fetched Target completed for " + container.titleOf(this));
        return this;
    }

// END SHOW target


    //BEGIN show applicability
    private String applicability;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 10000)
    @MemberOrder(name = "Annotation", sequence = "35")
    @PropertyLayout(typicalLength = 10000, multiLine = 3, named = "Applicability", hidden = Where.ALL_TABLES)
    //@Property(editing = Editing.DISABLED,editingDisabledReason = "Update using action that calls an API from the consolidation services")
    public String getApplicability() {
        return applicability;
    }

    public void setApplicability(final String applicability) {
        this.applicability = applicability;
    }

    public void modifyApplicability(final String applicability) {
        setApplicability(applicability);
    }

    public void clearApplicability() {
        setApplicability(null);
    }

    //@Action(semantics = SemanticsOf.IDEMPOTENT)
    @Action()
    //  @ActionLayout(named = "Check Terms", position = ActionLayout.Position.PANEL)
    @ActionLayout(position = ActionLayout.Position.PANEL)
    @MemberOrder(name = "Terms", sequence = "30")
    public TextItem ShowApplicability() {
        ShipClass shipClassFound = null;
        // CALLS THE APPLICABILITY API
        shipClassFound = restClient.GetApplicability(plainRegulationText);
        System.out.println("TEXTITEM:applicability shipclassfound OK");
        setApplicability(creationController.ShowShipClass(plainRegulationText, shipClassFound));
        System.out.println("TEXTITEM: applicability=" + applicability);


        container.flush();
        container.informUser("Fetched Target Ship Class completed for " + container.titleOf(this));
        return this;

    }
// END SHOW applicability

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
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<TextItem> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final TextItem source,
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
                final TextItem source,
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
        return ObjectContracts.toString(this, "itemNo, plainRegulationText, freeText, ownedBy");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final TextItem other) {
        return ObjectContracts.compare(this, other, "itemNo, plainRegulationText, freeText, ownedBy");
    }
    //endregion

    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private TextItems textItems;

    @javax.inject.Inject
    private CreationController creationController;

    @javax.inject.Inject
    private RESTclient restClient;

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
