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
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.actinvoc.ActionInvocationContext;
import org.apache.isis.applib.services.eventbus.ActionInteractionEvent;
import org.apache.isis.applib.services.eventbus.EventBusService;
import org.apache.isis.applib.services.wrapper.WrapperFactory;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.*;
import org.joda.time.LocalDate;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import java.math.BigInteger;
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
            name="Chapter_must_be_unique",
            members={"chapterLabel","chapterNumber"})
})
@javax.jdo.annotations.Queries( {

        @javax.jdo.annotations.Query(
                name = "findChaptersAnnexes", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Chapter "
                        + "WHERE chapterAnnexArticle == :chapterAnnexArticle")
,
        @javax.jdo.annotations.Query(
                name = "findChapterNumber", language = "JDOQL",
                value = "SELECT chapterNumber "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Chapter "
                        + "WHERE chapterAnnexArticle == :chapterAnnexArticle")
,
        @javax.jdo.annotations.Query(
                name = "findChapterTitle", language = "JDOQL",
                value = "SELECT chapterTitle "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Chapter "
                        + "WHERE chapterAnnexArticle == :chapterAnnexArticle"
                        + "&& chapterNumber == :chapterNumber")
,
        @javax.jdo.annotations.Query(
                name = "findRootURI", language = "JDOQL",
                value = "SELECT rootRdfNode "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Chapter "
                        + "WHERE chapterAnnexArticle == :chapterAnnexArticle")
})
@DomainObject(objectType="CHAPTER",autoCompleteRepository=Chapters.class, autoCompleteAction="autoComplete", bounded = true)
 // default unless overridden by autoCompleteNXxx() method
@DomainObjectLayout(bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
		columnSpans={6,0,0,6},
		left={"Chapter","Annotation","RDF"},
		middle={},
        right={})
