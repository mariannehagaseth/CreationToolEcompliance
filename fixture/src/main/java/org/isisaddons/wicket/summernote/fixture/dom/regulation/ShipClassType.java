
package org.isisaddons.wicket.summernote.fixture.dom.regulation;

import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.eventbus.ActionInteractionEvent;
import org.apache.isis.applib.services.eventbus.EventBusService;
import org.apache.isis.applib.services.wrapper.WrapperFactory;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import java.util.SortedSet;


@javax.jdo.annotations.PersistenceCapable(identityType= IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries( {
        @javax.jdo.annotations.Query(
                name = "findShipClassTypes", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.wicket.summernote.fixture.dom.regulation.ShipClassType ")
})
//@javax.jdo.annotations.Uniques({
//        @javax.jdo.annotations.Unique(
//                name="Regulation_description_must_be_unique",
//                members={"type"})
//})

@DomainObject(objectType="SHIPCLASSTYPE",autoCompleteRepository=ShipClassTypes.class, autoCompleteAction="autoComplete", bounded = true)
@DomainObjectLayout(bookmarking= BookmarkPolicy.AS_ROOT)
@MemberGroupLayout (
        columnSpans={4,4,4,12},
        left={"Tonnage","Length","Draught"},
        middle={"Ship Class","Units"},
        right={"Number of Passengers","Keel Laid Date"} )
public class ShipClassType implements Categorized, Comparable<ShipClassType>  {

    private final static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ShipClassType.class);

    // region > title, icon
    public String title() {
        final TitleBuffer buf = new TitleBuffer();
        buf.append(getType());
        return buf.toString();
    }
    //endregion



    @javax.jdo.annotations.Column(allowsNull="false", length=100)
    @MemberOrder(name="Ship Class",  sequence="5")
    @PropertyLayout(typicalLength=100)
    private String type;
    public String getType() {
        return type;
    }
    public void setType(String value) {
        this.type = value;
    }

    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="Tonnage",  sequence="10")
    @PropertyLayout(typicalLength=100,named = "Tonnage >=",hidden=Where.ALL_TABLES)
    private double minTonnageIn;
    public double getMinTonnageIn() {
        return minTonnageIn;
    }
    public void setMinTonnageIn(double value) {
        this.minTonnageIn = value;
    }

    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="Tonnage",  sequence="12")
    @PropertyLayout(typicalLength=100,named = "Tonnage >",hidden=Where.ALL_TABLES)
     private double minTonnageEx;
    public double getMinTonnageEx() {
        return minTonnageEx;
    }
    public void setMinTonnageEx(double value) {
        this.minTonnageEx = value;
    }

    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="Tonnage",  sequence="14")
    @PropertyLayout(typicalLength=100,named = "Tonnage <=",hidden=Where.ALL_TABLES)
    private double maxTonnageIn;
    public double getMaxTonnageIn() {
        return maxTonnageIn;
    }
    public void setMaxTonnageIn(double value) {
        this.maxTonnageIn = value;
    }


    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(name="Tonnage",  sequence="16")
    @PropertyLayout(typicalLength=100,named = "Tonnage <",hidden=Where.ALL_TABLES)
      private double maxTonnageEx;
    public double getMaxTonnageEx() {
        return maxTonnageEx;
    }
    public void setMaxTonnageEx(double value) {
        this.maxTonnageEx = value;
    }

    @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder(name="Length",  sequence="18")
    @PropertyLayout(typicalLength=100,named = "Length >=",hidden=Where.ALL_TABLES)
    private double minLengthIn;
    public double getMinLengthIn() {
        return minLengthIn;
    }
    public void setMinLengthIn(double value) {
        this.minLengthIn = value;
    }


    @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder(name="Length",  sequence="20")
    @PropertyLayout(typicalLength=100,named = "Length >",hidden=Where.ALL_TABLES)
      private double minLengthEx;
    public double getMinLengthEx() {
        return minLengthEx;
    }
    public void setMinLengthEx(double value) {
        this.minLengthEx = value;
    }


    @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder(name="Length",  sequence="22")
    @PropertyLayout(typicalLength=100,named = "Length <=",hidden=Where.ALL_TABLES)
    private double maxLengthIn;
    public double getMaxLengthIn() {
        return maxLengthIn;
    }
    public void setMaxLengthIn(double value) {
        this.maxLengthIn = value;
    }


     @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder(name="Length",  sequence="24")
    @PropertyLayout(typicalLength=100,named = "Length <",hidden=Where.ALL_TABLES)
      private double maxLengthEx;
    public double getMaxLengthEx() {
        return maxLengthEx;
    }
    public void setMaxLengthEx(double value) {
        this.maxLengthEx = value;
    }



    @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder(name="Draught",  sequence="26")
    @PropertyLayout(typicalLength=100,named = "Draught >=",hidden=Where.ALL_TABLES)
    private double minDraughtIn;
    public double getMinDraughtIn() {
        return minDraughtIn;
    }
    public void setMinDraughtIn(double value) {
        this.minDraughtIn = value;
    }

     @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder(name="Draught",  sequence="28")
    @PropertyLayout(typicalLength=100,named = "Draught >",hidden=Where.ALL_TABLES)
      private double minDraughtEx;
    public double getMinDraughtEx() {
        return minDraughtEx;
    }
    public void setMinDraughtEx(double value) {
        this.minDraughtEx = value;
    }

    @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder(name="Draught",  sequence="30")
    @PropertyLayout(typicalLength=100,named = "Draught <=",hidden=Where.ALL_TABLES)
    private double maxDraughtIn;
    public double getMaxDraughtIn() {
        return maxDraughtIn;
    }
    public void setMaxDraughtIn(double value) {
        this.maxDraughtIn = value;
    }


    @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder(name="Draught",  sequence="32")
    @PropertyLayout(typicalLength=100,named = "Draught <",hidden=Where.ALL_TABLES)
      private double maxDraughtEx;
    public double getMaxDraughtEx() {
        return maxDraughtEx;
    }
    public void setMaxDraughtEx(double value) {
        this.maxDraughtEx = value;
    }


    @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder(name="Number of Passengers",  sequence="34")
    @PropertyLayout(typicalLength=100,named = "No of Passengers >=",hidden=Where.ALL_TABLES)
    private int minPassengersIn;
    public int getMinPassengersIn() {
        return minPassengersIn;
    }
    public void setMinPassengersIn(int value) {
        this.minPassengersIn = value;
    }


     @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder(name="Number of Passengers",  sequence="36")
    @PropertyLayout(typicalLength=100,named = "No of Passengers >",hidden=Where.ALL_TABLES)
      private int minPassengersEx;
    public int getMinPassengersEx() {
        return minPassengersEx;
    }
    public void setMinPassengersEx(int value) {
        this.minPassengersEx = value;
    }

    @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder(name="Number of Passengers",  sequence="38")
    @PropertyLayout(typicalLength=100,named = "No of Passengers <=",hidden=Where.ALL_TABLES)
    private int maxPassengerIn;
    public int getMaxPassengerIn() {
        return maxPassengerIn;
    }
    public void setMaxPassengerIn(int value) {
        this.maxPassengerIn = value;
    }

    @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder(name="Number of Passengers",  sequence="40")
    @PropertyLayout(typicalLength=100,named = "No of Passengers <",hidden=Where.ALL_TABLES)
      private int maxPassengersEx;
    public int getMaxPassengersEx() {
        return maxPassengersEx;
    }
    public void setMaxPassengersEx(int value) {
        this.maxPassengersEx = value;
    }


    @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder( name="Keel Laid Date", sequence="42")
    @PropertyLayout(typicalLength=100,named = "Keel Laid Date >=",hidden=Where.ALL_TABLES)
    private  int minKeelLaidIn;
    public int getMinKeelLaidIn() {
        return minKeelLaidIn;
    }
    public void setMinKeelLaidIn(int value) {
        this.minKeelLaidIn = value;
    }

     @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder( name="Keel Laid Date", sequence="44")
    @PropertyLayout(typicalLength=100,named = "Keel Laid Date >",hidden=Where.ALL_TABLES)
      private int minKeelLaidEx;
    public int getMinKeelLaidEx() {
        return minKeelLaidEx;
    }
    public void setMinKeelLaidEx(int value) {
        this.minKeelLaidEx = value;
    }

    @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder(  name="Keel Laid Date",sequence="46")
    @PropertyLayout(typicalLength=100,named = "Keel Laid Date <=",hidden=Where.ALL_TABLES)
    private  int maxKeelLaidIn;
    public int getMaxKeelLaidIn() {
        return maxKeelLaidIn;
    }
    public void setMaxKeelLaidIn(int value) {
        this.maxKeelLaidIn = value;
    }


     @javax.jdo.annotations.Column(allowsNull="false" )
    @MemberOrder( name="Keel Laid Date", sequence="48")
    @PropertyLayout(typicalLength=100,named = "Keel Laid Date <",hidden=Where.ALL_TABLES)
      private  int maxKeelLaidEx;
    public int getMaxKeelLaidEx() {
        return maxKeelLaidEx;
    }
    public void setMaxKeelLaidEx(int value) {
        this.maxKeelLaidEx = value;
    }





     @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder( name="Units", sequence="50")
    @PropertyLayout(typicalLength=100,hidden=Where.ALL_TABLES)
      private  String lengthUnit;
    public String getLengthUnit() {
        return lengthUnit;
    }
    public void setLengthUnit(String value) {
        this.lengthUnit = value;
    }

     @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder( name="Units", sequence="52")
    @PropertyLayout(typicalLength=100,hidden=Where.ALL_TABLES)
     private  String tonnageUnit;
    public String getTonnageUnit() {
        return tonnageUnit;
    }
    public void setTonnageUnit(String value) {
        this.tonnageUnit = value;
    }

     @javax.jdo.annotations.Column(allowsNull="true", length=100)
    @MemberOrder( name="Units", sequence="54")
    @PropertyLayout(typicalLength=100,hidden=Where.ALL_TABLES)
     private  String draughtUnit;
    public String getDraughtUnit() {
        return draughtUnit;
    }
    public void setDraughtUnit(String value) {
        this.draughtUnit = value;
    }


    // BEGIN LINK FROM SHIPCLASSTYPE TO FREETEXT
    // mapping is done to this property:
    // Mapping back from ShipClassType to FreeText
    //Link to Section
    @javax.jdo.annotations.Column(allowsNull="true")
    @Property(editing= Editing.DISABLED,editingDisabledReason="Section Link cannot be updated from here")
    @PropertyLayout(hidden=Where.REFERENCES_PARENT, named = "Link to Ship Classes for Section")
    @MemberOrder(name="Ship Class", sequence="50")
    private FreeText applicableLink;
    @javax.jdo.annotations.Column(allowsNull="true")
    public FreeText  getApplicableLink() { return applicableLink; }
    @javax.jdo.annotations.Column(allowsNull="true")
    public void setApplicableLink(FreeText applicableLink) { this.applicableLink= applicableLink; }
    // END LINK FROM SHIPCLASSTYPE TO FREETEXT


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
    public static abstract class AbstractActionInteractionEvent extends ActionInteractionEvent<ShipClassType> {
        private static final long serialVersionUID = 1L;
        private final String description;
        public AbstractActionInteractionEvent(
                final String description,
                final ShipClassType source,
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
                final ShipClassType source,
                final Identifier identifier,
                final Object... arguments) {
            super("deleted", source, identifier, arguments);
        }
    }

    //endregion


    //region > toString, compareTo
    @Override
    public String toString() {
         return ObjectContracts.toString(this, "type");
    }

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}).
     */
    @Override
    public int compareTo(final ShipClassType other) {
        return ObjectContracts.compare(this, other, "type");
    }
    //endregion

    //endregion

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
