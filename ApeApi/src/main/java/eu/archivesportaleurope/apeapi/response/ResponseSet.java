/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author mahbub
 */
public class ResponseSet {
    @ApiModelProperty(required = true, value="Total number of documents found.")
    protected long totalResults;
    @ApiModelProperty(required = true, value="Position from where this response set started.")
    protected long startIndex;
    @ApiModelProperty(required = true, value="Total pages that can be generated based on query limit.")
    protected int totalPages;
    
    public long getTotalResults() {
        return totalResults;
    }

    public final void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public final void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }
    
    public int getTotalPages() {
        return totalPages;
    }

    public final void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
