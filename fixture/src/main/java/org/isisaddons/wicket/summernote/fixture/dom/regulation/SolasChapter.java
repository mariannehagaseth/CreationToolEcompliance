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
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.MySKOSConcept;
import org.joda.time.LocalDate;

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
@javax.jdo.annotations.Uniques({
    @javax.jdo.annotations.Unique(
            name="Regulation_description_must_be_unique", 
            members={"chapterAnnex","solasChapterNumber","solasPartNumber","solasRegulationNumber"})
})
@javax.jdo.annotations.Queries( {

        @javax.jdo.annotations.Query(
                name = "findByOwnedBy", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.SolasChapter "
                        + "WHERE ownedBy == :ownedBy")
        ,
        @javax.jdo.annotations.Query(
                name = "findChaptersAnnexes", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.SolasChapter "
                        + "WHERE chapterAnnex == :chapterAnnex")
        ,
        @javax.jdo.annotations.Query(
        name = "findCodesBySolasChapter", language = "JDOQL",
        value = "SELECT "
                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.SolasChapter "
                + "WHERE solasChapterNumber== :solasChapterNumber")
        ,
        @javax.jdo.annotations.Query(
                name = "findChapterTitleBySolasChapterNo", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.SolasChapter "
                        + "WHERE solasChapterNumber== :solasChapterNumber")
        ,
    @javax.jdo.annotations.Query(
            name = "findByOwnedByAndCompleteIsFalse", language = "JDOQL",
            value = "SELECT "
                    + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.SolasChapter "
                    + "WHERE ownedBy == :ownedBy "
                    + "   && finalized == false"),
    @javax.jdo.annotations.Query(
            name = "findByOwnedByAndCompleteIsTrue", language = "JDOQL",
            value = "SELECT "
                    + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.SolasChapter "
                    + "WHERE ownedBy == :ownedBy "
                    + "&& finalized == true"),
    @javax.jdo.annotations.Query(
            name = "findByOwnedByAndKpi", language = "JDOQL",
            value = "SELECT "
                    + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.SolasChapter "
                    + "WHERE ownedBy == :ownedBy "
                    + "&& kpi == :kpi"),
  /*
    @javax.jdo.annotations.Query(
            name = "findByOwnedByAndRegulationTitleContains", language = "JDOQL",
            value = "SELECT "
                    + "FROM dom.regulation.SolasChapter "
                    + "WHERE ownedBy == :ownedBy && "
                    + "regulationTitle.indexOf(:regulationTitle) >= 0"),
  */
   @javax.jdo.annotations.Query(
            name = "findByClauseOwnedBy", language = "JDOQL",
            value = "SELECT "
                  + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.Clause "
                  + "WHERE ownedBy == :ownedBy"),
   @javax.jdo.annotations.Query(
                          name = "findByListItem", language = "JDOQL",
                          value = "SELECT "
                                + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.ListItem "
                                + "WHERE ownedBy == :ownedBy"),
        @javax.jdo.annotations.Query(
                name = "findBySolasChapter", language = "JDOQL",
                value = "SELECT " + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.SolasChapter "
                        + "WHERE ownedBy == :ownedBy"),
    @javax.jdo.annotations.Query(
            name = "findByDefinitionItem", language = "JDOQL",
            value = "SELECT "
                  + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.DefinitionItem "
                  + "WHERE ownedBy == :ownedBy")
,
        @javax.jdo.annotations.Query(
                name = "findByFreeText", language = "JDOQL",
                value = "SELECT "
                        + "FROM dom.regulation.FreeText "
                        + "WHERE ownedBy == :ownedBy")
})
@DomainObject(objectType="SOLASCHAPTER",autoCompleteRepository=SolasChapters.class, autoCompleteAction="autoComplete", bounded = true)
 // default unless overridden by autoCompleteNXxx() method
@DomainObjectLayout(bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
		columnSpans={6,0,0,6},
		left={"SOLAS","Regulation Text (Edit)","Annotated Text","Annotation","Definition (Update)","Regulation Tags (Edit)","Search Term (Interpretation)","Select Ship Class (Interpretation)"},
		middle={},
        right={})
