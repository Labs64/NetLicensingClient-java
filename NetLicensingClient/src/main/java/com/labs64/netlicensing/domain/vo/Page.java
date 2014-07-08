/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.domain.vo;

import java.util.List;

/**
 * A page is a sub-list of a list of objects. It allows gain information about the position of it in the containing
 * entire list.
 * 
 * @param <Entity>
 */
public interface Page<Entity> extends Iterable<Entity> {

    /**
     * Returns the number of the current page. Is always non-negative.
     * 
     * @return the number of the current page.
     */
    int getPageNumber();

    /**
     * Returns the number of elements on the page.
     * 
     * @return the number of elements on the page.
     */
    int getItemsNumber();

    /**
     * Returns the number of total pages.
     * 
     * @return the number of total pages
     */
    int getTotalPages();

    /**
     * Returns the total amount of elements.
     * 
     * @return the total amount of elements
     */
    long getTotalItems();

    /**
     * Returns if there is a next page exists.
     * 
     * @return true if there is a next page exists, otherwise false.
     */
    boolean hasNext();

    /**
     * Return container content.
     * 
     * @return container content
     */
    List<Entity> getContent();

    /**
     * Returns if there is a content exists.
     * 
     * @return true if there is a content exists, otherwise false
     */
    boolean hasContent();

}
