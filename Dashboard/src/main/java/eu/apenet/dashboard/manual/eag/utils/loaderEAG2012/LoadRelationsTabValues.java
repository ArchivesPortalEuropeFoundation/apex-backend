package eu.apenet.dashboard.manual.eag.utils.loaderEAG2012;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.manual.eag.utils.EAG2012Loader;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Repository;

/**
 * Class for load description tab values from the XML
 */
public class LoadRelationsTabValues implements  LoaderEAG2012{

	private final Logger log = Logger.getLogger(getClass());
	
	private EAG2012Loader loader;
	/**
	 * Eag {@link Eag} JAXB object.
	 */
	protected Eag eag;
	private Repository repository;

	@Override
	public Eag LoaderEAG2012(Eag eag, EAG2012Loader eag2012Loader) {		
		this.eag=eag;
		this.loader = eag2012Loader;
		main();
		return this.eag;
	}

	/**
	 * Method to load all values of "relations" tab of institution
	 */
	private void main(){
		this.log.debug("Method start: \"Main of class LoadRelationsTabValues\"");
		if (this.eag.getRelations() != null) {
			loadResourceRelations();
			loadRepositoryRelation();
		}
		this.log.debug("End method: \"Main of class LoadRelationsTabValues\"");
	}

	/**
	 * Method to load all values of "relations" tab of institution in resource relations
	 */
	private void loadResourceRelations(){
		this.log.debug("Method start: \"loadResourceRelations\"");
		// Resource relations.
		if (!this.eag.getRelations().getResourceRelation().isEmpty()) {
			for (int i = 0; i < this.eag.getRelations().getResourceRelation().size(); i++) {
				// Type of your relation.
				//TypeOfYourRelation(i, false,this.eag.getRelations().getResourceRelation().get(i).getResourceRelationType(),Eag2012.OPTION_CREATOR);
				if (this.eag.getRelations().getResourceRelation().get(i) != null
						&& this.eag.getRelations().getResourceRelation().get(i).getResourceRelationType() != null
						&& !this.eag.getRelations().getResourceRelation().get(i).getResourceRelationType().isEmpty()) {
					String resourceRelationTypeValue = this.eag.getRelations().getResourceRelation().get(i).getResourceRelationType();

					if (Eag2012.OPTION_CREATOR_TEXT.equalsIgnoreCase(resourceRelationTypeValue)) {
						resourceRelationTypeValue = Eag2012.OPTION_CREATOR;
					}
					if (Eag2012.OPTION_SUBJECT_TEXT.equalsIgnoreCase(resourceRelationTypeValue)) {
						resourceRelationTypeValue = Eag2012.OPTION_SUBJECT;
					}
					if (Eag2012.OPTION_OTHER_TEXT.equalsIgnoreCase(resourceRelationTypeValue)) {
						resourceRelationTypeValue = Eag2012.OPTION_OTHER;
					}

					this.loader.addRelationsResourceRelationType(resourceRelationTypeValue);
				} else {
					this.loader.addRelationsResourceRelationType(Eag2012.OPTION_CREATOR);
				}
				// Website of your resource.
				loadWebsiteResourceRelation(i);
				// Title & ID of the related material.
				loadTitleResourceRelation(i);
				// Description of relation.
				loadDescriptionResourceRelation(i);
			}
		}
		this.log.debug("End method: \"loadResourceRelations\"");
	}

