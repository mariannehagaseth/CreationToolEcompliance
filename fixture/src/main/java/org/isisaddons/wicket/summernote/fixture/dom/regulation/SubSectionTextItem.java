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

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")

@javax.jdo.annotations.Queries( {
        @javax.jdo.annotations.Query(
                name = "findTextItems", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.TextItem "
                        + "WHERE ownedBy == :ownedBy"),
        @javax.jdo.annotations.Query(
        name = "findSubSectionTextItems", language = "JDOQL",
        value = "SELECT "
                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.SubSectionTextItem "),
    @javax.jdo.annotations.Query(
            name = "findByOwnedByAndCompleteIsFalse", language = "JDOQL",
            value = "SELECT "
                    + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.TextItem "
                    + "WHERE ownedBy == :ownedBy "
                    + "   && finalized == false")
})
@DomainObject(objectType="SUBSECTIONTEXTITEM",autoCompleteRepository=TextItems.class, autoCompleteAction="autoComplete")
 // default unless overridden by autoCompleteNXxx() method
@DomainObjectLayout(bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
		columnSpans={4,0,0,8},
		left={"TextItem"},
		middle={},
        right={})
public class SubSectionTextItem implements Categorized, Comparable<SubSectionTextItem> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(SubSectionTextItem.class);
      //endregion

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();

        if (getSubSection().getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.CHAPTER)) {buf.append("SOLAS CHAPTER ");}
        if (getSubSection().getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.ANNEX)) {buf.append("SOLAS ANNEX ");}
        if (getSubSection().getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.DIRECTIVE))  {buf.append("EU DIRECTIVE ");}
        buf.append(getSubSection().getFreeTextSection().getRegulationLink().getChapterNumber());
        if (!getSubSection().getFreeTextSection().getRegulationLink().getPartNumber().equalsIgnoreCase("-")) {
            if ((getSubSection().getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.CHAPTER)) || (getSubSection().getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.ANNEX))) {
                buf.append(" PART ");
            }
            if ((getSubSection().getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.DIRECTIVE))) {
                buf.append(" TITLE ");
            }
        }
        buf.append(getSubSection().getFreeTextSection().getRegulationLink().getPartNumber());
        if (getSubSection().getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.CHAPTER)) {buf.append(" REGULATION "); }
        if (getSubSection().getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.ANNEX)) {buf.append(" CHAPTER ");}
        if (getSubSection().getFreeTextSection().getRegulationLink().getChapterAnnexArticle().equals(Chapter.ChapterAnnex.DIRECTIVE)) {buf.append(" ARTICLE ");}
        buf.append(getSubSection().getFreeTextSection().getRegulationLink().getRegulationNumber());
        buf.append("SECTION ");
        buf.append(getSubSection().getFreeTextSection().getSectionNo());

        buf.append("SUBSECTION ");
        buf.append(getSubSection().getSubSectionNo());

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
    // Mapping back from SubsectionTextItem to SubSection
    //Link to SubSection from textItem
    @javax.jdo.annotations.Column(allowsNull="true")
    @Property(editing= Editing.DISABLED,editingDisabledReason="SubSection cannot be updated from here")
    @PropertyLayout(hidden=Where.REFERENCES_PARENT, named = "SubSection")
    @MemberOrder(name="TextItem", sequence="50")
    private SubSection subSection;
    @javax.jdo.annotations.Column(allowsNull="true")
    public SubSection getSubSection() { return subSection; }
    @javax.jdo.annotations.Column(allowsNull="true")
    public void setSubSection(SubSection subSection) { this.subSection= subSection; }
    // End   Regulation to RegulationRule




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
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<SubSectionTextItem> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final SubSectionTextItem source,
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
                final SubSectionTextItem source,
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
        return ObjectContracts.toString(this, "itemNo, plainRegulationText, ownedBy");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final SubSectionTextItem other) {
        return ObjectContracts.compare(this, other, "itemNo, plainRegulationText, ownedBy");
    }
    //endregion

    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private TextItems textItems;


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