public class Chapter implements Categorized, Comparable<Chapter> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Chapter.class);
      //endregion

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();
        if (getChapterAnnexArticle().equals(ChapterAnnex.CHAPTER)) {buf.append("SOLAS CHAPTER ");}
        if (getChapterAnnexArticle().equals(ChapterAnnex.ANNEX))  {buf.append("SOLAS ANNEX ");}
        if (getChapterAnnexArticle().equals(ChapterAnnex.DIRECTIVE))  {buf.append("EU DIRECTIVE ");}
        buf.append(getChapterNumber());
        return buf.toString();
    }
    //endregion

    public static enum ChapterAnnex {
        CHAPTER,
        ANNEX,
        DIRECTIVE;
          }

    private ChapterAnnex chapterAnnexArticle;
    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="Chapter", sequence="29")
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(named = " ")
    public ChapterAnnex getChapterAnnexArticle() {
        return chapterAnnexArticle;
    }

    public void setChapterAnnexArticle(final ChapterAnnex chapterAnnexArticle) {
        this.chapterAnnexArticle = chapterAnnexArticle;
        // SOLAS CHAPTER:
        if (chapterAnnexArticle == ChapterAnnex.CHAPTER) {
            chapterLabel = "CHAPTER";
            this.partLabel = "PART";
            this.label3 = "REGULATION";
            };
        // SOLAS ANNEX:
        if (chapterAnnexArticle == ChapterAnnex.ANNEX) {
            chapterLabel= "ANNEX";
            this.partLabel = "PART";
            this.label3 = "CHAPTER";
            };
        // EU DIRECTIVE:
        if (chapterAnnexArticle == ChapterAnnex.DIRECTIVE) {
            chapterLabel= "DIRECTIVE";
            this.partLabel = "TITLE";
            this.label3 = "ARTICLE";
        };
    }

    // This is the label of the chapter level: CHAPTER or ANNEX or DIRECTIVE.
    private String chapterLabel;
    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="Chapter", sequence="69")
    @Property(editing = Editing.DISABLED, hidden = Where.EVERYWHERE)
    @PropertyLayout(named = " ")
    public String getChapterLabel() {
        return chapterLabel;
    }
    public void setChapterLabel(final String chapterLabel) {
        if (chapterAnnexArticle == ChapterAnnex.CHAPTER) {this.chapterLabel = "CHAPTER";};
        if (chapterAnnexArticle == ChapterAnnex.ANNEX) {this.chapterLabel = "ANNEX";};
        if (chapterAnnexArticle == ChapterAnnex.DIRECTIVE) {this.chapterLabel = "DIRECTIVE";};
    }

    // Start region partLabel: THIS is the label for the PART level: PART or PART or TITLE
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private String partLabel;
    @Property(editing= Editing.DISABLED,editingDisabledReason="Set elsewhere")
    @PropertyLayout(hidden=Where.EVERYWHERE)
    @ActionLayout(hidden=Where.EVERYWHERE)
    @javax.jdo.annotations.Column(allowsNull="true")
    public String getPartLabel() {
        return partLabel;
    }
    public void setPartLabel(final String partLabel) {
        if (chapterAnnexArticle == ChapterAnnex.CHAPTER) {this.partLabel = "PART";};
        if (chapterAnnexArticle == ChapterAnnex.ANNEX) {this.partLabel = "PART";};
        if (chapterAnnexArticle == ChapterAnnex.DIRECTIVE) {this.partLabel = "TITLE";};
    }
    public void clearPartLabel() {
        setPartLabel(null);
    }
    //endregion

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
    @MemberOrder(name="Chapter", sequence="30")
    @PropertyLayout(named = "Number",typicalLength=5)
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


    // Region ChapterTitle
    private String chapterTitle;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Chapter", sequence="40")
    @PropertyLayout(named = "Title",typicalLength=100)
    public String getChapterTitle() {
        return chapterTitle;
    }
    public void setChapterTitle(final String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }
    public void modifyChapterTitle(final String chapterTitle) {
        setChapterTitle(chapterTitle);
    }
    public void clearChapterTitle() {
        setChapterTitle(null);
    }
    //endregion

    // Region plainRegulationText
    private String plainRegulationText;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Chapter", sequence="50")
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
   // @SummernoteEditor(height = 100, maxHeight = 300)
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
    public Chapter CheckTerms() {
        FragmentSKOSConceptOccurrences fragment = restClient.GetSkos(plainRegulationText);
        System.out.println("  fragment OK");
        List<String> annotation = new ArrayList<String>();
        setSkosTerms(creationController.ShowTerms(plainRegulationText, fragment).get(0));
     //summernoteeditor   setAnnotatedText(creationController.ShowTerms(plainRegulationText, fragment).get(1));
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
     @ActionLayout(position = ActionLayout.Position.PANEL)
    @MemberOrder(name="Terms", sequence="20")
    public Chapter ShowTarget() {
        ShipClass shipClassFound = null;
        // CALLS THE TARGET API
        shipClassFound = restClient.GetTarget(plainRegulationText);
        if (shipClassFound != null) {
            System.out.println(" :shipclassfound OK");
            setTarget(creationController.ShowShipClass(plainRegulationText,shipClassFound));
            System.out.println(" : target="+target);}
            else
        {
            // no ship class found
            System.out.println(" :shipclass NOT FOUND");
            setTarget("Target not found!");

        }
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
    public Chapter ShowApplicability() {
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

// START REGION Chapter=> Part Link
    @javax.jdo.annotations.Persistent(mappedBy="chapterLink")
    @javax.jdo.annotations.Join // Make a separate join table.
    private SortedSet<Part> parts = new TreeSet<Part>();
     @SuppressWarnings("deprecation")
     @CollectionLayout(named = "Parts" , sortedBy=PartsComparator.class,render=RenderType.EAGERLY)
    public SortedSet<Part> getParts() {
        return parts;
    }
    public void setParts(SortedSet<Part> part) {
        this.parts = part;
    }
    public void removeFromParts(final Part part) {
        if(part == null || !getParts().contains(part)) return;
        getParts().remove(part);
    }


    // / overrides the natural ordering
    public static class PartsComparator implements Comparator<Part> {
        @Override
        public int compare(Part p, Part q) {
            Ordering<Part> byPartNumber = new Ordering<Part>() {
                public int compare(final Part p, final Part q) {
                    return Ordering.natural().nullsFirst().compare(p.getPartNumber(),q.getPartNumber());
                }
            };
            return byPartNumber
                    .compound(Ordering.<Part>natural())
                    .compare(p, q);
        }
    }

    //This is the add-Button!!!
    @Action()
    @ActionLayout(named = "Add New Part")
    @MemberOrder(name = "parts", sequence = "15")
    public Chapter addNewPart(final @ParameterLayout(typicalLength=10,named = "Number") String partNumber,
                              final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=200,named = "Title") String partTitle

    )
    {
        // chapterLabel is the saying whether this part belongs to a SOLAS chapter, a SOLAS annex or a EU directive.
        getParts().add(newPartCall.newPart( getChapterAnnexArticle(),getChapterNumber(), partNumber, partTitle, getOwnedBy()));
        return this;
    }


    //This is the Remove-Button!!
    @MemberOrder(name="parts", sequence = "20")
    @ActionLayout(named = "Delete Part")
    public Chapter removePart(final @ParameterLayout(typicalLength=30) Part part) {
        // By wrapping the call, Isis will detect that the collection is modified
        // and it will automatically send a CollectionInteractionEvent to the Event Bus.
        // ToDoItemSubscriptions is a demo subscriber to this event
        if (part.getRegulations().size() == 0){
            System.out.println("Part is REMOVED OK!");
            container.informUser("DELETION completed for " + container.titleOf(this));
            wrapperFactory.wrapSkipRules(this).removeFromParts(part);
            container.removeIfNotAlready(part);
        }
        else {
            System.out.println("Part cannot be removed!");
            container.warnUser("CANNOT DELETE due to existing regulations for " + container.titleOf(this));
        }
        return this;
    }


    // disable action dependent on state of object
    public String disableRemovePart(final Part part) {
        return getParts().isEmpty()? "No Parts to remove": null;
    }
    // validate the provided argument prior to invoking action
    public String validateRemovePart(final Part part) {
        if(!getParts().contains(part)) {
            return "Not a Part";
        }
        return null;
    }

    // provide a drop-down
    public java.util.Collection<Part> choices0RemovePart() {
        return getParts();
    }
//endregion region Link Chapter -> Part


    //region > delete Chapter (action)
     @Action(
            domainEvent = DeletedEvent.class,
            invokeOn = InvokeOn.OBJECT_AND_COLLECTION
    )
     @ActionLayout(position = ActionLayout.Position.PANEL)
     @MemberOrder(name="Title", sequence="10")
    public List<Chapter> delete() {

        // obtain title first, because cannot reference object after deleted
        final String title = container.titleOf(this);

        final List<Chapter> returnList = null;

        if (getParts().size()== 0) {
            System.out.println("Can safely delete");
            container.informUser("DELETION completed for " + container.titleOf(this));
            container.removeIfNotAlready(this);
        }
        else {
            container.warnUser("COULD NOT DELETE beacuse of EXISTING REGULATIONS " + container.titleOf(this));
        }

        return returnList;
    }
    //endregion Chapter => Part link


    //region > rootURI (property)
    //rootURI contains the URI of the SOLAS version, the ANNEX version or the DIRECTIVE version. This the the root node of all chapters for each of this.
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private String rootURI;
    @PropertyLayout(hidden=Where.ALL_TABLES)
    @MemberOrder(name = "RDF", sequence = "10")
    @javax.jdo.annotations.Column(allowsNull="true")
    @Property(editing= Editing.DISABLED,editingDisabledReason="Programmatically updated")
    public String getRootURI() {
        return rootURI;
    }
    @ActionLayout(hidden=Where.EVERYWHERE)
    public void setRootURI(final String rootURI) {
        this.rootURI = rootURI;
    }
    //endregion


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


    //region > CREATE RDF node for the chapter: (action)
    @Action()
    @ActionLayout(named = "Make Persistent",position = ActionLayout.Position.PANEL)
    @MemberOrder(name="Title", sequence="5")
    public Chapter storeChapter() {

        RootNode rootNode;
        rootNode = null;

        // if URI for SOLAS Chapter is null, then have to fetch the URI using 192.168.33.10:9000/api/rdf/document/root
        System.out.println("Make Persistent_1");

        DocumentRoot chapterRoot = null;
        RootNode rootRDF = null;
        System.out.println("Make Persistent_2");

         // Must check if a RootURI has already been stored:
        if (getChapterAnnexArticle().equals(ChapterAnnex.CHAPTER)) {
            System.out.println("Call root =rootNodes.findChapterURI();");
            rootNode =rootNodes.findChapterURI();
        }
        if (getChapterAnnexArticle().equals(ChapterAnnex.ANNEX)) {
            rootNode =rootNodes.findAnnexURI();
        }
        if (getChapterAnnexArticle().equals(ChapterAnnex.DIRECTIVE)) {
            rootNode =rootNodes.findDirectiveURI();
        }

        if (rootNode == null){
            System.out.println("No root URI for "+getChapterLabel()+" is found");
            System.out.println("rootURI not stored for this chapter");
            // Must fetch a URI for the SOLAS-chapters node./SOLAS annexes root node, /EU directive root nodes
            chapterRoot = new DocumentRoot();
            chapterRoot.setTitle(getChapterAnnexArticle().toString()); // SOLAS or ANNEX or DIRECTIVE
            chapterRoot.setShortTitle(getChapterAnnexArticle().toString()); // SOLAS or ANNEX or DIRECTIVE
            chapterRoot.setAddConcept(true);
            if (getId() == null) {chapterRoot.setVersion(BigInteger.ZERO);}
            else {
                chapterRoot.setVersion(BigInteger.valueOf(getId()));
            }
            chapterRoot.setRealises("http://e-compliance-project.eu/riga-test/OWLNamedIndividual_c53f118e_35fb_4fcf_8b52_4512a4e0db361");
            chapterRoot.setText(getPlainRegulationText());

            String rootURIfound = restClient.CreateRdfRootNode(chapterRoot);
            setRootURI(rootURIfound);
            System.out.println("rootRdfNode= "+getRootURI()+".");

            if (getRootURI().length()>0) {
                rootRDF = rootNodes.newRootNode(getChapterAnnexArticle().toString(), getRootURI()); // store the new URI for later use by other CHAPTERs.
            }
        }
        else
        {
            // rootNode is not null, that is, already found.
            System.out.println("rootURI is already fetched by another Chapter and is = "+rootURI+".");
            setRootURI(rootNode.getRootURI());
        }

        // Must call 192.168.33.10:9000/api/rdf/document/component for this chapter to create a RDF Document for this chapter
        DocumentComponentList documentComponentList = new DocumentComponentList();

        if (getId() == null) {documentComponentList.setVersion("0");}
        else {
            documentComponentList.setVersion(getId().toString());
        }
        documentComponentList.setParent(getRootURI());
        documentComponentList.setComponentType(DocumentComponentType.DOCUMENT);

        DocumentComponent documentComponent = new DocumentComponent();

        documentComponent.setTitle(getChapterAnnexArticle()+" "+getChapterNumber()+" "+getChapterTitle());
        documentComponent.setShortTitle(getChapterAnnexArticle()+"_"+getChapterNumber());
        documentComponent.setText(getPlainRegulationText());
        documentComponentList.getComponent().add(documentComponent);

        // Need to call the Component API to create the RDF node for the chapter.
        // Will store the value in the documentURI property
        IRIList iriList =restClient.CreateDocumentComponentNode(documentComponentList);
        // has created the RDF node for only one CHAPTER, that is, only one Node, that is, only one IRI-element:
        if (iriList.getIris().size()>0) {
            setDocumentURI(iriList.getIris().get(0));
        }
        else
        {setDocumentURI("Could not find URI for RDF");
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

    //region > object-level validation

    /**
     * In a real app, if this were actually a rule, then we'd expect that
     * invoking the {@link #completed() done} action would clear the {@link #getDueBy() dueBy}
     * property (rather than require the user to have to clear manually).
     */
    //endregion


    //region > events
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<Chapter> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final Chapter source,
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
                final Chapter source,
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
        return ObjectContracts.toString(this, "chapterLabel,chapterNumber, ownedBy");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final Chapter other) {
        return ObjectContracts.compare(this, other, "chapterLabel,chapterNumber, ownedBy");
    }
    //endregion

    //region > injected services

    @javax.inject.Inject
    private Parts newPartCall;

    @javax.inject.Inject
    private Chapters chapters;

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

   // @javax.inject.Inject
   // private RootNode rootNode;

 @javax.inject.Inject
 private RootNodes rootNodes;

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