	/**
	 * Method to load all values of "relations" tab of institution in repository relation
	 */
	private void loadRepositoryRelation(){
		this.log.debug("Method start: \"loadRepositoryRelation\"");
		// Institution/Repository relation.
		if (!this.eag.getRelations().getEagRelation().isEmpty()) {
			for (int i = 0; i < this.eag.getRelations().getEagRelation().size(); i++) {
				this.loader.addRelationsNumberOfEagRelations("");
				//TypeOfYourRelation(i, true,this.eag.getRelations().getEagRelation().get(i).getEagRelationType(),Eag2012.OPTION_NONE);
				// Type of the relation.
				if (this.eag.getRelations().getEagRelation().get(i) != null
						&& this.eag.getRelations().getEagRelation().get(i).getEagRelationType() != null
						&& !this.eag.getRelations().getEagRelation().get(i).getEagRelationType().isEmpty()) {
					String eagRelationTypeValue = this.eag.getRelations().getEagRelation().get(i).getEagRelationType();
					
					if (Eag2012.OPTION_CHILD_TEXT.equalsIgnoreCase(eagRelationTypeValue)) {
						eagRelationTypeValue = Eag2012.OPTION_CHILD;
					}
					if (Eag2012.OPTION_PARENT_TEXT.equalsIgnoreCase(eagRelationTypeValue)) {
						eagRelationTypeValue = Eag2012.OPTION_PARENT;
					}
					if (Eag2012.OPTION_EARLIER_TEXT.equalsIgnoreCase(eagRelationTypeValue)) {
						eagRelationTypeValue = Eag2012.OPTION_EARLIER;
					}
					if (Eag2012.OPTION_LATER_TEXT.equalsIgnoreCase(eagRelationTypeValue)) {
						eagRelationTypeValue = Eag2012.OPTION_LATER;
					}
					if (Eag2012.OPTION_ASSOCIATIVE_TEXT.equalsIgnoreCase(eagRelationTypeValue)) {
						eagRelationTypeValue = Eag2012.OPTION_ASSOCIATIVE;
					}

					this.loader.addRelationsEagRelationType(eagRelationTypeValue);
				} else {
					this.loader.addRelationsEagRelationType(Eag2012.OPTION_NONE);
				}							
				// Website of the description of the institution.							
				loadWebsiteEagRelation(i);							
				// Title & ID of the related institution.
				if (this.eag.getRelations().getEagRelation().get(i) != null
						&& !this.eag.getRelations().getEagRelation().get(i).getRelationEntry().isEmpty()) {
					for (int j = 0 ; j < this.eag.getRelations().getEagRelation().get(i).getRelationEntry().size(); j++) {
						
						loadTitleEagRelation(i,j);
					}
				} else {
					this.loader.addRelationsEagRelationEntry("");
					this.loader.addRelationsEagRelationEntryLang(Eag2012.OPTION_NONE);
				}
				// Description of relation.
				loadDescriptionEagRelation(i);
				
			}
		}
		this.log.debug("End method: \"loadRepositoryRelation\"");
	}

