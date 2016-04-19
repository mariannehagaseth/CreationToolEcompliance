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
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.*;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import java.util.*;

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
                name = "findByOwnedBy", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.SubSection "
                        + "WHERE ownedBy == :ownedBy"),
        @javax.jdo.annotations.Query(
        name = "findSubSections", language = "JDOQL",
        value = "SELECT "
                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.SubSection "),
    @javax.jdo.annotations.Query(
            name = "findByOwnedByAndCompleteIsFalse", language = "JDOQL",
            value = "SELECT "
                    + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.SubSection "
                    + "WHERE ownedBy == :ownedBy "
                    + "   && finalized == false")
})
@DomainObject(objectType="SUBSECTION",autoCompleteRepository=SubSections.class, autoCompleteAction="autoComplete")
 // default unless overridden by autoCompleteNXxx() method
@DomainObjectLayout(bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
		columnSpans={6,0,0,6},
		left={"SubSection","Annotation"},
		middle={},
        right={})
public class SubSection implements Categorized, Comparable<SubSection> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(SubSection.class);
      //endregion

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();

        if (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.CHAPTER)) {buf.append("SOLAS CHAPTER ");}
        if (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.ANNEX)) {buf.append("SOLAS ANNEX ");}
        if (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.DIRECTIVE))  {buf.append("EU DIRECTIVE ");}
        buf.append(getFreeTextSection().getRegulationLink().getChapterNumber());
        if (!getFreeTextSection().getRegulationLink().getPartNumber().equalsIgnoreCase("-")) {
            if ((getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.CHAPTER)) || (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.ANNEX))) {
                buf.append(" PART ");
            }
            if ((getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.DIRECTIVE))) {
                buf.append(" TITLE ");
            }
        }
        buf.append(getFreeTextSection().getRegulationLink().getPartNumber());
        if (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.CHAPTER)) {buf.append(" REGULATION "); }
        if (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.ANNEX)) {buf.append(" CHAPTER ");}
        if (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.DIRECTIVE)) {buf.append(" ARTICLE ");}
        buf.append(getFreeTextSection().getRegulationLink().getRegulationNumber());
        buf.append("SECTION ");
        buf.append(getFreeTextSection().getSectionNo());
        buf.append("SUBSECTION ");
        buf.append(getSubSectionNo());
        return buf.toString();
    }
    //endregion


    // Region subSectionNo
    private String subSectionNo;
    @javax.jdo.annotations.Column(allowsNull="false", length=10)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="SubSection", sequence="10")
    @PropertyLayout(typicalLength=10)
    public String getSubSectionNo() {
        return subSectionNo;
    }
    public void setSubSectionNo(final String subSectionNo) {
        this.subSectionNo = subSectionNo;
    }
    public void modifySubSectionNo(final String subSectionNo) { this.subSectionNo = subSectionNo;}
    public void clearSubSectionNo() {
        setSubSectionNo(null);
    }
    //endregion


    // Region plainRegulationText
    private String plainRegulationText;
    @javax.jdo.annotations.Column(allowsNull="false", length=10000)
   // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="SubSection", sequence="15")
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
    // Mapping back from SubSection to FreeText
    @javax.jdo.annotations.Column(allowsNull="true")
    @Property(editing= Editing.DISABLED,editingDisabledReason="Section Number cannot be updated from here")
    @MemberOrder(name="SubSection", sequence="50")
    @PropertyLayout(hidden=Where.REFERENCES_PARENT, named = "Section")
    private FreeText freeTextSection;
    @javax.jdo.annotations.Column(allowsNull="true",name = "Section")
    public FreeText getFreeTextSection() { return freeTextSection; }
    @javax.jdo.annotations.Column(allowsNull="true")
    public void setFreeTextSection(FreeText freeTextSection) { this.freeTextSection= freeTextSection; }

    // End   Regulation to RegulationRule


    // BEGIN REGION Link to SubSectionTextItems, a list of text items from SubSection to SubSectionTextItems.
    @javax.jdo.annotations.Persistent(mappedBy="subSection")
    @javax.jdo.annotations.Join // Make a separate join table.
    private SortedSet<SubSectionTextItem> subSectionTextItems= new TreeSet<SubSectionTextItem>();
    @SuppressWarnings("deprecation")
    @CollectionLayout(named = "List of Items" , sortedBy=SubSectionTextItemComparator.class,render=RenderType.EAGERLY)
    @MemberOrder(name="List of Items",sequence = "10")
    public SortedSet<SubSectionTextItem> getSubSectionTextItems() {
        return subSectionTextItems;
    }
    public void setSubSectionTextItems(SortedSet<SubSectionTextItem> subSectionTextItem) {
        this.subSectionTextItems = subSectionTextItem;
    }
    public void removeFromSubSectionTextItems(final SubSectionTextItem item) {
        if(item == null || !getSubSectionTextItems().contains(item)) return;
        getSubSectionTextItems().remove(item);
    }

    // / overrides the natural ordering
    public static class SubSectionTextItemComparator implements Comparator<SubSectionTextItem> {
        @Override
        public int compare(SubSectionTextItem p, SubSectionTextItem q) {
            Ordering<SubSectionTextItem> byItemNo= new Ordering<SubSectionTextItem>() {
                public int compare(final SubSectionTextItem p, final SubSectionTextItem q) {
                    return Ordering.natural().nullsFirst().compare(p.getItemNo(),q.getItemNo());
                }
            };
            return byItemNo
                    .compound(Ordering.<SubSectionTextItem>natural())
                    .compare(p, q);
        }
    }

    //This is the add-Button!!!

    @Action()
    @ActionLayout(named = "Add New Item")
    @MemberOrder(name = "List of Items", sequence = "70")
    public SubSection addNewSubSectionTextItem(final @ParameterLayout(typicalLength=5,named = "Item No") String itemNo,
                                   final  @ParameterLayout(typicalLength=1000, multiLine=8,named = "Text") String plainRegulationText
    )
    {
        getSubSectionTextItems().add(newSubSectionTextItemCall.newSubSectionTextItem(itemNo, plainRegulationText));
        return this;
    }

    //This is the Remove-Button!!
    @MemberOrder(name="List of Items", sequence = "80")
    @ActionLayout(named = "Delete Item")
    public SubSection removeSubSectionTextItem(final @ParameterLayout(typicalLength=5) SubSectionTextItem item) {
        // By wrapping the call, Isis will detect that the collection is modified
        // and it will automatically send a CollectionInteractionEvent to the Event Bus.
        // ToDoItemSubscriptions is a demo subscriber to this event
        wrapperFactory.wrapSkipRules(this).removeFromSubSectionTextItems(item);
        container.removeIfNotAlready(item);
        return this;
    }

    // disable action dependent on state of object
    public String disableRemoveSubSectionTextItem(final SubSectionTextItem item) {
        return getSubSectionTextItems().isEmpty()? "No Item to remove": null;
    }
    // validate the provided argument prior to invoking action
    public String validateRemoveSubSectionTextItem(final SubSectionTextItem item) {
        if(!getSubSectionTextItems().contains(item)) {
            return "Not an Item";
        }
        return null;
    }

    // provide a drop-down
    public java.util.Collection<SubSectionTextItem> choices0RemoveSubSectionTextItem() {
        return getSubSectionTextItems();
    }
    //endregion region Link  SubSection --> to (several) items SubSectionTextItems



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
    public SubSection CheckTerms() {
        FragmentSKOSConceptOccurrences fragment = restClient.GetSkos(plainRegulationText);
        System.out.println("SUBSECTION: fragment OK");
        List<String> annotation = new ArrayList<String>();
        setSkosTerms(creationController.ShowTerms(plainRegulationText, fragment).get(0));
//  SummernoteEditor:  setAnnotatedText(creationController.ShowTerms(plainRegulationText, fragment).get(1));
        setAnnotatedText(plainRegulationText);
        System.out.println("SUBSECTION: skosTerms=" + skosTerms);
        System.out.println("SUBSECTION: annotatedText=" + annotatedText);
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
    public SubSection ShowTarget() {
        ShipClass shipClassFound = null;
        // CALLS THE TARGET API
        shipClassFound = restClient.GetTarget(plainRegulationText);
        // shipClassFound = restClient.GetApplicability(plainRegulationText);
        System.out.println("SUBSECTION:shipclassfound OK");
        setTarget(creationController.ShowShipClass(plainRegulationText, shipClassFound));
        System.out.println("SUBSECTION: target=" + target);
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
    public SubSection ShowApplicability() {
        ShipClass shipClassFound = null;
        // CALLS THE APPLICABILITY API
        shipClassFound = restClient.GetApplicability(plainRegulationText);
        System.out.println("SUBSECTION:applicability shipclassfound OK");
        setApplicability(creationController.ShowShipClass(plainRegulationText, shipClassFound));
        System.out.println("SUBSECTION: applicability=" + applicability);

        container.flush();
        container.informUser("Fetched Target Ship Class completed for " + container.titleOf(this));
        return this;

    }
// END SHOW applicability


    //region > documentURI (property)
    // documentURI contains the URI of the RDF-node storing the text for this chapter-node.
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private String documentURI;
    @PropertyLayout(hidden=Where.ALL_TABLES)
    @MemberOrder(name = "RDF", sequence = "20")
    @javax.jdo.annotations.Column(allowsNull="true")
    @Property(editing= Editing.DISABLED,editingDisabledReason="Programmatically updated")
    public String getDocumentURI() {
        return documentURI;
    }
    @ActionLayout(hidden=Where.EVERYWHERE)
    public void setDocumentURI(final String documentURI) {
        this.documentURI = documentURI;
    }
    //endregion

    //region > targetShipClassURI (property)
    // targetShipClassURI contains the URI of the target ship class for this text in this RDF-node.
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private String targetShipClassURI;
    @PropertyLayout(hidden=Where.ALL_TABLES)
    @MemberOrder(name = "RDF", sequence = "30")
    @javax.jdo.annotations.Column(allowsNull="true")
    @Property(editing= Editing.DISABLED,editingDisabledReason="Programmatically updated")
    public String getTargetShipClassURI() {
        return targetShipClassURI;
    }
    @ActionLayout(hidden=Where.EVERYWHERE)
    public void setTargetShipClassURI(final String targetShipClassURI) {
        this.targetShipClassURI = targetShipClassURI;
    }
    //endregion




    //region > CREATE RDF node for the SubSection: (action)
    @Action()
    @ActionLayout(named = "Make Persistent",position = ActionLayout.Position.PANEL)
    @MemberOrder(name="Text", sequence="5")
    public SubSection storeSubSection() {

        System.out.println("Make Persistent_1");
        // Must check if the documentURI has already been stored:

        if (getDocumentURI() == null){
            System.out.println("DocumentURI not fetched for this SubSection");

            // Must call 192.168.33.10:9000/api/rdf/document/component for this chapter to create a RDF Document for this chapter
            DocumentComponentList documentComponentList = new DocumentComponentList();

            if (getId() == null) {documentComponentList.setVersion("0");}
            else {
                documentComponentList.setVersion(getId().toString());
            }
            // Parent is documentURI of the Chapter:
            // freeTextSection is the link back to FreeText:
            documentComponentList.setParent(freeTextSection.getDocumentURI());
            documentComponentList.setComponentType(DocumentComponentType.DOCUMENT);

            DocumentComponent documentComponent = new DocumentComponent();

            documentComponent.setTitle("SUBECTION"+" "+getSubSectionNo());
            documentComponent.setShortTitle("SUBSECTION"+" "+getSubSectionNo());
            documentComponent.setText(getPlainRegulationText());
//      THIS METHOD is missing:
            //       documentComponentList.setComponent(documentComponent);


            // Need to call the Component API to create the RDF node for the part.
            // Will store the value in the documentURI property
            IRIList iriList =restClient.CreateDocumentComponentNode(documentComponentList);
            // has created the RDF node for only one CHAPTER, that is, only one Node, that is, only one IRI-element:
            if (iriList.getIris().size()>0) {
                setDocumentURI(iriList.getIris().get(0));
            }
            else
            {setDocumentURI("Could not find URI for RDF");
            }
        }
        else
        {
            // rootNode is not null, that is, already found.
            System.out.println("DocumentURI is already fetched for this  = "+getDocumentURI()+".");
        }
        return this;
    }
    //endregion


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
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<SubSection> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final SubSection source,
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
                final SubSection source,
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
        return ObjectContracts.toString(this, "subSectionNo,plainRegulationText, freeTextSection, ownedBy");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final SubSection other) {
        return ObjectContracts.compare(this, other, "subSectionNo,plainRegulationText, freeTextSection, ownedBy");
    }
    //endregion

    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private SubSections subSections;

    @javax.inject.Inject
    private SubSectionTextItems newSubSectionTextItemCall;


    @SuppressWarnings("deprecation")
	Bulk.InteractionContext bulkInteractionContext;
    public void injectBulkInteractionContext(@SuppressWarnings("deprecation") Bulk.InteractionContext bulkInteractionContext) {
        this.bulkInteractionContext = bulkInteractionContext;
    }

    @javax.inject.Inject
    private CreationController creationController;

    @javax.inject.Inject
    private RESTclient restClient;

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
