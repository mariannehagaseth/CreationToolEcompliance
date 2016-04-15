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
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.FragmentSKOSConceptOccurrences;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.ShipClass;
import org.isisaddons.wicket.summernote.fixture.dom.regulation.Chapter.ChapterAnnex;
import org.joda.time.LocalDate;

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
@javax.jdo.annotations.Uniques({
    @javax.jdo.annotations.Unique(
            name="Part_must_be_unique",
            members={"chapterLabel","chapterNumber","partNumber"})
})
@javax.jdo.annotations.Queries( {

        @javax.jdo.annotations.Query(
                name = "findPartsByChapter", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Part "
                        + "WHERE ownedBy == :ownedBy "
                        + "&& chapterAnnexArticle == :chapterAnnexArticle "
                        + "&& chapterNumber == :chapterNumber")
,
        @javax.jdo.annotations.Query(
                name = "findParts", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Part "
                        + "WHERE chapterAnnexArticle == :chapterAnnexArticle "
                        )

})
@DomainObject(objectType="PART",autoCompleteRepository=Parts.class, autoCompleteAction="autoComplete", bounded = true)
 // default unless overridden by autoCompleteNXxx() method
@DomainObjectLayout(bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
		columnSpans={6,0,0,6},
		left={"Part", "Annotation"},
		middle={},
        right={})