	/**
	 * Method for load description of relation in resource relation
	 * @param i {@link int i}
	 */
	private void loadDescriptionResourceRelation(int i){
		this.log.debug("Method start: \"loadDescriptionResourceRelation\"");
		// Description of relation.
		if (this.eag.getRelations().getResourceRelation().get(i) != null
				&& this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote() != null
				&& !this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().isEmpty()) {
			for (int j = 0; j < this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().size(); j++) {
				if (this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j) != null
						&& this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getContent() != null
						&& !this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
					this.loader.addRelationsResourceRelationEntryDescription(this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getContent());

					if (this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getLang() != null
							&& !this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
						this.loader.addRelationsResourceRelationEntryDescriptionLang(this.eag.getRelations().getResourceRelation().get(i).getDescriptiveNote().getP().get(j).getLang());
					} else {
						this.loader.addRelationsResourceRelationEntryDescriptionLang(Eag2012.OPTION_NONE);
					}
				} else {
					this.loader.addRelationsResourceRelationEntryDescription("");
					this.loader.addRelationsResourceRelationEntryDescriptionLang(Eag2012.OPTION_NONE);
				}
			}
		} else {
			this.loader.addRelationsResourceRelationEntryDescription("");
			this.loader.addRelationsResourceRelationEntryDescriptionLang(Eag2012.OPTION_NONE);
		}
		this.log.debug("End method: \"loadDescriptionResourceRelation\"");
	}

	/**
	 * Method for load description of relation in Eag Relation
	 * @param i {@link int i}
	 */
	private void loadDescriptionEagRelation(int i){
		this.log.debug("Method start: \"loadDescriptionEagRelation\"");
		if (this.eag.getRelations().getEagRelation().get(i) != null
				&& this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote() != null
				&& !this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().isEmpty()) {
			for (int j = 0; j < this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().size(); j++) {
				if (this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j) != null
						&& this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getContent() != null
						&& !this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
					this.loader.addRelationsEagRelationEntryDescription(this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getContent());

					if (this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getLang() != null
							&& !this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
						this.loader.addRelationsEagRelationEntryDescriptionLang(this.eag.getRelations().getEagRelation().get(i).getDescriptiveNote().getP().get(j).getLang());
					} else {
						this.loader.addRelationsEagRelationEntryDescriptionLang(Eag2012.OPTION_NONE);
					}
				} else {
					this.loader.addRelationsEagRelationEntryDescription("");
					this.loader.addRelationsEagRelationEntryDescriptionLang(Eag2012.OPTION_NONE);
				}
			}
		} else {
			this.loader.addRelationsEagRelationEntryDescription("");
			this.loader.addRelationsEagRelationEntryDescriptionLang(Eag2012.OPTION_NONE);
		}
		this.log.debug("End method: \"loadDescriptionEagRelation\"");
	}

	/**
	 * Method for load title of relation in Resource Relation
	 * @param i {@link int i}
	 */
	private void loadTitleResourceRelation(int i){
		this.log.debug("Method start: \"loadTitleResourceRelation\"");
		if (this.eag.getRelations().getResourceRelation().get(i) != null
			&& this.eag.getRelations().getResourceRelation().get(i).getRelationEntry() != null
			&& this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getContent() != null
			&& !this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getContent().isEmpty()) {						
		this.loader.addRelationsResourceRelationEntry(this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getContent());

		if (this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getLang() != null
				&& !this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getLang().isEmpty()) {
			this.loader.addRelationsResourceRelationEntryLang(this.eag.getRelations().getResourceRelation().get(i).getRelationEntry().getLang());
		} else {
			this.loader.addRelationsResourceRelationEntryLang(Eag2012.OPTION_NONE);
		}
		} else {
			this.loader.addRelationsResourceRelationEntry("");
			this.loader.addRelationsResourceRelationEntryLang(Eag2012.OPTION_NONE);
		}
		this.log.debug("End method: \"loadTitleResourceRelation\"");
	}

	/**
	 * Method for load title of relation in Eag Relation
	 * @param i {@link int}
	 * @param j {@link int} j is repository
	 */
	private void loadTitleEagRelation(int i, int j){
		this.log.debug("Method start: \"loadTitleEagRelation\"");
		if (this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j) != null
				&& this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getContent() != null
				&& !this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getContent().isEmpty()) {
			this.loader.addRelationsEagRelationEntry(this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getContent());

			if (this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getLang() != null
					&& !this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getLang().isEmpty()) {
				this.loader.addRelationsEagRelationEntryLang(this.eag.getRelations().getEagRelation().get(i).getRelationEntry().get(j).getLang());
			} else {
				this.loader.addRelationsEagRelationEntryLang(Eag2012.OPTION_NONE);
			}
		} else {
			this.loader.addRelationsEagRelationEntry("");
			this.loader.addRelationsEagRelationEntryLang(Eag2012.OPTION_NONE);
		}
		this.log.debug("End method: \"loadTitleEagRelation\"");
	}

	/**
	 * Method for load website of relation in Resource Relation
	 * @param i {@link int}
	 */
	private void loadWebsiteResourceRelation(int i){
		this.log.debug("Method start: \"loadWebsiteResourceRelation\"");
		// Website of your resource.
		if (this.eag.getRelations().getResourceRelation().get(i) != null
				&& this.eag.getRelations().getResourceRelation().get(i).getHref() != null
				&& !this.eag.getRelations().getResourceRelation().get(i).getHref().isEmpty()) {
			this.loader.addRelationsResourceRelationHref(this.eag.getRelations().getResourceRelation().get(i).getHref());
		} else {
			this.loader.addRelationsResourceRelationHref("");
		}
		this.log.debug("End method: \"loadWebsiteResourceRelation\"");
	}

	/**
	 * Method for load website of relation in Eag Relation
	 * @param i {@link int}
	 */
	private void loadWebsiteEagRelation(int i){
		this.log.debug("Method start: \"loadWebsiteEagRelation\"");
		if (this.eag.getRelations().getEagRelation().get(i) != null
				&& this.eag.getRelations().getEagRelation().get(i).getHref() != null
				&& !this.eag.getRelations().getEagRelation().get(i).getHref().isEmpty()) {
			this.loader.addRelationsEagRelationHref(this.eag.getRelations().getEagRelation().get(i).getHref());
		} else {
			this.loader.addRelationsEagRelationHref("");
		}
		this.log.debug("End method: \"loadWebsiteEagRelation\"");
	}
}
