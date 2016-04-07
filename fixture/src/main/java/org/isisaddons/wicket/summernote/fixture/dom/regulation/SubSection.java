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

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

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
		left={"SubSection"},
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

        if (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(SolasChapter.ChapterAnnex.CHAPTER)) {buf.append("SOLAS CHAPTER ");}
        if (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(SolasChapter.ChapterAnnex.ANNEX)) {buf.append("SOLAS ANNEX ");}
        if (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(SolasChapter.ChapterAnnex.DIRECTIVE))  {buf.append("EU DIRECTIVE ");}
        buf.append(getFreeTextSection().getRegulationLink().getChapterNumber());
        if (!getFreeTextSection().getRegulationLink().getPartNumber().equalsIgnoreCase("-")) {
            if ((getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(SolasChapter.ChapterAnnex.CHAPTER)) || (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(SolasChapter.ChapterAnnex.ANNEX))) {
                buf.append(" PART ");
            }
            if ((getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(SolasChapter.ChapterAnnex.DIRECTIVE))) {
                buf.append(" TITLE ");
            }
        }
        buf.append(getFreeTextSection().getRegulationLink().getPartNumber());
        if (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(SolasChapter.ChapterAnnex.CHAPTER)) {buf.append(" REGULATION "); }
        if (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(SolasChapter.ChapterAnnex.ANNEX)) {buf.append(" CHAPTER ");}
        if (getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(SolasChapter.ChapterAnnex.DIRECTIVE)) {buf.append(" ARTICLE ");}
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
