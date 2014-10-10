package eu.apenet.persistence.vo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Collection generated by hbm2java
 */
@Entity
@Table(name = "collection")
public class Collection implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	@Column(name = "title", length = 200)
	private String title;
	@Column(name = "liferay_user_id", nullable = false)
	private long liferayUserId;
	@Column(name = "public", nullable = false)
	private boolean public_;
	@Column(name = "edit", nullable = false)
	private boolean edit;
	@Column(name = "description")
	private String description;
	@OneToMany(mappedBy = "collection")
	private Set<CollectionContent> collectionContents = new HashSet<CollectionContent>();

	public Collection() {
	}

	public Collection(long id, long liferayUserId, boolean public_, boolean edit) {
		this.id = id;
		this.liferayUserId = liferayUserId;
		this.public_ = public_;
		this.edit = edit;
	}

	public Collection(long id, String title, long liferayUserId,
			boolean public_, boolean edit, String description,
			Set<CollectionContent> collectionContents) {
		this.id = id;
		this.title = title;
		this.liferayUserId = liferayUserId;
		this.public_ = public_;
		this.edit = edit;
		this.description = description;
		this.collectionContents = collectionContents;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getLiferayUserId() {
		return this.liferayUserId;
	}

	public void setLiferayUserId(long liferayUserId) {
		this.liferayUserId = liferayUserId;
	}

	public boolean isPublic_() {
		return this.public_;
	}

	public void setPublic_(boolean public_) {
		this.public_ = public_;
	}

	public boolean isEdit() {
		return this.edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<CollectionContent> getCollectionContents() {
		return this.collectionContents;
	}

	public void setCollectionContents(Set<CollectionContent> collectionContents) {
		this.collectionContents = collectionContents;
	}

}