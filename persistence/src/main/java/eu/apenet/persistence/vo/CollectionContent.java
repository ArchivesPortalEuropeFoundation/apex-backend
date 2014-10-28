package eu.apenet.persistence.vo;

// Generated 01-ago-2014 13:48:20 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * CollectionSearch generated by hbm2java
 */
@Entity
@Table(name = "collection_content")
public class CollectionContent implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_collection")
	private Collection collection;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_search")
	private EadSavedSearch eadSavedSearch;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_bookmarks")
	private SavedBookmarks savedBookmarks;

	public CollectionContent() {
	}

	public CollectionContent(long id) {
		this.id = id;
	}

	public CollectionContent(long id, Collection collection,
			EadSavedSearch eadSavedSearch,SavedBookmarks savedBookmarks) {
		this.id = id;
		this.collection = collection;
		this.eadSavedSearch = eadSavedSearch;
		this.savedBookmarks = savedBookmarks;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_collection", nullable = false)
	public Collection getCollection() {
		return this.collection;
	}

	public void setCollection(Collection collection) {
		this.collection = collection;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_search")
	public EadSavedSearch getEadSavedSearch() {
		return this.eadSavedSearch;
	}

	public void setEadSavedSearch(EadSavedSearch eadSavedSearch) {
		this.eadSavedSearch = eadSavedSearch;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_bookmarks")
	public SavedBookmarks getSavedBookmarks() {
		return this.savedBookmarks;
	}

	public void setSavedBookmarks(SavedBookmarks savedBookmarks) {
		this.savedBookmarks = savedBookmarks;
	}

}