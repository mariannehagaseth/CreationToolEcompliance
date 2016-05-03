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
import org.isisaddons.wicket.summernote.fixture.dom.regulation.Chapter.ChapterAnnex;

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
		columnSpans={6,0,0,6},
		left={"Section", "Annotation","List","List of Items","SubSections","Ship Classes (Applicability)","SKOS Terms (Select)", "RDF"},
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
        if (getRegulationLink().getChapterAnnexArticle().equals(ChapterAnnex.CHAPTER)) {
            buf.append("SOLAS CHAPTER ");
        }
        if (getRegulationLink().getChapterAnnexArticle().equals(ChapterAnnex.ANNEX)) {
            buf.append("SOLAS ANNEX ");
        }
        if (getRegulationLink().getChapterAnnexArticle().equals(ChapterAnnex.DIRECTIVE)) {
            buf.append("EU DIRECTIVE ");
        }
        buf.append(getRegulationLink().getChapterNumber());
        if (!getRegulationLink().getPartNumber().equalsIgnoreCase("-")) {
            if ((getRegulationLink().getChapterAnnexArticle().equals(ChapterAnnex.CHAPTER)) || (getRegulationLink().getChapterAnnexArticle().equals(ChapterAnnex.ANNEX))) {
                buf.append(" PART ");
            }
            if ((getRegulationLink().getChapterAnnexArticle().equals(ChapterAnnex.DIRECTIVE))) {
                buf.append(" TITLE ");
            }
        }
        buf.append(getRegulationLink().getPartNumber());
        if (getRegulationLink().getChapterAnnexArticle().equals(ChapterAnnex.CHAPTER)) {
            buf.append(" REGULATION ");
        }
        if (getRegulationLink().getChapterAnnexArticle().equals(ChapterAnnex.ANNEX)) {
            buf.append(" CHAPTER ");
        }
        if (getRegulationLink().getChapterAnnexArticle().equals(ChapterAnnex.DIRECTIVE)) {
            buf.append(" ARTICLE ");
        }
        buf.append(getRegulationLink().getRegulationNumber());
        buf.append("SECTION ");
        buf.append(getSectionNo());
        return buf.toString();
    }
    //endregion


    // Region sectionNo
    private String sectionNo;

    @javax.jdo.annotations.Column(allowsNull = "false", length = 10)
    // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name = "Section", sequence = "10")
    @PropertyLayout(typicalLength = 10)
    public String getSectionNo() {
        return sectionNo;
    }

    public void setSectionNo(final String sectionNo) {
        this.sectionNo = sectionNo;
    }

    public void modifySectionNo(final String sectionNo) {
        this.sectionNo = sectionNo;
    }

    public void clearSectionNo() {
        setSectionNo(null);
    }
    //endregion


    // Region plainRegulationText
    private String sectionTitle;
    @javax.jdo.annotations.Column(allowsNull = "true", length = 100)
    @MemberOrder(name = "Section", sequence = "14")
    @PropertyLayout(typicalLength = 100, named = "Title")
    public String getSectionTitle() {
        return sectionTitle;
    }
    public void setSectionTitle(final String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }
    //endregion


    // Region plainRegulationText
    private String plainRegulationText;
    @javax.jdo.annotations.Column(allowsNull = "true", length = 10000)
    @MemberOrder(name = "Section", sequence = "15")
    @PropertyLayout(typicalLength = 10000, multiLine = 4, named = "Text")
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
    public FreeText CheckTerms() {
        FragmentSKOSConceptOccurrences fragment = restClient.GetSkos(plainRegulationText);
        System.out.println("FREETEXT: fragment OK");
        List<String> annotation = new ArrayList<String>();
        setSkosTerms(creationController.ShowTerms(plainRegulationText, fragment).get(0));
//  SummernoteEditor:  setAnnotatedText(creationController.ShowTerms(plainRegulationText, fragment).get(1));
        setAnnotatedText(plainRegulationText);
        System.out.println("FREETEXT: skosTerms=" + skosTerms);
        System.out.println("FREETEXT: annotatedText=" + annotatedText);
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
    public FreeText ShowTarget() {
        ShipClass shipClassFound = null;
        // CALLS THE TARGET API
        shipClassFound = restClient.GetTarget(plainRegulationText);
        // shipClassFound = restClient.GetApplicability(plainRegulationText);
        System.out.println("FREETEXT:shipclassfound OK");
        setTarget(creationController.ShowShipClass(plainRegulationText, shipClassFound));
        System.out.println("FREETEXT: target=" + target);
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
    public FreeText ShowApplicability() {
        ShipClass shipClassFound = null;
        // CALLS THE APPLICABILITY API
        shipClassFound = restClient.GetApplicability(plainRegulationText);
        System.out.println("FREETEXT:applicability shipclassfound OK");
        setApplicability(creationController.ShowShipClass(plainRegulationText, shipClassFound));
        System.out.println("FREETEXT: applicability=" + applicability);


        container.flush();
          container.informUser("Fetched Target Ship Class completed for " + container.titleOf(this));
         return this;

    }
// END SHOW applicability


    //region > ownedBy (property)
    @javax.jdo.annotations.Persistent(defaultFetchGroup = "true")
    private String ownedBy;

    @PropertyLayout(hidden = Where.EVERYWHERE)
    @ActionLayout(hidden = Where.EVERYWHERE)
    @javax.jdo.annotations.Column(allowsNull = "false")
    @Property(editing = Editing.DISABLED, editingDisabledReason = "OwnedBy is static")
    public String getOwnedBy() {
        return ownedBy;
    }

    @ActionLayout(hidden = Where.EVERYWHERE)
    public void setOwnedBy(final String ownedBy) {
        this.ownedBy = ownedBy;
    }
    //endregion



/*
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
*/


    // Add FreeText to REGULATION
    // mapping is done to this property:
    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.DISABLED, editingDisabledReason = "REGULATION cannot be updated from here")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT, named = "Parent Link")
    @MemberOrder(name = "Section", sequence = "50")
    private Regulation regulationLink;

    @javax.jdo.annotations.Column(allowsNull = "true")
    public Regulation getRegulationLink() {
        return regulationLink;
    }

    @javax.jdo.annotations.Column(allowsNull = "true")
    public void setRegulationLink(Regulation regulationLink) {
        this.regulationLink = regulationLink;
    }
    // End   Regulation to RegulationRule


      // Region regulationAND
    private boolean regulationAND;
    @javax.jdo.annotations.Column(allowsNull = "true")
    @javax.jdo.annotations.Persistent(defaultFetchGroup = "true")
    @PropertyLayout(hidden = Where.ALL_TABLES)
    @MemberOrder(name = "List", sequence = "40")
    public boolean getRegulationAND() {
        return regulationAND;
    }
    public void setRegulationAND(final boolean regulationAND) {
        this.regulationAND = regulationAND;
    }
    //end region


    // BEGIN REGION Link to TextItems, a list of text items.
    @javax.jdo.annotations.Persistent(mappedBy = "freeText")
    @javax.jdo.annotations.Join // Make a separate join table.
    private SortedSet<TextItem> textItems = new TreeSet<TextItem>();

    @SuppressWarnings("deprecation")
    @CollectionLayout(named = "List of Items", sortedBy = TextItemComparator.class, render = RenderType.EAGERLY)
   @MemberOrder(name = "List of Items", sequence = "10")
   // @MemberOrder( sequence = "10")
    public SortedSet<TextItem> getTextItems() {
        return textItems;
    }

    public void setTextItems(SortedSet<TextItem> textItem) {
        this.textItems = textItem;
    }

    public void removeFromTextItems(final TextItem item) {
        if (item == null || !getTextItems().contains(item)) return;
        getTextItems().remove(item);
    }

    // / overrides the natural ordering
    public static class TextItemComparator implements Comparator<TextItem> {

        int parseInt(String str) {
            try{ return Integer.parseInt(str); } catch(Exception ex) { return -1; }
        }

        @Override
        public int compare(TextItem p, TextItem q) {
            Ordering<TextItem> byItemNo = new Ordering<TextItem>() {
                public int compare(final TextItem p, final TextItem q) {
                    if ((parseInt(p.getItemNo()) == -1)||(parseInt(q.getItemNo()) == -1)) {

                        return Ordering.natural().nullsFirst().compare(p.getItemNo(), q.getItemNo());
                    }
                    return Ordering.natural().nullsFirst().compare(parseInt(p.getItemNo()),parseInt(q.getItemNo()));

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
    @MemberOrder(name = "List of Items", sequence = "10")
    public FreeText addNewTextItem(final @ParameterLayout(typicalLength = 10, named = "Item No") String itemNo,
                                   final @ParameterLayout(typicalLength = 1000, multiLine = 8, named = "Text") String plainRegulationText
    ) {
        getTextItems().add(newTextItemCall.newTextItem(itemNo, plainRegulationText));
        return this;
    }

    //This is the Remove-Button!!
    @MemberOrder(name = "List of Items", sequence = "20")
    @ActionLayout(named = "Delete Item")
    public FreeText removeTextItem(final @ParameterLayout(typicalLength = 30) TextItem item) {
        // By wrapping the call, Isis will detect that the collection is modified
        // and it will automatically send a CollectionInteractionEvent to the Event Bus.
        // ToDoItemSubscriptions is a demo subscriber to this event
        wrapperFactory.wrapSkipRules(this).removeFromTextItems(item);
        container.removeIfNotAlready(item);
        return this;
    }

    // disable action dependent on state of object
    public String disableRemoveTextItem(final TextItem item) {
        return getTextItems().isEmpty() ? "No Item to remove" : null;
    }

    // validate the provided argument prior to invoking action
    public String validateRemoveTextItem(final TextItem item) {
        if (!getTextItems().contains(item)) {
            return "Not an Item";
        }
        return null;
    }

    // provide a drop-down
    public java.util.Collection<TextItem> choices0RemoveTextItem() {
        return getTextItems();
    }
    //END REGION Link FreeText (Section) --> to (several) TextItems


    // BEGIN REGION Link to SubSections
    @javax.jdo.annotations.Persistent(mappedBy = "freeTextSection")
    @javax.jdo.annotations.Join // Make a separate join table.
    @MemberOrder(name = "SubSections", sequence = "20")
    private SortedSet<SubSection> subSections = new TreeSet<SubSection>();

    @SuppressWarnings("deprecation")
    @CollectionLayout(named = "Sub Sections", sortedBy = SubSectionsComparator.class, render = RenderType.EAGERLY)
 @MemberOrder(name = "SubSections", sequence = "20")
   // @MemberOrder(sequence = "20")
    public SortedSet<SubSection> getSubSections() {
        return subSections;
    }

    public void setSubSections(SortedSet<SubSection> subSection) {
        this.subSections = subSection;
    }

    public void removeFromSubSections(final SubSection subSection) {
        if (subSection == null || !getSubSections().contains(subSection)) return;
        getSubSections().remove(subSection);
    }

    // / overrides the natural ordering
    public static class SubSectionsComparator implements Comparator<SubSection> {

        int parseInt(String str) {
            try{ return Integer.parseInt(str); } catch(Exception ex) { return -1; }
        }

        @Override
        public int compare(SubSection p, SubSection q) {
            Ordering<SubSection> bySubSectionNo = new Ordering<SubSection>() {
                public int compare(final SubSection p, final SubSection q) {
                    if ((parseInt(p.getSubSectionNo()) == -1) || (parseInt(q.getSubSectionNo()) == -1)) {
                        // This is not a number.
                        return Ordering.natural().nullsFirst().compare(p.getSubSectionNo(), q.getSubSectionNo());
                    }
                    return Ordering.natural().nullsFirst().compare(parseInt(p.getSubSectionNo()), parseInt(q.getSubSectionNo()));
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
    public FreeText addNewSubSection(final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(typicalLength = 10, named = "Sub Section No") String subSectionNo,
                                     final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout( typicalLength = 10, named = "Title") String subSectionTitle,
                                     final @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(typicalLength = 1000, multiLine = 8, named = "Regulation Text") String plainRegulationText
    ) {
        getSubSections().add(newSubSectionCall.newSubSection(subSectionNo, subSectionTitle, plainRegulationText));
        return this;
    }

    //This is the Remove-Button!!
    @MemberOrder(name = "subSections", sequence = "20")
    @ActionLayout(named = "Delete SubSection")
    public FreeText removeSubSection(final @ParameterLayout(typicalLength = 30) SubSection subSection) {
        // By wrapping the call, Isis will detect that the collection is modified
        // and it will automatically send a CollectionInteractionEvent to the Event Bus.
        // ToDoItemSubscriptions is a demo subscriber to this event
        wrapperFactory.wrapSkipRules(this).removeFromSubSections(subSection);
        container.removeIfNotAlready(subSection);
        return this;
    }

    // disable action dependent on state of object
    public String disableRemoveSubSection(final SubSection subSection) {
        return getSubSections().isEmpty() ? "No Text to remove" : null;
    }

    // validate the provided argument prior to invoking action
    public String validateRemoveSubSection(final SubSection subSection) {
        if (!getSubSections().contains(subSection)) {
            return "Not a SubSection";
        }
        return null;
    }

    // provide a drop-down
    public java.util.Collection<SubSection> choices0RemoveSubSection() {
        return getSubSections();
    }
    //ENDREGION Link FreeText (Section) --> to (several) SubSections


    // BEGIN REGION Link to ShipClassType, a list of ship classes that this text is applicable to.
    @javax.jdo.annotations.Persistent(mappedBy = "applicableLink")
    @javax.jdo.annotations.Join // Make a separate join table.
    private SortedSet<ShipClassType> shipClasses = new TreeSet<ShipClassType>();

    @SuppressWarnings("deprecation")
  @MemberOrder(name = "Ship Classes (Applicability)", sequence = "90")
   // @MemberOrder(sequence = "90")
    @CollectionLayout(named = "Ship Classes (Applicability)", sortedBy = ShipClassTypeComparator.class, render = RenderType.EAGERLY)
    public SortedSet<ShipClassType> getShipClasses() {
        return shipClasses;
    }

    public void setShipClasses(SortedSet<ShipClassType> shipClass) {
        this.shipClasses = shipClass;
    }

    public void removeFromShipClasses(final ShipClassType shipClass) {
        if (shipClass == null || !getShipClasses().contains(shipClass)) return;
        getShipClasses().remove(shipClass);
    }

    // / overrides the natural ordering
    public static class ShipClassTypeComparator implements Comparator<ShipClassType> {
        @Override
        public int compare(ShipClassType p, ShipClassType q) {
            Ordering<ShipClassType> byShiptype = new Ordering<ShipClassType>() {
                public int compare(final ShipClassType p, final ShipClassType q) {
                    return Ordering.natural().nullsFirst().compare(p.getType(), q.getType());
                }
            };
            return byShiptype
                    .compound(Ordering.<ShipClassType>natural())
                    .compare(p, q);
        }
    }

    // BEGIN fetch Applicability classes.
    @Action()
    @MemberOrder(name = "Ship Classes (Applicability)", sequence = "60")
    @ActionLayout(named = "Fetch")
    public FreeText FetchApplicability() {
          // SHOW the ship class in the collection:
        // CALLS THE TARGET API
        ShipClassType newShipClass = null;
        ShipClassType shipClassFoundList = null;
        // THIS CAN BE CHANGED TO APPLICABILITY, IF NEEDED!!::
        shipClassFoundList = restClient.GetShipClass(plainRegulationText);
        System.out.println("FetchApplicability:applicability shipclassfoundList OK");
        if (!shipClassFoundList.equals(null)){
            newShipClass = creationController.ShowFoundShipClass(plainRegulationText, shipClassFoundList);
            System.out.println("FetchApplicability: applicability=" + applicability);}
        // setShipClasses(null); // Fetch the ship classes each time, so set to null in between/first.

        container.flush();
        if (!newShipClass.equals(null)) {
            if (newShipClass.getType().length() == 0) {
                container.warnUser("NO  TARGET SHIP CLASS FOUND for " + container.titleOf(this));

            } else {
                getShipClasses().add(newShipClass);

                container.informUser("Fetched Target Ship Class completed for " + container.titleOf(this));
            }
        }
        return this;

    }
// END FETCH applicability


    //This is the add-Button!!!
    @Action()
    @MemberOrder(name = "Ship Classes (Applicability)", sequence = "70")
    @ActionLayout(named = "Add")
    public FreeText addNewShipClassType(
            final @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(typicalLength = 100, named = "Name") String type,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Tonnage >") double minTonnageIn,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Tonnage >=") double minTonnageEx,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Tonnage <") double maxTonnageIn,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Tonnage <= ") double maxTonnageEx,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Length >") double minLengthIn,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Length >=") double minLengthEx,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Length <") double maxLengthIn,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Length <=") double maxLengthEx,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Draught >") double minDraughtIn,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Draught >=") double minDraughtEx,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Draught <") double maxDraughtIn,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Draught <=") double maxDraughtEx,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "No of Passengers >") int minPassengersIn,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "No of Passengers >=") int minPassengersEx,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "No of Passengers <") int maxPassengerIn,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "No of Passengers <=") int maxPassengersEx,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Keel Laid Date >") int minKeelLaidIn,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Keel Laid Date >=") int minKeelLaidEx,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Keel Laid Date <") int maxKeelLaidIn,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Keel Laid Date <=") int maxKeelLaidEx,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Length Unit") String lengthUnit,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Tonnage Unit") String tonnageUnit,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(typicalLength = 100, named = "Draught Unit") String draughtUnit
    ) {
        getShipClasses().add(newShipClassTypeCall.newShipClassType(
                        type,
                        minTonnageIn,
                        minTonnageEx,
                        maxTonnageIn,
                        maxTonnageEx,
                        minLengthIn,
                        minLengthEx,
                        maxLengthIn,
                        maxLengthEx,
                        minDraughtIn,
                        minDraughtEx,
                        maxDraughtIn,
                        maxDraughtEx,
                        minPassengersIn,
                        minPassengersEx,
                        maxPassengerIn,
                        maxPassengersEx,
                        minKeelLaidIn,
                        minKeelLaidEx,
                        maxKeelLaidIn,
                        maxKeelLaidEx,
                        lengthUnit,
                        tonnageUnit,
                        draughtUnit
                )
        );
        return this;
    }

    public double default1AddNewShipClassType() {
        return 0;
    }

    public double default2AddNewShipClassType() {
        return 0;
    }

    public double default3AddNewShipClassType() {
        return 0;
    }

    public double default4AddNewShipClassType() {
        return 0;
    }

    public double default5AddNewShipClassType() {
        return 0;
    }

    public double default6AddNewShipClassType() {
        return 0;
    }

    public double default7AddNewShipClassType() {
        return 0;
    }

    public double default8AddNewShipClassType() {
        return 0;
    }

    public double default9AddNewShipClassType() {
        return 0;
    }

    public double default10AddNewShipClassType() {
        return 0;
    }

    public double default11AddNewShipClassType() {
        return 0;
    }

    public double default12AddNewShipClassType() {
        return 0;
    }

    public int default13AddNewShipClassType() {
        return 0;
    }

    public int default14AddNewShipClassType() {
        return 0;
    }

    public int default15AddNewShipClassType() {
        return 0;
    }

    public int default16AddNewShipClassType() {
        return 0;
    }

    public int default17AddNewShipClassType() {
        return 0;
    }

    public int default18AddNewShipClassType() {
        return 0;
    }

    public int default19AddNewShipClassType() {
        return 0;
    }

    public int default20AddNewShipClassType() {
        return 0;
    }

    public String default21AddNewShipClassType() {
        return "M";
    }

    public String default22AddNewShipClassType() {
        return "GT";
    }

    public String default23AddNewShipClassType() {
        return "M";
    }



    @MemberOrder(name = "Ship Classes (Applicability)", sequence = "90")
    @ActionLayout(named = "Remove")
    public FreeText removeShipClass(final @ParameterLayout(typicalLength = 30) ShipClassType shipClassType) {
        wrapperFactory.wrapSkipRules(this).removeFromShipClasses(shipClassType);
        container.removeIfNotAlready(shipClassType);
        return this;
    }

    // disable action dependent on state of object
    public String disableRemoveShipClass(final ShipClassType shipClassType) {
        return getShipClasses().isEmpty() ? "No Ship Classes to remove" : null;
    }

    // validate the provided argument prior to invoking action
    public String validateRemoveShipClass(final ShipClassType shipClassType) {
        if (!getShipClasses().contains(shipClassType)) {
            return "Not a Ship Class";
        }
        return null;
    }

    // provide a drop-down
    public java.util.Collection<ShipClassType> choices0RemoveShipClass() {
        return getShipClasses();
    }


    @MemberOrder(name = "Ship Classes (Applicability)", sequence = "80")
    @ActionLayout(named = "Update")
    public FreeText applicableShipClass() {
        // By wrapping the call, Isis will detect that the collection is modified
        // and it will automatically send a CollectionInteractionEvent to the Event Bus.
        // ToDoItemSubscriptions is a demo subscriber to this event
        // Call REST API:

        //Create an instance of the interface class
  //      OntologyInterface oi = new OntologyInterface();

        //The create function takes lists of ships as input - in this case, there is only one ship
        Set<ShipClass> shipset = new HashSet<ShipClass>();
        ShipClass oneShipClass = new ShipClass();
        if (getShipClasses().size() == 0) {
            // No applicability on this text exists!!
            container.warnUser("No target ship class exists for  " + container.titleOf(this) + ". You must ADD a ship class before updating the APPLICABILITY of the text.");
        }
        else
        {
            Iterator it =getShipClasses().iterator();
            while (it.hasNext()) {
                ShipClassType thisShipClassType = (ShipClassType) it.next();
                // Only handles the first ship type on the list!!
                oneShipClass.setType(thisShipClassType.getType());
                oneShipClass.setMinTonnageEx(thisShipClassType.getMinTonnageEx());
                oneShipClass.setMaxTonnageEx(thisShipClassType.getMaxTonnageEx());
                oneShipClass.setMinTonnageIn(thisShipClassType.getMinTonnageIn());
                oneShipClass.setMaxTonnageIn(thisShipClassType.getMaxTonnageIn());
                oneShipClass.setMinLengthEx(thisShipClassType.getMinLengthEx());
                oneShipClass.setMaxLengthEx(thisShipClassType.getMaxLengthEx());
                oneShipClass.setMinLengthIn(thisShipClassType.getMinLengthIn());
                oneShipClass.setMaxLengthIn(thisShipClassType.getMaxLengthIn());
                oneShipClass.setMinDraughtEx(thisShipClassType.getMinDraughtEx());
                oneShipClass.setMaxDraughtEx(thisShipClassType.getMaxDraughtEx());
                oneShipClass.setMinDraughtIn(thisShipClassType.getMinDraughtIn());
                oneShipClass.setMaxDraughtIn(thisShipClassType.getMaxDraughtIn());
                oneShipClass.setMinPassengerEx(thisShipClassType.getMinPassengersEx());
                oneShipClass.setMaxPassengerEx(thisShipClassType.getMaxPassengersEx());
                oneShipClass.setMinPassengerIn(thisShipClassType.getMinPassengersIn());
                oneShipClass.setMaxPassengerIn(thisShipClassType.getMaxPassengerIn());
                oneShipClass.setMinKeelLaidEx(thisShipClassType.getMinKeelLaidEx());
                oneShipClass.setMaxKeelLaidEx(thisShipClassType.getMaxKeelLaidEx());
                oneShipClass.setMinKeelLaidIn(thisShipClassType.getMinKeelLaidIn());
                oneShipClass.setMaxKeelLaidIn(thisShipClassType.getMaxPassengerIn());
                oneShipClass.setLengthUnit(thisShipClassType.getLengthUnit());
                oneShipClass.setTonnageUnit(thisShipClassType.getTonnageUnit());
                oneShipClass.setDraughtUnit(thisShipClassType.getDraughtUnit());
                shipset.add(oneShipClass);
            }
            container.informUser("APPLICABILITY is updated for regulation " + container.titleOf(this));
        }

            // CREATE TARGETSHIPCLASS

 // PLUS: NEED TO LINK TO THE REGULATION TEXT ITSELF!!

        String targetShipClassURI = restClient.CreateTargetShipClass(oneShipClass);
        setTargetShipClassURI(targetShipClassURI);
        System.out.println("targetShipClassURI has been found!=" + targetShipClassURI);

        return this;
}
    // disable action dependent on state of object
    public String disableApplicableShipClass() {
        return getShipClasses().isEmpty()? "No Ship Classes to Update": null;
    }
     //END REGION Link FreeText (Section) --> to (several) ShipClassTypes (APPLICABILITY)



    // BEGIN REGION Link to ShipClassType, a list of ship classes that this text is applicable to.
    @javax.jdo.annotations.Persistent(mappedBy = "skosLink")
    @javax.jdo.annotations.Join // Make a separate join table.
    private SortedSet<SKOS> skoses = new TreeSet<SKOS>();
    @SuppressWarnings("deprecation")
    @MemberOrder(name = "SKOS Terms (Select)", sequence = "90")
    // @MemberOrder(sequence = "90")
    @CollectionLayout(named = "SKOS Terms (Select)", sortedBy = SKOSComparator.class, render = RenderType.EAGERLY)
    public SortedSet<SKOS> getSkoses() {
        return skoses;
    }
    public void setSkoses(SortedSet<SKOS> skoses) {
        this.skoses = skoses;
    }

    public void removeFromSkoses(final SKOS skos) {
        if (skos == null || !getSkoses().contains(skos)) return;
        getSkoses().remove(skos);
    }

    // / overrides the natural ordering
    public static class SKOSComparator implements Comparator<SKOS> {
        @Override
        public int compare(SKOS p, SKOS q) {
            Ordering<SKOS> bySkos= new Ordering<SKOS>() {
                public int compare(final SKOS p, final SKOS q) {
                    return Ordering.natural().nullsFirst().compare(p.getUri(), q.getUri());
                }
            };
            return bySkos
                    .compound(Ordering.<SKOS>natural())
                    .compare(p, q);
        }
    }

    // BEGIN fetch SKOSs
    @Action()
    @MemberOrder(name = "SKOS Terms (Select)", sequence = "60")
    @ActionLayout(named = "Fetch")
    public FreeText FetchSkosShow() {

        FragmentSKOSConceptOccurrences fragment = restClient.GetSkos(plainRegulationText);
        System.out.println("FREETEXT: fragment OK");
        List<SKOS>  skosTermList = creationController.ShowSKOSlist(plainRegulationText,fragment);

        Iterator it = skosTermList.iterator();
        while (it.hasNext()) {
            SKOS thisSkos = (SKOS) it.next();
            getSkoses().add(thisSkos);
        }
        container.flush();
        container.informUser("SKOS LIST fetched for" + container.titleOf(this));
        return this;
    }
