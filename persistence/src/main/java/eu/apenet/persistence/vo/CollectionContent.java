package eu.apenet.persistence.vo;

// Generated 01-ago-2014 13:48:20 by Hibernate Tools 3.4.0.CR1

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

/**
 * This class contains a collection content object
 */
@Entity
@Table(name = "collection_content")
public class CollectionContent implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_collection", foreignKey = @ForeignKey(name="collection_content_fkey_collection"))
	private Collection collection;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_search", foreignKey = @ForeignKey(name="collection_content_fkey_search"))
	private EadSavedSearch eadSavedSearch;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_bookmarks", foreignKey = @ForeignKey(name="collection_content_fkey_saved_bookmarks"))
	private SavedBookmarks savedBookmarks;

	public CollectionContent() {
	}

	/***
	 * Collectioncontent object
	 * 
	 * @param id {@link long} collection content identifier
	 */
	public CollectionContent(long id) {
		this.id = id;
	}

	/***
	 * Collectioncontent object
	 * 
	 * @param id {@link long} collection content identifier
	 * @param collection {@link Collection} the collection object
	 * @param eadSavedSearch {@link EadSavedSearch} a saved search object
	 * @param savedBookmarks {@link SavedBookmarks} a saved bookmark object
	 */
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
