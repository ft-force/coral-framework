package com.ftf.coral.core.page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PageRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static int defaultPageSize = 10;

    public static int defaultSortIndex = 0;

    protected int page = 1;
    protected int size = defaultPageSize;
    protected String pagerId;
    protected int sort = defaultSortIndex;
    protected Map<String, Object> conditionMap = new HashMap<>();

    /**
     * Returns requested page number.
     * 
     * @see #setPage(int)
     */
    public int getPage() {
        return page;
    }

    /**
     * Specifies requested page number. Page numbers are 1-based.
     */
    public void setPage(final int page) {
        this.page = page > 0 ? page : 1;
        // this.page = page;
    }

    /**
     * Returns size of the page. Page size refers to total numbers of items per
     * page.
     * 
     * @see #setSize(int)
     */
    public int getSize() {
        return size;
    }

    /**
     * Specifies page size, i.e. number of elements per page.
     */
    public void setSize(final int size) {
        this.size = size > 0 ? size : defaultPageSize;
        // this.size = size;
    }

    // ---------------------------------------------------------------- sort

    /**
     * Returns sort 1-based index of column that should be sorted. If
     * <code>0</code>, nothing should be sorted. Positive values represents
     * ascending order, negative values descending.
     * <p>
     * By using the index we also hide the real column names.
     */
    public int getSort() {
        return sort;
    }

    /**
     * Returns sort index.
     * 
     * @see #getSort()
     */
    public void setSort(final int sort) {
        this.sort = sort;
    }

    /**
     * Returns pager id.
     */
    public String getPagerId() {
        return pagerId;
    }

    /**
     * Returns pager id.
     */
    public void setPagerId(final String pagerId) {
        this.pagerId = pagerId;
    }

    /**
     * Calculates offset.
     */
    public int calcOffset() {
        return (page - 1) * size;
    }

    @Override
    public String toString() {
        return "PageRequest{" + "page=" + page + ", size=" + size + ", sort=" + sort + ", pagerId=" + pagerId + '}';
    }

    protected int totalItems;

    /**
     * Returns total number of items.
     */
    public int getTotalItems() {
        return this.totalItems;
    }

    public Map<String, Object> getConditionMap() {
        return conditionMap;
    }

    public void setConditionMap(Map<String, Object> map) {
        this.conditionMap = map;
    }
}