// END FETCH applicability


    @MemberOrder(name = "SKOS Terms (Select)", sequence = "90")
    @ActionLayout(named = "Remove")
    public FreeText removeSkos(final @ParameterLayout(typicalLength = 30) SKOS skos) {
        wrapperFactory.wrapSkipRules(this).removeFromSkoses(skos);
        container.removeIfNotAlready(skos);
        return this;
    }

    // disable action dependent on state of object
    public String disableRemoveSkos(final SKOS skos) {
        return getSkoses().isEmpty() ? "No SKOSs to remove" : null;
    }

    // validate the provided argument prior to invoking action
    public String validateRemoveSkos(final SKOS skos) {
        if (!getSkoses().contains(skos)) {
            return "Not a SKOS";
        }
        return null;
    }


    // BEGIN  update SKOSs
    @Action()
    @MemberOrder(name = "SKOS Terms (Select)", sequence = "80")
    @ActionLayout(named = "Update")
    public FreeText updateSKOS() {

        // CODE IS MISSING HERE to select the actual SKOS term for the text
        return this;
    }
    // disable action dependent on state of object
    public String disableUpdateSKOS() {
        return getSkoses().isEmpty() ? "No SKOSs to update" : null;
    }
// END UPDATE skos


    // provide a drop-down
    public java.util.Collection<SKOS> choices0RemoveSkos() {
        return getSkoses();
    }

    //END REGION Link Freetext to SKOS list


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

