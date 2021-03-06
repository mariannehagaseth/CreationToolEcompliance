/*
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
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionInteractionEvent;
import org.apache.isis.applib.services.eventbus.EventBusService;
import org.apache.isis.applib.services.wrapper.WrapperFactory;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.*;
import org.isisaddons.wicket.summernote.fixture.dom.regulation.Chapter.ChapterAnnex;
import org.joda.time.LocalDate;
import org.jvnet.hk2.internal.Collector;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import java.util.*;
import java.util.stream.Collectors;


@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
@javax.jdo.annotations.Uniques({
    @javax.jdo.annotations.Unique(
            name="Regulation_must_be_unique",
            members={"chapterLabel","chapterNumber","partNumber","regulationNumber"})
})
@javax.jdo.annotations.Queries( {

        @javax.jdo.annotations.Query(
                name = "findRegulationsByPart", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Regulation "
                        + "WHERE ownedBy == :ownedBy "
                        + "&& chapterAnnexArticle == :chapterAnnexArticle "
                        + "&& chapterNumber == :chapterNumber"
                        + "&& partNumber == :partNumber")
,
        @javax.jdo.annotations.Query(
                name = "findSimilarReg", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Regulation "
                        + "WHERE (regulationType == :regulationType "
                        + "&& regulationType != null)"
                        + "|| (kpi == :kpi "
                        + "&& kpi != null)")

,        @javax.jdo.annotations.Query(
                name = "findRegulations", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Regulation "
                        + "WHERE chapterAnnexArticle == :chapterAnnexArticle "
                        )
,        @javax.jdo.annotations.Query(
        name = "findRegByRegTitle", language = "JDOQL",
        value = "SELECT "
                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Regulation "
                + "WHERE regulationTitle == :regulationTitle")
        ,        @javax.jdo.annotations.Query(
        name = "findRegByRegulationType", language = "JDOQL",
        value = "SELECT "
                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Regulation "
                + "WHERE regulationType == :regulationType"
                + "&& regulationType != null ")
        ,        @javax.jdo.annotations.Query(
        name = "findRegByApplicabilityDate", language = "JDOQL",
        value = "SELECT "
                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Regulation "
                + "WHERE applicabilityDate == :applicabilityDate"
                + "&& applicabilityDate != null ")
        ,        @javax.jdo.annotations.Query(
        name = "findRegByApplicableIn", language = "JDOQL",
        value = "SELECT "
                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Regulation "
                + "WHERE applicableIn == :applicableIn"
                + "&& applicableIn != null ")
        ,        @javax.jdo.annotations.Query(
        name = "findRegByKpi", language = "JDOQL",
        value = "SELECT "
                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Regulation "
                + "WHERE kpi == :kpi"
                + "&& kpi != null ")
        ,        @javax.jdo.annotations.Query(
        name = "findRegulationByURI", language = "JDOQL",
        value = "SELECT "
                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Regulation "
                + "WHERE documentURI == :documentURI"
                + "&& documentURI != null ")
        ,        @javax.jdo.annotations.Query(
        name = "findAllReg", language = "JDOQL",
        value = "SELECT "
                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Regulation ")
})
@DomainObject(objectType="REGULATION",autoCompleteRepository=Regulations.class, autoCompleteAction="autoComplete", bounded = true)
 // default unless overridden by autoCompleteNXxx() method
@DomainObjectLayout(bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
		columnSpans={6,0,0,6},
		left={"Regulation","Annotation","Regulation Tags","RDF","Referenced Regulations","Search Similar Regulations"},
		middle={},
        right={})
public class Regulation implements Categorized, Comparable<Regulation> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Regulation.class);
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
        if (getChapterAnnexArticle().equals(ChapterAnnex.CHAPTER)) {buf.append(" REGULATION "); }
        if (getChapterAnnexArticle().equals(ChapterAnnex.ANNEX)) {buf.append(" CHAPTER ");}
        if (getChapterAnnexArticle().equals(ChapterAnnex.DIRECTIVE)) {buf.append(" ARTICLE ");}
        buf.append(getRegulationNumber());
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
     @MemberOrder(name="Part", sequence="10")
    @javax.jdo.annotations.Column(allowsNull="true")
    @PropertyLayout(hidden=Where.EVERYWHERE)
    @ActionLayout(hidden=Where.EVERYWHERE)
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

    // Region partNumber
    private String partNumber;
    @javax.jdo.annotations.Column(allowsNull="false", length=10)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Part", sequence="20")
    @PropertyLayout(hidden=Where.EVERYWHERE)
    @ActionLayout(hidden=Where.EVERYWHERE)
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
    @PropertyLayout(hidden=Where.EVERYWHERE)
    @ActionLayout(hidden=Where.EVERYWHERE)
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

    // Start region regulationLabel: THIS is the label for the Regulation level: REGULATION or CHAPTER or ARTICLE
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private String regulationLabel;
    @Property(editing= Editing.DISABLED,editingDisabledReason="Set elsewhere")
    @MemberOrder(name="Regulation", sequence="10")
    @javax.jdo.annotations.Column(allowsNull="true")
    @PropertyLayout(named = " ",typicalLength=5)
    public String getRegulationLabel() {
        return regulationLabel;
    }
    public void setRegulationLabel(final String regulationLabel) {
        this.regulationLabel =regulationLabel;
        if (chapterAnnexArticle == ChapterAnnex.CHAPTER) {this.regulationLabel = "REGULATION";};
        if (chapterAnnexArticle == ChapterAnnex.ANNEX) {this.regulationLabel = "CHAPTER";};
        if (chapterAnnexArticle == ChapterAnnex.DIRECTIVE) {this.regulationLabel = "ARTICLE";};
    }
    public void clearRegulationLabel() {
        setRegulationLabel(null);
    }
    //endregion

    // Region regulationNumber
    private String regulationNumber;
    @javax.jdo.annotations.Column(allowsNull="false", length=10)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Regulation", sequence="20")
    @PropertyLayout(named = "Number",typicalLength=5)
    public String getRegulationNumber() {
        return regulationNumber;
    }
    public void setRegulationNumber(final String regulationNumber) {
        this.regulationNumber = regulationNumber;
    }
    public void modifyRegulationNumber(final String regulationNumber) {
        setRegulationNumber(regulationNumber);
    }
    public void clearRegulationNumber() {
        setRegulationNumber(null);
    }
    //endregion

    // Region regulationTitle
    private String regulationTitle;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Regulation", sequence="30")
    @PropertyLayout(named = "Title",typicalLength=5)
    public String getRegulationTitle() {
        return regulationTitle;
    }
    public void setRegulationTitle(final String regulationTitle) {
        this.regulationTitle = regulationTitle;
    }
    public void modifyRegulationTitle(final String regulationTitle) {
        setRegulationTitle(regulationTitle);
    }
    public void clearRegulationTitle() {
        setRegulationTitle(null);
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






    // Region plainRegulationText
    private String plainRegulationText;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    @MemberOrder(name="Regulation", sequence="80")
    @PropertyLayout(typicalLength=10000, multiLine=4, named = "Regulation Text")
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


    private String annotatedText;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    @MemberOrder(name="Annotation", sequence="5")
    @PropertyLayout(typicalLength=10000, multiLine=3, hidden=Where.ALL_TABLES)
    @Property(editing = Editing.DISABLED,editingDisabledReason = "Update using action that calls an API from the consolidation services")
    // @SummernoteEditor(height = 100, maxHeight = 300)
    //  @SummernoteEditor()
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


    private String skosTerms;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
    @MemberOrder(name="Annotation", sequence="10")
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
    public Regulation CheckTerms() {
        FragmentSKOSConceptOccurrences fragment = restClient.GetSkos(plainRegulationText);
        System.out.println("REGULATION: fragment OK");
        List<String> annotation = new ArrayList<String>();
        setSkosTerms(creationController.ShowTerms(plainRegulationText, fragment).get(0));
       // SummernoteEditor:  setAnnotatedText(creationController.ShowTerms(plainRegulationText, fragment).get(1));
        setAnnotatedText(plainRegulationText);
        System.out.println("REGULATION: skosTerms="+skosTerms);
        System.out.println("REGULATION: annotatedText="+annotatedText);
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
    public Regulation ShowTarget() {
        ShipClass shipClassFound = null;
        // CALLS THE TARGET API
        shipClassFound = restClient.GetTarget(plainRegulationText);
        // shipClassFound = restClient.GetApplicability(plainRegulationText);
        System.out.println("FREETEXT:shipclassfound OK");
        setTarget(creationController.ShowShipClass(plainRegulationText, shipClassFound));
        System.out.println("FREETEXT: target="+target);
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

     @Action()
    //  @ActionLayout(named = "Check Terms", position = ActionLayout.Position.PANEL)
    @ActionLayout(position = ActionLayout.Position.PANEL)
    @MemberOrder(name="Terms", sequence="30")
    public Regulation ShowApplicability() {
        ShipClass shipClassFound = null;
        // CALLS THE APPLICABILITY API
        //    shipClassFound = restClient.GetRule(plainRegulationText);
        shipClassFound = restClient.GetApplicability(plainRegulationText);
        System.out.println("reg:applicability shipclassfound OK");
        setApplicability(creationController.ShowShipClass(plainRegulationText,shipClassFound));
        System.out.println("reg: applicability="+applicability);
        container.flush();
        container.informUser("Fetched Applicability completed for " + container.titleOf(this));
        return this;
    }
// END SHOW applicability

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



    // Add Regulation to Part:
    // mapping is done to this property:
    @javax.jdo.annotations.Column(allowsNull="true")
    @Property(editing= Editing.DISABLED,editingDisabledReason="Part cannot be updated from here")
    @PropertyLayout(hidden=Where.PARENTED_TABLES, named = "Parent Link")
    @MemberOrder(name="Regulation", sequence="90")
    private Part partLink;
    @javax.jdo.annotations.Column(allowsNull="true")
    public Part getPartLink() { return partLink; }
    @javax.jdo.annotations.Column(allowsNull="true")
    public void setPartLink(Part partLink) { this.partLink = partLink; }
    // End   Regulation to RegulationRule


// Begin REGION Link Regulation --> to several Sections (Freetexts)
    @javax.jdo.annotations.Persistent(mappedBy="regulationLink")
    @javax.jdo.annotations.Join // Make a separate join table.
    private SortedSet<FreeText> freeTexts = new TreeSet<FreeText>();
    @SuppressWarnings("deprecation")
    @CollectionLayout(named = "Sections" , sortedBy=FreeTextsComparator.class,render=RenderType.EAGERLY)
    public SortedSet<FreeText> getFreeTexts() {
        return freeTexts;
    }
    public void setFreeTexts(SortedSet<FreeText> freeText) {
        this.freeTexts = freeText;
    }
    public void removeFromFreeTexts(final FreeText freeText) {
        if(freeText == null || !getFreeTexts().contains(freeText)) return;
        getFreeTexts().remove(freeText);
    }


    // / overrides the natural ordering
    public static class FreeTextsComparator implements Comparator<FreeText> {

        int parseInt(String str) {
            try{ return Integer.parseInt(str); } catch(Exception ex) { return -1; }
        }

        @Override
        public int compare(FreeText p, FreeText q) {
            Ordering<FreeText> bySectionNo = new Ordering<FreeText>() {
                public int compare(final FreeText p, final FreeText q) {
                    if ((parseInt(p.getSectionNo()) == -1)||(parseInt(q.getSectionNo()) == -1)) {

                        return Ordering.natural().nullsFirst().compare(p.getSectionNo(), q.getSectionNo());
                    }
                    return Ordering.natural().nullsFirst().compare(parseInt(p.getSectionNo()),parseInt(q.getSectionNo()));
                }
            };
            return bySectionNo
                    .compound(Ordering.<FreeText>natural())
                    .compare(p, q);
        }
    }

    //This is the add-Button!!!

    @Action()
    @ActionLayout(named = "Add New Section")
    @MemberOrder(name = "freeTexts", sequence = "15")
    public Regulation addNewFreeText(final @ParameterLayout(typicalLength=10,named = "Section No") String sectionNo,
                                     final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=100,named = "Title") String sectionTitle,
                                       final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=1000, multiLine=8,named = "Regulation Text") String plainRegulationText
     )
    {
         getFreeTexts().add(newFreeTextCall.newFreeText(sectionNo, sectionTitle, plainRegulationText));
        /* addParticipant(participantsRepo.newParticipant(firstname, surname, dob));*/
        return this;
    }

    //This is the Remove-Button!!
    @MemberOrder(name="freeTexts", sequence = "20")
    @ActionLayout(named = "Delete Section")
    public Regulation removeFreeText(final @ParameterLayout(typicalLength=30) FreeText freeText) {
        // By wrapping the call, Isis will detect that the collection is modified
        // and it will automatically send a CollectionInteractionEvent to the Event Bus.
        // ToDoItemSubscriptions is a demo subscriber to this event
        wrapperFactory.wrapSkipRules(this).removeFromFreeTexts(freeText);
        container.removeIfNotAlready(freeText);
        return this;
    }


    // disable action dependent on state of object
    public String disableRemoveFreeText(final FreeText freeText) {
        return getFreeTexts().isEmpty()? "No Text to remove": null;
    }
    // validate the provided argument prior to invoking action
    public String validateRemoveFreeText(final FreeText freeText) {
        if(!getFreeTexts().contains(freeText)) {
            return "Not a FreeText";
        }
        return null;
    }

    // provide a drop-down
    public java.util.Collection<FreeText> choices0RemoveFreeText() {
        return getFreeTexts();
    }
    //endregion region Link Regulation --> to several Sections (Freetexts)



    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private CreationController.RegulationType regulationType;
    @javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(name="Regulation Tags", sequence="10")
    @PropertyLayout(hidden=Where.ALL_TABLES)
    public CreationController.RegulationType getRegulationType() {
        return regulationType;
    }
    public void setRegulationType(final CreationController.RegulationType regulationType) {
        this.regulationType = regulationType;
    }
    //endregion


    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private CreationController.KPI kpi;
    @javax.jdo.annotations.Column(allowsNull="true")
    //   @Property(editing= Editing.DISABLED,editingDisabledReason="Use action to update kpi")
    /*The @Disabled annotation means that the member cannot be used in any instance of the class. When applied to the property it means that the user may not modify the value of that property (though it may still be modified programmatically). When applied to an action method, it means that the user cannot invoke that method.*/
    @MemberOrder(name="Regulation Tags", sequence="20")
    @PropertyLayout(hidden=Where.ALL_TABLES,named = "KPI")
    public CreationController.KPI getKpi() {
        return kpi;
    }
    public void setKpi(final CreationController.KPI kpi) {
        this.kpi = kpi;
    }
    //endregion

    // Start region applicabilityDate
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    @PropertyLayout(hidden=Where.ALL_TABLES)
    private LocalDate applicabilityDate;
    @javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(name="Regulation Tags", sequence="30")
    public LocalDate getApplicabilityDate() {
        return applicabilityDate;
    }
    public void setApplicabilityDate(final LocalDate applicabilityDate) {
        this.applicabilityDate = applicabilityDate;
    }
    // endregion

    @PropertyLayout(hidden=Where.ALL_TABLES)
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private CreationController.ApplicableInType applicableIn;
    @javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(name="Regulation Tags", sequence="40")
    public CreationController.ApplicableInType getApplicableIn() {
        return applicableIn;
    }
    public void setApplicableIn(final CreationController.ApplicableInType applicableIn) {
        this.applicableIn = applicableIn;
    }
    //endregion


    // region Invalidated (property)
    private boolean mandatory;
    @javax.jdo.annotations.Column(allowsNull="true")
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    @PropertyLayout(hidden=Where.ALL_TABLES)
    @ActionLayout(hidden=Where.ALL_TABLES)
    @MemberOrder(name="Regulation Tags", sequence="49")
    public boolean getMandatory() {
        return mandatory;
    }
    public void setMandatory(final boolean mandatory) {
        this.mandatory = mandatory;
    }
    // end region

    // region Invalidated (property)
    private boolean invalidated;
    @javax.jdo.annotations.Column(allowsNull="true")
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    @PropertyLayout(hidden=Where.ALL_TABLES)
    @ActionLayout(hidden=Where.ALL_TABLES)
    @MemberOrder(name="Regulation Tags", sequence="50")
    public boolean getInvalidated() {
        return invalidated;
    }
    public void setInvalidated(final boolean invalidated) {
        this.invalidated = invalidated;
    }
    // end region


    //region > finalized (property)
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private boolean finalized;
    // @Property(editing= Editing.DISABLED,editingDisabledReason="Finalized is changed elsewhere")
    @javax.jdo.annotations.Column(allowsNull="true")
    @PropertyLayout(hidden=Where.ALL_TABLES)
    @ActionLayout(hidden=Where.ALL_TABLES)
    @MemberOrder(name="Regulation Tags", sequence="60")
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



    //region similar Regulations found by manual meta-data
    // Search Similar Regulations (Interpretation)
    // Do not need to store the search result!!
    // Combined semantic and ontological search!
    //region  dependencies (property), add (action), remove (action)
    private SortedSet<Regulation> similarRegulations = new TreeSet<>();
    @Collection()
    @CollectionLayout(  render = RenderType.EAGERLY,named = "Search Similar Regulations") // not compatible with neo4j (as of DN v3.2.3)
    public SortedSet<Regulation> getSimilarRegulations() {return similarRegulations;}
    public void setSimilarRegulations(SortedSet<Regulation> similarRegulations) {this.similarRegulations=similarRegulations;}

    @MemberOrder(name="Search Similar Regulations", sequence="20")
    public Regulation SearchSimilarRegulations(){
        setSimilarRegulations(null);
        container.flush();

        final List<Regulation> items = container.allMatches(
                new QueryDefault<Regulation>(Regulation.class,
                        "findSimilarReg",
                        "regulationType", regulationType,
                        "kpi", kpi));
        if(items.isEmpty()) {
            container.warnUser("No Similar Regulations found.");
        }

        Iterator it = items.iterator();
        while (it.hasNext()) {
            Regulation thisItem = (Regulation) it.next();
            if (!thisItem.equals(this)) {
                getSimilarRegulations().add(thisItem);
            }
        }
        return this;
    }
    //endregion similar Regulations found by search on manual meta-data

