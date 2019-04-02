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

    private FacetType startDateFacetType = FacetType.START_DATE;
    private FacetType endDateFacetType = FacetType.END_DATE;

    public SolrQuery getTermQuery(String term) {
        SolrQuery query = new SolrQuery();
        // query.setShowDebugInfo(true);
        query.setTermsPrefix(term.toLowerCase());
        query.setTermsLower(term.toLowerCase());
        query.setRequestHandler("/terms");

        return query;
    }

    public SolrQuery getListViewQuery(int start, List<ListFacetSettings> facetSettingsList, String startDate, String endDate,
            boolean highlight) throws SolrServerException, ParseException {
        boolean withStartdateAndEnddate = false;
        SolrQuery query = new SolrQuery();
        query.setHighlight(highlight);
        if (facetSettingsList != null) {
            withStartdateAndEnddate = this.addFacets(query, facetSettingsList);

            if (withStartdateAndEnddate) {
                buildDateRefinement(query, startDate, this.startDateFacetType, true);
                buildDateRefinement(query, endDate, this.endDateFacetType, true);
            }
            query.setParam("facet.method", "enum");
        }
        query.setStart(start);

        return query;
    }

    public void addDateFilter(SolrQuery query, FacetType type, String dateId) throws SolrServerException, ParseException {
        this.buildDateRefinement(query, dateId, type, true);
    }

    public void addFilters(SolrQuery query, FacetType facet, List<String> values) {
        if (query != null && facet != null) {
            this.addFilters(query, facet.getRefinementFieldWithLabel(), values);
        }
    }

    public void addFilters(SolrQuery query, String criteria, List<String> values) {
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
                if (facetType.isStartDate()) {
                    this.startDateFacetType = facetType;
                } else {
                    this.endDateFacetType = facetType;
                }
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

    private void buildDateRefinement(SolrQuery query, String dateValue, FacetType dateFacet, boolean searchResults)
            throws SolrServerException, ParseException {
        boolean facetDate = true;

        if (dateFacet == null) {
            return;
        }

        if (StringUtils.isNotBlank(dateValue)) {
            String[] splittedStartDate = dateValue.split("_");
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
                query.addFilterQuery(dateFacet.getName() + ":[" + finalStartDateString + " TO " + finalEndDateString + "]");
                if (searchResults && dateGap.next() != null) {
                    query.setParam("f." + dateFacet.getName() + ".facet.date.start", finalStartDateString);
                    query.setParam("f." + dateFacet.getName() + ".facet.date.end", finalEndDateString);
                    query.set("f." + dateFacet.getName() + ".facet.date.gap", "+" + dateGap.getName());
                } else {
                    facetDate = false;
                }
            } else {
                facetDate = false;
            }
        } else if (searchResults) {
            query.setParam("f." + dateFacet.getName() + ".facet.date.start", "0000-01-01T00:00:00Z");
            query.setParam("f." + dateFacet.getName() + ".facet.date.end", "NOW");
            query.set("f." + dateFacet.getName() + ".facet.date.gap", "+200YEARS");
        } else {
            facetDate = false;
        }

        if (facetDate) {
            String[] hasStartParam = query.getParams("facet.date");
            if (hasStartParam == null) {
                query.setParam("facet.date", dateFacet.getName());
            } else {
                if (hasStartParam.length < 2) {
                    query.setParam("facet.date", hasStartParam[0], dateFacet.getName());
                }
            }
            query.setParam("facet.date.include", "lower");
        }

    }
}