public class SolasChapter implements Categorized, Comparable<SolasChapter> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(SolasChapter.class);
      //endregion

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();
        if (getChapterAnnex().equals(ChapterAnnex.CHAPTER)) {buf.append("SOLAS CHAPTER ");}
        if (getChapterAnnex().equals(ChapterAnnex.ANNEX))  {buf.append("SOLAS ANNEX ");}
        if (getChapterAnnex().equals(ChapterAnnex.DIRECTIVE))  {buf.append("EU DIRECTIVE ");}
        buf.append(getSolasChapterNumber());
      //  if (getSolasPartNumber() != "-") {buf.append(" PART "); buf.append(getSolasPartNumber());}
        if (!getSolasPartNumber().equalsIgnoreCase("-")) {
            if ((getChapterAnnex().equals(ChapterAnnex.CHAPTER)) || (getChapterAnnex().equals(ChapterAnnex.ANNEX))) {
                buf.append(" PART ");
            }
            if ((getChapterAnnex().equals(ChapterAnnex.DIRECTIVE))) {
                buf.append(" TITLE ");
            }
        }
        buf.append(getSolasPartNumber());
        if (getChapterAnnex().equals(ChapterAnnex.CHAPTER)) {buf.append(" REGULATION "); }
        if (getChapterAnnex().equals(ChapterAnnex.ANNEX)) {buf.append(" CHAPTER ");}
        if (getChapterAnnex().equals(ChapterAnnex.DIRECTIVE)) {buf.append(" ARTICLE ");}
        buf.append(getSolasRegulationNumber());
        return buf.toString();
    }
    //endregion



    public static enum ChapterAnnex {
        CHAPTER,
        ANNEX,
        DIRECTIVE;
          }

    private ChapterAnnex chapterAnnex;
    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="SOLAS", sequence="29")
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(named = " ")
    public ChapterAnnex getChapterAnnex() {
        return chapterAnnex;
    }

    public void setChapterAnnex(final ChapterAnnex chapterAnnex) {
        this.chapterAnnex = chapterAnnex;
        if (chapterAnnex == ChapterAnnex.CHAPTER) {regulationChapter = "REGULATION";};
        if (chapterAnnex == ChapterAnnex.ANNEX) {regulationChapter = "CHAPTER";};
        if (chapterAnnex == ChapterAnnex.DIRECTIVE) {regulationChapter = "ARTICLE";};
    }


    private String regulationChapter;
    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="SOLAS", sequence="69")
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(named = " ")
    public String getRegulationChapter() {
        return regulationChapter;
    }
    public void setRegulationChapter(final String regulationChapter) {
        if (chapterAnnex == ChapterAnnex.CHAPTER) {this.regulationChapter = "REGULATION";};
        if (chapterAnnex == ChapterAnnex.ANNEX) {this.regulationChapter = "CHAPTER";};
        if (chapterAnnex == ChapterAnnex.DIRECTIVE) {this.regulationChapter = "ARTICLE";};
    }

    // Region solasChapterNumber
    private String solasChapterNumber;
    @javax.jdo.annotations.Column(allowsNull="false", length=10)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="SOLAS", sequence="30")
    @PropertyLayout(typicalLength=5)
    public String getSolasChapterNumber() {
/*
            final List<SolasChapter> existingChapters = container.allMatches(
                    new QueryDefault<SolasChapter>(SolasChapter.class,
                            "findChapterTitleBySolasChapterNo",
                            "solasChapterNumber", solasChapterNumber));
        if (!existingChapters.isEmpty())
        {
             disableSolasChapterNumber();
        } */
        return solasChapterNumber;
    }
    public void setSolasChapterNumber(final String solasChapterNumber) {
        this.solasChapterNumber = solasChapterNumber;
    }
    public void modifySolasChapterNumber(final String solasChapterNumber) {
        setSolasChapterNumber(solasChapterNumber);
    }
    public void clearSolasChapterNumber() {
        setSolasChapterNumber(null);
    }
