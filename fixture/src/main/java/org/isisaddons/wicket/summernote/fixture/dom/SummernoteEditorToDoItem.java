/*
 *  Copyright 2013~2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
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
package org.isisaddons.wicket.summernote.fixture.dom;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Ordering;

import org.joda.time.LocalDate;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.InvokeOn;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.clock.Clock;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;
import org.apache.isis.applib.value.Blob;

import org.isisaddons.wicket.summernote.cpt.applib.SummernoteEditor;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Uniques({
    @javax.jdo.annotations.Unique(
            name="ToDoItem_description_must_be_unique",
            members={"ownedBy","description"})
})
@javax.jdo.annotations.Queries( {
    @javax.jdo.annotations.Query(
            name = "todo_all", language = "JDOQL",
            value = "SELECT "
                    + "FROM dom.todo.ToDoItem "
                    + "WHERE ownedBy == :ownedBy"),
    @javax.jdo.annotations.Query(
            name = "todo_notYetComplete", language = "JDOQL",
            value = "SELECT "
                    + "FROM dom.todo.ToDoItem "
                    + "WHERE ownedBy == :ownedBy "
                    + "   && complete == false"),
    @javax.jdo.annotations.Query(
            name = "todo_complete", language = "JDOQL",
            value = "SELECT "
                    + "FROM dom.todo.ToDoItem "
                    + "WHERE ownedBy == :ownedBy "
                    + "&& complete == true"),
    @javax.jdo.annotations.Query(
            name = "todo_similarTo", language = "JDOQL",
            value = "SELECT "
                    + "FROM dom.todo.ToDoItem "
                    + "WHERE ownedBy == :ownedBy "
                    + "&& category == :category"),
    @javax.jdo.annotations.Query(
            name = "todo_autoComplete", language = "JDOQL",
            value = "SELECT "
                    + "FROM dom.todo.ToDoItem "
                    + "WHERE ownedBy == :ownedBy && "
                    + "description.indexOf(:description) >= 0")
})
@DomainObject(
        objectType = "TODO",
        autoCompleteRepository = SummernoteEditorToDoItems.class,
        autoCompleteAction = "autoComplete"
)
@DomainObjectLayout(
        named = "ToDo Item"
)
public class SummernoteEditorToDoItem implements Comparable<SummernoteEditorToDoItem> {

    // //////////////////////////////////////
    // Identification in the UI
    // //////////////////////////////////////

    public String title() {
        final TitleBuffer buf = new TitleBuffer();
        buf.append(getDescription());
        if (isComplete()) {
            buf.append("- Completed!");
        } else {
            if (getDueBy() != null) {
                buf.append(" due by", getDueBy());
            }
        }
        return buf.toString();
    }

    public String iconName() {
        return "ToDoItem-" + (!isComplete() ? "todo" : "done");
    }

    // //////////////////////////////////////
    // Description (property)
    // //////////////////////////////////////

    private String description;

    @javax.jdo.annotations.Column(allowsNull="false", length=100)
    @Property(
            regexPattern = "\\w[@&:\\-\\,\\.\\+ \\w]*"
    )
    @PropertyLayout(
            typicalLength = 50
    )
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    // //////////////////////////////////////
    // DueBy (property)
    // //////////////////////////////////////

    @javax.jdo.annotations.Persistent(defaultFetchGroup="true")
    private LocalDate dueBy;

    @javax.jdo.annotations.Column(allowsNull="true")
    public LocalDate getDueBy() {
        return dueBy;
    }

    public void setDueBy(final LocalDate dueBy) {
        this.dueBy = dueBy;
    }
    public void clearDueBy() {
        setDueBy(null);
    }
    // proposed new value is validated before setting
    public String validateDueBy(final LocalDate dueBy) {
        if (dueBy == null) {
            return null;
        }
        return isMoreThanOneWeekInPast(dueBy) ? "Due by date cannot be more than one week old" : null;
    }

    // //////////////////////////////////////
    // Category and Subcategory (property)
    // //////////////////////////////////////

    public static enum Category {
        Professional {
            @Override
            public List<Subcategory> subcategories() {
                return Arrays.asList(null, Subcategory.OpenSource, Subcategory.Consulting, Subcategory.Education);
            }
        }, Domestic {
            @Override
            public List<Subcategory> subcategories() {
                return Arrays.asList(null, Subcategory.Shopping, Subcategory.Housework, Subcategory.Garden, Subcategory.Chores);
            }
        }, Other {
            @Override
            public List<Subcategory> subcategories() {
                return Arrays.asList(null, Subcategory.Other);
            }
        };

        public abstract List<Subcategory> subcategories();
    }

    public enum Subcategory {
        // professional
        OpenSource, Consulting, Education, Marketing,
        // domestic
        Shopping, Housework, Garden, Chores,
        // other
        Other;

        public static List<Subcategory> listFor(final Category category) {
            return category != null? category.subcategories(): Collections.<Subcategory>emptyList();
        }

        static String validate(final Category category, final Subcategory subcategory) {
            if(category == null) {
                return "Enter category first";
            }
            return !category.subcategories().contains(subcategory)
                    ? "Invalid subcategory for category '" + category + "'"
                    : null;
        }
/* MHAGA kutter denne...
        public static Predicate<Subcategory> thoseFor(final Category category) {
            return subcategory1 -> category.subcategories().contains(subcategory1);
        }
*/
    }

    // //////////////////////////////////////


    private Category category;

    @javax.jdo.annotations.Column(allowsNull="false")
    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    // //////////////////////////////////////

    private Subcategory subcategory;

    @javax.jdo.annotations.Column(allowsNull="true")
    public Subcategory getSubcategory() {
        return subcategory;
    }
    public void setSubcategory(final Subcategory subcategory) {
        this.subcategory = subcategory;
    }


    // //////////////////////////////////////
    // OwnedBy (property)
    // //////////////////////////////////////

    private String ownedBy;

    @javax.jdo.annotations.Column(allowsNull="false")
    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(final String ownedBy) {
        this.ownedBy = ownedBy;
    }


    // //////////////////////////////////////
    // Complete (property),
    // Done (action), Undo (action)
    // //////////////////////////////////////

    private boolean complete;

    @Property(
            editing = Editing.DISABLED
    )
    public boolean isComplete() {
        return complete;
    }

    public void setComplete(final boolean complete) {
        this.complete = complete;
    }

    public SummernoteEditorToDoItem completed() {
        setComplete(true);
        return this;
    }
    // disable action dependent on state of object
    public String disableCompleted() {
        return isComplete() ? "Already completed" : null;
    }

    public SummernoteEditorToDoItem notYetCompleted() {
        setComplete(false);

        return this;
    }
    // disable action dependent on state of object
    public String disableNotYetCompleted() {
        return !complete ? "Not yet completed" : null;
    }

    // //////////////////////////////////////
    // Cost (property), PreviousCost, UpdateCosts (action)
    // //////////////////////////////////////

    private BigDecimal cost;

    @javax.jdo.annotations.Column(allowsNull="true", scale=2)
    @javax.validation.constraints.Digits(integer=10, fraction=2)
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Update using action"
    )
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(final BigDecimal cost) {
        this.cost = cost!=null?cost.setScale(2, BigDecimal.ROUND_HALF_EVEN):null;
    }

    // //////////////////////////////////////

    private BigDecimal previousCost;

    @javax.jdo.annotations.Column(allowsNull="true", scale=2)
    @javax.validation.constraints.Digits(integer=10, fraction=2)
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Update using action"
    )
    public BigDecimal getPreviousCost() {
        return previousCost;
    }

    public void setPreviousCost(final BigDecimal previousCost) {
        this.previousCost = previousCost!=null?previousCost.setScale(2, BigDecimal.ROUND_HALF_EVEN):null;
    }

    // //////////////////////////////////////

    public SummernoteEditorToDoItem updateCosts(
            @ParameterLayout(named="Cost")
            @Parameter(optionality = Optionality.OPTIONAL)
            @javax.validation.constraints.Digits(integer=10, fraction=2)
            final BigDecimal cost,
            @ParameterLayout(named="Previous cost")
            @Parameter(optionality = Optionality.OPTIONAL)
            @javax.validation.constraints.Digits(integer=10, fraction=2)
            final BigDecimal previousCost
            ) {
        setCost(cost);
        return this;
    }

    // provide a default value for argument #0
    public BigDecimal default0UpdateCosts() {
        return getCost();
    }

    // provide a default value for argument #1
    public BigDecimal default1UpdateCosts() {
        return getPreviousCost();
    }

    // validate action arguments
    public String validateUpdateCosts(final BigDecimal proposedCost, final BigDecimal proposedPreviousCost) {
        if (proposedCost != null && proposedCost.compareTo(BigDecimal.ZERO) < 0) {
            return "Cost must be positive";
        }
        if (proposedPreviousCost != null && proposedPreviousCost.compareTo(BigDecimal.ZERO) < 0) {
            return "Previous cost must be positive";
        }
        return null;
    }


    // //////////////////////////////////////
    // Notes (property)
    // //////////////////////////////////////

    private String notes = "";

    @javax.jdo.annotations.Column(allowsNull="true", length=400)
    @SummernoteEditor(height = 100, maxHeight = 300)
    public String getNotes() {
        return notes;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }

    // //////////////////////////////////////
    // Attachment (property)
    // //////////////////////////////////////

    private Blob attachment;

    @javax.jdo.annotations.Persistent(defaultFetchGroup="false")
    @javax.jdo.annotations.Column(allowsNull="true", jdbcType="BLOB", sqlType="LONGBINARY")
    public Blob getAttachment() {
        return attachment;
    }

    public void setAttachment(final Blob attachment) {
        this.attachment = attachment;
    }

    // //////////////////////////////////////
    // Version (derived property)
    // //////////////////////////////////////

    public Long getVersionSequence() {
        if(!(this instanceof javax.jdo.spi.PersistenceCapable)) {
            return null;
        }
        final javax.jdo.spi.PersistenceCapable persistenceCapable = (javax.jdo.spi.PersistenceCapable) this;
        final Long version = (Long) JDOHelper.getVersion(persistenceCapable);
        return version;
    }
    // hide property (imperatively, based on state of object)
    public boolean hideVersionSequence() {
        return !(this instanceof javax.jdo.spi.PersistenceCapable);
    }

    // //////////////////////////////////////
    // Dependencies (collection),
    // Add (action), Remove (action)
    // //////////////////////////////////////

    // overrides the natural ordering
    public static class DependenciesComparator implements Comparator<SummernoteEditorToDoItem> {
        @Override
        public int compare(final SummernoteEditorToDoItem p, final SummernoteEditorToDoItem q) {
            final Ordering<SummernoteEditorToDoItem> byDescription = new Ordering<SummernoteEditorToDoItem>() {
                public int compare(final SummernoteEditorToDoItem p, final SummernoteEditorToDoItem q) {
                    return Ordering.natural().nullsFirst().compare(p.getDescription(), q.getDescription());
                }
            };
            return byDescription
                    .compound(Ordering.<SummernoteEditorToDoItem>natural())
                    .compare(p, q);
        }
    }



    @javax.jdo.annotations.Persistent(table="SummernoteEditorToDoItemDependencies")
    @javax.jdo.annotations.Join(column="dependingId")
    @javax.jdo.annotations.Element(column="dependentId")
    private SortedSet<SummernoteEditorToDoItem> dependencies = new TreeSet<>();

    @CollectionLayout(
            sortedBy = DependenciesComparator.class
    )
    public SortedSet<SummernoteEditorToDoItem> getDependencies() {
        return dependencies;
    }

    public void setDependencies(final SortedSet<SummernoteEditorToDoItem> dependencies) {
        this.dependencies = dependencies;
    }


    public SummernoteEditorToDoItem add(final SummernoteEditorToDoItem toDoItem) {
        getDependencies().add(toDoItem);
        return this;
    }
    public List<SummernoteEditorToDoItem> autoComplete0Add(final @MinLength(2) String search) {
        final List<SummernoteEditorToDoItem> list = toDoItems.autoComplete(search);
        list.removeAll(getDependencies());
        list.remove(this);
        return list;
    }

    public String disableAdd(final SummernoteEditorToDoItem toDoItem) {
        if(isComplete()) {
            return "Cannot add dependencies for items that are complete";
        }
        return null;
    }
    // validate the provided argument prior to invoking action
    public String validateAdd(final SummernoteEditorToDoItem toDoItem) {
        if(getDependencies().contains(toDoItem)) {
            return "Already a dependency";
        }
        if(toDoItem == this) {
            return "Can't set up a dependency to self";
        }
        return null;
    }

    public SummernoteEditorToDoItem remove(final SummernoteEditorToDoItem toDoItem) {
        getDependencies().remove(toDoItem);
        return this;
    }
    // disable action dependent on state of object
    public String disableRemove(final SummernoteEditorToDoItem toDoItem) {
        if(isComplete()) {
            return "Cannot remove dependencies for items that are complete";
        }
        return getDependencies().isEmpty()? "No dependencies to remove": null;
    }
    // validate the provided argument prior to invoking action
    public String validateRemove(final SummernoteEditorToDoItem toDoItem) {
        if(!getDependencies().contains(toDoItem)) {
            return "Not a dependency";
        }
        return null;
    }
    // provide a drop-down
    public Collection<SummernoteEditorToDoItem> choices0Remove() {
        return getDependencies();
    }


    // //////////////////////////////////////
    // Delete (action)
    // //////////////////////////////////////

    @Action(
            invokeOn = InvokeOn.OBJECT_AND_COLLECTION
    )
    public List<SummernoteEditorToDoItem> delete() {
        container.removeIfNotAlready(this);
        container.informUser("Deleted " + container.titleOf(this));
        // invalid to return 'this' (cannot render a deleted object)
        return toDoItems.notYetComplete();
    }



    // //////////////////////////////////////
    // Programmatic Helpers
    // //////////////////////////////////////

    private static final long ONE_WEEK_IN_MILLIS = 7 * 24 * 60 * 60 * 1000L;

    @Programmatic // excluded from the framework's metamodel
    public boolean isDue() {
        if (getDueBy() == null) {
            return false;
        }
        return !isMoreThanOneWeekInPast(getDueBy());
    }

    private static boolean isMoreThanOneWeekInPast(final LocalDate dueBy) {
        return dueBy.toDateTimeAtStartOfDay().getMillis() < Clock.getTime() - ONE_WEEK_IN_MILLIS;
    }

    // //////////////////////////////////////
    // Predicates
    // //////////////////////////////////////

    public static class Predicates {

        public static Predicate<SummernoteEditorToDoItem> thoseOwnedBy(final String currentUser) {
            return new Predicate<SummernoteEditorToDoItem>() {
                @Override
                public boolean apply(final SummernoteEditorToDoItem toDoItem) {
                    return Objects.equal(toDoItem.getOwnedBy(), currentUser);
                }
            };
        }

        public static Predicate<SummernoteEditorToDoItem> thoseCompleted(
                final boolean completed) {
            return new Predicate<SummernoteEditorToDoItem>() {
                @Override
                public boolean apply(final SummernoteEditorToDoItem t) {
                    return Objects.equal(t.isComplete(), completed);
                }
            };
        }

        public static Predicate<SummernoteEditorToDoItem> thoseWithSimilarDescription(final String description) {
            return new Predicate<SummernoteEditorToDoItem>() {
                @Override
                public boolean apply(final SummernoteEditorToDoItem t) {
                    return t.getDescription().contains(description);
                }
            };
        }

        @SuppressWarnings("unchecked")
        public static Predicate<SummernoteEditorToDoItem> thoseSimilarTo(final SummernoteEditorToDoItem toDoItem) {
            return com.google.common.base.Predicates.and(
                    thoseNot(toDoItem),
                    thoseOwnedBy(toDoItem.getOwnedBy()),
                    thoseCategorised(toDoItem.getCategory()));
        }

        public static Predicate<SummernoteEditorToDoItem> thoseNot(final SummernoteEditorToDoItem toDoItem) {
            return new Predicate<SummernoteEditorToDoItem>() {
                @Override
                public boolean apply(final SummernoteEditorToDoItem t) {
                    return t != toDoItem;
                }
            };
        }

        public static Predicate<SummernoteEditorToDoItem> thoseCategorised(final Category category) {
            return new Predicate<SummernoteEditorToDoItem>() {
                @Override
                public boolean apply(final SummernoteEditorToDoItem toDoItem) {
                    return Objects.equal(toDoItem.getCategory(), category);
                }
            };
        }

        public static Predicate<SummernoteEditorToDoItem> thoseSubcategorised(
                final Subcategory subcategory) {
            return new Predicate<SummernoteEditorToDoItem>() {
                @Override
                public boolean apply(final SummernoteEditorToDoItem t) {
                    return Objects.equal(t.getSubcategory(), subcategory);
                }
            };
        }

        public static Predicate<SummernoteEditorToDoItem> thoseCategorised(
                final Category category, final Subcategory subcategory) {
            return com.google.common.base.Predicates.and(
                    thoseCategorised(category),
                    thoseSubcategorised(subcategory));
        }

    }

    // //////////////////////////////////////
    // toString
    // //////////////////////////////////////

    @Override
    public String toString() {
        return ObjectContracts.toString(this, "description,complete,dueBy,ownedBy");
    }

    // //////////////////////////////////////
    // compareTo
    // //////////////////////////////////////

    /**
     * Required so can store in {@link SortedSet sorted set}s (eg {@link #getDependencies()}).
     */
    @Override
    public int compareTo(final SummernoteEditorToDoItem other) {
        return ObjectContracts.compare(this, other, "complete,dueBy,description");
    }

    // //////////////////////////////////////
    // Injected Services
    // //////////////////////////////////////

    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private SummernoteEditorToDoItems toDoItems;

    @javax.inject.Inject
    @SuppressWarnings("unused")
    private ClockService clockService;

}