// Set links to related regulations. Done manually by the user.
   //region  dependencies (property), add (action), remove (action)
    private SortedSet<Regulation> referencedRegulations = new TreeSet<>();
    @Collection()
    @CollectionLayout( sortedBy = RefRegComparator.class, render = RenderType.EAGERLY,named = "Referenced Regulations") // not compatible with neo4j (as of DN v3.2.3)
    public SortedSet<Regulation> getReferencedRegulations() {return referencedRegulations;}
    public void setReferencedRegulations(SortedSet<Regulation> referencedRegulations) {this.referencedRegulations=referencedRegulations;}

    public void addToReferencedRegulations(final Regulation reg) {
        getReferencedRegulations().add(reg);
    }

    public void removeFromReferencedRegulations(final Regulation reg) {
        if (reg == null || !getReferencedRegulations().contains(reg)) return;
        getReferencedRegulations().remove(reg);
    }

    // / overrides the natural ordering
    public static class RefRegComparator implements Comparator<Regulation> {
        @Override
        public int compare(Regulation p, Regulation q) {
            Ordering<Regulation> byItemNo = new Ordering<Regulation>() {
                public int compare(final Regulation p, final Regulation q) {
                    return Ordering.natural().nullsFirst().compare(p.getRegulationNumber(), q.getRegulationNumber());
                }
            };
            return byItemNo
                    .compound(Ordering.<Regulation>natural())
                    .compare(p, q);
        }
    }


    //This is the add-Button!!!
    @Action()
    @ActionLayout(named = "Add Reference")
    @MemberOrder(name = "Referenced Regulations", sequence = "10")
    public Regulation add(
            @ParameterLayout(typicalLength = 20)
            final Regulation reg) {
        // By wrapping the call, Isis will detect that the collection is modified
        // and it will automatically send CollectionInteractionEvents to the Event Bus.
        // ToDoItemSubscriptions is a demo subscriber to this event
        wrapperFactory.wrapSkipRules(this).addToReferencedRegulations(reg);
        return this;
    }