/*
    public String disableSolasChapterNumber() {
        return null;
    }
*/

    //endregion


    // Region solasChapterTitle
    private String solasChapterTitle;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="SOLAS", sequence="40")
    @PropertyLayout(named = " ",typicalLength=100)
    public String getSolasChapterTitle() {
        return solasChapterTitle;
    }
    public void setSolasChapterTitle(final String solasChapterTitle) {
        this.solasChapterTitle = solasChapterTitle;
    }
    public void modifySolasChapterTitle(final String solasChapterTitle) {
        setSolasChapterTitle(solasChapterTitle);
    }
    public void clearSolasChapterTitle() {
        setSolasChapterTitle(null);
    }
    //endregion

    // Region solasPartNumber
    private String solasPartNumber;
    @javax.jdo.annotations.Column(allowsNull="true", length=10)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="SOLAS", sequence="50")
    @PropertyLayout(typicalLength=5, named = "Part No")
    public String getSolasPartNumber() {
        return solasPartNumber;
    }
    public void setSolasPartNumber(final String solasPartNumber) {
        if (solasPartNumber == null)
            {this.solasPartNumber = "-";}
        else
        {this.solasPartNumber = solasPartNumber;}
    }
    public void modifySolasPartNumber(final String solasPartNumber) {
        setSolasPartNumber(solasPartNumber);
    }
    public void clearSolasPartNumber() {
        setSolasPartNumber(null);
    }
    //endregion


    // Region solasPartTitle
    private String solasPartTitle;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="SOLAS", sequence="60")
    @PropertyLayout(named = "Title", typicalLength=100)
    public String getSolasPartTitle() {
        return solasPartTitle;
    }
    public void setSolasPartTitle(final String solasPartTitle) {
        this.solasPartTitle = solasPartTitle;
    }
    public void modifySolasPartTitle(final String solasPartTitle) {
        setSolasPartTitle(solasPartTitle);
    }
    public void clearSolasPartTitle() {
        setSolasPartTitle(null);
    }
    //endregion

    // Region solasRegulationNumber
    private String solasRegulationNumber;
    @javax.jdo.annotations.Column(allowsNull="true", length=10)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="SOLAS", sequence="70")
    @PropertyLayout(named = " ",typicalLength=5)
    public String getSolasRegulationNumber() {
        return solasRegulationNumber;
    }
    public void setSolasRegulationNumber(final String solasRegulationNumber) {
        this.solasRegulationNumber = solasRegulationNumber;
    }
    public void modifySolasRegulationNumber(final String solasRegulationNumber) {
        setSolasRegulationNumber(solasRegulationNumber);
    }
    public void clearSolasRegulationNumber() {
        setSolasRegulationNumber(null);
    }
    //endregion


    // Region solasRegulationTitle
    private String solasRegulationTitle;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="SOLAS", sequence="80")
    @PropertyLayout(named = "Title",typicalLength=100)
    public String getSolasRegulationTitle() {
        return solasRegulationTitle;
    }
    public void setSolasRegulationTitle(final String solasRegulationTitle) {
        this.solasRegulationTitle = solasRegulationTitle;
    }
    public void modifySolasRegulationTitle(final String solasRegulationTitle) {
        setSolasPartTitle(solasRegulationTitle);
    }
    public void clearSolasRegulationTitle() {
        setSolasRegulationTitle(null);
    }
    //endregion

    // Region solasRegulationTitle
    private String solasRegulationIntroText;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="SOLAS", sequence="90")
    @PropertyLayout(named = "Intro Text (Applicability)", typicalLength=1000, multiLine = 5)
    public String getSolasRegulationIntroText() {
        return solasRegulationIntroText;
    }
    public void setSolasRegulationIntroText(final String solasRegulationIntroText) {
        this.solasRegulationIntroText = solasRegulationIntroText;
    }
    public void modifySolasRegulationIntroText(final String solasRegulationIntroText) {
        setSolasPartTitle(solasRegulationIntroText);
    }
    public void clearSolasRegulationIntroText() {
        setSolasRegulationIntroText(null);
    }
    //endregion



    // Region semantifiedRegulationText
    private String semantifiedRegulationText;
    @javax.jdo.annotations.Column(allowsNull="true", length=10000)
   // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="SOLAS", sequence="95")
    @PropertyLayout(typicalLength=10000, multiLine=8, named = "Annotated Text")
    @Property(editing = Editing.DISABLED,editingDisabledReason = "Update using action that calls an SPI from the consolidation services")
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
    @ActionLayout(named = "Annotate", position = ActionLayout.Position.PANEL)
    @MemberOrder(name="semantifiedRegulationText",
            sequence="10")
    public SolasChapter updateSemantifiedRegulationText() {
                // Call API to do semantification:
                String AsyncRestTest = restClientTest.SkosFreetextAsync();
        System.out.println("AsyncRestTest = " + AsyncRestTest);
        this.setSemantifiedRegulationText(AsyncRestTest);
                container.flush();
                container.informUser("Annotation completed for " + container.titleOf(this));
                System.out.println("Calling Semantify here!!");
                return this;
     }
     //endregion


    // Region target
    private String term;
    @javax.jdo.annotations.Column(allowsNull="true", length=3000)
    //@Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Annotation", sequence="11")
    @PropertyLayout(typicalLength=3000, multiLine=3)
    public String getTerm() {
        return term;
    }
    public void setTerm(final String term) {
        this.term = term;
    }
    public void modifyTerm(final String term) {
        setTarget(term);
    }
    public void clearTerm() {
        setTerm(null);
    }
    //endregion


    // Region target
    private String target;
    @javax.jdo.annotations.Column(allowsNull="true", length=3000)
    //@Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Annotation", sequence="11")
    @PropertyLayout(typicalLength=3000, multiLine=3)
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
    //endregion

    @PropertyLayout(hidden=Where.EVERYWHERE)
    // Region requirement
    private String requirement;
    @javax.jdo.annotations.Column(allowsNull="true", length=3000)
    //@Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Annotation", sequence="20")
    @PropertyLayout(typicalLength=3000, multiLine=3)
    public String getRequirement() {
        return requirement;
    }
    public void setRequirement(final String requirement) {
        this.requirement = requirement;
    }
    public void modifyRequirement(final String requirement) {
        setRequirement(requirement);
    }
    public void clearRequirement() {
        setRequirement(null);
    }
    //endregion


    @PropertyLayout(hidden=Where.EVERYWHERE)
    // Region context
    private String context;
    @javax.jdo.annotations.Column(allowsNull="true", length=3000)
    //@Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Annotation", sequence="30")
    @PropertyLayout(typicalLength=3000, multiLine=3)
    public String getContext() {
        return context;
    }
    public void setContext(final String context) {
        this.context = context;
    }
    public void modifyContext(final String context) {
        setContext(context);
    }
    public void clearContext() {
        setContext(null);
    }
    //endregion


    // Region exception
    @PropertyLayout(hidden=Where.EVERYWHERE)
    private String exception;
    @javax.jdo.annotations.Column(allowsNull="true", length=3000)
    //@Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Annotation", sequence="40")
    @PropertyLayout(typicalLength=3000, multiLine=3)
    public String getException() {
        return exception;
    }
    public void setException(final String exception) {
        this.exception = exception;
    }
    public void modifyException(final String exception) {
        setException(exception);
    }
    public void clearException() {
        setException(null);
    }
    //endregion



    // Region definitionName
    private String definitionName;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    //@Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Definition (Update)", sequence="10")
    @PropertyLayout(typicalLength=100)
    public String getDefinitionName() {
        return definitionName;
    }
    public void setDefinitionName(final String definitionName) {
        this.definitionName = definitionName;
    }
    public void modifyDefinitionName(final String definitionName) {
        setDefinitionName(definitionName);
    }
    public void clearDefinitionName() {
        setDefinitionName(null);
    }
    //endregion


    // Region definitionDescription
    private String definitionDescription;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    //@Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Definition (Update)", sequence="20")
    @PropertyLayout(typicalLength=300,multiLine=3)
    public String getDefinitionDescription() {
        return definitionDescription;
    }
    public void setDefinitionDescription(final String definitionDescription) {
        this.definitionDescription= definitionDescription;
    }
    public void modifyDefinitionDescription(final String definitionDescription) {
        setDefinitionDescription(definitionDescription);
    }
    public void clearDefinitionDescription() {
        setDefinitionDescription(null);
    }
    //endregion


