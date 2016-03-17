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
import org.apache.isis.applib.services.eventbus.ActionInteractionEvent;
import org.apache.isis.applib.services.eventbus.EventBusService;
import org.apache.isis.applib.services.wrapper.WrapperFactory;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.SKOSConceptOccurrence;
import org.isisaddons.wicket.summernote.fixture.dom.regulation.SolasChapter.ChapterAnnex;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
/* @javax.jdo.annotations.Uniques({
    @javax.jdo.annotations.Unique(
            name="FreeText_description_must_be_unique",
            members={"sectionNo","solasChapter"})
})
*/

@javax.jdo.annotations.Queries( {
        @javax.jdo.annotations.Query(
                name = "findByOwnedBy", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.FreeText "
                        + "WHERE ownedBy == :ownedBy"),
        @javax.jdo.annotations.Query(
        name = "findFreeTexts", language = "JDOQL",
        value = "SELECT "
                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.FreeText "),
    @javax.jdo.annotations.Query(
            name = "findByOwnedByAndCompleteIsFalse", language = "JDOQL",
            value = "SELECT "
                    + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.FreeText "
                    + "WHERE ownedBy == :ownedBy "
                    + "   && finalized == false")
})
@DomainObject(objectType="FREETEXT",autoCompleteRepository=FreeTexts.class, autoCompleteAction="autoComplete")
 // default unless overridden by autoCompleteNXxx() method
@DomainObjectLayout(bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
		columnSpans={4,0,0,8},
		left={"Section", "Annotation"},
		middle={},
        right={})
public class FreeText implements Categorized, Comparable<FreeText> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(FreeText.class);
      //endregion

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();
        if (getSolasChapter().getChapterAnnex().equals(ChapterAnnex.CHAPTER)) {buf.append("SOLAS CHAPTER ");}
        if (getSolasChapter().getChapterAnnex().equals(ChapterAnnex.ANNEX)) {buf.append("SOLAS ANNEX ");}
        if (getSolasChapter().getChapterAnnex().equals(ChapterAnnex.DIRECTIVE))  {buf.append("EU DIRECTIVE ");}
        buf.append(getSolasChapter().getSolasChapterNumber());
        if (!getSolasChapter().getSolasPartNumber().equalsIgnoreCase("-")) {
            if ((getSolasChapter().getChapterAnnex().equals(ChapterAnnex.CHAPTER)) || (getSolasChapter().getChapterAnnex().equals(ChapterAnnex.ANNEX))) {
                buf.append(" PART ");
            }
            if ((getSolasChapter().getChapterAnnex().equals(ChapterAnnex.DIRECTIVE))) {
                buf.append(" TITLE ");
            }
        }
        buf.append(getSolasChapter().getSolasPartNumber());
        if (getSolasChapter().getChapterAnnex().equals(ChapterAnnex.CHAPTER)) {buf.append(" REGULATION "); }
        if (getSolasChapter().getChapterAnnex().equals(ChapterAnnex.ANNEX)) {buf.append(" CHAPTER ");}
        if (getSolasChapter().getChapterAnnex().equals(ChapterAnnex.DIRECTIVE)) {buf.append(" ARTICLE ");}
        buf.append(getSolasChapter().getSolasRegulationNumber());
        buf.append("SECTION ");
        buf.append(getSectionNo());
        return buf.toString();
    }
    //endregion


    // Region sectionNo
    private String sectionNo;
    @javax.jdo.annotations.Column(allowsNull="false", length=10)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Section", sequence="10")
    @PropertyLayout(typicalLength=10)
    public String getSectionNo() {
        return sectionNo;
    }
    public void setSectionNo(final String sectionNo) {
        this.sectionNo = sectionNo;
    }
    public void modifySectionNo(final String sectionNo) { this.sectionNo = sectionNo;}
    public void clearSectionNo() {
        setSectionNo(null);
    }
    //endregion


    // Region plainRegulationText
    private String plainRegulationText;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
   // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Section", sequence="15")
    @PropertyLayout(typicalLength=10000, multiLine=8, named = "Text")
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


    // Region semantifiedRegulationText
    private String semantifiedRegulationText;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    @MemberOrder(name="Section", sequence="30")
    @PropertyLayout(typicalLength=10000, multiLine=8, named = "Annotated Text")
    @Property(editing = Editing.DISABLED,editingDisabledReason = "Update using action that calls an API from the consolidation services")
    public String getSemantifiedRegulationText() {
        return semantifiedRegulationText;
    }
    public void setSemantifiedRegulationText(final String semantifiedRegulationText) {
        this.semantifiedRegulationText = semantifiedRegulationText;
    }
    public void modifySemantifiedRegulationText(final String semantifiedRegulationText) {
        setSemantifiedRegulationText(semantifiedRegulationText);
    }
    public void clearSemantifiedRegulationText() {
        setSemantifiedRegulationText(null);
    }
    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Annotate (Get Rule)", position = ActionLayout.Position.PANEL)
    @MemberOrder(name="Section",
            sequence="40")
    public FreeText updateSemantifiedRegulationText() {
        // Call API to do semantification:
//        String AsyncRestTest = restClientTest.SkosFreetextAsync();
       // String asyncRestCall = restClient.GetTarget();
        // Get target is fetching the whole RULE
        String asyncRestCall = restClient.GetRule();
        System.out.println("asyncRest  (restClient.GetRule) = " + asyncRestCall);
        this.setSemantifiedRegulationText(asyncRestCall);
        container.flush();
        container.informUser("Annotation completed for " + container.titleOf(this));
        System.out.println("Fetching RULE here using GetRule!!");
        // MHAGA: Must show target in blue!! Other values also!!
        return this;
    }
    //endregion

    // Region skosTerm
    private String skosTerms;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    @MemberOrder(name="Annotation", sequence="10")
    @PropertyLayout(typicalLength=10000, multiLine=8, named = "SKOS Terms")
    @Property(editing = Editing.DISABLED,editingDisabledReason = "Update using action that calls an API from the consolidation services")
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
    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Annotate (Get SKOS Terms)", position = ActionLayout.Position.PANEL)
    @MemberOrder(name="Annotation",
            sequence="20")
    public FreeText updateSkosTerms() {
        // Call API to do semantification:
//        String AsyncRestTest = restClientTest.SkosFreetextAsync();
        // String asyncRestCall = restClient.GetTarget();
        // Get target is fetching the whole RULE
        System.out.println("plainRegulationText = " + plainRegulationText);
        String asyncRestCall = restClient.GetSkos2(plainRegulationText);
       System.out.println("asyncRest  (restClient.GetSkos) = " + asyncRestCall);
       this.setSkosTerms(asyncRestCall);
        container.flush();
        container.informUser("Annotation (GetSkos) completed for " + container.titleOf(this));
        System.out.println("Fetching SKOS Terms here using GetSkos!!");
        // MHAGA: Must show SKOS in yellow!! Other values also!!
        // Must show different values
        return this;
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



    // Add Regulation to RegulationRule
    // Add FreeText to SOLASchapter
    // department=regulation=SOLASchapter
    //empolyee=RegulationRule=FreeText

    // mapping is done to this property:
    @javax.jdo.annotations.Column(allowsNull="true")
    @Property(editing= Editing.DISABLED,editingDisabledReason="SOLAS Chapter cannot be updated from here")
    @PropertyLayout(hidden=Where.REFERENCES_PARENT, named = "SOLAS Link")
    @MemberOrder(name="Section", sequence="50")
    private SolasChapter solasChapter;
    @javax.jdo.annotations.Column(allowsNull="true")
    public SolasChapter getSolasChapter() { return solasChapter; }
    @javax.jdo.annotations.Column(allowsNull="true")
    public void setSolasChapter(SolasChapter solasChapter) { this.solasChapter = solasChapter; }

    // End   Regulation to RegulationRule


    // BEGIN REGION Link to TextItems, a list of text items.
    @javax.jdo.annotations.Persistent(mappedBy="freeText")
    @javax.jdo.annotations.Join // Make a separate join table.
    private SortedSet<TextItem> textItems= new TreeSet<TextItem>();
    @SuppressWarnings("deprecation")
    @CollectionLayout(named = "List of Items" , sortedBy=TextItemComparator.class,render=RenderType.EAGERLY)
    @MemberOrder(name="List of Items",sequence = "10")
    public SortedSet<TextItem> getTextItems() {
        return textItems;
    }
    public void setTextItems(SortedSet<TextItem> textItem) {
        this.textItems = textItem;
    }
    public void removeFromTextItems(final TextItem item) {
        if(item == null || !getTextItems().contains(item)) return;
        getTextItems().remove(item);
    }

    // / overrides the natural ordering
    public static class TextItemComparator implements Comparator<TextItem> {
        @Override
        public int compare(TextItem p, TextItem q) {
            Ordering<TextItem> byItemNo= new Ordering<TextItem>() {
                public int compare(final TextItem p, final TextItem q) {
                    return Ordering.natural().nullsFirst().compare(p.getItemNo(),q.getItemNo());
                }
            };
            return byItemNo
                    .compound(Ordering.<TextItem>natural())
                    .compare(p, q);
        }
    }

    //This is the add-Button!!!

    @Action()
    @ActionLayout(named = "Add New Item")
    @MemberOrder(name = "List of Items", sequence = "70")
    public FreeText addNewTextItem(final @ParameterLayout(typicalLength=10,named = "Item No") String itemNo,
                                   final  @ParameterLayout(typicalLength=1000, multiLine=8,named = "Text") String plainRegulationText
    )
    {
        getTextItems().add(newTextItemCall.newTextItem(itemNo, plainRegulationText));
        return this;
    }

    //This is the Remove-Button!!
    @MemberOrder(name="List of Items", sequence = "80")
    @ActionLayout(named = "Delete Item")
    public FreeText removeTextItem(final @ParameterLayout(typicalLength=30) TextItem item) {
        // By wrapping the call, Isis will detect that the collection is modified
        // and it will automatically send a CollectionInteractionEvent to the Event Bus.
        // ToDoItemSubscriptions is a demo subscriber to this event
        wrapperFactory.wrapSkipRules(this).removeFromTextItems(item);
        container.removeIfNotAlready(item);
        return this;
    }

    // disable action dependent on state of object
    public String disableRemoveTextItem(final TextItem item) {
        return getTextItems().isEmpty()? "No Item to remove": null;
    }
    // validate the provided argument prior to invoking action
    public String validateRemoveTextItem(final TextItem item) {
        if(!getTextItems().contains(item)) {
            return "Not an Item";
        }
        return null;
    }

    // provide a drop-down
    public java.util.Collection<TextItem> choices0RemoveTextItem() {
        return getTextItems();
    }
    //endregion region Link FreeText (Section) --> to (several) TextItems



    // BEGIN REGION Link to SubSections
//    @javax.jdo.annotations.Persistent(mappedBy="solasChapter")
    @javax.jdo.annotations.Persistent(mappedBy="freeTextSection")
    @javax.jdo.annotations.Join // Make a separate join table.
    private SortedSet<SubSection> subSections= new TreeSet<SubSection>();
     @SuppressWarnings("deprecation")
     @CollectionLayout(named = "SubSections" , sortedBy=SubSectionsComparator.class,render=RenderType.EAGERLY)
     @MemberOrder(name="SubSections",sequence = "20")
     public SortedSet<SubSection> getSubSections() {
        return subSections;
    }
    public void setSubSections(SortedSet<SubSection> subSection) {
        this.subSections = subSection;
    }
    public void removeFromSubSections(final SubSection subSection) {
        if(subSection == null || !getSubSections().contains(subSection)) return;
        getSubSections().remove(subSection);
    }

    // / overrides the natural ordering
    public static class SubSectionsComparator implements Comparator<SubSection> {
        @Override
        public int compare(SubSection p, SubSection q) {
            Ordering<SubSection> bySubSectionNo = new Ordering<SubSection>() {
                public int compare(final SubSection p, final SubSection q) {
                    return Ordering.natural().nullsFirst().compare(p.getSubSectionNo(),q.getSubSectionNo());
                }
            };
            return bySubSectionNo
                    .compound(Ordering.<SubSection>natural())
                    .compare(p, q);
        }
    }

    //This is the add-Button!!!

    @Action()
    @ActionLayout(named = "Add New SubSection")
    @MemberOrder(name = "subSections", sequence = "15")
    public FreeText addNewSubSection(final @ParameterLayout(typicalLength=10,named = "Sub Section No") String subSectionNo,
                                       final  @ParameterLayout(typicalLength=1000, multiLine=8,named = "Regulation Text") String plainRegulationText
    )
    {
         getSubSections().add(newSubSectionCall.newSubSection(subSectionNo, plainRegulationText));
         return this;
    }

    //This is the Remove-Button!!
    @MemberOrder(name="subSections", sequence = "20")
    @ActionLayout(named = "Delete SubSection")
    public FreeText removeSubSection(final @ParameterLayout(typicalLength=30) SubSection subSection) {
        // By wrapping the call, Isis will detect that the collection is modified
        // and it will automatically send a CollectionInteractionEvent to the Event Bus.
        // ToDoItemSubscriptions is a demo subscriber to this event
        wrapperFactory.wrapSkipRules(this).removeFromSubSections(subSection);
        container.removeIfNotAlready(subSection);
        return this;
    }

    // disable action dependent on state of object
    public String disableRemoveSubSection(final SubSection subSection) {
        return getSubSections().isEmpty()? "No Text to remove": null;
    }
    // validate the provided argument prior to invoking action
    public String validateRemoveSubSection(final SubSection subSection) {
        if(!getSubSections().contains(subSection)) {
            return "Not a SubSection";
        }
        return null;
    }

    // provide a drop-down
    public java.util.Collection<SubSection> choices0RemoveSubSection() {
        return getSubSections();
    }
    //endregion region Link FreeText (Section) --> to (several) SubSections





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
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<FreeText> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final FreeText source,
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
                final FreeText source,
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
        return ObjectContracts.toString(this, "sectionNo,plainRegulationText, solasChapter, ownedBy");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final FreeText other) {
        return ObjectContracts.compare(this, other, "sectionNo,plainRegulationText, solasChapter, ownedBy");
    }
    //endregion

    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;

   // @javax.inject.Inject
   // private FreeTexts freeTexts;

    @javax.inject.Inject
    private TextItems newTextItemCall;

    @javax.inject.Inject
    private SubSections newSubSectionCall;

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