public class Part implements Categorized, Comparable<Part> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Part.class);
      //endregion

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();
        if (getChapterAnnexArticle().equals(ChapterAnnex.CHAPTER)) {buf.append("SOLAS CHAPTER ");}
        if (getChapterAnnexArticle().equals(ChapterAnnex.ANNEX))  {buf.append("SOLAS ANNEX ");}
        if (getChapterAnnexArticle().equals(ChapterAnnex.DIRECTIVE))  {buf.append("EU DIRECTIVE ");}
        buf.append(getChapterNumber());

        if (!getPartNumber().equalsIgnoreCase("-")) {
            if ((getChapterAnnexArticle().equals(ChapterAnnex.CHAPTER)) || (getChapterAnnexArticle().equals(ChapterAnnex.ANNEX))) {
                buf.append(" PART ");
            }
            if ((getChapterAnnexArticle().equals(ChapterAnnex.DIRECTIVE))) {
                buf.append(" TITLE ");
            }
        }
        buf.append(getPartNumber());
        return buf.toString();
    }
    //endregion



    private ChapterAnnex chapterAnnexArticle;
    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="Part", sequence="29")
    @PropertyLayout(hidden=Where.EVERYWHERE)
    @ActionLayout(hidden=Where.EVERYWHERE)
    public ChapterAnnex getChapterAnnexArticle() {
        return chapterAnnexArticle;
    }
    public void setChapterAnnexArticle(final ChapterAnnex chapterAnnexArticle) {
        this.chapterAnnexArticle = chapterAnnexArticle;
        if (chapterAnnexArticle == ChapterAnnex.CHAPTER) {
            chapterLabel = "CHAPTER";
            partLabel = "PART";
            label3 = "REGULATION";
            };
        if (chapterAnnexArticle == ChapterAnnex.ANNEX) {
            chapterLabel= "ANNEX";
            partLabel = "PART";
            label3 = "CHAPTER";};
        if (chapterAnnexArticle == ChapterAnnex.DIRECTIVE) {
            chapterLabel= "DIRECTIVE";
            this.partLabel = "TITLE";
            this.label3 = "ARTICLE";};
    }

    // This is the label of the chapter level: CHAPTER or ANNEX or DIRECTIVE.
    private String chapterLabel;
    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="Part", sequence="10")
    @PropertyLayout(hidden=Where.EVERYWHERE)
    @ActionLayout(hidden=Where.EVERYWHERE)
    @Property(editing = Editing.DISABLED, hidden = Where.EVERYWHERE)
    public String getChapterLabel() {
        return chapterLabel;
    }
    public void setChapterLabel(final String chapterLabel) {
        if (chapterAnnexArticle == ChapterAnnex.CHAPTER) {this.chapterLabel = "REGULATION";};
        if (chapterAnnexArticle == ChapterAnnex.ANNEX) {this.chapterLabel = "CHAPTER";};
        if (chapterAnnexArticle == ChapterAnnex.DIRECTIVE) {this.chapterLabel = "ARTICLE";};
    }

    // Start region Label3 => This is the label of level 3: REGULATION, CHAPTER or ARTICLE.
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private String label3;
    @Property(editing= Editing.DISABLED,editingDisabledReason="Set elsewhere")
    @PropertyLayout(hidden=Where.EVERYWHERE)
    @ActionLayout(hidden=Where.EVERYWHERE)
    @javax.jdo.annotations.Column(allowsNull="true")
    public String getLabel3() {
        return label3;
    }
    public void setLabel3(final String label3) {
        if (chapterAnnexArticle == ChapterAnnex.CHAPTER) {this.label3 = "REGULATION";};
        if (chapterAnnexArticle == ChapterAnnex.ANNEX) {this.label3 = "CHAPTER";};
        if (chapterAnnexArticle == ChapterAnnex.DIRECTIVE) {this.label3 = "ARTICLE";};
    }
    public void clearLabel3() {
        setLabel3(null);
    }
    //endregion

    // Region chapterNumber
    private String chapterNumber;
    @javax.jdo.annotations.Column(allowsNull="false", length=10)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @PropertyLayout(hidden=Where.EVERYWHERE)
    @ActionLayout(hidden=Where.EVERYWHERE)
    public String getChapterNumber() {
        return chapterNumber;
    }
    public void setChapterNumber(final String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }
    public void modifyChapterNumber(final String chapterNumber) {
        setChapterNumber(chapterNumber);
    }
    public void clearChapterNumber() {
        setChapterNumber(null);
    }

    //endregion



    // Start region partLabel: THIS is the label for the PART level: PART or PART or TITLE
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private String partLabel;
    @Property(editing= Editing.DISABLED,editingDisabledReason="Set elsewhere")
    @PropertyLayout(named = " ")
    @MemberOrder(name="Part", sequence="10")
    @javax.jdo.annotations.Column(allowsNull="true")
    public String getPartLabel() {
        return partLabel;
    }
    public void setPartLabel(final String partLabel) {
        this.partLabel =partLabel;
        if (chapterAnnexArticle == ChapterAnnex.CHAPTER) {this.partLabel = "PART";};
        if (chapterAnnexArticle == ChapterAnnex.ANNEX) {this.partLabel = "PART";};
        if (chapterAnnexArticle == ChapterAnnex.DIRECTIVE) {this.partLabel = "TITLE";};
    }
    public void clearPartLabel() {
        setPartLabel(null);
    }
    //endregion

    // Region partNumber
    private String partNumber;
    @javax.jdo.annotations.Column(allowsNull="false", length=10)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Part", sequence="20")
    @PropertyLayout(named = "Number",typicalLength=5)
    public String getPartNumber() {
        return partNumber;
    }
    public void setPartNumber(final String partNumber) {
        this.partNumber = partNumber;
    }
    public void modifyPartNumber(final String partNumber) {
        setPartNumber(partNumber);
    }
    public void clearPartNumber() {
        setPartNumber(null);
    }
    //endregion

     // Region partTitle
    private String partTitle;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Part", sequence="30")
    @PropertyLayout(named = "Title",typicalLength=100)
    public String getPartTitle() {
        return partTitle;
    }
    public void setPartTitle(final String partTitle) {
        this.partTitle = partTitle;
    }
    public void modifyPartTitle(final String partTitle) {
        setPartTitle(partTitle);
    }
    public void clearPartTitle() {
        setPartTitle(null);
    }
    //endregion


    // Region plainRegulationText
    private String plainRegulationText;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Part", sequence="40")
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
    @MemberOrder(name="Annotation", sequence="10")
    @PropertyLayout(typicalLength=10000, multiLine=3, hidden=Where.ALL_TABLES)
    @Property(editing = Editing.DISABLED,editingDisabledReason = "Update using action that calls an API from the consolidation services")
    //@SummernoteEditor(height = 100, maxHeight = 300)
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
    @MemberOrder(name="Annotation", sequence="20")
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
    @MemberOrder(name="Terms", sequence="20")
    public Part CheckTerms() {
        FragmentSKOSConceptOccurrences fragment = restClient.GetSkos(plainRegulationText);
        System.out.println("  fragment OK");
        List<String> annotation = new ArrayList<String>();
        setSkosTerms(creationController.ShowTerms(plainRegulationText, fragment).get(0));
        // summernoteeditor setAnnotatedText(creationController.ShowTerms(plainRegulationText, fragment).get(1));
        setAnnotatedText(plainRegulationText);
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
    @MemberOrder(name="Annotation", sequence="30")
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
    @MemberOrder(name="Terms", sequence="20")
    public Part ShowTarget() {
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
    @MemberOrder(name="Annotation", sequence="35")
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
    @MemberOrder(name="Terms", sequence="30")
    public Part ShowApplicability() {
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


    // region Invalidated (property)
    private boolean invalidated;
    @javax.jdo.annotations.Column(allowsNull="true")
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    @PropertyLayout(hidden=Where.EVERYWHERE)
    @ActionLayout(hidden=Where.EVERYWHERE)
    @MemberOrder(name="Part", sequence="50")
    public boolean getInvalidated() {
        return invalidated;
    }
    public void setInvalidated(final boolean invalidated) {
        this.invalidated = invalidated;
    }
    // end region
  

    
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



    // Add Part to Chapter

    // mapping is done to this property:
    @javax.jdo.annotations.Column(allowsNull="true")
    @Property(editing= Editing.DISABLED,editingDisabledReason="Chapter cannot be updated from here")
    @PropertyLayout(hidden=Where.REFERENCES_PARENT, named = "Parent Link")
    @MemberOrder(name="Part", sequence="90")
    private Chapter chapterLink;
    @javax.jdo.annotations.Column(allowsNull="true")
    public Chapter getChapterLink() { return chapterLink; }
    @javax.jdo.annotations.Column(allowsNull="true")
    public void setChapterLink(Chapter chapterLink) { this.chapterLink = chapterLink; }
    // End   Regulation to RegulationRule



    // Start region amendmentDate
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private LocalDate amendmentDate;
    @Property(editing= Editing.DISABLED,editingDisabledReason="Always set to current date")
    @PropertyLayout(hidden=Where.EVERYWHERE)
    @ActionLayout(hidden=Where.EVERYWHERE)
    @javax.jdo.annotations.Column(allowsNull="true")
    public LocalDate getAmendmentDate() {
        return amendmentDate;
    }
    public void setAmendmentDate(final LocalDate amendmentDate) {this.amendmentDate = amendmentDate;}
    public void clearAmendmentDate() {
        setAmendmentDate(null);
    }
    public String validateAmendmentDate(final LocalDate amendmentDate) {
        if (amendmentDate == null) {
            return null;
        }
        //return regulations.validateAmendmentDate(amendmentDate);
        return null; // do not want to test
    }
    //endregion


	 //region > finalized (property)
	   @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private boolean finalized;
   // @Property(editing= Editing.DISABLED,editingDisabledReason="Finalized is changed elsewhere")
       @javax.jdo.annotations.Column(allowsNull="true")
       @PropertyLayout(hidden=Where.EVERYWHERE)
       @ActionLayout(hidden=Where.EVERYWHERE)
       @MemberOrder(name="Part", sequence="60")
       public boolean getFinalized() {
	    	        return finalized;
	    }
	    public void setFinalized(final boolean finalized) {
	        this.finalized = finalized;
	    }
	   // public boolean isFinalized() {
	     //   return finalized;
	    //}
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



    // START REGION  Part Link => Regulation.
    @javax.jdo.annotations.Persistent(mappedBy="partLink")
    @javax.jdo.annotations.Join // Make a separate join table.
    private SortedSet<Regulation> regulations = new TreeSet<Regulation>();
    @SuppressWarnings("deprecation")
    @CollectionLayout(named = "Regulations" , sortedBy=RegulationsComparator.class,render=RenderType.EAGERLY)
    public SortedSet<Regulation> getRegulations() {
        return regulations;
    }
    public void setRegulations(SortedSet<Regulation> regulation) {
        this.regulations=regulation;
    }
    public void removeFromRegulations(final Regulation regulation) {
        if(regulation == null || !getRegulations().contains(regulation)) return;
        getRegulations().remove(regulation);
    }


    // / overrides the natural ordering
    public static class RegulationsComparator implements Comparator<Regulation> {
        @Override
        public int compare(Regulation p, Regulation q) {
            Ordering<Regulation> byRegulationNumber = new Ordering<Regulation>() {
                public int compare(final Regulation p, final Regulation q) {
                    return Ordering.natural().nullsFirst().compare(p.getRegulationNumber(),q.getRegulationNumber());
                }
            };
            return byRegulationNumber
                    .compound(Ordering.<Regulation>natural())
                    .compare(p, q);
        }
    }

    //This is the add-Button!!!
    @Action()
    @ActionLayout(named = "Add New Regulation")
    @MemberOrder(name = "Regulations", sequence = "15")
    public Part addNewRegulation(final @ParameterLayout(typicalLength=10,named = "Number") String regulationNumber,
                              final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=200,named = "Title") String regulationTitle
    )
    {
        // chapterLabel is the saying whether this part belongs to a SOLAS chapter, a SOLAS annex or a EU directive.
        getRegulations().add(newRegulationCall.newRegulation(getChapterAnnexArticle(), getChapterNumber(), partNumber, partTitle, regulationNumber, regulationTitle, getOwnedBy()));
        return this;
    }


    //This is the Remove-Button!!
    @MemberOrder(name="Regulations", sequence = "20")
    @ActionLayout(named = "Delete Regulation")
    public Part removeRegulation(final @ParameterLayout(typicalLength=30) Regulation regulation) {
        // By wrapping the call, Isis will detect that the collection is modified
        // and it will automatically send a CollectionInteractionEvent to the Event Bus.
        // ToDoItemSubscriptions is a demo subscriber to this event
        wrapperFactory.wrapSkipRules(this).removeFromRegulations(regulation);
        container.removeIfNotAlready(regulation);
        return this;
    }


    // disable action dependent on state of object
    public String disableRemoveRegulation(final Regulation regulation) {
        return getRegulations().isEmpty()? "No Regulations to remove": null;
    }
    // validate the provided argument prior to invoking action
    public String validateRemoveRegulation(final Regulation regulation) {
        if(!getRegulations().contains(regulation)) {
            return "Not a Regulation";
        }
        return null;
    }

    // provide a drop-down
    public java.util.Collection<Regulation> choices0RemoveRegulation() {
        return getRegulations();
    }
//endregion region Link Part => Regulation link


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
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<Part> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final Part source,
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
                final Part source,
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
        return ObjectContracts.toString(this, "chapterLabel, chapterNumber, partNumber,ownedBy");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final Part other) {
        return ObjectContracts.compare(this, other, "chapterLabel, chapterNumber,partNumber, ownedBy");
    }
    //endregion

    //region > injected services


    @javax.inject.Inject
    private DomainObjectContainer container;


    @javax.inject.Inject
    private Regulations newRegulationCall;

    @javax.inject.Inject
    private Parts parts;


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