/*
    // Region regulationTitle
    private String regulationTitle;
    @javax.jdo.annotations.Column(allowsNull="true", length=255)
    @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Regulation Tags (Edit)", sequence="10")
    @PropertyLayout(typicalLength=80)
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
*/
    //region > regulationType (property)
    public static enum RegulationType {
        Certificate,
        Procedure,
        Checklist,
        TechnicalSpecification,
        OperationalSpecification,
        FunctionalRequirement,
        GoalBasedRegulation,
        Guideline,
        ReportSpecification,
        Template,
        Other;
    }
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private RegulationType regulationType;
    @javax.jdo.annotations.Column(allowsNull="true")
    //@Property(editing= Editing.DISABLED,editingDisabledReason="Use action to update regulationType")
    @MemberOrder(name="Regulation Tags (Edit)", sequence="20")
    public RegulationType getRegulationType() {
        return regulationType;
    }
    public void setRegulationType(final RegulationType regulationType) {
        this.regulationType = regulationType;
    }
    //endregion



//region > kpi (property)
    	   public static enum KPI {
    		   ManagementLeadershipAndAccountability,
    		   RecruitmentAndManagementOfShoreBasedPersonnel,
    		   RecruitmentAndManagementOfShipPersonnel,
    		   ReliabilityAndMaintenanceStandards,
    		   NavigationalSafety,
    		   CargoAndBallastOperations,
    		   ManagementOfChange,
    		   IncidentInvestigationAndAnalysis,
    		   SafetyManagementShoreBasedMonitoring,
    		   EnvironmentalManagement,
    		   EmergencyPreparednessAndContingencyPlanning,
    		   MeasurementAnalysisAndImprovement;
    }
 @javax.jdo.annotations.Persistent(defaultFetchGroup="true")  
    private KPI kpi;
    @javax.jdo.annotations.Column(allowsNull="true")
  //   @Property(editing= Editing.DISABLED,editingDisabledReason="Use action to update kpi")
    /*The @Disabled annotation means that the member cannot be used in any instance of the class. When applied to the property it means that the user may not modify the value of that property (though it may still be modified programmatically). When applied to an action method, it means that the user cannot invoke that method.*/
    @MemberOrder(name="Regulation Tags (Edit)", sequence="30")
    public KPI getKpi() {
        return kpi;
    }
    public void setKpi(final KPI kpi) {
        this.kpi = kpi;
    }
    //endregion


    @PropertyLayout(hidden=Where.EVERYWHERE)
    // Region regulationAND
    private boolean regulationAND;
    @javax.jdo.annotations.Column(allowsNull="true")
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    @MemberOrder(name="Regulation Tags (Edit)", sequence="40")
    public boolean getRegulationAND() {
        return regulationAND;
    }
    public void setRegulationAND(final boolean regulationAND) {
        this.regulationAND = regulationAND;
    }
    //end region




    // region Invalidated (property)
    private boolean invalidated;
    @javax.jdo.annotations.Column(allowsNull="true")
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    @MemberOrder(name="Regulation Tags (Edit)", sequence="50")
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
       @MemberOrder(name="Regulation Tags (Edit)", sequence="60")
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

 /***
  *  // region > ParentRegulation
    private SolasChapter parentRegulation;
    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    @javax.jdo.annotations.Column(allowsNull="true")
    // @Mandatory
    @MemberOrder(name="Regulation Tags (Edit)",sequence = "70")
   // @Property(editing=Editing.DISABLED)
    public SolasChapter getParentRegulation()
    { return parentRegulation; }
    public void setParentRegulation(final SolasChapter parentRegulation)
    { this.parentRegulation = parentRegulation; }
    public List<SolasChapter> choicesParentRegulation()
    { List<SolasChapter> list = container.allInstances(SolasChapter.class);
        list.remove(this);
     return list;
    }
// end region ParentRegulation.

**/


    // Region searchTerm
    private String searchTerm;
    @javax.jdo.annotations.Column(allowsNull="true", length=1000)
    @MemberOrder(name="Search Term (Interpretation)", sequence="10")
    @PropertyLayout(typicalLength=100)
    public String getSearchTerm() {
        return searchTerm;
    }
    public void setSearchTerm(final String searchTerm) {
        this.searchTerm = searchTerm;
    }
    public void modifySearchTerm(final String searchTerm) {
        setSearchTerm(searchTerm);
    }
    public void clearSearchTerm() {
        setSearchTerm(null);
    }
    @MemberOrder(name="searchTerm", sequence="11")
    @Action(semantics = SemanticsOf.IDEMPOTENT)
    public SolasChapter searchTerm() {
        String restTest = restClientTest.SkosFreetext();
        this.setDefinitionTerm(restTest);
        container.flush();
        container.informUser("Search Term finished for " + container.titleOf(this));
        return this;
    }
    //endregion



    // Region broaderTerm
    @PropertyLayout(hidden=Where.EVERYWHERE)
    private String broaderTerm;
    @javax.jdo.annotations.Column(allowsNull="true", length=1000)
    @MemberOrder(name="Search Term (Interpretation)", sequence="20")
    @PropertyLayout(typicalLength=100)
    @Property(editing=Editing.DISABLED)
    public String getBroaderTerm() {
        return broaderTerm;}
    public void setBroaderTerm(final String broaderTerm) {
        this.broaderTerm = broaderTerm;}
    public void modifyBroaderTerm(final String broaderTerm) {
        setBroaderTerm(broaderTerm);}
    public void clearBroaderTerm() {
        setBroaderTerm(null);}
    //endregion


    // Region preferredTerm
    private String preferredTerm;
    @javax.jdo.annotations.Column(allowsNull="true", length=1000)
    @MemberOrder(name="Search Term (Interpretation)", sequence="30")
    @PropertyLayout(typicalLength=100)
    @Property(editing=Editing.DISABLED)
    public String getPreferredTerm() {
        return preferredTerm;}
    public void setPreferredTerm(final String preferredTerm) {
        this.preferredTerm = preferredTerm;}
    public void modifyPreferredTerm(final String preferredTerm) {
        setPreferredTerm(preferredTerm);}
    public void clearPreferredTerm() {
        setPreferredTerm(null);}
    //endregion



    // Region definitionTerm
    private String definitionTerm;
    @javax.jdo.annotations.Column(allowsNull="true", length=1000)
    @MemberOrder(name="Search Term (Interpretation)", sequence="40")
    @PropertyLayout(typicalLength=100)
    @Property(editing=Editing.DISABLED)
    public String getDefinitionTerm() {
        return definitionTerm;}
    public void setDefinitionTerm(final String definitionTerm) {
        this.definitionTerm = definitionTerm;}
    public void modifyDefinitionTerm(final String definitionTerm) {
        setDefinitionTerm(definitionTerm);}
    public void clearDefinitionTerm() {
        setDefinitionTerm(null);}
    //endregion



    // Region searchShipTerm
    @PropertyLayout(hidden=Where.EVERYWHERE)
    private String searchShipClass;
    @javax.jdo.annotations.Column(allowsNull="true", length=1000)
    @MemberOrder(name="Select Ship Class (Interpretation)", sequence="10")
    @PropertyLayout(typicalLength=100, named = "Ship Class")
    //@Property(editing=Editing.DISABLED)
    public String getSearchShipClass() {
        return searchShipClass;}
    public void setSearchShipClass(final String searchShipClass) {
        this.searchShipClass = searchShipClass;}
    public void modifySearchShipClass(final String searchShipClass) {
        setSearchShipClass(searchShipClass);}
    public void clearSearchShipClass() {
        setSearchShipClass(null);}
    // IDEMPOTENT correct?
    @MemberOrder(name="searchShipClass", sequence="20")
    //@javax.jdo.annotations.Column(allowsNull="true")
    @Action(semantics = SemanticsOf.IDEMPOTENT)
   // public ShipType searchShipClass() {
    public SolasChapter searchShipClass() {
        // Must change to shipType:  ShipType restTest = restClientTest.fetchSKOSconcept();
        System.out.println("restTestSKOSconcept = restClientTest.fetchSKOSconcept();");
        MySKOSConcept restTestSKOSconcept = restClientTest.fetchSKOSconcept();
        // Nå returnerer bare null!!
        System.out.println("stringShip");
        // Må returnere en liste av mulige ship classes her!!
      //  SKOSConceptProperty skosConProp = restTestSKOSconcept.getSkosConceptProperty();
       // System.out.println("skosConProp = " + skosConProp);
       // String stringShip = skosConProp.toString();
        //System.out.println("stringShip = " + stringShip);
        //System.out.println("this.setListOfShipClasses(stringShip);");
        //this.setListOfShipClasses(stringShip);

        System.out.println("flush...");
        container.flush();
        container.informUser("Search Ship Class finished for " + container.titleOf(this));
     //   return restTestSKOSconcept.getText(); // Denne må endres til å hente ship class-list!!
        this.setListOfShipClasses("ListOfShipClasses");
        return this;
    }
    //endregion