public List<Regulation> choices0Add(final Regulation reg){

    List<Regulation> regChoices = container.allInstances(Regulation.class);

       List<Regulation> regChoice = regChoices
            .stream()
            .filter(x -> x != Regulation.this)
            .collect(Collectors.toList());

     //  Have to delete the regulations that are already linked to:
    Iterator it = getReferencedRegulations().iterator();
    while (it.hasNext()) {
        Regulation thisItem = (Regulation) it.next();
        if (regChoice.contains(thisItem)) {
            regChoice.remove(thisItem);
        }
    }

return regChoice;
}

    //This is the Remove-Button!!
    @MemberOrder(name = "Referenced Regulations", sequence = "20")
    @ActionLayout(named = "Delete Reference")
    public Regulation removeReferencedRegulation (final @ParameterLayout(typicalLength = 30) Regulation reg) {
        // By wrapping the call, Isis will detect that the collection is modified
        // and it will automatically send a CollectionInteractionEvent to the Event Bus.
        // ToDoItemSubscriptions is a demo subscriber to this event
        wrapperFactory.wrapSkipRules(this).removeFromReferencedRegulations(reg);
        return this;
    }

    // disable action dependent on state of object
    public String disableRemoveReferencedRegulation(final Regulation reg) {
        return getReferencedRegulations().isEmpty() ? "No references to remove" : null;
    }

    // validate the provided argument prior to invoking action
    public String validateRemoveReferencedRegulation(final Regulation reg) {
        if (!getReferencedRegulations().contains(reg)) {
            return "Not a Regulation";
        }
        return null;
    }

    // provide a drop-down
    public java.util.Collection<Regulation> choices0RemoveReferencedRegulation() {
        return getReferencedRegulations();
    }
    //END REGION Link Referenced regulations.

    //endregion similar Regulations found by semantic-ontological search.


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



    //region > CREATE RDF node for the regulation: (action)
    @Action()
    @ActionLayout(named = "Make Persistent",position = ActionLayout.Position.PANEL)
    @MemberOrder(name="Title", sequence="5")
    public Regulation storeRegulation() {

        System.out.println("Make Persistent_1");
       // Must check if the documentURI has already been stored:

        if (getDocumentURI() == null){
            System.out.println("DocumentURI not fetched for this Regulation");

            // Must call 192.168.33.10:9000/api/rdf/document/component for this chapter to create a RDF Document for this chapter
            DocumentComponentList documentComponentList = new DocumentComponentList();

            if (getId() == null) {documentComponentList.setVersion("0");}
            else {
                documentComponentList.setVersion(getId().toString());
            }
            // Parent is documentURI of the Chapter:
            documentComponentList.setParent(partLink.getDocumentURI());
            documentComponentList.setComponentType(DocumentComponentType.DOCUMENT);

            DocumentComponent documentComponent = new DocumentComponent();

            documentComponent.setTitle(getRegulationLabel()+" "+getRegulationNumber()+" "+getRegulationTitle());
            documentComponent.setShortTitle(getRegulationTitle().replace(" ","_"));
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
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<Regulation> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final Regulation source,
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
                final Regulation source,
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
        return ObjectContracts.toString(this, "chapterLabel, chapterNumber, partNumber,regulationNumber,ownedBy");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final Regulation other) {
        return ObjectContracts.compare(this, other, "chapterLabel, chapterNumber,partNumber,regulationNumber,ownedBy");
    }
    //endregion

    //region > injected services

    @javax.inject.Inject
    private FreeTexts newFreeTextCall;

    @javax.inject.Inject
    private RESTclient restClient;

    @javax.inject.Inject
    private CreationController creationController;


    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private Regulations regulations;

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

