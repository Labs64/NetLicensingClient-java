/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Basic {@code Page} implementation.
 * 
 * @param <Entity>
 *            the type of which the page consists.
 */
public class PageImpl<Entity> implements Page<Entity>, Serializable {

    private static final long serialVersionUID = 867755906494344406L;

    private final List<Entity> content = new ArrayList<Entity>();
    private final int pageNumber;
    private final int itemsNumber;
    private final int totalPages;
    private final long totalItems;
    private final boolean hasNext;

    /**
     * Constructor of {@code PageImpl}.
     * 
     * @param content
     *            the content of this page, must not be {@literal null}.
     * @param pageNumber
     *            the number of the current page
     * @param itemsNumber
     *            the number of elements on the page
     * @param totalPages
     *            the number of total pages
     * @param totalItems
     *            the total amount of elements
     * @param hasNext
     *            is there a next page exists
     */
    public PageImpl(final List<Entity> content, final int pageNumber, final int itemsNumber, final int totalPages,
            final long totalItems, final boolean hasNext) {
        assert content != null : "Content must not be null!";

        this.content.addAll(content);

        this.pageNumber = pageNumber;
        this.itemsNumber = itemsNumber;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.hasNext = hasNext;
    }

    /**
     * Safe create instance of {@code Page}.
     * 
     * @param content
     *            the content of this page, must not be {@literal null}.
     * @param pageNumber
     *            the number of the current page
     * @param itemsNumber
     *            the number of elements on the page
     * @param totalPages
     *            the number of total pages
     * @param totalItems
     *            the total amount of elements
     * @param hasNext
     *            is there a next page exists
     * @param <E>
     *            type of page entity
     */
    public static <E> PageImpl<E> createInstance(final List<E> content,
            final String pageNumber, final String itemsNumber,
            final String totalPages, final String totalItems, final String hasNext) {
        try {
            return new PageImpl<E>(content,
                    Integer.valueOf(pageNumber),
                    Integer.valueOf(itemsNumber),
                    Integer.valueOf(totalPages),
                    Long.valueOf(totalItems),
                    Boolean.valueOf(hasNext));
        } catch (Exception e) {
            return new PageImpl<E>(content, 0, 0, 0, 0, false);
        }
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getItemsNumber() {
        return itemsNumber;
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public long getTotalItems() {
        return totalItems;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public Iterator<Entity> iterator() {
        return content.iterator();
    }

    @Override
    public List<Entity> getContent() {
        return Collections.unmodifiableList(content);
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public String toString() {
        String contentType = "UNKNOWN";
        List<Entity> content = getContent();

        if (hasContent()) {
            contentType = content.get(0).getClass().getName();
        }

        return String.format("Page %s of %d containing %s instances", getPageNumber(), getTotalPages(), contentType);
    }

}