// region select ship class
    @MemberOrder(name="listOfShipClasses", sequence="30")
    //@javax.jdo.annotations.Column(allowsNull="true")
    @Action(semantics = SemanticsOf.IDEMPOTENT)
    // public ShipType searchShipClass() {
    public SolasChapter selectShipClass() {
        // Must change to shipType:  ShipType restTest = restClientTest.fetchSKOSconcept();
        // Must change this!Must add a link to an instance containing the position in the string, the listed name and the ship class.
        this.setSelectedShipClass("Selected ship class");
        return this;
        }
    // end region select ship class



/**
    // Region listOfShipClasses
    private ShipType listOfShipClasses;
    @javax.jdo.annotations.Column(allowsNull="true", length=1000)
    @MemberOrder(name="Select Ship Class (Interpretation)", sequence="30")
    @PropertyLayout(typicalLength=100)
    // @Property(editing=Editing.DISABLED)
    public ShipType getListOfShipClasses() {
        return listOfShipClasses;}
    public void setListOfShipClasses(final ShipType listOfShipClasses) {
        this.listOfShipClasses = listOfShipClasses;}
    public void modifyListOfShipClasses(final ShipType listOfShipClasses) {
        setListOfShipClasses(listOfShipClasses);}
    public void clearListOfShipClasses() {
        setListOfShipClasses(null);}
    //endregion
**/
    // Region listOfShipClasses: Must be updated to list of ship classes.
    private String listOfShipClasses;
    @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder(name="Select Ship Class (Interpretation)", sequence="40")
    @PropertyLayout(typicalLength=100)
    // @Property(editing=Editing.DISABLED)
    public String getListOfShipClasses() {
        return listOfShipClasses;}
    public void setListOfShipClasses(final String listOfShipClasses) {
        this.listOfShipClasses = listOfShipClasses;}
    public void modifyListOfShipClasses(final String listOfShipClasses) {
        setListOfShipClasses(listOfShipClasses);}
    public void clearListOfShipClasses() {
        setListOfShipClasses(null);}
    //endregion

    // Region searchShipTerm
    private String selectedShipClass;
    @javax.jdo.annotations.Column(allowsNull="true", length=1000)
    @MemberOrder(name="Select Ship Class (Interpretation)", sequence="50")
    @PropertyLayout(typicalLength=100, named = "Selected Ship Class")
    //@Property(editing=Editing.DISABLED)
    public String getSelectedShipClass() {
        return searchShipClass;}
    public void setSelectedShipClass(final String selectedShipClass) {
        this.selectedShipClass = selectedShipClass;}
