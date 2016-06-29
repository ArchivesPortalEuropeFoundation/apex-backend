/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.commons.solr;

import static eu.apenet.commons.solr.AbstractSearcher.SOLR_DATE_FORMAT;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

/**
 * Refactored code, to reduce code duplication. Original author is (most
 * probably) bastiaan
 *
 * @author mahbub
 */
public class SolrQueryBuilder {

    public SolrQuery getTermQuery(String term) {
        SolrQuery query = new SolrQuery();
        // query.setShowDebugInfo(true);
        query.setTermsPrefix(term.toLowerCase());
        query.setTermsLower(term.toLowerCase());
        query.setRequestHandler("/terms");

        return query;
    }

    public SolrQuery getListViewQuery(int start, List<ListFacetSettings> facetSettingsList, String orderByField, String startDate, String endDate,
            boolean highlight) throws SolrServerException, ParseException {
        boolean withStartdateAndEnddate = false;
        SolrQuery query = new SolrQuery();
        query.setHighlight(highlight);
        if (facetSettingsList != null) {
            withStartdateAndEnddate = this.addFacets(query, facetSettingsList);

            if (withStartdateAndEnddate) {
                buildDateRefinement(query, startDate, endDate, true);
            }
            query.setParam("facet.method", "enum");
        }
        query.setStart(start);

        // query.setFacetLimit(ListFacetSettings.DEFAULT_FACET_VALUE_LIMIT);
        if (orderByField != null && orderByField.length() > 0 && !"relevancy".equals(orderByField)) {
            query.addSort(orderByField, SolrQuery.ORDER.asc);
            if (withStartdateAndEnddate && orderByField.equals("startdate")) {
                query.addSort("enddate", SolrQuery.ORDER.asc);
            }
        }

        return query;
    }
    
    public void addFromDateFilter(SolrQuery query, String fromDateId) throws SolrServerException, ParseException {
        this.buildDateRefinement(query, fromDateId, "", true);
    }
    
    public void addToDateFilter(SolrQuery query, String toDateId) throws SolrServerException, ParseException {
        this.buildDateRefinement(query, "", toDateId, true);
    }

    public void addFilters(SolrQuery query, FacetType facet, ArrayList<String> values) {
        if (query != null && facet != null) {
            this.addFilters(query, facet.getRefinementFieldWithLabel(), values);
        }
    }

    public void addFilters(SolrQuery query, String criteria, ArrayList<String> values) {
        if (query != null && criteria != null && !criteria.isEmpty()) {
            query.addFilterQuery(criteria + ":" + SearchUtil.convertToOrQuery(values));
        }
    }

    private boolean addFacets(SolrQuery query, List<ListFacetSettings> facetSettingsList) {
        boolean withStartdateAndEnddate = false;
        query.setFacetMinCount(1);
        for (ListFacetSettings facetSettings : facetSettingsList) {
            FacetType facetType = facetSettings.getFacetType();
            if (facetType.isDate()) {
                withStartdateAndEnddate = true;
            } else {
                query.addFacetField(facetType.getNameWithLabel());
                query.setParam("f." + facetType.getName() + ".facet.limit", facetSettings.getLimit() + "");
                if (facetSettings.getMincount() != null) {
                    query.setParam("f." + facetType.getName() + ".facet.mincount", facetSettings.getMincount() + "");
                }
            }
        }
        return withStartdateAndEnddate;
    }

    private void buildDateRefinement(SolrQuery query, String startDate, String endDate, boolean searchResults)
            throws SolrServerException, ParseException {
        boolean facetStartDate = true;
        boolean facetEndDate = true;
        if (StringUtils.isNotBlank(startDate)) {
            String[] splittedStartDate = startDate.split("_");
            String startDateString = splittedStartDate[0];
            String gapString = splittedStartDate[1];
            DateGap dateGap = DateGap.getGapById(gapString);
            if (dateGap != null) {
                Date beginDate = SOLR_DATE_FORMAT.parse(startDateString);
                Calendar endDateCalendar = Calendar.getInstance();
                endDateCalendar.setTime(beginDate);
                endDateCalendar.add(dateGap.getType(), dateGap.getSolrTimespan());
                String finalStartDateString = startDateString + "T00:00:00Z";
                String finalEndDateString = finalStartDateString + "+" + dateGap.previous().getName();
                query.addFilterQuery("startdate:[" + finalStartDateString + " TO " + finalEndDateString + "]");
                if (searchResults && dateGap.next() != null) {
                    query.setParam("f.startdate.facet.date.start", finalStartDateString);
                    query.setParam("f.startdate.facet.date.end", finalEndDateString);
                    query.set("f.startdate.facet.date.gap", "+" + dateGap.getName());
                } else {
                    facetStartDate = false;
                }
            } else {
                facetStartDate = false;
            }
        } else if (searchResults) {
            query.setParam("f.startdate.facet.date.start", "0000-01-01T00:00:00Z");
            query.setParam("f.startdate.facet.date.end", "NOW");
            query.set("f.startdate.facet.date.gap", "+200YEARS");
        } else {
            facetStartDate = false;
        }
        if (StringUtils.isNotBlank(endDate)) {
            String[] splittedStartDate = endDate.split("_");
            String startDateString = splittedStartDate[0];
            String gapString = splittedStartDate[1];
            DateGap dateGap = DateGap.getGapById(gapString);
            if (dateGap != null) {
                Date beginDate = SOLR_DATE_FORMAT.parse(startDateString);
                Calendar endDateCalendar = Calendar.getInstance();
                endDateCalendar.setTime(beginDate);
                endDateCalendar.add(dateGap.getType(), dateGap.getSolrTimespan());
                String finalStartDateString = startDateString + "T00:00:00Z";
                String finalEndDateString = finalStartDateString + "+" + dateGap.previous().getName();
                query.addFilterQuery("enddate:[" + finalStartDateString + " TO " + finalEndDateString + "]");
                if (searchResults && dateGap.next() != null) {
                    query.setParam("f.enddate.facet.date.start", finalStartDateString);
                    query.setParam("f.enddate.facet.date.end", finalEndDateString);
                    query.set("f.enddate.facet.date.gap", "+" + dateGap.getName());
                } else {
                    facetEndDate = false;
                }
            } else {
                facetEndDate = false;
            }
        } else if (searchResults) {
            query.setParam("f.enddate.facet.date.start", "0000-01-01T00:00:00Z");
            query.setParam("f.enddate.facet.date.end", "NOW");
            query.set("f.enddate.facet.date.gap", "+200YEARS");
        } else {
            facetEndDate = false;
        }
        if (facetStartDate && facetEndDate) {
            query.setParam("facet.date", "startdate", "enddate");
        } else if (facetStartDate) {
            query.setParam("facet.date", "startdate");
        } else if (facetEndDate) {
            query.setParam("facet.date", "enddate");
        }
        if (facetStartDate || facetEndDate) {
            query.setParam("facet.date.include", "lower");
        }

    }
}
