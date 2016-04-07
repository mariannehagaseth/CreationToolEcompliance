
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
import org.isisaddons.wicket.summernote.cpt.applib.SummernoteEditor;
import org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos.MySKOSConcept;
import org.joda.time.LocalDate;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import java.util.List;
import java.util.SortedSet;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")

@javax.jdo.annotations.Queries( {
 })
@DomainObject(objectType="INTERPRETATIONTEST",autoCompleteRepository=Interpretations.class, autoCompleteAction="autoComplete")
@DomainObjectLayout(bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
		columnSpans={12,0,0,0},
		left={"Interpretation"},
		middle={},
        right={})
public class InterpretationTEST implements Categorized, Comparable<InterpretationTEST> {

    //region > LOG
    /**
     * It isn't common for entities to log, but they can if required.  
     * Isis uses slf4j API internally (with log4j as implementation), and is the recommended API to use.
     */
    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(InterpretationTEST.class);
      //endregion

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();
        buf.append("Interpretation");
        return buf.toString();
    }
    //endregion


    // Region plainRegulationText
    private String plainRegulationText;
    @javax.jdo.annotations.Column(allowsNull="false", length=10000)
   // @Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Interpretation", sequence="10")
    @PropertyLayout(typicalLength=10000, multiLine=8)
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


    // Region target
    private String target;
    @javax.jdo.annotations.Column(allowsNull="true", length=3000)
    //@Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Interpretation", sequence="10")
    @PropertyLayout(typicalLength=3000, multiLine=3)
    //@SummernoteEditor(height = 100, maxHeight = 300)
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

    // Region requirement
    private String requirement;
    @javax.jdo.annotations.Column(allowsNull="true", length=3000)
    //@Property(regexPattern="\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(name="Interpretation", sequence="20")
    @PropertyLayout(typicalLength=3000, multiLine=3)
    @SummernoteEditor(height = 100, maxHeight = 300)
    public String getRequirement() {
        return requirement;
    }
 /*   public void setRequirement(final String requirement) {
        this.requirement = requirement;
    } */
    public void setRequirement(final String requirement) {
       /* this.requirement = "<span style=\"background-color: yellow;\">every\"\" ship</span> must have a polar code certificate.<br>"; */
       /*THIS WORKS: this.requirement = "<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif;" +
                "font-size: small; line-height: normal;\"><span style=\"background-color:" +
                "yellow;\">Passenger ships</span> have more than 12 passengers.</span>";
    */
        /*THIS WORKS OK AS WELL:*/
   //     this.requirement = "<span style=\"background-color: rgb(0, 255, 0);\">Every ship</span> must have a polar code certificate.<br>";
        this.requirement = setColour();
    }
    public void modifyRequirement(final String requirement) {
        setRequirement(requirement);
    }
    public void clearRequirement() {
        setRequirement(null);
    }
    //endregion

@Programmatic
public String setColour () {
    return "<span style=\"background-color: rgb(0, 255, 0);\">Every ship</span> must have a polar code certificate.";
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
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<InterpretationTEST> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final InterpretationTEST source,
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
                final InterpretationTEST source,
                final Identifier identifier,
                final Object... arguments) {
            super("deleted", source, identifier, arguments);
        }
    }

    //endregion


    //region > toString, compareTo
    @Override
    public String toString() {
         return ObjectContracts.toString(this, "plainRegulationText");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}). 
     */
    @Override
    public int compareTo(final InterpretationTEST other) {
         return ObjectContracts.compare(this, other, "plainRegulationText");
    }
    //endregion

    //region > injected services



    @SuppressWarnings("deprecation")
	Bulk.InteractionContext bulkInteractionContext;
    public void injectBulkInteractionContext(@SuppressWarnings("deprecation") Bulk.InteractionContext bulkInteractionContext) {
        this.bulkInteractionContext = bulkInteractionContext;
    }

    EventBusService eventBusService;
    public void injectEventBusService(EventBusService eventBusService) {
        this.eventBusService = eventBusService;
    }

    @javax.inject.Inject
    private WrapperFactory wrapperFactory;

    //endregion
}
