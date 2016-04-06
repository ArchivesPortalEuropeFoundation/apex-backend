package eu.apenet.persistence.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "topic_mapping")
public class TopicMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_id", foreignKey = @ForeignKey(name = "topic_mapping_ai_id_fkey"), insertable = false, updatable = false)
    private ArchivalInstitution archivalInstitution;
    @Column(name = "ai_id")
    private Integer aiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", foreignKey = @ForeignKey(name = "country_id_fkey"), insertable = false, updatable = false)
    private Country country;
    @Column(name = "country_id")
    private Integer countryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", foreignKey = @ForeignKey(name = "topic_mapping_topic_id_fkey"), insertable = false, updatable = false)
    private Topic topic;
    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "controlaccess_keyword")
    private String controlaccessKeyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sg_id", foreignKey = @ForeignKey(name = "topic_mapping_sg_id_fkey"), insertable = false, updatable = false)
    private SourceGuide sourceGuide;
    @Column(name = "sg_id")
    private Integer sgId;

    public Long getId() {
        return id;
    }

    public ArchivalInstitution getArchivalInstitution() {
        return archivalInstitution;
    }

    public Integer getAiId() {
        return aiId;
    }

    public Topic getTopic() {
        return topic;
    }

    public Long getTopicId() {
        return topicId;
    }

    public String getControlaccessKeyword() {
        return controlaccessKeyword;
    }

    public SourceGuide getSourceGuide() {
        return sourceGuide;
    }

    public Integer getSgId() {
        return sgId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
        this.archivalInstitution = archivalInstitution;
    }

    public void setAiId(Integer aiId) {
        this.aiId = aiId;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public void setControlaccessKeyword(String controlaccessKeyword) {
        this.controlaccessKeyword = controlaccessKeyword;
    }

    public void setSourceGuide(SourceGuide sourceGuide) {
        this.sourceGuide = sourceGuide;
    }

    public void setSgId(Integer sgId) {
        this.sgId = sgId;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }
}