// end region


/**
    //region > someTextObjects (collection)
    public java.util.List<TextObject> getSomeTextObjects() {
        return textObjects.listAll();
    }
    //endregion
    **/


@javax.jdo.annotations.Persistent(mappedBy="solasChapter")
@javax.jdo.annotations.Join // Make a separate join table.
private SortedSet<FreeText> freeTexts = new TreeSet<FreeText>();
//    @MemberOrder(name="Sections", sequence = "82")

     //   @PropertyLayout(named="FreeTextSection")
    @SuppressWarnings("deprecation")
    //@CollectionInteraction
  //  @Collection(domainEvent=SolasChapter .FreeTexts.class)
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
        @Override
        public int compare(FreeText p, FreeText q) {
            Ordering<FreeText> bySectionNo = new Ordering<FreeText>() {
                public int compare(final FreeText p, final FreeText q) {
                    return Ordering.natural().nullsFirst().compare(p.getSectionNo(),q.getSectionNo());
                }
            };
            return bySectionNo
                    .compound(Ordering.<FreeText>natural())
                    .compare(p, q);
        }
    }



    //This is the add-Button!!!

    /* do not need the add button...
     public java.util.List<FreeText> autoComplete0AddFreeText(final String search) {
        final java.util.List<FreeText> list = container.allMatches(new QueryDefault<FreeText>(FreeText.class,"findByOwnedBy", "ownedBy", "ecompliance"));
        list.removeAll(getFreeTexts());//Remove those FreeTexts already linked to the SOLASchapter
        return list;
    }
    @Action()
    @ActionLayout(named = "Add")
    @MemberOrder(name = "freeTexts", sequence = "13")
    public SolasChapter addFreeText(final FreeText freeText) {
         wrapperFactory.wrapSkipRules(freeText).setSolasChapter(this);
        getFreeTexts().add(freeText);
        return this;
    }
*/
    @Action()
    @ActionLayout(named = "Add New Section")
    @MemberOrder(name = "freeTexts", sequence = "15")
    public SolasChapter addNewFreeText(final @ParameterLayout(typicalLength=10,named = "Section No") String sectionNo,
                                       final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength=1000, multiLine=8,named = "Regulation Text") String plainRegulationText
                                        //,final SolasChapter solasChapter
                                        )
    {
       // getFreeTexts().add(freeText);
//        getFreeTexts().add(newFreeTextCall.newFreeText(sectionNo, plainRegulationText, solasChapter));
        getFreeTexts().add(newFreeTextCall.newFreeText(sectionNo, plainRegulationText));
        /* addParticipant(participantsRepo.newParticipant(firstname, surname, dob));*/
        return this;
    }
    /*from here

    @Action()
    @ActionLayout(named = "Add")
    @MemberOrder(name = "participations", sequence = "1")
    public Activity addParticipant(final Participant participant) {
        if (findParticipation(participant) == null) {
            participantsRepo.createParticipation(this, participant);
        } else {
            container.informUser("A Participant (" +
                    participant.getFullName() + ") is already participating in this Activity");
        }
        return this;
    }

    @Action()
    @ActionLayout(named = "Add New")
    @MemberOrder(name = "participations", sequence = "2")
    public Activity addNewParticipant(final @ParameterLayout(named = "First
            name") String firstname, final @ParameterLayout(named = "Surname") String surname,
            final @ParameterLayout(named = "Date of Birth") LocalDate dob) {
        addParticipant(participantsRepo.newParticipant(firstname, surname, dob));
        return this;
    }
 to here */


    //This is the Remove-Button!!
    @MemberOrder(name="freeTexts", sequence = "20")
    @ActionLayout(named = "Delete Section")
    public SolasChapter removeFreeText(final @ParameterLayout(typicalLength=30) FreeText freeText) {
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
    //endregion region Link Regulation --> to (several) Rules

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

    //region Fetch all the annexes (codes) relevant for this SOLAS chapter. May be only one.
    // Only fetching is done here, no updates.
    // Do not need to store the !result
    //region > dependencies (property), add (action), remove (action)
   /**
    private Set<SolasCode> solasCodes = new TreeSet<>();
    @Collection()
    @CollectionLayout(  render = RenderType.EAGERLY) // not compatible with neo4j (as of DN v3.2.3)
    public Set<SolasCode> getSolasCodes() {return solasCodes;}
    public void setSolasCodes(Set<SolasCode> solasCodes) {this.solasCodes=solasCodes;}
    @MemberOrder(name="SolasCodes", sequence="20")
    public SolasChapter FetchSolasCodes(){
        setSolasCodes(restClientTest.findSolasCodes(this));
        return this;
    }
    //endregion similar Regulations found by semantic-ontological search.
**/


    //region > events
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<SolasChapter> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final SolasChapter source,
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
                final SolasChapter source,
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
        return ObjectContracts.toString(this, "solasChapterNumber,solasRegulationNumber, ownedBy");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final SolasChapter other) {
        return ObjectContracts.compare(this, other, "solasChapterNumber,solasRegulationNumber, ownedBy");
    }
    //endregion

    //region > injected services

  @javax.inject.Inject
    private FreeTexts newFreeTextCall;

    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private SolasChapters solasChapters;

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

