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

//import dom.todo.ToDoItem.Category;
//import dom.todo.ToDoItem.Subcategory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.queryresultscache.QueryResultsCache;

import java.util.List;

/*
 * @DomainService:
 * Indicates that the (concrete) class should be automatically instantiated as a 
 * domain service, and specify a number of UI hints.
 * Domain services with this annotation do NOT need to be registered explicitly in isis.properties; 
 * they will be discovered automatically on the CLASSPATH.
 * The following attributes can be specified:
 * menuBar - the menubar in which the menu that hold's this service's 
 * actions should reside (1.8.0-SNAPSHOT)
 * menuOrder - the order of the service's menu with respect to other service's.
 * repositoryFor - if this domain service acts as a repository for an entity type, 
 * specify that entity type. (This is currently informational only)
 * The menuBar can be set to PRIMARY, SECONDARY or TERTIARY. In the Wicket viewer 
 * the PRIMARY menu bar is left-aligned, the SECONDARY menu bar is right aligned, and the 
 * TERTIARY menu bar is associated with the user's name (far top-right).
 * */
//@DomainService
@DomainService(nature=NatureOfService.DOMAIN)
public class RegulationContributions extends AbstractFactoryAndRepository {
/*If you inherit from the org.apache.isis.applib.AbstractFactoryAndRepository 
 * adapter class then this will automatically have the DomainObjectContainer injected, 
 * and provides convenience methods that delegate to the container. Using this is not mandatory, however.
*/
    //region > priority (contributed property)
    @SuppressWarnings("deprecation")
	@ActionLayout(contributed=Contributed.AS_ACTION,describedAs ="The relative priority of this item compared to others not yet finalized (using 'validityDate' date)")
//    @NotInServiceMenu
   // @ActionSemantics(Of.SAFE)
    @Action(semantics=SemanticsOf.SAFE,hidden=Where.NOWHERE)
   // @NotContributed(As.ACTION) // ie contributed as association
//    @Hidden(where=Where.ALL_TABLES)
// Change to always hidden: OK ???
//    @Hidden
    /***
    @Property(editingDisabledReason="Relative priority, derived from Validity Date")
    public Integer relativePriority(final RegulationText regulation) {
        return queryResultsCache.execute(new Callable<Integer>(){
            @Override
            public Integer call() throws Exception {
                if(regulation.isFinalized()) {
                    return null;
                }

                // sort items, then locate this one
             //  int i=1;
              //  for (RegulationText each : sortedNotYetFinalized()) {
               //     if(each == regulation) {
                //        return i;
                 //   }
                  //  i++;
                //}
                return null;
            }}, RegulationContributions.class, "relativePriority", regulation);
    }

***/
    //endregion
/***
    //region >  next, previous (contributed actions)
	@ActionLayout(contributed=Contributed.AS_ASSOCIATION, describedAs ="The next item not yet finalized")
    //@NotInServiceMenu
    @Action(semantics=SemanticsOf.SAFE)
    //@ActionSemantics(Of.SAFE)
   // @NotContributed(As.ASSOCIATION) // ie contributed as action
    public RegulationText next(final RegulationText item) {
        final Integer priority = relativePriority(item);
        if(priority == null) {
            return item;
        }
        int priorityOfNext = priority != null ? priority + 1 : 0;
        return itemWithPriorityElse(priorityOfNext, item);
    }
    public String disableNext(final RegulationText regulation) {
        if (regulation.isFinalized()) {
            return "Finalized";
        } 
        if(next(regulation) == null) {
            return "No next item";
        }
        return null;
    }
***/
    // //////////////////////////////////////
/***
    @ActionLayout(contributed=Contributed.AS_ASSOCIATION,describedAs ="The previous item not yet finalized")
   // @NotInServiceMenu
//    @ActionSemantics(Of.SAFE)
    @Action(semantics=SemanticsOf.SAFE)
 //   @NotContributed(As.ASSOCIATION) // ie contributed as action
    public RegulationText previous(final RegulationText item) {
        final Integer priority = relativePriority(item);
        if(priority == null) {
            return item;
        }
        int priorityOfPrevious = priority != null? priority - 1 : 0;
        return itemWithPriorityElse(priorityOfPrevious, item);
    }
    public String disablePrevious(final RegulationText regulation) {
        if (regulation.isFinalized()) {
            return "Finalized";
        }
        if(previous(regulation) == null) {
            return "No previous item";
        }
        return null;
    }
***/
    // //////////////////////////////////////

    /**
     * @param priority : 1-based priority
     */
    private RegulationText itemWithPriorityElse(int priority, final RegulationText itemElse) {
        if(priority < 1) {
            return null;
        }
        /*
        final List<RegulationText> items = sortedNotYetFinalized();
        if(priority > items.size()) {
            return null;
        }*/
/*        return priority>=0 && items.size()>=priority? items.get(priority-1): itemElse; */
        // set this instead for dummy:
        return null;
    }
    //endregion

    //region > similarTo (contributed collection)
    @SuppressWarnings("deprecation")
	//@NotInServiceMenu
    @Action(semantics=SemanticsOf.SAFE)
//    @ActionSemantics(Of.SAFE)
//    @DomainService(nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)
    @ActionLayout(contributed=Contributed.AS_ACTION)
   // @NotContributed(As.ACTION)
    public List<RegulationText> similarTo(final RegulationText regulation) {
        final List<RegulationText> similarRegulations = allMatches(
                new QueryDefault<RegulationText>(RegulationText.class,
                        "findByOwnedByAndKpi", 
                        "ownedBy",
                        "ecompliance",
                        "kpi", regulation.getKpi()));
        return Lists.newArrayList(Iterables.filter(similarRegulations, excluding(regulation)));
    }


    private static Predicate<RegulationText> excluding(final RegulationText regulation) {
        return new Predicate<RegulationText>() {
            @Override
            public boolean apply(RegulationText input) {
                return input != regulation;
            }
        };
    }
    //endregion



    //region > injected services
    @javax.inject.Inject
    private RegulationTexts regulations;

    @javax.inject.Inject
    private QueryResultsCache queryResultsCache;
    //endregion

}
