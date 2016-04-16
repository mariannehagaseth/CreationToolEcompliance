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

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;
import org.isisaddons.wicket.summernote.fixture.dom.regulation.Chapter.ChapterAnnex;

//import java.math.BigDecimal;

//@DomainServiceLayout(named="Regulation Hierarchy",menuOrder="10")
@DomainService(repositoryFor = RootNode.class)
@DomainServiceLayout(menuBar= DomainServiceLayout.MenuBar.TERTIARY,named="ROOT node",menuOrder="50")
public class RootNodes {


    //region > findChapterURI (action)
@Programmatic
    public  RootNode findChapterURI() {
    System.out.println("Find CHAPTER node");

    final RootNode node = container.uniqueMatch(
                new QueryDefault<RootNode>(RootNode.class,
                        "findRoot",
                        "nodeLabel", ChapterAnnex.CHAPTER.toString())
          );
         System.out.println("Finished with findRoot");
    return node;
    }
    //endregion

    //region > findAnnexURI (action)
    @Programmatic
    public  RootNode findAnnexURI() {
        final  RootNode node = container.uniqueMatch(
                new QueryDefault<RootNode>(RootNode.class,
                        "findRoot",
                        "nodeLabel", ChapterAnnex.ANNEX.toString()));
        return node;
    }
    //endregion

    //region > findAnnexURI (action)
    @Programmatic
    public  RootNode findDirectiveURI() {
        final  RootNode node = container.uniqueMatch(
                new QueryDefault<RootNode>(RootNode.class,
                        "findRoot",
                        "nodeLabel", ChapterAnnex.DIRECTIVE.toString()));
        return node;
    }
    //endregion

    //region > helpers
    @Programmatic
    /*The @Programmatic annotation can be used to cause Apache Isis to complete ignore a class member. 
     * This means it won't appear in any viewer, its value will not be persisted, 
     * and it won't appear in any XML snapshots .*/
    public RootNode newRootNode(
            final String nodeLabel,
            final String rootURI
            )
    {
        final RootNode node = container.newTransientInstance(RootNode.class);

        node.setNodeLabel(nodeLabel);
        node.setRootURI(rootURI);

        container.persist(node);
        container.flush();
       // Generate id: solasChapter.setSolasChapterNumber(solasChapter.getIdString());
        return node;
    }


    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private ClockService clockService;

    //@javax.inject.Inject
    //private SolasCode solasCode;


    //endregion
}