// BEGIN REGION SAVE TEXT IN THE RDF GRAPH

    // IF SOLAS => CALL ROOT-API TO CREATE ROOT NODE FOR SOLAS
    // IF ANNEX => CALL ROOT-API TO CREATE ROOT NODE FOR ANNEX
    // IF EU DIR => CALL ROOT-APT TO CREATE ROOT NODE FOR EU DIRECTIVE

// END REGION SAVE TEXT IN THE RDF GRAPH





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


    //region > CREATE RDF node for the FreeTExtSection: (action)
    @Action()
    @ActionLayout(named = "Make Persistent",position = ActionLayout.Position.PANEL)
    @MemberOrder(name="Text", sequence="5")
    public FreeText storeFreeText() {

        System.out.println("Make Persistent_1");
        // Must check if the documentURI has already been stored:

        if (getDocumentURI() == null){
            System.out.println("DocumentURI not fetched for this Section");

            // Must call 192.168.33.10:9000/api/rdf/document/component for this chapter to create a RDF Document for this chapter
            DocumentComponentList documentComponentList = new DocumentComponentList();

            if (getId() == null) {documentComponentList.setVersion("0");}
            else {
                documentComponentList.setVersion(getId().toString());
            }
            // Parent is documentURI of the Chapter:
            documentComponentList.setParent(regulationLink.getDocumentURI());
            documentComponentList.setComponentType(DocumentComponentType.DOCUMENT);

            DocumentComponent documentComponent = new DocumentComponent();

            documentComponent.setTitle("SECTION"+" "+getSectionNo());
            documentComponent.setShortTitle("SECTION"+" "+getSectionNo());
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
            return ObjectContracts.toString(this, "sectionNo,plainRegulationText, regulationLink, ownedBy");
        }

        /**
         * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}).
         */
    @Override
    public int compareTo(final FreeText other) {
        return ObjectContracts.compare(this, other, "sectionNo,plainRegulationText, regulationLink, ownedBy");
    }
    //endregion

    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;

   // @javax.inject.Inject
   // private FreeTexts freeTexts;

    @javax.inject.Inject
    private ShipClassTypes newShipClassTypeCall;

    @javax.inject.Inject
    private TextItems newTextItemCall;

    @javax.inject.Inject
    private SubSections newSubSectionCall;

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
