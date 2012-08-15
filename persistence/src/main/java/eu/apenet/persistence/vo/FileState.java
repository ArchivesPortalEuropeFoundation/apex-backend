package eu.apenet.persistence.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "file_state")
public class FileState implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6991991028295893359L;
	public static final String NEW = "New";
	public static final String VALIDATED_CONVERTED = "Validated_Converted";
	public static final String NOT_VALIDATED_NOT_CONVERTED = "Not_Validated_Not_Converted";
	public static final String NOT_VALIDATED_CONVERTED = "Not_Validated_Converted";
	public static final String VALIDATED_NOT_CONVERTED = "Validated_Not_Converted";
	public static final String VALIDATING_FINAL_ERROR = "Validated_Final_Error";
	public static final String INDEXING = "Indexing";
	public static final String INDEXED = "Indexed_Not converted to ESE/EDM";
	public static final String INDEXED_CONVERTED_EUROPEANA = "Indexed_Converted to ESE/EDM";
	public static final String INDEXED_DELIVERED_EUROPEANA = "Indexed_Delivered to Europeana";
	public static final String INDEXED_HARVESTED_EUROPEANA = "Indexed_Harvested to Europeana";
	public static final String INDEXED_NO_HTML = "Indexed_No html";
	public static final String INDEXED_NOT_LINKED = "Indexed_Not linked";
	public static final String INDEXED_LINKED = "Indexed_Linked";
	public static final String READY_TO_INDEX = "Ready_to_index";
	public static final String[] INDEXED_FILE_STATES = new String []{INDEXED,INDEXED_CONVERTED_EUROPEANA,INDEXED_DELIVERED_EUROPEANA,INDEXED_HARVESTED_EUROPEANA,INDEXED_NO_HTML,INDEXED_NOT_LINKED,INDEXED_LINKED};
	public static final String[] NOT_INDEXED_FILE_STATES = new String []{NEW,VALIDATED_CONVERTED,NOT_VALIDATED_NOT_CONVERTED,VALIDATED_NOT_CONVERTED,NOT_VALIDATED_CONVERTED,VALIDATING_FINAL_ERROR,READY_TO_INDEX};
	public static final String[] ALL_FILE_STATES = new String []{NEW,VALIDATED_CONVERTED,NOT_VALIDATED_NOT_CONVERTED,VALIDATED_NOT_CONVERTED,NOT_VALIDATED_CONVERTED,VALIDATING_FINAL_ERROR,INDEXED,INDEXED_CONVERTED_EUROPEANA,INDEXING,INDEXED_DELIVERED_EUROPEANA,INDEXED_HARVESTED_EUROPEANA,INDEXED_NO_HTML,INDEXED_NOT_LINKED,INDEXED_LINKED,READY_TO_INDEX};
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String state;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}


}
